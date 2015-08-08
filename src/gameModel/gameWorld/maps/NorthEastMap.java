package gameModel.gameWorld.maps;


import game.userInterface.RendererWindow.Renderer.Direction;
import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameObjects.items.BronzeKey;
import gameModel.gameObjects.items.Potion;
import gameModel.gameObjects.landscape.BronzeChest;
import gameModel.gameObjects.landscape.Tree;
import gameModel.gameObjects.monsters.Slime;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;





import java.awt.Point;

/**
 * North East Map of the game. Map where players spawn.
 * @author  Chris Chin (chinchri1)
 *  ____ ___ ____
 * |_NW_|_N_|_NE_|
 * |__W_|_M_|__E_| Current Map (NE)
 * |_SW_|_S_|_SE_|
 *
 */
public class NorthEastMap extends GameMap {
	private static final long serialVersionUID = 3639820236131466212L;
	private Tree tree;
	private Slime slime;
	private BronzeKey key2 = new BronzeKey("2"); // key that is dropped when slime is killed
	private BronzeChest chest2;
	private boolean key2Spawned = false;

	/**
	 * Represents the North East Map of the game. Size 8x8
	 * This Map contains a slime that drops key2
	 *
	 * @param world
	 *            the world this map belongs in
	 */
	public NorthEastMap(World world) {
		super(world, new Point(1, 2));
		westMapName = "NorthMap";
		southMapName = "EastMap";
	}

	@Override
	protected GameObject[][] initialize() {
		tree = new Tree();
		slime = new Slime();
		chest2 = new BronzeChest("2");

		return new GameObject[][] {
				{ tree, tree, tree, tree, null, tree, tree, tree },
				{ tree, tree, chest2, tree, null, null, null, tree },
				{ tree, null, null, null, null, tree, null, tree },
				{ tree, null, null, slime, null, null, null, tree },
				{ tree, null, null, tree, null, null, null, null },
				{ tree, null, null, null, null, tree, null, tree },
				{ tree, null, null, null, null, null, null, tree },
				{ tree, tree, tree, tree, tree, tree, tree, tree } };
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
	public void tick() {
		super.tick();
		if (!key2Spawned) {
			Point spawnPoint = new Point(3,3);
			if (!(this.getObjects().get(spawnPoint) instanceof Slime)) { //Slime is dead
				this.spawn(key2, spawnPoint);
				key2Spawned = true;
			}
		}
	}

	@Override
	public boolean use(PlayerCharacter pc, int itemIndex) {
		Direction d = pc.getDirection();
		if (pc.getItemAtIndex(itemIndex) instanceof BronzeKey) {
			Point target = directionFromPoint(getPosition(pc), d);
			if (!isValidPoint(target)) {
				return false;
			}
			if (pc.useItem(itemIndex, objects[target.x][target.y])) {
				objects[target.x][target.y] = new Potion();
				return true;
			}
			return false;
		} else
			return super.use(pc, itemIndex);
	}

	@Override
	public int saveEventFlags() {
		int value = 0;
		value += (key2Spawned ? 1 : 0);
		return value;
	}

	@Override
	public void loadEventFlags(int flags) {
		key2Spawned = (flags & 1) == 1;
	}

}
