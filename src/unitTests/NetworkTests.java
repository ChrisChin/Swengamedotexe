package unitTests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import game.Network.ChatMessage;
import game.Network.Client;
import game.Network.Packet;
import game.Network.Server;
import game.userInterface.RendererWindow.Renderer;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameObjects.classes.Swordsman;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;

import org.junit.Test;

/**
 * 
 * @author Wendell (wendel)
 * 
 */
public class NetworkTests {

	private int delay = 2000;

	@Test
	public void serverTest1() {
		try {
			new Server(null, 80);
			fail("Cannot put null instead of world instance");
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void serverTest2() {
		try {
			new Server(80, null);
			fail("Should be not able to create server with filename null");
		} catch (Exception e) {
		}
	}

	@Test
	public void serverTest3() {
		try {
			new Server(null, 80);
			new Server(null, 80);
			fail("Should be not able to run 2 servers with the same port");
		} catch (Exception e) {

		}
	}

	@Test
	public void clientTest1() {
		try {
			new Client(null, "127.0.0.1", 9112);
			fail("Should be not able to create client with player name null");
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void clientTest2() {
		try {
			new Client("Coco", null, 9112);
			fail("Should be not able to create client with server address null");
		} catch (NullPointerException e) {
		}
	}

	/**
	 * Should add new character to the server, if the user first time plays the
	 * game
	 */
	@Test
	public void connectionTest1() {
		Server server = new Server(new World(), 9112);
		server.start();
		Client client = new Client("Coco", "127.0.0.1", 9112);
		client.start();

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
		assertTrue(server.getTotClients() == 1);

		server.stopRunning();
		client.stopRunning();

	}

	/**
	 * Player should be able to continue playing with the character he played
	 * before
	 */
	@Test
	public void connectionTest2() {
		World world = new World();
		PlayerCharacter p = new Swordsman("Coco");
		world.addPlayer(p);
		Server server = new Server(world, 9112);
		server.start();
		Client client = new Client("Coco", "127.0.0.1", 9112);
		client.start();

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}
		assertTrue(server.getTotClients() == 1);
		assertTrue(client.getPc().equals(p));

		server.stopRunning();
		client.stopRunning();
	}

	/**
	 * Client should be able to send packet to server
	 */
	@Test
	public void packetTransmissionTest1() {
		World world = new World();
		PlayerCharacter p = new Swordsman("Coco");
		world.addPlayer(p);
		Server server = new Server(world, 9112);
		server.start();
		Client client = new Client("Coco", "127.0.0.1", 9112);
		client.start();

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}

		for (int i = 0; i < 10; i++) {
			client.addPacket(createDummyPacket("chat"));
		}

		server.stopRunning();
		client.stopRunning();
	}

	/**
	 * Client should be able to receive update from server
	 */
	@Test
	public void packetTransmissionTest2() {
		World world = new World();
		PlayerCharacter p = new Swordsman("Coco");
		world.addPlayer(p);
		Server server = new Server(world, 9112);
		server.start();
		Client client = new Client("Coco", "127.0.0.1", 9112);
		client.start();

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}

		// get character current position
		int x = client.getPc().getPosition().x;

		// create dummy packet
		client.addPacket(createDummyPacket("Player"));

		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
		}

		assertTrue(x + 5 == client.getPc().getPosition().x);

		server.stopRunning();
		client.stopRunning();
	}

	public Packet createDummyPacket(String type) {
		switch (type) {
		case "chat":
			return new Packet(new ChatMessage("Coco", "Hola"));
		case "command":
			return new Packet(Packet.Commands.INITIAL_PULL);
		case "Player":
			return new Packet(GameMap.PlayerAction.MOVE, new Swordsman("Coco"),
					Renderer.Direction.EAST);
		}
		return new Packet();
	}
}
