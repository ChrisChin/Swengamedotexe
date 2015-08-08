package unitTests;
import org.custommonkey.xmlunit.*;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import game.dataStorage.GameFile;
import game.dataStorage.GameFile.FileError;
import gameModel.gameObjects.*;
import gameModel.gameObjects.classes.*;
import gameModel.gameObjects.items.BronzeKey;
import gameModel.gameObjects.items.Potion;
import gameModel.gameObjects.landscape.Barrel;
import gameModel.gameObjects.landscape.BronzeChest;
import gameModel.gameObjects.landscape.Tree;
import gameModel.gameObjects.monsters.Slime;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;
import gameModel.gameWorld.maps.StartingMap;

import org.junit.Test;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

/**
 * JUnit tests for GameFile.java
 * 
 * @author Chris Chin (chinchri1)
 * 
 */
public class FileTests {
	private String fileName = "test.xml";

	@Test
	public void testWorldisNotNull() {
		try {
			new GameFile(fileName).saveGame(null);
		} catch (FileError e) {
			return;
		}
		fail();
	}

	/**
	 * Tests saving a file using SwordsMan and PigFarmer
	 * 
	 * @throws IOException
	 *             IOException
	 * @throws SAXException 
	 */
	@Test
	public void testSaveValidPlayer1() throws IOException, SAXException {
		GameFile f = new GameFile("DataStorageTests/temp1.xml");
		World w = new World();
		w.addPlayer(new Swordsman("Chris"));
		w.addPlayer(new PigFarmer("Bob"));
		f.saveGame(w);
		String output = xmlToString("DataStorageTests/testPlayer1.xml");
		String expectedOutput = xmlToString("DataStorageTests/temp1.xml");
		Diff myDiff = new Diff(output, expectedOutput);
		assertTrue("pieces of XML are similar " + myDiff, myDiff.similar());
	}

	/**
	 * Tests saving the player's inventory
	 * 
	 * @throws IOException
	 *             IOException
	 * @throws SAXException 
	 */
	@Test
	public void testSaveValidPlayer2() throws IOException, SAXException {
		GameFile f = new GameFile("DataStorageTests/temp2.xml");
		World w = new World();
		PlayerCharacter player = new Swordsman("Chris");
		player.addItemToInventory(new BronzeKey("42"));
		w.addPlayer(player);
		f.saveGame(w);
		String output = xmlToString("DataStorageTests/testPlayer2.xml");
		String expectedOutput = xmlToString("DataStorageTests/temp2.xml");
		Diff myDiff = new Diff(output, expectedOutput);
		assertTrue("pieces of XML are similar " + myDiff, myDiff.similar());
	}

	/**
	 * Tests saving no players
	 * 
	 * @throws IOException
	 *             IOException
	 * @throws SAXException 
	 */
	@Test
	public void testSaveValidPlayer3() throws IOException, SAXException {
		GameFile f = new GameFile("DataStorageTests/temp3.xml");
		World w = new World();
		f.saveGame(w);
		String output = xmlToString("DataStorageTests/testPlayer3.xml");
		String expectedOutput = xmlToString("DataStorageTests/temp3.xml");
		Diff myDiff = new Diff(output, expectedOutput);
		assertTrue("pieces of XML are similar " + myDiff, myDiff.similar());
	}

	/**
	 * Tests a bad world object which contains a player in a map who is not in
	 * the players list
	 * 
	 * @throws IOException
	 *             IOException
	 */
	@Test
	public void testSaveInvalidPlayerFile() throws IOException {
		try {
			GameFile f = new GameFile("DataStorageTests/temp.xml");
			World w = new World();
			PlayerCharacter player1 = new Swordsman("Chris");
			player1.addItemToInventory(new BronzeKey("42"));
			w.addPlayer(player1);
			Set<GameMap> maps = w.getMaps();

			for (GameMap map : maps) {
				if (map instanceof StartingMap) {
					Map<Point, GameObject> objectMap = map.getObjects();
					objectMap.remove((player1.getArrayPosition())); // removes
																	// Chris
					objectMap.put(new Point(1, 1), new Swordsman("Bob")); // puts
																			// bob
																			// instead
																			// of
																			// Chris
					map.setObjects(objectMap); // sets the new objectMap to the
												// startingmap
					break;
				}
			}
			w.setMaps(maps);
			f.saveGame(w);
		} catch (FileError e) {
			System.out.println(e);
			return;
		}
		fail("Should of thrown a FileError as bob is not in the player map");
	}

	/**
	 * Tests loading an badly formatted xml doc
	 * 
	 * @throws IOException
	 *             IOException
	 */
	@Test
	public void testSaveInvalidFormatFile() throws IOException {
		try {
			GameFile f = new GameFile("////");
			f.saveGame(new World());
		} catch (FileError e) {
			System.out.println(e);
			return;
		}
		fail("Should of thrown a FileError as it is an invalidFile as //// is not a valid address");
	}

	/**
	 * Checks the players inventory
	 * 
	 * @throws IOException
	 *             IOException
	 */
	@Test
	public void testLoadValidPlayerInventory() throws IOException {
		GameFile f = new GameFile("DataStorageTests/testPlayer2.xml");
		World w = f.loadGame();
		assertTrue(w.getPlayer("Bob") == null); // Bob should not be in the
												// players
		PlayerCharacter player = w.getPlayer("Chris");
		assertFalse(player == null); // Chris should be in the player
		// Checks player fields
		List<Item> itemList = player.getInventoryItems();
		assertTrue(itemList.contains(new BronzeKey("42")));
		assertTrue(itemList.contains(new Potion()));
		assertTrue(itemList.size() == 2);
	}

