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
 * South Map of the game. Map where players spawn.
 * @author Chris Chin (chinchri1)
 *  ____ ___ ____
 * |_NW_|_N_|_NE_|
 * |__W_|_M_|__E_| Current Map (S)
 * |_SW_|_S_|_SE_|
 *
 */
public class SouthMap extends GameMap {
	private static final long serialVersionUID = -2115096402206044542L;
	private Tree tree;
	private Slime slime;
	private BronzeKey key1;
	private BronzeChest chest1 = new BronzeChest("1"); //chest that is dropped when slime is killed
	private boolean chest1Spawned = false;

	/**
	 * Represents the South Map of the game. Size 8x8
	 * This map contains key1 and a slime that drops chest1
	 *
	 * @param world
	 *            the world this map belongs in
	 */
	public SouthMap(World world) {
		super(world, new Point(1, 1));
		northMapName = "StartingMap";
		westMapName = "SouthWestMap";
		eastMapName = "SouthEastMap";
	}

	@Override
	protected GameObject[][] initialize() {
		tree = new Tree();
		slime = new Slime();
		key1 = new BronzeKey("1");
		return new GameObject[][] {
				{ tree, tree, tree, tree, null, tree, tree, tree },
				{ tree, null, null, null, null, null, null, tree },
				{ tree, null, null, null, tree, null, null, tree },
				{ tree, null, tree, slime, tree, tree, null, tree },
				{ null, null, new Slime(), key1, new Slime(), null, null, tree },
				{ tree, null, tree, new Slime(), tree, tree, null, tree },
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
	public void tick() {
		super.tick();
		if (!chest1Spawned) {
			Point spawnPoint = new Point(3,3);
			if (!(this.getObjects().get(spawnPoint) instanceof Slime)) { //Slime is dead
				this.spawn(chest1, spawnPoint);
				chest1Spawned = true;
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
		value += (chest1Spawned ? 1 : 0);
		return value;
	}

	@Override
	public void loadEventFlags(int flags) {
		chest1Spawned = (flags & 1) == 1;
	}

}