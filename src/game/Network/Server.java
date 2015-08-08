package game.Network;

import game.dataStorage.GameFile;
import gameModel.gameObjects.NonPlayerCharacter;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Server receives connections from clients to be able to play in the game. The
 * server is intended to implement singleton design pattern in order to
 * preventing client creating many servers in the same computer which may lead
 * to break the connections.
 *
 * @author Wendell (wendel), Jiaheng Wang (wangjiah)
 *
 */
public class Server extends Thread {
	public static int DELAY = 50;
	public static int TIMEOUT = 5000;
	public static int MAX_CLIENTS = 3;
	private final String DEFAULT_FILENAME = "savefile.xml";

	private List<ServerThread> threads;
	private int totClients;
	private String filename;

	private ServerSocket serverSocket;
	private boolean isRunning;
	private int port;

	private World world;

	/**
	 *
	 * @param world
	 *            is the world of the game
	 * @param port
	 *            is the port number the server is running on
	 */
	public Server(World world, int port) throws NullPointerException {
		if (world == null) {
			throw new NullPointerException("world cannot be null");
		}
		this.world = world;
		this.port = port;
		this.threads = new ArrayList<ServerThread>(MAX_CLIENTS);
		this.filename = DEFAULT_FILENAME;
		Thread t = new Thread(world);
		t.start();
	}

	/**
	 *
	 * @param filename
	 *            is the name of the file the game will later be saved to
	 * @param port
	 *            is the port number the server is running on
	 */
	public Server(int port, String filename) {
		this.port = port;
		this.threads = new ArrayList<ServerThread>(MAX_CLIENTS);
		this.filename = filename;

		GameFile gm = new GameFile(filename);
		world = gm.loadGame();
		for (PlayerCharacter p : world.getPlayers()) {
			System.out.println(p.playerName);
		}

		Thread t = new Thread(world);
		t.start();
	}

	/**
	 * Remove ClientThread from list This is usually done whenever the client is
	 * disconnected
	 *
	 * @param ct
	 *            the ServerThread to remove
	 */
	public void removeClientThread(ServerThread ct) {
		threads.remove(ct);
		totClients--;
	}

	/**
	 * Returns the game world currently hosted by server
	 *
	 * @return the world currently hosted by the server
	 */
	public World getWorld() {
		return world;
	}

	/**
	 * This method overrides run() method of Thread class. The process of
	 * receiving connections from client is done here. It opens the server
	 * socket and wait the connection from clients. There is a delay while
	 * receiving packet from client defined in static field above.
	 */
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			System.out
					.println("Failed to create server socket. This address may be already in use");
			try {
				throw new IOException();
			} catch (IOException e1) {
			}
		}
		if (serverSocket != null) {
			try {
				isRunning = true;
				System.out.println("Server started");
				while (isRunning) {
					if (totClients < MAX_CLIENTS) {
						Socket clientSocket = serverSocket.accept();
						clientSocket.setSoTimeout(TIMEOUT);
						ServerThread ct = new ServerThread(this, clientSocket);
						threads.add(ct);
						totClients++;
						ct.start();
						saveState();
					}
				}
				// close the socket and save the state
				serverSocket.close();
				saveState();

			} catch (IOException e) {
				System.out.println("Connection failed");
			}
		}
	}

	/**
	 * broadcast the message received to all clients
	 *
	 * @param ct
	 *            the ServerThread that received the message
	 * @param cm
	 *            the ChatMessage to broadcast
	 */
	public void broadcastMessage(ServerThread ct, ChatMessage cm) {
		if (cm.getMessage().charAt(0) == '/') {
			parseCommand(ct, cm.getMessage());
		} else {
			for (int i = 0; i < threads.size(); i++) {
				threads.get(i).addPacket(new Packet(cm));
			}
		}
	}

	/**
	 * Reads and executes the text command
	 *
	 * @param ct
	 *            the serverthread that received the packet (essentially the
	 *            player that sent it)
	 * @param message
	 *            the command, already extracted from the packet
	 */
	private void parseCommand(ServerThread ct, String message) {
		switch (message) {
		case "/clearstatus":
		case "/cs":
			if (world.allMapsClear()) {
				broadcastMessage(null, new ChatMessage("System",
						"All maps have been cleared. You win!"));
				return;
			}
			ct.addPacket(new Packet(new ChatMessage("Server", world
					.clearedMaps())));
			return;
		case "/unclearmaps":
		case "/ucm":
			if (world.allMapsClear()) {
				broadcastMessage(null, new ChatMessage("System",
						"All maps have been cleared. You win!"));
				return;
			}
			ct.addPacket(new Packet((new ChatMessage("Server", world
					.unclearedMaps()))));
			return;
		case "/kill":
			for (NonPlayerCharacter npc : world.getMap(ct.getPlayerCharacter())
					.npcs()) {
				npc.setHealth(0);
			}
			world.getMap(ct.getPlayerCharacter()).cleanUp();
			break;
		case "/killall":
			for (GameMap gm : world.getMaps()) {
				for (NonPlayerCharacter npc : gm.npcs()) {
					npc.setHealth(0);
				}
				gm.cleanUp();
			}
			break;
		default:
			ct.addPacket(new Packet(new ChatMessage("System", "Usage:")));
			ct.addPacket(new Packet(
					new ChatMessage("System",
							"/clearstatus, /cm: tells you which maps have been cleared")));
			ct.addPacket(new Packet(
					new ChatMessage("System",
							"/unclearmaps, /ucm: tells you which maps have not been cleared")));
			ct.addPacket(new Packet(new ChatMessage("System",
					"/kill: kills all NPC on the current map")));
			ct.addPacket(new Packet(new ChatMessage("System",
					"/killall: kills all NPCs in the world")));
			break;
		}
	}

	/**
	 * Tells the total clients playing in this server
	 *
	 * @return gives the total number of clients connected
	 */
	public int getTotClients() {
		return totClients;
	}

	/**
	 * Returns a list of names of all online players
	 *
	 * @return gives a list of name of all online players
	 */
	public List<String> getOnlinePlayerNames() {
		List<String> playerNames = new ArrayList<String>();
		for (ServerThread it : threads) {
			if (it.getPlayerCharacter() != null) {
				playerNames.add(it.getPlayerCharacter().playerName);
			}
		}
		return playerNames;
	}

	/**
	 * save this game state into a file
	 */
	public void saveState() {
		GameFile f = new GameFile(filename);
		f.saveGame(world);
	}

	/**
	 * Disconnects all clients and closes the socket
	 */
	public void stopRunning() {
		for (ServerThread st : threads) {
			st.stopRunning();
		}
		isRunning = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
		} catch (NullPointerException e) {

		}
		saveState();
		System.out.println("Server has been terminated");
	}
}
