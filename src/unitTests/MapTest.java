package unitTests;

import static org.junit.Assert.assertFalse;
import gameModel.gameObjects.GameObject;
import gameModel.gameWorld.GameMap;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Jiaheng Wang (wangjiah)
 *
 */
public class MapTest {

	GameMap map;

	@SuppressWarnings("serial")
	@Before
	public void initialize() {
		this.map = new GameMap(null, new Point(1, 1)) {

			@Override
			protected GameObject[][] initialize() {
				return new GameObject[][] { { null, null, null },
						{ null, null, null }, { null, null, null } };
			}

			@Override
			protected Tile[][] initializeTiles() {
				return new Tile[][] { { null, null, null },
						{ null, null, null }, { null, null, null } };
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
	public void testDoesNotContainNull() {
		assertFalse(map.contains(null));
	}

	
}
