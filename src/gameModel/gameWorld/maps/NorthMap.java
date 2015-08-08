package gameModel.gameWorld.maps;


import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.landscape.Tree;
import gameModel.gameObjects.monsters.Slime;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;



import java.awt.Point;

/**
 * North Map of the game. Map where players spawn.
 * @author Chris Chin (chinchri1)
 *  ____ ___ ____
 * |_NW_|_N_|_NE_|
 * |__W_|_M_|__E_| Current Map (N)
 * |_SW_|_S_|_SE_|
 *
 */
public class NorthMap extends GameMap {
	private static final long serialVersionUID = -2115096402206044542L;
	private Tree tree;
	private Slime slime;

	/**
	 * Represents the North Map of the game. Size 8x8
	 * This map contains no drops
	 *
	 * @param world
	 *            the world this map belongs in
	 */
	public NorthMap(World world) {
		super(world, new Point(1, 1));
		westMapName = "NorthWestMap";
		eastMapName = "NorthEastMap";
		southMapName = "StartingMap";				
	}

	@Override
	protected GameObject[][] initialize() {
		tree = new Tree();
		slime = new Slime();
		return new GameObject[][] {
				{ tree, tree, tree, tree, null, tree, tree, tree },
				{ tree, null, null, null, null, tree, null, tree },
				{ tree, null, tree, null, tree, tree, null, tree },
				{ tree, null, tree, slime, null, null, null, tree },
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
