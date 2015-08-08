package gameModel.gameWorld;

import game.userInterface.RendererWindow.Renderer.Direction;
import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.Item;
import gameModel.gameObjects.MortalObject;
import gameModel.gameObjects.NonPlayerCharacter;
import gameModel.gameObjects.PlayerCharacter;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author Jiaheng Wang (wangjiah) , Chris Chin (chinchri1)
 * 
 */
public abstract class GameMap implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 8427749362462454123L;

	public enum PlayerAction {
		ATTACK, MOVE, PICKUP/*
							 * INSPECT, inspect is now client side only, and is
							 * not the responsibility of the map
							 */
	}

	public enum Tile {
		DIRT, GRASS, GRASS_BLOODY
	}

	private final int height;
	private final int width;
	private long lastTick = System.currentTimeMillis();
	protected GameObject[][] objects;
	private final Point spawnPoint;
	public final Tile[][] tiles;
	protected final World world;
	protected final Point northExit = new Point(4, 0); // Teleporter square that
														// moves the map North
														// of this
	protected final Point westExit = new Point(0, 4); // Teleporter square that
														// moves the map West of
														// this
	protected final Point eastExit = new Point(7, 4); // Teleporter square that
														// moves the map East of
														// this
	protected final Point southExit = new Point(4, 7); // Teleporter square that
														// moves the map South
														// of this
	protected String northMapName;
	protected String westMapName;
	protected String eastMapName;
	protected String southMapName;

	/**
	 * Constructor for the GameMap. Should be passed in the World instance that
	 * called the constructor.
	 * 
	 * @param world
	 *            the world instance that called this constructor
	 * @param spawnPoint
	 *            the default spawn point of the map
	 */
	public GameMap(World world, Point spawnPoint) {
		this.world = world;
		this.objects = initialize();
		this.tiles = initializeTiles();
		width = objects.length;
		height = objects[0].length;
		this.spawnPoint = spawnPoint;
		assert (width > 0 && height > 0);
		assert (isValidPoint(spawnPoint));
		assert (this.tiles.length == width);
		for (int i = 0; i < width; i++) {
			assert (objects[i].length == height);
			assert (tiles[i].length == height);
		}
	}

	public void action(PlayerCharacter pc, PlayerAction pa,
			game.userInterface.RendererWindow.Renderer.Direction direction) {
		Point target = directionFromPoint(getPosition(pc), direction);
		switch (pa) {
		case MOVE:
			move(pc, direction);
			break;
		case PICKUP:
			if (objects[target.x][target.y] instanceof Item) {
				if (pc.addItemToInventory((Item) objects[target.x][target.y])) {
					// addItemToInventory returns true if successfully added to
					// inventory
					// so remove item from the world
					objects[target.x][target.y] = null;
				}
			}
			break;
		case ATTACK:
			if (objects[target.x][target.y] instanceof MortalObject) {
				MortalObject mo = (MortalObject) objects[target.x][target.y];
				if (mo instanceof PlayerCharacter && !World.PVPActive) {
					break;
				}
				pc.attack(mo);
				if (mo.isDead()) {
					objects[target.x][target.y] = null;
					bloodyTile(target);
				}
			}
			break;
		// case INSPECT:
		// inspect(pc, objects[target.x][target.y]);
		// break;
		default:
			break;
		}
	}

	/**
	 * Returns true if given GameObject is in this Map
	 * 
	 * @param obj
	 *            the GameObject to look for
	 * @return true if given GameObject is in the Map
	 */
	public boolean contains(GameObject obj) {
		if (obj == null) {
			return false;
		}
		for (GameObject[] row : objects) {
			for (GameObject go : row) {
				if (obj.equals(go)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Spawns a GameObject at the map's default location. If default location is
	 * unavailable, another location will be found, until the whole map has been
	 * tried.
	 * 
	 * @param go
	 *            The object to spawn
	 * @return true if spawned successfully
	 */
	public boolean spawn(GameObject go) {
		return spawn(go, this.spawnPoint);
	}

	/**
	 * Spawns a GameObject at a given location. If given location is
	 * unavailable, another location will be found, until the whole map has been
	 * tried.
	 * 
	 * Returns true if object was spawned. False otherwise.
	 * 
	 * Requires: Given Point must be a vaild point on the map.
	 * 
	 * @param go
	 *            The object to spawn
	 * @param pos
	 *            The location to spawn at
	 * @return true if spawned successfully
	 */
	public boolean spawn(GameObject go, Point pos) {
		assert (isValidPoint(pos));
		int xRange = 1;
		int yRange = 0;
		int xDiff = 0;
		int yDiff = 0;
		outer: while (xRange < width && yRange < height) {
			for (; xDiff < xRange; xDiff++) { // going right
				if (isValidSpawnPoint(pos.x + xDiff, pos.y + yDiff)) {
					break outer;
				}
			}
			++yRange;
			for (; yDiff < yRange; yDiff++) { // going up on the right
				if (isValidSpawnPoint(pos.x + xDiff, pos.y + yDiff)) {
					break outer;
				}
			}
			for (; xDiff > -xRange; xDiff--) { // going left on the top
				if (isValidSpawnPoint(pos.x + xDiff, pos.y + yDiff)) {
					break outer;
				}
			}
			for (; yDiff > -yRange; yDiff--) { // going down on the left
				if (isValidSpawnPoint(pos.x + xDiff, pos.y + yDiff)) {
					break outer;
				}
			}
			++xRange;
		}
		int x = pos.x + xDiff;
		int y = pos.y + yDiff;
		if (isValidSpawnPoint(x, y)) {
			objects[x][y] = go;
			if (go instanceof PlayerCharacter) {
				((PlayerCharacter) go).setArrayPosition(new Point(x, y));
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Makes all NPCs in the map move. Should be called at a regular interval.
	 */
	public void tick() {
		long currentTick = System.currentTimeMillis();

		for (NonPlayerCharacter npc : npcs()) {
			npc.tick(currentTick - lastTick);
			if (!npc.isAggresive() || !npc.isReady()) {
				continue;
			}
			for (GameObject go : getSurroundingGameObjects(npc)) {
				if (go instanceof PlayerCharacter) {
					if (npc.attack(go)) { // gameobject died
						if (((MortalObject) go).isDead()) {
							Point pos = getPosition(go);
							world.respawn((PlayerCharacter) go);
							objects[pos.x][pos.y] = null;
							bloodyTile(pos);
						}
					}
				}
				npc.resetCooldown();
			}
		}

		if (northMapName != null)
			calculateTeleport(northExit, northMapName, southExit);
		if (eastMapName != null)
			calculateTeleport(eastExit, eastMapName, westExit);
		if (westMapName != null)
			calculateTeleport(westExit, westMapName, eastExit);
		if (southMapName != null)
			calculateTeleport(southExit, southMapName, northExit);

		lastTick = currentTick;
	}

	/**
	 * Uses an item.
	 * 
	 * @param pc
	 *            the player that is using the item
	 * @param itemIndex
	 *            the inventory index of the item
	 * @return true if used successfully, false otherwise
	 */
	public boolean use(PlayerCharacter pc, int itemIndex) {
		Point p = directionFromPoint(getPosition(pc), pc.getDirection());
		if (pc.useItem(itemIndex, objects[p.x][p.y])) {
			return true;
		} else {
			return pc.useItem(itemIndex, pc);
		}
	}

	/**
	 * Removes a player from this map
	 * 
	 * @param pc
	 *            the player to remove
	 */
	public void removePlayer(PlayerCharacter pc) {
		if (!contains(pc)) {
			return;
		}
		Point pos = getPosition(pc);
		objects[pos.x][pos.y] = null;
	}

	/**
	 * Returns the height of the map
	 * 
	 * @return the height of the map
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the width of the map
	 * 
	 * @return the width of the map
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Creates a map of the Point:GameObject Point is where the gameObject is
	 * located : GameObject object.
	 * 
	 * @return a map of the Point:GameObject Point is where the gameObject is
	 *         located : GameObject object.
	 */
	public Map<Point, GameObject> getObjects() {
		Map<Point, GameObject> map = new LinkedHashMap<Point, GameObject>();
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (objects[x][y] != null) {
					map.put(new Point(x, y), objects[x][y]);
				}
			}
		}
		return map;
	}

	/**
	 * Returns the object at the given co-ordinates
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	private GameObject getObject(int x, int y) {
		if (isValidPoint(x, y)) {
			return objects[x][y];
		}
		return null;
	}

	/**
	 * Turns the tile bloody
	 * 
	 * @param p
	 *            the point to change the tile at
	 */
	private void bloodyTile(Point p) {
		assert(isValidPoint(p));
		if (tiles[p.x][p.y] == Tile.GRASS) {
			tiles[p.x][p.y] = Tile.GRASS_BLOODY;
		}
	}

	/**
	 * Finds and returns the position of a certain GameObject
	 * 
	 * @param go
	 *            the GameObject to find
	 * @return the position of the given GameObject if found, null otherwise
	 */
	public Point getPosition(GameObject go) {
		if (go == null) {
			return null;
		}
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				if (go.equals(objects[x][y])) {
					return new Point(x, y);
				}
			}
		}
		return null;
	}

	/**
	 * Returns all objects surrounding a given object
	 * 
	 * @param go
	 *            the original object
	 * @return set of all objects surround the original object
	 */
	public Set<GameObject> getSurroundingGameObjects(GameObject go) {
		Point pos = getPosition(go);
		if (pos != null) {
			return getSurroundingGameObjects(pos);
		} else {
			return new HashSet<GameObject>();
		}
	}

	/**
	 * Returns all objects surrounding a position
	 * 
	 * REQUIRES: the point be located on the map
	 * 
	 * @param pos
	 *            the original position
	 * @return set of all objects surrounding the original position
	 */
	public Set<GameObject> getSurroundingGameObjects(Point pos) {
		assert (isValidPoint(pos));
		Set<GameObject> set = new HashSet<GameObject>();
		if (pos.x > 0) {
			set.add(objects[pos.x - 1][pos.y]);
		}
		if (pos.y > 0) {
			set.add(objects[pos.x][pos.y - 1]);
		}
		if (pos.x < width - 1) {
			set.add(objects[pos.x + 1][pos.y]);
		}
		if (pos.y < height - 1) {
			set.add(objects[pos.x][pos.y + 1]);
		}
		return set;
	}

	/**
	 * Returns a setup of the map's objects
	 * 
	 * ENSURES: forall GameObject[] in return value, GameObject[].length is
	 * consistent
	 * 
	 * @return a GameObject[][] containing the map's objects
	 */
	abstract protected GameObject[][] initialize();

	/**
	 * Returns a setup of the map's tiles
	 * 
	 * ENSURES: forall Tile[] in return value, Tile[].length are consistent
	 * 
	 * @return a Tile[][] containing the map's tiles
	 */
	abstract protected Tile[][] initializeTiles();

	/**
	 * Returns true if the given point is a valid point in the map
	 * 
	 * @param p
	 *            the point to check
	 * @return true if the given point is a valid point in the map
	 */
	public boolean isValidPoint(Point p) {
		return isValidPoint(p.x, p.y);
	}

	/**
	 * Returns true if the given co-ordinate is a valid co-ordinate in the map.
	 * 
	 * @param x
	 *            the x co-ordinate to check
	 * @param y
	 *            the y co-ordinate to check
	 * @return true if the given co-ordinate is a valid co-ordinate in the map
	 */
	public boolean isValidPoint(int x, int y) {
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	/**
	 * Returns true if the given point represents a valid point for spawning.
	 * 
	 * @param p
	 *            the point to check
	 * @return true if the given point represents a valid point for spawning
	 */
	public boolean isValidSpawnPoint(Point p) {
		return isValidPoint(p.x, p.y);
	}

	/**
	 * Returns true if the given co-ordinates represents a valid point for
	 * spawning.
	 * 
	 * @param x
	 *            the x co-ordinate to check
	 * @param y
	 *            the y co-ordinate to check
	 * @return true if the given co-ordinates represents a valid point for
	 *         spawning
	 */
	public boolean isValidSpawnPoint(int x, int y) {
		if (x <= 0 || x >= width - 1) {
			return false;
		}
		if (y <= 0 || y >= height - 1) {
			return false;
		}
		if (getObject(x, y) != null) {
			return false;
		}
		return true;
	}

	/**
	 * Moves a given player in the given direction
	 * 
	 * @param pc
	 *            the player to move
	 * @param d
	 *            the direction to move the player in
	 */
	private void move(PlayerCharacter pc, Direction d) {
		Point old = pc.getPosition();
		Point oldCoord = pc.getArrayPosition();
		pc.move(d);
		Point source = getPosition(pc);
		assert (oldCoord.equals(source));
		Point destination = pc.getArrayPosition();
		if (!isValidPoint(destination.x, destination.y)) {
			pc.setPosition(old);
			return;
		}
		if (objects[destination.x][destination.y] != null
				&& objects[destination.x][destination.y] != pc) {
			pc.setPosition(old);
			return;
		}
		objects[source.x][source.y] = null;
		objects[destination.x][destination.y] = pc;
	}

	/**
	 * Returns a set of all NPCs currently in the map.
	 * 
	 * @return a set contains all the NPCs currently in the map.
	 */
	public Set<NonPlayerCharacter> npcs() {
		HashSet<NonPlayerCharacter> set = new HashSet<NonPlayerCharacter>();
		for (GameObject[] goArray : objects) {
			for (GameObject go : goArray) {
				if (go instanceof NonPlayerCharacter) {
					set.add((NonPlayerCharacter) go);
				}
			}
		}
		return set;
	}

	/**
	 * Sets the objects to the new objects map
	 * 
	 * @param objectsMap
	 *            map of Point: GameObject
	 */
	public void setObjects(Map<Point, GameObject> objectsMap) {
		this.objects = new GameObject[width][height];
		for (Entry<Point, GameObject> entry : objectsMap.entrySet()) {
			Point p = entry.getKey();
			GameObject object = entry.getValue();
			objects[p.x][p.y] = object; // adds the object at the given position
		}
	}

	/**
	 * Calculates and teleports the player to an adjacent map depending on the
	 * map and the teleEntrance they are standing on
	 * 
	 * @param teleEntrance
	 *            Point of the tele entrance of this map
	 * @param exitMapName
	 *            new Map of where the tele leads to
	 * @param teleExit
	 *            Point of the tele exit where the player spawns in the new map
	 */
	private void calculateTeleport(Point teleEntrance, String exitMapName,
			Point teleExit) {
		GameObject o = objects[teleEntrance.x][teleEntrance.y]; // Object at the
																// tele entrance
		if (o instanceof PlayerCharacter) {
			PlayerCharacter player = (PlayerCharacter) o;
			GameMap map = world.getMap(exitMapName); // Finds the exit Map
			if (map != null) {
				map.spawn(player, teleExit);
				objects[teleEntrance.x][teleEntrance.y] = null;
			}
		}
	}

	/**
	 * Returns the event flags as an int. To be saved and loaded later
	 * 
	 * @return int representing the state of the events flags
	 */
	abstract public int saveEventFlags();

	/**
	 * Loads the event flags from an int
	 * 
	 * @param flags
	 *            the flags to load, stored as an int
	 */
	abstract public void loadEventFlags(int flags);

	/**
	 * Returns true if the map has been cleared
	 * 
	 * @return true if the map has been cleared
	 */
	public boolean isClear() {
		for (NonPlayerCharacter npc : this.npcs()) {
			if (!npc.isDead()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Calculates and returns a point. The pointed returned is one step from the
	 * given point in the given direction
	 * 
	 * @param p
	 *            the origin point
	 * @param d
	 *            the direction to move in
	 * @return the point that is one step from the origin point in the given
	 *         direction
	 */
	public static Point directionFromPoint(Point p, Direction d) {
		switch (d) {
		case NORTH:
			return new Point(p.x, p.y - 1);
		case EAST:
			return new Point(p.x + 1, p.y);
		case SOUTH:
			return new Point(p.x, p.y + 1);
		case WEST:
			return new Point(p.x - 1, p.y);
		default:
			return null;
		}
	}

	/**
	 * Cleans up the map, removing any dead npcs still on the map
	 */
	public void cleanUp() {
		for (NonPlayerCharacter npc : npcs()) {
			if (npc.isDead()) {
				Point point = getPosition(npc);
				objects[point.x][point.y] = null;
				bloodyTile(point);
			}
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
