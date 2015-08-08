package gameModel.gameWorld;

import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameWorld.maps.*;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * The main model of the world. Holds all the maps and all the players in the
 * game.
 * 
 * @author Jiaheng Wang (wangjiah), Chris Chin (chinchri1)
 * 
 */
public class World implements Serializable, Runnable {

	/**
	 *
	 */
	private static final long serialVersionUID = 3673662128751013178L;
	public static final boolean PVPActive = false;
	public static final int TICKRATE = 16;
	private Map<String, GameMap> maps;
	private Set<PlayerCharacter> playersSet;

	public World() {
		playersSet = new HashSet<PlayerCharacter>();
		maps = new LinkedHashMap<String, GameMap>();

		this.addMap(new NorthEastMap(this));
		this.addMap(new NorthMap(this));
		this.addMap(new NorthWestMap(this));

		this.addMap(new EastMap(this));
		this.addMap(new StartingMap(this));
		this.addMap(new WestMap(this));

		this.addMap(new SouthEastMap(this));
		this.addMap(new SouthMap(this));
		this.addMap(new SouthWestMap(this));
	}

	/**
	 * Adds a map to the world
	 * 
	 * @param gm
	 *            the map to add
	 */
	private void addMap(GameMap gm) {
		if (maps.containsKey(gm.getClass().getSimpleName())) {
			throw new IllegalStateException(
					"Cannot have two maps with the same name: "
							+ gm.getClass().getSimpleName());
		}
		maps.put(gm.getClass().getSimpleName(), gm);
	}

	/**
	 * Returns the PlayerCharacter object associated with the given name
	 * 
	 * @param name
	 *            the name of the player to return
	 * @return the PlayerCharacter object associated with the given name
	 */
	public PlayerCharacter getPlayer(String name) {
		for (PlayerCharacter pc : playersSet) {
			if (pc.playerName.equals(name)) {
				return pc;
			}
		}
		return null;
	}

	/**
	 * Returns true if if a player with the name given exists
	 * 
	 * @param name
	 *            the name to look for
	 * @return true if name exists, false otherwise
	 */
	public boolean containsPlayer(String name) {
		for (PlayerCharacter pc : playersSet) {
			if (pc.playerName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds a new player to the game
	 * 
	 * @param character
	 *            the character (swordsman etc.) of that player
	 */
	public void addPlayer(PlayerCharacter character) {
		playersSet.add(character);
		respawn(character);
	}

	/**
	 * Returns a map of players currently existing in the game
	 * 
	 * @return the map of players
	 */
	public Set<PlayerCharacter> getPlayers() {
		return playersSet;
	}

	/**
	 * Sets the map of players to the new map
	 * 
	 * @param playersSet
	 *            sets the players in this world to the set given
	 */
	public void setPlayers(Set<PlayerCharacter> playersSet) {
		this.playersSet = playersSet;
	}

	/**
	 * Disconnects a player from the world.
	 * 
	 * @param pc
	 *            the playerCharacter to disconnect
	 */
	public void disconnectPlayer(PlayerCharacter pc) {
		if (getMap(pc) != null) {
			getMap(pc).removePlayer(pc);
		}
		pc.setPosition(new Point(-1, -1));
	}

	/**
	 * Returns the map holding the given player
	 * 
	 * @param player
	 *            the player to search for
	 * @return the GameMap holding the player given if exists, null otherwise
	 */
	public GameMap getMap(PlayerCharacter player) {
		for (GameMap map : maps.values()) {
			if (map.contains(player)) {
				return map;
			}
		}
		return null;
	}

	/**
	 * Returns all the GameMaps currently in the game
	 * 
	 * @return all the GameMaps currently in the game
	 */
	public Set<GameMap> getMaps() {
		return new LinkedHashSet<GameMap>(maps.values());
	}

	/**
	 * Gets a GameMap from a given name
	 * 
	 * @param mapName
	 *            the name of the map to return
	 * @return the map with the given name
	 */
	public GameMap getMap(String mapName) {
		return maps.get(mapName);
	}

	/**
	 * Sets the maps to the new maps
	 * 
	 * @param maps
	 *            the set of maps to set this World's set of maps to
	 */
	public void setMaps(Set<GameMap> maps) {
		this.maps.clear();
		for (GameMap gm : maps) {
			addMap(gm);
		}
	}

	/**
	 * Respawns the given player in the starting map. Used for when the player
	 * dies.
	 * 
	 * @param pc
	 *            the PlayerCharacter to be respawned
	 */
	public void respawn(PlayerCharacter pc) {
		GameMap gm = maps.get(StartingMap.class.getSimpleName());
		gm.spawn(pc);
		pc.recover(99999);
	}

	/**
	 * Returns a string of GameMap names that have been cleared
	 * 
	 * @return a string of GameMap names that have been cleared
	 */
	public String clearedMaps() {
		String clearedMaps = "";
		boolean first = true;
		int clearCount = 0;
		for (Map.Entry<String, GameMap> entry : maps.entrySet()) {
			if (entry.getValue().isClear()) {
				if (!first) {
					clearedMaps += ", ";
				}
				first = false;
				clearedMaps += entry.getKey();
				clearCount++;
			}
		}
		if (maps.size() <= clearCount) {
			return "All maps cleared";
		}
		if (first == true) {
			return "No maps cleared";
		}
		return clearedMaps;
	}

	/**
	 * Returns a string of GameMap names that have not been cleared
	 * 
	 * @return a string of GameMap names that have not been cleared
	 */
	public String unclearedMaps() {
		String unclearedMaps = "";
		boolean first = true;
		for (Map.Entry<String, GameMap> entry : maps.entrySet()) {
			if (!entry.getValue().isClear()) {
				if (!first) {
					unclearedMaps += ", ";
				}
				first = false;
				unclearedMaps += entry.getKey();
			}
		}
		if (first == true) {
			return "All maps cleared";
		}
		return unclearedMaps;
	}

	/**
	 * Returns true if all the maps in the world have been cleared
	 * 
	 * @return true if all the maps in the world have been cleared
	 */
	public boolean allMapsClear() {
		for (GameMap gm : maps.values()) {
			if (!gm.isClear()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void run() {
		while (true) {
			for (GameMap gm : maps.values()) {
				gm.tick();
			}
			try {
				Thread.sleep(TICKRATE);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
