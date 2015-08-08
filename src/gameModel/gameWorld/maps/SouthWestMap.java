package gameModel.gameWorld.maps;


import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.landscape.Tree;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;

import java.awt.Point;

/**
 * South West Map of the game. Map where players spawn.
 * @author Chris Chin (chinchri1)
 *  ____ ___ ____
 * |_NW_|_N_|_NE_|
 * |__W_|_M_|__E_| Current Map (SW)
 * |_SW_|_S_|_SE_|
 *
 */
public class SouthWestMap extends GameMap {
	private static final long serialVersionUID = -2115096402206044542L;
	private Tree tree;

	/**
	 * Represents the South West Map of the game. Size 8x8
	 * This map contains no items
	 *
	 * @param world
	 *            the world this map belongs in
	 */
	public SouthWestMap(World world) {
		super(world, new Point(1, 1));
		northMapName = "WestMap";
		eastMapName = "SouthMap";
	}

	@Override
	protected GameObject[][] initialize() {
		tree = new Tree();
		return new GameObject[][] {
				{ tree, tree, tree, tree, tree, tree, tree, tree },
				{ tree, tree, null, null, null, null, tree, tree },
				{ tree, null, null, null, tree, null, null, tree },
				{ tree, null, null, tree, null, tree, null, tree },
				{ null, null, tree, null, tree, null, tree, tree },
				{ tree, tree, null, tree, null, tree, null, tree },
				{ tree, null, tree, null, null, null, tree, tree },
				{ tree, tree, tree, tree, null, tree, tree, tree } };
	}

	@Override
	protected Tile[][] initializeTiles() {
		return new Tile[][] {
				{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS },
				{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS },
				{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS },
				{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS },
				{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS },
				{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS },
				{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS },
				{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS } };
	}

	@Override
	public int saveEventFlags() {
		int value = 0;
		return value;
	}

	@Override
	public void loadEventFlags(int flags) {
	}

}