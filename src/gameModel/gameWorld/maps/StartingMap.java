package gameModel.gameWorld.maps;

import game.userInterface.RendererWindow.Renderer.Direction;
import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameObjects.items.BronzeKey;
import gameModel.gameObjects.items.Potion;
import gameModel.gameObjects.landscape.Barrel;
import gameModel.gameObjects.landscape.BronzeChest;
import gameModel.gameObjects.landscape.Tree;
import gameModel.gameObjects.monsters.Slime;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;

import java.awt.Point;

/**
 * Starting Map of the game. Map where players spawn.
 * 
 * @author Jiaheng Wang (wangjiah) , Chris Chin (chinchri1) 
 * ____ ___ ____
 *|_NW_|_N_|_NE_| 
 *|__W_|_M_|__E_| Current Map (M) 
 *|_SW_|_S_|_SE_|
 * 
 */
public class StartingMap extends GameMap {
	private static final long serialVersionUID = -1016206097829247742L;
	private Barrel keyBarrel;
	private BronzeKey key21 = new BronzeKey("21"); // key that is dropped when
													// slime0 is killed
	private BronzeChest chest21 = new BronzeChest("21"); // chest that is
															// dropped when
															// slime1 is killed
	private BronzeKey key42 = new BronzeKey("42"); // key that is dropped when
													// keyBarrel is killed
	private BronzeChest chest42;
	private Slime slime0;
	private Slime slime1;
	private Tree tree;
	private boolean chest21Spawned = false;
	private boolean key21Spawned = false;
	private boolean key42Spawned = false;

	/**
	 * Represents the StartingMap of the game. Size 8x8 This map contains
	 * keyBarrel which drops key 42 slime0 which drops key21 slime1 which drops
	 * chest21
	 * 
	 * @param world
	 *            the world this map belongs in
	 */
	public StartingMap(World world) {
		super(world, new Point(1, 1));
		northMapName = "NorthMap";
		westMapName = "WestMap";
		eastMapName = "EastMap";
		southMapName = "SouthMap";
	}

	@Override
	protected GameObject[][] initialize() {
		chest42 = new BronzeChest("42");
		keyBarrel = new Barrel();
		tree = new Tree();
		slime0 = new Slime();
		slime1 = new Slime();
		return new GameObject[][] {
				{ tree, tree, tree, tree, null, tree, tree, tree },
				{ tree, null, null, keyBarrel, null, null, chest42, tree },
				{ tree, null, null, null, null, null, null, tree },
				{ tree, null, null, null, null, null, slime1, tree },
				{ null, null, null, null, new Slime(), null, null, null },
				{ tree, null, null, null, null, null, null, tree },
				{ tree, null, null, null, null, new Slime(), slime0, tree },
				{ tree, tree, tree, tree, null, tree, tree, tree } };
	}

	@Override
	protected Tile[][] initializeTiles() {
		return new Tile[][] {
				{ Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS },
				{ Tile.GRASS, Tile.DIRT, Tile.DIRT, Tile.DIRT, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS },
				{ Tile.GRASS, Tile.DIRT, Tile.DIRT, Tile.GRASS, Tile.GRASS,
						Tile.GRASS, Tile.GRASS, Tile.GRASS },
				{ Tile.GRASS, Tile.DIRT, Tile.GRASS, Tile.GRASS, Tile.GRASS,
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
		if (!chest21Spawned) {
			Point spawnPoint =  new Point(3, 6);
			if (!(this.getObjects().get(spawnPoint) instanceof Slime)) { //slime1 is dead
				this.spawn(chest21,spawnPoint);
				chest21Spawned = true;
			}
		}
		if (!key42Spawned) {
			Point spawnPoint =  new Point(1, 3);
			if (!(this.getObjects().get(spawnPoint) instanceof Barrel)) { //KeyBarrel is dead
				System.out.println("drop key");
				this.spawn(key42, spawnPoint);
				key42Spawned = true;
			}
		}
		if (!key21Spawned) {
			Point spawnPoint = new Point(6, 6);
			if (!(this.getObjects().get(spawnPoint) instanceof Slime)) { //slime0 is dead
				this.spawn(key21, spawnPoint);
				key21Spawned = true;
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
		value += (chest21Spawned ? 1 : 0);
		value += (key21Spawned ? 1 : 0) << 1;
		value += (key42Spawned ? 1 : 0) << 2;
		return value;
	}

	@Override
	public void loadEventFlags(int flags) {
		chest21Spawned = (flags & 1) == 1;
		key21Spawned = ((flags >> 1) & 1) == 1;
		key42Spawned = ((flags >> 2) & 1) == 1;
	}

}
