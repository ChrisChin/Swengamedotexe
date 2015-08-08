package gameModel.gameWorld.maps;


import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.items.BronzeKey;
import gameModel.gameObjects.landscape.Tree;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;


import java.awt.Point;

/**
 * North West Map of the game. Map where players spawn.
 * @author Chris Chin (chinchri1)
 *  ____ ___ ____
 * |_NW_|_N_|_NE_|
 * |__W_|_M_|__E_| Current Map (NW)
 * |_SW_|_S_|_SE_|
 *
 */
public class NorthWestMap extends GameMap {
	private static final long serialVersionUID = 2215161396267031602L;
	private BronzeKey key;
	private Tree tree;

	/**
	 * Represents the North West Map of the game. Size 8x8
	 * This map contains no drops
	 *
	 * @param world
	 *            the world this map belongs in
	 */
	public NorthWestMap(World world) {
		super(world, new Point(1, 1));
		eastMapName = "NorthMap";
		southMapName = "WestMap";
	}

	@Override
	protected GameObject[][] initialize() {
		key = new BronzeKey("21");
		tree = new Tree();

		return new GameObject[][] {
				{ tree, tree, tree, tree, tree, tree, tree, tree },
				{ tree, null, null, null, null, null, null, tree },
				{ tree, null, tree, null, tree, tree, null, tree },
				{ tree, null, tree, null, key, tree, null, tree },
				{ tree, null, tree, tree, tree, tree, null, null },
				{ tree, null, tree, tree, tree, tree, null, tree },
				{ tree, null, null, null, null, null, null, tree },
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
		return 0;
	}

	@Override
	public void loadEventFlags(int flags) {
	}

}
