package unitTests;

import static org.junit.Assert.*;
import game.Network.Client;
import game.userInterface.RendererWindow.Renderer;
import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.GameMap.Tile;

import java.awt.Image;
import java.awt.Point;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

public class RendererTests {
	GameMap map;
	PlayerCharacter mapPlayer;
	PlayerCharacter mapPlayer2;
	PlayerCharacter mainPlayer;
	Renderer renderer;
	Client client;

	@SuppressWarnings("serial")
	@Before
	public void initialize() {
		this.mapPlayer = new PlayerCharacter(100, 100, 100, "Player") {
			@Override
			public Image getImage() {
				return null;
			}
		};
		this.mapPlayer.setPosition(new Point(250, 0));
		this.mapPlayer2 = new PlayerCharacter(100, 100, 100, "Player2") {
			@Override
			public Image getImage() {
				return null;
			}
		};
		this.mapPlayer2.setPosition(new Point(250, 0));
		this.mainPlayer = new PlayerCharacter(100, 100, 100, "mainPlayer") {
			@Override
			public Image getImage() {
				return null;
			}
		};
		this.mainPlayer.setPosition(new Point(0, 256));

		this.map = new GameMap(null, new Point(1, 1)) {

			@Override
			protected GameObject[][] initialize() {
				return new GameObject[][] { { null, null, null, mapPlayer },
						{ null, null, null, null }, { null, null, null, null },
						{ null, null, null, mapPlayer2 } };
			}

			@Override
			protected Tile[][] initializeTiles() {
				return new Tile[][] {
						{ Tile.DIRT, Tile.GRASS, Tile.GRASS, Tile.GRASS },
						{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS },
						{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS },
						{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS } };
			}

			@Override
			public String toString() {
				for (GameObject[] gos : objects) {
					System.out.println(gos);
				}
				return null;
			}

			@Override
			public int saveEventFlags() {
				return 0;
			}

			@Override
			public void loadEventFlags(int flags) {
			}

		};

		this.renderer = new Renderer(this.map.tiles,
				mapToArray(this.map.getObjects()), mainPlayer);// test
																// constructor
	}

	@Test
	public void testPlayerRotations_01() {// position should be the same
		initialize();
		Point initalPt = mapPlayer.getPosition();
		Method left = null;
		try {
			left = renderer.getClass().getDeclaredMethod("rotateLeft");
			left.setAccessible(true);
			left.invoke(renderer);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!initalPt.equals(mapPlayer.getPosition())) {
			fail();
		}
	}

	@Test
	public void testPlayerRotations_02() {// position should have updated
		initialize();
		Method left = null;
		GameObject[][] transMapObjects = renderer.getTransMapObjects();
		if (transMapObjects[0][3].equals(mapPlayer2)) {
			fail();
		}
		try {
			left = renderer.getClass().getDeclaredMethod("rotateLeft");
			left.setAccessible(true);
			left.invoke(renderer);
			transMapObjects = renderer.getTransMapObjects();

		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!transMapObjects[0][3].equals(mapPlayer2)) {
			fail();
		}
	}

	@Test
	public void testTileRotations_01() {// position should have updated
		initialize();
		Method left = null;
		Tile[][] transMapTiles = renderer.getTransMapTiles();
		if (transMapTiles[3][0].equals(Tile.DIRT)) {
			fail();
		}
		try {
			left = renderer.getClass().getDeclaredMethod("rotateLeft");
			left.setAccessible(true);
			left.invoke(renderer);
			transMapTiles = renderer.getTransMapTiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!transMapTiles[3][0].equals(Tile.DIRT)) {
			fail();
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
