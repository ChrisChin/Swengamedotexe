package gameModel.gameObjects;

import game.userInterface.RendererWindow.Renderer;
import game.userInterface.RendererWindow.Renderer.Direction;
import gameModel.gameObjects.items.Potion;
import gameModel.gameWorld.World;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player's character. Implementations should be of different (in
 * game) classes such as swordsman etc.
 * 
 * @author Jiaheng Wang (wangjiah) , Chris Chin (chinchri1)
 * 
 */
public abstract class PlayerCharacter extends MortalObject {

	/**
	 *
	 */
	private static final long serialVersionUID = -7989176077394571414L;
	public static final int CARRY_CAPACITY = 8;
	public final String playerName;
	private Direction direction = Direction.NORTH;
	private List<Item> inventoryItems = new ArrayList<Item>(8);
	private Point position;

	public static final int STARTING_HP = 100;
	public static final int STARTING_ATK = 10;
	public static final int STARTING_DEF = 5;
	public static final int SPEED = 5;

	public PlayerCharacter(int maxHP, int ATK, int DEF, String playerName) {
		super(World.PVPActive ? STARTING_HP : maxHP,
				World.PVPActive ? STARTING_ATK : ATK,
				World.PVPActive ? STARTING_DEF : DEF);
		this.playerName = playerName;
		this.addItemToInventory(new Potion());
		// free potion to start
	}

	/**
	 * Adds an item to the inventory to the first open slot. Returns true if
	 * Item is added successfully. False otherwise. Most likely reason for
	 * failing is no open slots are found
	 * 
	 * @param item
	 *            the item to add
	 * @return true if added successfully, false otherwise
	 */
	public boolean addItemToInventory(Item item) {
		if (inventoryItems.size() < CARRY_CAPACITY) {
			return inventoryItems.add(item);
		}
		return false;
	}

	/**
	 * Returns true if the player is holding the item
	 * 
	 * @param item
	 *            the item to find.
	 * @return true if the item is found.
	 */
	public boolean isHolding(Item item) {
		for (Item i : inventoryItems) {
			if (item.equals(i)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Uses an item in the inventory on a given target
	 * 
	 * @param index
	 *            the index of the item to use
	 * @param go
	 *            the GameObject to use the item on
	 * @return true if the item has been used successfully
	 */
	public boolean useItem(int index, GameObject go) {
		if (inventoryItems.size() < index) {
			return false;
		} else if (inventoryItems.get(index).use(go)) {
			inventoryItems.remove(index);
			return true;
		} else {
			return false;
		}
	}

	public void setPosition(Point p) {
		this.position = p;
	}

	public void setArrayPosition(Point p) {
		int x = p.x * Renderer.TILE_HEIGHT;
		x += Renderer.TILE_HEIGHT / 2;
		int y = p.y * Renderer.TILE_HEIGHT;
		y += Renderer.TILE_HEIGHT / 2;
		this.position = new Point(x, y);
	}

	public void move(Direction d) {
		int x = position.x;
		int y = position.y;
		switch (d) {
		case NORTH:
			y -= SPEED;
			break;
		case EAST:
			x += SPEED;
			break;
		case SOUTH:
			y += SPEED;
			break;
		case WEST:
			x -= SPEED;
			break;
		default:
			break;
		}
		this.position = new Point(x, y);
		this.direction = d;
	}

	/**
	 * Returns the precise position of the player in the map
	 * 
	 * @return a Point representing the precise position of the player in the
	 *         map
	 */
	public Point getPosition() {
		return (Point) this.position.clone();
	}

	/**
	 * Returns the position of the player in the array in the map
	 * 
	 * @return Point representing the position of the player in the array
	 */
	public Point getArrayPosition() {
		return new Point(position.x / Renderer.TILE_HEIGHT, position.y
				/ Renderer.TILE_HEIGHT);// WARNING! Unsure if should TILE_HEIGHT
										// over TILE_WIDTH, calculations are
										// done on a square plane.
	}

	/**
	 * Returns the players inventory items
	 * 
	 * @return array of inventory items
	 */
	public List<Item> getInventoryItems() {
		return new ArrayList<Item>(inventoryItems);
	}

	/**
	 * Returns the item at the given inventory index
	 * 
	 * @param index
	 *            the index of the item needed
	 * @return the item at the given index
	 */
	public Item getItemAtIndex(int index) {
		return inventoryItems.get(index);
	}

	/**
	 * Sets the inventoryItems to the new items in the list items
	 * 
	 * REQUIRES: items.length < CARRY_CAPACITY
	 * 
	 * @param items
	 *            list of new items
	 */
	public void setInventoryItems(List<Item> items) {
		inventoryItems = new ArrayList<Item>(items);
	}

	/**
	 * Returns the maximum capacity of the inventory
	 * 
	 * @return the maximum capacity of the inventory
	 */
	public int getCapacity() {
		return CARRY_CAPACITY;
	}

	/**
	 * Gives the current Direction of the player
	 * 
	 * @return the current Direction of the player
	 */
	public Direction getDirection() {
		return direction;
	}

	/**
	 * Sets this player to the given direction
	 * 
	 * @param d
	 *            the direction to set this player to
	 */
	public void setDirection(Direction d) {
		this.direction = d;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null) {
			return false;
		}
		if (arg0 instanceof PlayerCharacter) {
			PlayerCharacter pc = (PlayerCharacter) arg0;
			return pc.playerName.equals(this.playerName);
		}
		return false;
	}

	@Override
	public int hashCode() {
		int a = 0;
		for (char c : playerName.toCharArray()) {
			a += c;
		}
		return a;

	}

	@Override
	public String description() {
		return "It's " + playerName + "."
				+ ((!World.PVPActive) ? "" : " Kill them!!");
	}

	@Override
	public String toString() {
		return playerName;
	}

	/**
	 * Return the root location of the animated images for this character model.
	 * 
	 * @return The string of the images root location.
	 */
	public String getImageSource() {
		return "characters/swordsman/";
	}

}
