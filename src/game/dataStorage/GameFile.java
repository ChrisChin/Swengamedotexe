package game.dataStorage;

import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.Item;
import gameModel.gameObjects.MortalObject;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameObjects.classes.PigFarmer;
import gameModel.gameObjects.classes.Swordsman;
import gameModel.gameObjects.items.BronzeKey;
import gameModel.gameObjects.items.Potion;
import gameModel.gameObjects.landscape.Barrel;
import gameModel.gameObjects.landscape.BronzeChest;
import gameModel.gameObjects.landscape.Tree;
import gameModel.gameObjects.monsters.Slime;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;
import gameModel.gameWorld.maps.*;

import java.awt.Point;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
/**
 *
 * @author Chris Chin (chinchri1)
 * This is responsible for reading and writing files
 * that represent the stage of the game.
 * The format used for this should be XML
 *
 */
public class GameFile {
	private String fileName;

	public GameFile(String fileName){
		this.fileName = fileName;
	}

	public class FileError extends Error{
		private static final long serialVersionUID = 8194046959998336167L;
		FileError(String message){
			super(message);
		}
	}

	/**
	 * Creates a new save file and saves it to a xml doc
	 *
	 * @param world 	World object of the game
	 */
	public void saveGame(World world){
		if(world == null) throw new FileError("World must not be null");
		Set<PlayerCharacter> players = world.getPlayers();//get players list
		Set<GameMap> maps = world.getMaps(); //get map list
		try{
			Element worldE = new Element("world");
			Document document = new Document(worldE);

			//Saves Players
			for(PlayerCharacter player : players){
				String name = player.playerName; // Name of the player
				Element playerElement = new Element("player");

				//Saves the Fields within MortalObject
				playerElement.addContent(new Element("type").setText(player.getClass().getSimpleName())); // saves the type of PLayerCharacter
				playerElement.addContent(new Element("name").setText(name));
				playerElement.addContent(new Element("x").setText("" + player.getPosition().x));
				playerElement.addContent(new Element("y").setText("" + player.getPosition().y));
				saveMortalObjectFields(playerElement,player);

				//Saves Player's inventory
				List<Item> inventoryItems = player.getInventoryItems(); //Player's inventory
				Element item = new Element("inventoryItems");
				for(Item i: inventoryItems){
					Element itemElement = new Element("item");
					itemElement.addContent(new Element("type").setText(i.getClass().getSimpleName()));
					itemElement.addContent(new Element("name").setText(i.getName()));
					item.addContent(itemElement);
				}
				playerElement.addContent(item); // puts the items into the player
				document.getRootElement().addContent(playerElement);
			}

			//Map
			for(GameMap map:maps){
				Element mapElement = new Element("map");
				mapElement.addContent(new Element("name").setText(map.getClass().getSimpleName()));
				mapElement.addContent(new Element("flags").setText("" +map.saveEventFlags())); // Stores the flags

				//Saves GameObjects in the map
				Map<Point,GameObject> objects = map.getObjects();
				Element gameObjectElement = new Element("gameObjects");
				for(Entry<Point, GameObject> entry : objects.entrySet()){
					Point point = entry.getKey(); //Position of the object
					GameObject object = entry.getValue();
					if(object instanceof Tree)continue;//Do not save trees
					Element objectElement = new Element("object");
					objectElement.addContent(new Element("type").setText(object.getClass().getSimpleName())); // Saves the type of the object
					objectElement.addContent(new Element("x").setText("" + point.x));
					objectElement.addContent(new Element("y").setText("" + point.y));

					//Checks if the object is a player and finds the name of the player
					if(object instanceof PlayerCharacter){
						PlayerCharacter player = (PlayerCharacter) object;

						boolean foundPlayer = false;
						for(PlayerCharacter p : players){
							String name = p.playerName; // Player's name
							if(player.equals(p)){
								objectElement.addContent(new Element("name").setText(name));
								foundPlayer = true;
								break;
							}
						}
						if(!foundPlayer) throw new FileError("invalid world file. Contains a player object that is not in the player's map");
					}

					//Checks if the object is a MortalObject and stores its fields
					else if(object instanceof MortalObject){
						MortalObject mObject = (MortalObject) object;
						saveMortalObjectFields(objectElement,mObject);
					}
					else if(object instanceof Item){
						Item item = (Item) object;
						objectElement.addContent(new Element("name").setText(item.getName()));
					}
					else if(object instanceof BronzeChest){
						BronzeChest chest = (BronzeChest) object;
						objectElement.addContent(new Element("name").setText(chest.getName()));
					}
					gameObjectElement.addContent(objectElement);

				}
				mapElement.addContent(gameObjectElement);
				document.getRootElement().addContent(mapElement);
			}
			XMLOutputter xmlOutputer = new XMLOutputter();
			// write the XML File with a nice formating and alignment
			xmlOutputer.setFormat(Format.getPrettyFormat());
			xmlOutputer.output(document, new FileWriter(fileName));
	       //print to console
//	        System.out.println(xmlOutputer.outputString(document));
	    } catch (IOException ex) {
	        throw new FileError(ex.getMessage());
	    }
	}

