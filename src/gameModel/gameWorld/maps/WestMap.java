package gameModel.gameWorld.maps;


import game.userInterface.RendererWindow.Renderer.Direction;
import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameObjects.items.BronzeKey;
import gameModel.gameObjects.items.Potion;
import gameModel.gameObjects.landscape.Barrel;
import gameModel.gameObjects.landscape.Tree;
import gameModel.gameObjects.monsters.Slime;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.World;





import java.awt.Point;

/**
 * West Map of the game. Map where players spawn.
 * @author Chris Chin (chinchri1)
 *  ____ ___ ____
 * |_NW_|_N_|_NE_|
 * |__W_|_M_|__E_| Current Map (W)
 * |_SW_|_S_|_SE_|
 *
 */
public class WestMap extends GameMap {
	private static final long serialVersionUID = -5234448616726511062L;
	private Tree tree;
	private Slime slime1;
	private Slime slime2;
	private Slime slime3;
	private Barrel barrel;
	private Potion potion = new Potion(); // Potion that is dropped when the barrel is killed
	private boolean potionSpawned = false;
	/**
	 * Represents the West Map of the game. Size 8x8
	 * This map contains a room of barrels where one drops a potion
	 *
	 * @param world
	 *            the world this map belongs in
	 */
	public WestMap(World world) {
		super(world, new Point(1, 1));
		northMapName = "NorthWestMap";
		eastMapName = "StartingMap";
		southMapName = "SouthWestMap";
	}

	@Override
	protected GameObject[][] initialize() {
		tree = new Tree();
		slime1 = new Slime();
		slime2 = new Slime();
		slime3 = new Slime();
		barrel = new Barrel();

		return new GameObject[][] {
				{ tree, tree, tree, tree,tree, tree, tree, tree },
				{ tree, null, null, null, null, tree, null, tree },
				{ tree, null, tree, tree, tree, tree, null, tree },
				{ tree, null, tree, barrel, slime1, tree, null, tree },
				{ null, null, tree, tree, new Barrel(), tree, slime2, null  },
				{ tree, null, tree, new Barrel(), new Barrel(), tree, null, tree },
				{ tree, null, null, null, slime3, null, null, tree },
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
		if (!potionSpawned) {
			Point spawnPoint = new Point(3,3);
			if (!(this.getObjects().get(spawnPoint) instanceof Barrel)) { //Barrel is dead
				this.spawn(potion, spawnPoint);
				potionSpawned = true;
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
		value += (potionSpawned ? 1 : 0);
		return value;
	}

	@Override
	public void loadEventFlags(int flags) {
		potionSpawned = (flags & 1) == 1;
	}

}
