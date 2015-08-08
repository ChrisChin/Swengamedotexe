package game.Network;

import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * A thread on the server for an individual client.
 * 
 * @author Wendell (wendel), Jiaheng Wang (wangjiah)
 * 
 */
public class ServerThread extends Thread {

	private Server server;
	private Socket clientSocket;
	private boolean isRunning;
	private PlayerCharacter pc;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private Queue<Packet> incomingPackets = new ConcurrentLinkedDeque<Packet>();
	private Queue<Packet> outgoingPackets = new ConcurrentLinkedDeque<Packet>();

	public ServerThread(Server server, Socket clientSocket) {
		this.server = server;
		this.clientSocket = clientSocket;
	}

	public void run() {
		try {
			output = new ObjectOutputStream(clientSocket.getOutputStream());
			input = new ObjectInputStream(clientSocket.getInputStream());

			isRunning = true;
			while (isRunning) {
				output.reset();
				try {
					// the client thread always receives packet and sends packet
					// respectively in every loop
					// receive packet

					try {
						Packet pck = (Packet) input.readObject();
						incomingPackets.offer(pck);
					} catch (SocketException e) {
					}

					while (incomingPackets.peek() != null) {
						Packet packet = incomingPackets.poll();
						if (packet.hasCommand()) {
							parseCommand(packet, output);
							System.out.println("Command parsed");
							continue;
						} else if (packet.isAction()) {
							GameMap gm = server.getWorld().getMap(pc);
							synchronized (packet) {
								gm.action(pc, packet.action, packet.direction);
							}
							addPacket(new Packet(pc, gm));
						} else if (packet.hasMessage()) {
							server.broadcastMessage(this, packet.chatMsg);
						} else if (packet.hasItem()) {
							server.getWorld().getMap(pc)
									.use(pc, packet.inventIndex);
						}
					}

					if (outgoingPackets.peek() != null) {
						try {
							output.writeObject(outgoingPackets.poll());
						} catch (SocketException e) {

						}
					} else {
						try {
							output.writeObject(new Packet(pc, server.getWorld()
									.getMap(pc)));
						} catch (SocketException e) {

						}
					}

				} catch (ClassNotFoundException e) {
					System.out.println("Object type is not known");
					isRunning = false;
				}
				try {
					Thread.sleep(Server.DELAY);
				} catch (InterruptedException e) {
				}
			}

			input.close();
			output.close();
			clientSocket.close();
		} catch (IOException e) {
			System.out.println("Connection timeout");
		} finally {
			closeConnection();
		}
	}

	public PlayerCharacter getPlayerCharacter() {
		return pc;
	}

	public void parseCommand(Packet packet, ObjectOutputStream output)
			throws IOException {
		switch (packet.command) {
		case GET_PLAYER_FROM_NAME:
			/*
			 * for (String name : server.getOnlinePlayerNames()) { if
			 * (name.equals(packet.getPlayerName())) { closeConnection(); } }
			 */
			System.out.println(packet.playerName + " requested");
			World w = server.getWorld();
			this.pc = w.getPlayer(packet.playerName);
			if (pc != null) {
				server.getWorld().respawn(pc);
			}
			// addPacket(new Packet(pc));
			output.writeObject(pc);
			System.out.println(packet.playerName + " sent");
			break;
		case ADD_NEW_PLAYER:
			/*
			 * for (String name : server.getOnlinePlayerNames()) { if
			 * (name.equals(packet.getPlayerName())) { closeConnection(); } }
			 */
			this.pc = packet.playerChara;
			server.getWorld().addPlayer(this.pc);
			System.out.println(packet.playerChara.playerName + " added");
			break;
		case INITIAL_PULL:
			output.writeObject(new Packet(pc, server.getWorld().getMap(pc)));
			break;
		default:
			break;
		}
	}

	public void stopRunning() {
		isRunning = false;
		try {
			output.close();
		} catch (IOException e1) {
		}
		try {
			input.close();
		} catch (IOException e1) {
		}
		try {
			clientSocket.close();
		} catch (Exception e1) {
		}
		server.saveState();
	}

	public void addPacket(Packet packet) {
		outgoingPackets.offer(packet);
	}

	public void closeConnection() {
		try {
			server.getWorld().disconnectPlayer(pc);
			server.removeClientThread(this);
			System.out.println("Manually Closing");
		} catch (NullPointerException e) {

		}
	}
}
