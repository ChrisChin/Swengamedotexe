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
 * East Map of the game. Map where players spawn.
 *
 * @author Chris Chin (chinchri1)
 * ____ ___ ____
 * |_NW_|_N_|_NE_|
 * |__W_|_M_|__E_| Current Map (E)
 * |_SW_|_S_|_SE_|
 *
 */
public class EastMap extends GameMap {
	private static final long serialVersionUID = 8307187773745306032L;
	private Tree tree;
	private Slime slime;
	private BronzeChest chest2 = new BronzeChest("2"); // chest dropped when the
														// slime is killed
	private boolean chest2Spawned = false;

	/**
	 * Represents the East Map of the game. Size 8x8 This map contains a room of
	 * slimes where one will drop chest2
	 *
	 * @param world
	 *            the world this map belongs in
	 */
	public EastMap(World world) {
		super(world, new Point(1, 1));
		northMapName = "NorthEastMap";
		southMapName = "SouthEastMap";
		westMapName = "StartingMap";
	}

	@Override
	protected GameObject[][] initialize() {
		tree = new Tree();
		slime = new Slime();
		return new GameObject[][] {
				{ tree, tree, tree, tree, null, tree, tree, tree },
				{ tree, new BronzeKey("2"), null, null, null, new Slime(), new Slime(), tree },
				{ tree, null, new Slime(), new Slime(), new Slime(),
						new Slime(), new Slime(), tree },
				{ tree, null, new Slime(), new Slime(), new Slime(),
						new Slime(), slime, tree },
				{ null, null, new Slime(), new Slime(), new Slime(),
						new Slime(), new Slime(), null },
				{ tree, null, new Slime(), new Slime(), new Slime(),
						new Slime(), new Slime(), tree },
				{ tree, null, null, null, null, new Slime(), new Slime(), tree },
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
		if (!chest2Spawned) {
			Point spawnPoint = new Point(3, 6);
			if (!(this.getObjects().get(spawnPoint) instanceof Slime)) { //Slime is dead
				this.spawn(chest2, spawnPoint);
				chest2Spawned = true;
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
		value += (chest2Spawned ? 1 : 0);
		return value;
	}

	@Override
	public void loadEventFlags(int flags) {
		chest2Spawned = (flags & 1) == 1;
	}

}