	/**
	 * Loads the game from the file
	 *
	 * @return 	a world object created from the xml doc
	 */
	public World loadGame(){
		SAXBuilder builder = new SAXBuilder();
		File xmlFile = new File(fileName);
		  try {
			  Document document = (Document) builder.build(xmlFile);
			  Element rootNode = document.getRootElement(); //Creates the root - world
		      List<Element> playerListElements = rootNode.getChildren("player"); // List of players
		      World world = new World(); // creates a new world

		      //Sets up the playersMap
		      Set<PlayerCharacter> playersSet = new LinkedHashSet<PlayerCharacter>();
		      for (Element playerElement : playerListElements) {
		    	  //Creates a new playerCharacter object
		    	  PlayerCharacter player= createPlayer(playerElement);
		    	  int x = Integer.parseInt(playerElement.getChildText("x"));
		    	  int y = Integer.parseInt(playerElement.getChildText("y"));
		    	  player.setPosition(new Point(x,y));


			      //Sets the Fields
			      setMortalObjectFields(player, playerElement);

			      //Sets the inventoryItems
			      Element inventoryItemsListElements =  playerElement.getChild("inventoryItems"); //list of inventory items
			      List<Item> inventoryItems = new ArrayList<Item>();
			      for(Element itemElement : inventoryItemsListElements.getChildren("item")){
			    	  Item item = createItem(itemElement.getChildText("type"), itemElement.getChildText("name"));
			    	  inventoryItems.add(item);
			      }
			      player.setInventoryItems(inventoryItems); //sets the inventory to the player

			      playersSet.add(player); //adds the player into the map
			      // player name is now player.playerName
		      }
		      world.setPlayers(playersSet);

		      //Sets up the maps
		      List<Element> mapListElements = rootNode.getChildren("map"); // List of maps
		      Set<GameMap> maps = new LinkedHashSet<GameMap>();
		      for (Element mapElement : mapListElements) {
		    	  //Creates a new map object
		    	  GameMap map = createMap(mapElement.getChildText("name"),world);
		    	  map.loadEventFlags(Integer.parseInt(mapElement.getChildText("flags")));

		    	  //Sets the objects in the map
		    	  Map<Point, GameObject> objectsMap = map.getObjects();

		    	  //Removes everything but trees
		    	  List<Point> removeList = new ArrayList<Point>(); // list of keys to remove from the map
		    	  for (Entry<Point, GameObject> entry : objectsMap.entrySet()){
						Point point = entry.getKey(); //Position of the object
						GameObject object = entry.getValue();
						if(!(object instanceof Tree)) removeList.add(point); //adds to list to remove
		    	  }
		    	  for(Point p : removeList) objectsMap.remove(p); // removes all the non trees and non barrels

		    	  Element gameObjectListElement =  mapElement.getChild("gameObjects"); //list of objects
		    	  List<Element> objectListElements =  gameObjectListElement.getChildren("object"); //list of objects
		    	   for(Element  objectsElement: objectListElements){
			    	  GameObject object = createObject(objectsElement,objectsElement.getChildText("type"),playersSet);
			    	  Point position = getObjectPosition(objectsElement); //gets the position of the object
			    	  objectsMap.put(position,object); // adds the object to the map
			      }
		    	  map.setObjects(objectsMap); //sets the map of objects in the map
		    	  maps.add(map); //adds the map to the collection of maps
		      }
		      world.setMaps(maps);

		      return world;
			  } catch (IOException io) {
				System.out.println(io.getMessage());
			  } catch (JDOMException jdomex) {
				System.out.println(jdomex.getMessage());
			  }
		  throw new FileError("Invalid File");

	}

