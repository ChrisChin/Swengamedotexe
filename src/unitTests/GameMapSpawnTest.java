package unitTests;

import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameWorld.GameMap;

import java.awt.Image;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Jiaheng Wang (wangjiah)
 *
 */
public class GameMapSpawnTest {

	GameMap map;

	@SuppressWarnings("serial")
	@Before
	public void initialize() {
		this.map = new GameMap(null, new Point(1, 1)) {

			@Override
			protected GameObject[][] initialize() {
				return new GameObject[][] { { null, null, null, null },
						{ null, null, null, null }, { null, null, null, null },
						{ null, null, null, null } };
			}

			@Override
			protected Tile[][] initializeTiles() {
				return new Tile[][] { { null, null, null, null },
						{ null, null, null, null }, { null, null, null, null },
						{ null, null, null, null } };
			}

			@Override
			public String toString() {
				for (GameObject[] gos : objects) {
					System.out.println(gos);
				}
				return null;
			}

			@Override
			public int saveEventFlags() {
				return 0;
			}

			@Override
			public void loadEventFlags(int flags) {
			}

		};
	}

	@Test
	public void testSpawns() {
		for (int x = 0; x < 10; ++x) {
			map.spawn(new TempPlayer(x));
		}
		map.toString();
	}

	@SuppressWarnings("serial")
	private class TempPlayer extends PlayerCharacter {
		public TempPlayer(Integer playerName) {
			super(1, 1, 1, playerName.toString());
		}

		@Override
		public Image getImage() {
			return null;
		}

		@Override
		public String toString() {
			return playerName;
		}
	}
}
