package game.Network;

import game.Network.Packet.Commands;
import game.userInterface.GUISetter;
import game.userInterface.applicationWindow.CharacterDialog;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameWorld.GameMap;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Client allow a player to connect the game to server.
 * 
 * @author Wendell (wendel), Jiaheng Wang (wangjiah)
 * 
 */
public class Client extends Thread {

	private Queue<Packet> incomingPackets = new ConcurrentLinkedDeque<Packet>();
	private Queue<Packet> outgoingPackets = new ConcurrentLinkedDeque<Packet>();
	private static int DELAY = 50;
	public static int TIMEOUT = 50000;
	private String address;
	private int port;
	private Socket socket;
	private boolean isRunning;
	private boolean isConnected;
	private ChatMessage message;
	private GameMap map;
	private String playerName;
	private PlayerCharacter pc;

	/**
	 * Constructor that takes a playerName, an address, and a port number
	 * 
	 * @param playerName
	 *            the name of the player
	 * @param address
	 *            the IP address to connect to
	 * @param port
	 *            the port number
	 * @throws NullPointerException
	 *             if the playerName or the address is null
	 */
	public Client(String playerName, String address, int port)
			throws NullPointerException {
		if (playerName == null || address == null) {
			throw new NullPointerException();
		}
		this.address = address;
		this.port = port;
		this.playerName = playerName;
	}

	/**
	 * create new Socket to the address with the port specified
	 * 
	 * @param address
	 *            the destination address where the socket is opened to that
	 *            connection
	 * @param port
	 *            the port number of the connection
	 * @return a new socket connecting to the destination address, null if it
	 *         fails
	 */
	public static Socket openSocket(String address, int port) {
		try {
			return new Socket(address, port);
		} catch (IOException e) {
			System.out.println("Cannot reach the server or server is full");
			return null;
		}
	}

	/**
	 * This method overrides run() method of Thread class. Here the client does
	 * the handshaking with the server and exchange the player informations.
	 * There is a delay while receiving packet from client defined in static
	 * field above.
	 */
	public void run() {
		try {
			socket = openSocket(address, port);
			socket.setSoTimeout(TIMEOUT);

			if (socket != null) {
				isConnected = true;
			}

			if (isConnected) {
				isRunning = true;
				try {
					ObjectInputStream input = new ObjectInputStream(
							socket.getInputStream());
					ObjectOutputStream output = new ObjectOutputStream(
							socket.getOutputStream());

					try {
						if (!socket.isClosed()) {
							// playerName = GameFrame.askPlayerName();
							output.writeObject(new Packet(
									Commands.GET_PLAYER_FROM_NAME, playerName));
							System.out.println("Requesting " + playerName);

							this.pc = (PlayerCharacter) input.readObject();
							System.out.println("Request answered");

							if (pc == null) {
								CharacterDialog uni = new CharacterDialog(
										playerName);
								pc = uni.getNewPlayer();
								output.writeObject(new Packet(
										Commands.ADD_NEW_PLAYER, pc));
							} else {
								System.out.println("Loaded character named "
										+ pc.playerName);
								System.out
										.println("Now begin transmitting packet to server.");
							}
							output.writeObject(new Packet(Commands.INITIAL_PULL));

							while (map == null || pc.getPosition() == null) {
								incomingPackets.offer((Packet) input
										.readObject());
								unpackCurrentPacket();
							}
							assert (pc != null);
							assert (pc.getPosition() != null);
							assert (map != null);
							GUISetter gs = new GUISetter(this);
							Thread t = new Thread(gs);
							t.start();
							while (isRunning && !socket.isClosed()) {
								output.reset();
								// the client thread always sends packet and
								// receives packet
								// respectively in every loop

								if (outgoingPackets.peek() != null) {
									Packet p = (Packet) outgoingPackets.poll();
									output.writeObject(p);
								} else {
									output.writeObject(new Packet());
								}

								try {
									Packet pck = (Packet) input.readObject();
									incomingPackets.offer(pck);
								} catch (EOFException e) {
								} catch (SocketException e) {

								}
								while (incomingPackets.peek() != null) {
									unpackCurrentPacket();
								}
								try {
									Thread.sleep(DELAY);
								} catch (InterruptedException e) {
									e.printStackTrace();
									isRunning = false;
								}
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						System.out.println("Object type is not known");
					}
					isConnected = false;
					try {
						output.flush();
						output.close();
					} catch (SocketException e) {
						System.out
								.println("Failed to flush and close the socket");
					}
				} catch (IOException e) {
					System.out
							.println("Connection timeout, server is busy or not responding");
				}
			}
		} catch (SocketException e) {
			System.out
					.println("Cannot reach the server or the server limit is full");
		}
	}

	/**
	 * Add new packet to the outgoing queue in the client. This packet will be
	 * later sent to the game server
	 * 
	 * @param packet
	 *            which will be sent to server
	 */
	public void addPacket(Packet packet) {
		outgoingPackets.offer(packet);
	}

	/**
	 * Apply the changes event to the game World. Please make sure that the
	 * Packet and World objects being hold by this class are not null. This
	 * method will later apply the informations and events from packet being
	 * hold by this Client.
	 */
	public void unpackCurrentPacket() {
		Packet packet = incomingPackets.poll();
		if (packet != null) {
			if (packet.command == Commands.DUPLICATE_ONLINE_PLAYER) {
				System.out.println("Name already online");
			}
			if (packet.hasMap()) {
				map = packet.map;
			}
			if (packet.playerChara != null) {
				pc = packet.playerChara;
			}
			if (packet.chatMsg != null) {
				message = packet.chatMsg;
			}

			// packet.unpack(map);
		}
	}

	/**
	 * Disconnect the client and closes connections
	 */
	public void stopRunning() {
		isConnected = false;
		isRunning = false;
		try {
			socket.close();
		} catch (IOException e1) {
		}
		System.out.println("Client has been terminated");
	}

	/**
	 * Return the message from other players
	 * 
	 * @return the message
	 */
	public ChatMessage getChats() {
		return message;
	}

	/**
	 * Use to reset the chat message
	 */
	public void resetChats() {
		message = null;
	}

	/**
	 * Return the map the player is currently in
	 * 
	 * @return the map the player is in
	 */
	public GameMap getMap() {
		return map;
	}

	/**
	 * Retrun the player character the player currently has
	 * 
	 * @return the player character
	 */
	public PlayerCharacter getPc() {
		return pc;
	}

	/**
	 * Determine whether the client is currently connected to server
	 * 
	 * @return true is connected to server, false otherwise
	 */
	public boolean isConnected() {
		return isConnected;
	}

	/**
	 * Return the ping value to the server the client is currently connected
	 * 
	 * @return the ping value
	 */
	public long getPing() {
		if (address == null) {
			return Long.MAX_VALUE;
		}
		try {
			long currentTime = System.currentTimeMillis();
			boolean isPinged = InetAddress.getByName(address).isReachable(2000);
			currentTime = System.currentTimeMillis() - currentTime;
			if (isPinged) {
				return Math
						.max(currentTime / 1000, (2000 - currentTime) / 1000);
			}
		} catch (UnknownHostException e) {
		} catch (IOException e) {
		}
		return Long.MAX_VALUE;
	}
}