	/**
	 * Saves the fields for a mortal object
	 *
	 * @param objectElement Element object of the Mortal Object
	 * @param mObject		Mortal object
	 */
	private void saveMortalObjectFields(Element objectElement, MortalObject mObject){
		objectElement.addContent(new Element("health").setText("" + mObject.getHealth()));
		objectElement.addContent(new Element("maxHealth").setText("" + mObject.getMaxHealth()));
		objectElement.addContent(new Element("attack").setText("" + mObject.getAttack()));
		objectElement.addContent(new Element("defence").setText("" + mObject.getDefence()));
	}

	/**
	 * Creates a new object from a given objectElement
	 * @param objectElement	Element object of the GameObject
	 * @param objectName	Name of the type of GameObject
	 * @return				GameObject using the given object name
	 */
	private GameObject createObject(Element objectElement, String objectName,Set<PlayerCharacter> playersSet) {
		//Tries to create a player
		String name = objectElement.getChildText("name");
		for(PlayerCharacter p:playersSet){
			if(p.playerName.equals(name)){
				return p;
			}
		}

		//Tries to create an item
		try{
			return createItem(objectElement.getChildText("type"), objectElement.getChildText("name"));
		}
		catch(FileError e){}
		if(objectName.equals("Slime")){
			Slime slime = new Slime();
			setMortalObjectFields(slime,objectElement);
			return slime;
		}
		else if (objectName.equals("BronzeChest"))return new BronzeChest(objectElement.getChildText("name"));
		else if (objectName.equals("Barrel")){
			Barrel barrel = new Barrel();
			setMortalObjectFields(barrel,objectElement);
			return barrel;
		}
		else throw new FileError("object Name " + objectName + " does not exist");
	}
	/**
	 * Creates a new Point from the given element
	 *
	 * @param objectsElement	Element object of the GameObject
	 * @return					Point from the given Object Element
	 */
	private Point getObjectPosition(Element objectsElement) {
		int x = Integer.parseInt(objectsElement.getChildText("x"));
		int y = Integer.parseInt(objectsElement.getChildText("y"));
		return new Point(x,y);
	}

	/**
	 * Creates a new map from the given name
	 *
	 * @param mapName	Name of the Map
	 * @param world		Current World Object
	 * @return			GameMap Object with the given map name
	 */
	private GameMap createMap(String mapName,World world) {
		if(mapName.equals("NorthWestMap")) return new NorthWestMap(world);
		if(mapName.equals("NorthMap")) return new NorthMap(world);
		if(mapName.equals("NorthEastMap")) return new NorthEastMap(world);
		if(mapName.equals("WestMap")) return new WestMap(world);
		if(mapName.equals("StartingMap")) return new StartingMap(world);
		if(mapName.equals("EastMap")) return new EastMap(world);
		if(mapName.equals("SouthWestMap")) return new SouthWestMap(world);
		if(mapName.equals("SouthMap")) return new SouthMap(world);
		if(mapName.equals("SouthEastMap")) return new SouthEastMap(world);
		else throw new FileError("MapName " + mapName + " does not exist");
	}

	/**
	 * Creates an Item and returns new item from a given string
	 * @param  type		The type of item object
	 * @return item		An Item object with the given type
	 */
	private Item createItem(String type, String name) {
		if(type.equals("BronzeKey")) return new BronzeKey(name);
		else if (type.equals("Potion")) return new Potion();
		else throw new FileError("Item type " + type + " does not exist");
	}

	/**
	 * Creates and returns a player from the given playerType
	 * @param playerElement 	Element object of the Player
	 * @return 					A new playerCharacter object using the given PlayerElement
	 */
	private PlayerCharacter createPlayer(Element playerElement) {
		String type = playerElement.getChildText("type");
		String name = playerElement.getChildText("name");
		if(type.equals("Swordsman")) return new Swordsman(name);
		else if(type.equals("PigFarmer")) return new PigFarmer(name);
		else throw new FileError("Player type " + type + " does not exist");
	}

	/**
	 * Sets the health,maxHealth,attack and defence of the mortalObject
	 * @param mObject 	MortalObject object
	 * @param mElement	Element object of the MortalObject
	 */
	private void setMortalObjectFields(MortalObject mObject, Element mElement){
		String[] type = new String[]{ "health", "maxHealth", "attack", "defence"};
		for(int i = 0; i<type.length; i++){
			String fieldType = type[i];
			int newValue = Integer.parseInt(mElement.getChildText(fieldType));
			if(fieldType.equals("health")) mObject.setHealth(newValue);
	        else if(fieldType.equals("maxHealth")) mObject.setMaxHealth(newValue);
	        else if(fieldType.equals("attack")) mObject.setAttack(newValue);
	        else mObject.setDefence(newValue);
		}
	}
}