	/**
	 * Checks the players fields are correct ( Attack, Defence, health, max
	 * health)
	 * 
	 * @throws IOException
	 *             IOException
	 */
	@Test
	public void testLoadValidPlayerFields() throws IOException {
		GameFile f = new GameFile("DataStorageTests/testPlayer1.xml");
		World w = f.loadGame();
		assertTrue(w.getPlayer("Dave") == null); // Dave should not be in the
													// players
		PlayerCharacter player = w.getPlayer("Chris");
		assertFalse(player == null); // Chris should be in the player
		// Checks player fields
		assertTrue(player.getAttack() == 20);
		assertTrue(player.getDefence() == 5);
		assertTrue(player.getHealth() == 100);
		assertTrue(player.getMaxHealth() == 100);
	}

	/**
	 * Checks whether it loaded the map correctly
	 * 
	 * @throws IOException
	 *             IOException
	 */
	@Test
	public void testLoadValidMap() throws IOException {
		GameFile f = new GameFile("DataStorageTests/testPlayer1.xml");
		World w = f.loadGame();
		PlayerCharacter player = w.getPlayer("Chris");
		Set<GameMap> maps = w.getMaps();
		assertTrue(maps.size() == 9);
		for (GameMap map : maps) {
			if (map.getClass().getSimpleName().equals("StartingMap")) {
				Map<Point, GameObject> objectMap = map.getObjects();
				System.out.println(objectMap.size());
				assertTrue(objectMap.size() == 32); // should be 32 objects - 30
													// + the 2 players
				assertTrue(objectMap.get(new Point(1, 1)).equals(player)); // player
																			// should
																			// be
																			// at
																			// the
																			// spawn
																			// point
																			// 1,1
				// Checks positions of object in the map
				assertTrue(objectMap.get(new Point(1, 3)) instanceof Barrel);
				assertTrue(map.getPosition(new BronzeChest("42")).equals(
						new Point(1, 6)));
				assertTrue(objectMap.get(new Point(6, 6)) instanceof Slime);
				assertTrue(objectMap.get(new Point(2, 0)).equals(new Tree()));
			}
		}
	}

	/**
	 * Tests an invalid map name
	 * 
	 * @throws IOException
	 *             IOException
	 */
	@Test
	public void testLoadinvalidMap() throws IOException {
		try {
			GameFile f = new GameFile("DataStorageTests/testInvalidMap.xml");
			@SuppressWarnings("unused")
			World w = f.loadGame();
		} catch (FileError e) {
			System.out.println(e);
			return;
		}
		fail("Should of thrown a FileError as it is an invalid map name BadMapName");

	}

	/**
	 * Tests loading an empty xml doc
	 * 
	 * @throws IOException
	 *             IOException
	 */
	@Test
	public void testLoadInvalidEmptyFile() throws IOException {
		try {
			GameFile f = new GameFile("DataStorageTests/empty.xml");
			@SuppressWarnings("unused")
			World w = f.loadGame();
		} catch (FileError e) {
			System.out.println(e);
			return;
		}
		fail("Should of thrown a FileError as it is an invalidFile as it does not exist");
	}

	/**
	 * Tests loading an badly formatted xml doc
	 * 
	 * @throws IOException
	 *             IOException
	 */
	@Test
	public void testLoadInvalidFormatFile() throws IOException {
		try {
			GameFile f = new GameFile("DataStorageTests/testInvalidxml.xml");
			@SuppressWarnings("unused")
			World w = f.loadGame();
		} catch (FileError e) {
			System.out.println(e);
			return;
		}
		fail("Should of thrown a FileError as it is an invalidFile as it is not formatted correctly");
	}

	/**
	 * Tests player in the map but not in the players list
	 * 
	 * @throws IOException
	 *             IOException
	 */
	@Test
	public void testLoadInvalidPlayer() throws IOException {
		try {
			GameFile f = new GameFile("DataStorageTests/testInvalidPlayer.xml");
			@SuppressWarnings("unused")
			World w = f.loadGame();
		} catch (FileError e) {
			System.out.println(e);
			return;
		}
		fail("Should of thrown a FileError as it is an invalid object as it couldnt find the player");
	}

	@Test
	public void testLoadInvalidPlayerType() throws IOException {
		try {
			GameFile f = new GameFile(
					"DataStorageTests/testInvalidPlayerType.xml");
			@SuppressWarnings("unused")
			World w = f.loadGame();
		} catch (FileError e) {
			System.out.println(e);
			return;
		}
		fail("Should of thrown a FileError as it is an invalid player type BadPlayerType");
	}

	// Helper Method
	/**
	 * Creates a string from a xml file
	 * @param fileName filename of the file to load
	 * @return String of the contents of the xml file
	 * @throws IOException
	 *             IOException
	 */
	public String xmlToString(String fileName) throws IOException {
		@SuppressWarnings("resource")
		BufferedReader br = new BufferedReader(new FileReader(
				new File(fileName)));
		String line;
		StringBuilder sb = new StringBuilder();

		while ((line = br.readLine()) != null) {
			sb.append(line.trim());
		}

		String s = sb.toString();
		s = s.substring(38, s.length());
		return s;
	}

}