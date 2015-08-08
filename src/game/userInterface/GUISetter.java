package game.userInterface;

import game.Network.Client;
import game.userInterface.RendererWindow.Renderer;
import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameWorld.GameMap.Tile;

import java.awt.Point;
import java.util.Map;
import java.util.Map.Entry;

/**
 * A class designed to update all GUI related classes below it by dequeuing
 * packets from a client.
 * 
 * @author Geoffrey Longuet (longuegeof)
 * 
 */
public class GUISetter implements Runnable {
	private final Client client;
	private final GameFrame frame;
	private final Renderer renderer;
	private Map<Point, GameObject> currentObjects;
	private Tile[][] currentTiles;
	private PlayerCharacter currentPlayer;
	private PlayerCharacter healthCheckPlayer;

	/**
	 * Constructs a threaded object which will update GUI
	 * related objects  from a client as fast as possible. 
	 * @param client The client to get updates from.
	 */
	public GUISetter(Client client) {
		try {
			int i = 0;
			do {
				Thread.sleep(20);
				i++;
				if (client.getMap() != null) {
					break;
				}
			} while (i < 10);
			if (client.getMap() == null) {
				new RuntimeException("Map was null. Error in received world.");
			}
		} catch (InterruptedException e) {
			new RuntimeException("Init of GUISetter failed", e);
		}
		this.client = client;
		currentPlayer = client.getPc();
		healthCheckPlayer = client.getPc();
		currentObjects = client.getMap().getObjects();
		currentTiles = client.getMap().tiles;
		this.renderer = new Renderer(currentTiles, mapToArray(currentObjects),
				client);
		this.frame = new GameFrame(client, renderer);
		this.renderer.setChatBox(this.frame.getChatBox());
	}

	@Override
	public void run() {
		while (true) {
			PlayerCharacter player = client.getPc();
			if (!client.getMap().getObjects().equals(currentObjects)) {
				if (!client.getMap().tiles.equals(currentTiles)) {
					currentObjects = client.getMap().getObjects();
					currentTiles = client.getMap().tiles;
					renderer.updateMap(currentTiles, mapToArray(currentObjects));
				} else {
					currentObjects = client.getMap().getObjects();
					renderer.updateMap(mapToArray(currentObjects));
				}
			}
			if (!client.getPc().getInventoryItems()
					.equals(player.getInventoryItems())) {
				currentPlayer = client.getPc();
				frame.getCanvas().updatePlayer(currentPlayer);
			}
			if (client.getChats() != null) {
				frame.updateMessage(client.getChats());
				client.resetChats();
			}
			if (client.getPc() != null) {
				renderer.updatePlayerCharacter(client.getPc());
			}
			if (client.getPc().getHealth() != healthCheckPlayer.getHealth()) {
				healthCheckPlayer = client.getPc();
				frame.getCanvas().updateHealth(healthCheckPlayer);
			}
		}
	}

	/**
	 * Helper method to convert a mapping of Points to GameObjects into a 2D
	 * array, where if a point does not exist the array will contain null.
	 * 
	 * @param map
	 *            The map to convert to a 2D array.
	 * @return A 2D array of GameObjects where the if the point did not exist
	 *         there is a null.
	 */
	private GameObject[][] mapToArray(Map<Point, GameObject> map) {
		int sizeX = 0;
		int sizeY = 0;
		for (Point p : map.keySet()) {
			if (p.x > sizeX) {
				sizeX = p.x;
			}
			if (p.y > sizeY) {
				sizeY = p.y;
			}
		}
		GameObject[][] toRet = new GameObject[sizeX + 1][sizeY + 1];
		for (Entry<Point, GameObject> e : map.entrySet()) {
			int x = e.getKey().x;
			int y = e.getKey().y;
			toRet[x][y] = e.getValue();
		}
		return toRet;
	}
}
