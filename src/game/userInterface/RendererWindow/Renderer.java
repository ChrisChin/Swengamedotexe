package game.userInterface.RendererWindow;

import game.Network.Client;
import game.Network.Packet;
import game.userInterface.GuiHelper;
import game.userInterface.applicationWindow.ChatBox;
import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameWorld.GameMap.PlayerAction;
import gameModel.gameWorld.GameMap.Tile;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;

/**
 * An extension of the JPanel object which can be used to display the game on.
 * 
 * @author Geoffrey Longuet (longuegeof)
 * 
 */
public class Renderer extends JPanel implements ActionListener {
	private static final long serialVersionUID = 2255386580775672451L;
	public static final int PANEL_WIDTH = 1024;
	public static final int PANEL_HEIGHT = 512;

	public static final int TILE_WIDTH = 128;
	public static final int TILE_HEIGHT = TILE_WIDTH / 2;

	public static final int REFRESH_INTERVAL = 16;
	public static final Color BG_COLOR = new Color(75, 75, 75);
	public static final int HEALTH_BAR_SIZE = 60;
	private static Image BLANK_IMG = GuiHelper.loadImage("tiles/blank.png");
	private Tile[][] mapTiles;
	private Tile[][] transMapTiles;
	private GameObject[][] mapObjects;
	private GameObject[][] transMapObjects;
	private List<Image> tiles;
	private PlayerCharacter currentPlayer;
	private Point transPlayer;
	private List<Image> compass;
	private Direction currentDir = Direction.NORTH;
	private Client client;
	private Map<PlayerCharacter, AnimationSet> animations;
	private long KEY_PRESS_INTERVAL = 100; // ms
	private long currentCooldown = KEY_PRESS_INTERVAL;// ms
	private ChatBox chatbox;

	public enum Direction {
		NORTH, EAST, SOUTH, WEST
	}

	private enum DirectionKeys {
		w, d, s, a
	}

	/**
	 * Constructs a Renderer panel. This can be used as a regular swing
	 * component. Will draw a 2D array of tiles and objects in isometric space,
	 * with full support for rotations.
	 * 
	 * @param mapTiles
	 *            The 2D array of tiles to draw in isometric space.
	 * @param mapObjects
	 *            The 2D array of GameObjects to draw in isometric space.
	 * @param client
	 *            The client which the Renderer will use to communicate player
	 *            updates to the server.
	 */
	public Renderer(Tile[][] mapTiles, GameObject[][] mapObjects, Client client) {
		super();
		initImages();
		initKeyBindings();
		setFocusable(true);
		this.client = client;
		this.mapTiles = translateMap(mapTiles, Direction.NORTH);// clone
		this.mapObjects = translateMap(mapObjects, Direction.NORTH);// clone
		this.transMapTiles = translateMap(this.mapTiles, currentDir);
		this.transMapObjects = translateMap(this.mapObjects, currentDir);
		this.animations = new HashMap<PlayerCharacter, AnimationSet>();
		updatePlayerCharacter(client.getPc());
		transPlayer = translatePlayerPosition(currentPlayer.getPosition(),
				currentDir);
		Timer timer = new Timer(REFRESH_INTERVAL, this);
		timer.start();
	}
	
	/**
	 * 
	 * @param mapTiles
	 *            The 2D array of tiles to draw in isometric space.
	 * @param mapObjects
	 *            The 2D array of GameObjects to draw in isometric space.
	 * @param plyr
	 *            The testing PlayerCharacter.
	 */
	public Renderer(Tile[][] mapTiles, GameObject[][] mapObjects, PlayerCharacter plyr) {
		super();
		initImages();
		initKeyBindings();
		setFocusable(true);
		this.mapTiles = translateMap(mapTiles, Direction.NORTH);// clone
		this.mapObjects = translateMap(mapObjects, Direction.NORTH);// clone
		this.transMapTiles = translateMap(this.mapTiles, currentDir);
		this.transMapObjects = translateMap(this.mapObjects, currentDir);
		this.animations = new HashMap<PlayerCharacter, AnimationSet>();
		updatePlayerCharacter(plyr);
		transPlayer = translatePlayerPosition(currentPlayer.getPosition(),
				currentDir);
		}

	/**
	 * Set the current chatBox which the Renderer will write object descriptions
	 * to.
	 * 
	 * @param chatbox
	 *            The ChatBox that the Renderer will use.
	 */
	public void setChatBox(ChatBox chatbox) {
		this.chatbox = chatbox;
	}

	/**
	 * Updates the objects and tiles that will be displayed on the map. Expects
	 * perfectly square 2D arrays.
	 * 
	 * @param mapTiles
	 *            The tiles to display on the map.
	 * @param mapObjects
	 *            The objects to display on the map.
	 */
	public void updateMap(Tile[][] mapTiles, GameObject[][] mapObjects) {
		this.mapTiles = mapTiles;
		this.mapObjects = mapObjects;
		this.transMapTiles = translateMap(this.mapTiles, currentDir);
		this.transMapObjects = translateMap(this.mapObjects, currentDir);
	}

	/**
	 * Updates the objects that will be displayed on the map. Expects a
	 * perfectly square 2D array.
	 * 
	 * @param mapObjects
	 *            The objects to display on the map.
	 */
	public void updateMap(GameObject[][] mapObjects) {
		this.mapObjects = mapObjects;
		this.transMapObjects = translateMap(this.mapObjects, currentDir);
	}

	/**
	 * Update the current player which is considered the main character for this
	 * Renderer.
	 * 
	 * @param pc
	 *            The character to use in place of the existing main character
	 *            for this Renderer.
	 */
	public void updatePlayerCharacter(PlayerCharacter pc) {
		this.currentPlayer = pc;
		transPlayer = translatePlayerPosition(currentPlayer.getPosition(),
				currentDir);
	}

	/**
	 * Helper method to initialise the default key bindings to the correct
	 * Actions.
	 */
	private void initKeyBindings() {
		int WINDOWFOCUS = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap inputMap = this.getInputMap(WINDOWFOCUS);
		ActionMap actionMap = this.getActionMap();

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
		actionMap.put(LEFT, left);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
		actionMap.put(RIGHT, right);

		inputMap.put(KeyStroke.getKeyStroke('w'), W);
		actionMap.put(W, wPress);

		inputMap.put(KeyStroke.getKeyStroke('a'), A);
		actionMap.put(A, aPress);

		inputMap.put(KeyStroke.getKeyStroke('s'), S);
		actionMap.put(S, sPress);

		inputMap.put(KeyStroke.getKeyStroke('d'), D);
		actionMap.put(D, dPress);

		inputMap.put(KeyStroke.getKeyStroke('f'), F);
		actionMap.put(F, fPress);

		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), SPACE);
		actionMap.put(SPACE, spacePress);

		inputMap.put(KeyStroke.getKeyStroke('e'), E);
		actionMap.put(E, ePress);
	}

	/**
	 * Helper method to load in frequent-use images.
	 */
	private void initImages() {
		tiles = new ArrayList<Image>();
		for (int i = 0; i < Tile.values().length; i++) {
			tiles.add(GuiHelper.loadImage("tiles/" + i + ".png"));
		}

		compass = new ArrayList<Image>();
		compass.add(GuiHelper.loadImage("compass/north.png"));
		compass.add(GuiHelper.loadImage("compass/east.png"));
		compass.add(GuiHelper.loadImage("compass/south.png"));
		compass.add(GuiHelper.loadImage("compass/west.png"));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		transPlayer = translatePlayerPosition(currentPlayer.getPosition(),
				currentDir);
		Point defIsoPt = twoDtoIso(transPlayer);
		g.setColor(BG_COLOR);
		g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		// DRAW TILES
		for (int x = 0; x < transMapObjects.length; x++) {
			for (int y = 0; y < transMapObjects[x].length; y++) {
				Image tileImg = tiles.get(transMapTiles[x][y].ordinal());
				int calX = x * TILE_HEIGHT;
				int calY = y * TILE_HEIGHT;
				Point p = twoDtoIso(new Point(calX, calY));
				p.y = p.y + (PANEL_HEIGHT / 2) - defIsoPt.y;
				p.x = p.x + (PANEL_WIDTH / 2) - (TILE_WIDTH / 2) - defIsoPt.x;
				g.drawImage(tileImg, p.x, p.y, TILE_WIDTH, TILE_HEIGHT, null);
			}
		}
		// DRAW OBJECTS AND PLAYERS
		for (int x = 0; x < transMapObjects.length; x++) {
			for (int y = 0; y < transMapObjects[x].length; y++) {
				Image objImg = (transMapObjects[x][y] == null ? objImg = BLANK_IMG
						: transMapObjects[x][y].getImage());
				objImg = (objImg == null ? BLANK_IMG : objImg);
				if (transMapObjects[x][y] instanceof PlayerCharacter) {
					PlayerCharacter plyr = (PlayerCharacter) transMapObjects[x][y];
					Image animImg = getCharacterImage(plyr);
					drawPlayer(g, defIsoPt, plyr, animImg);

				} else {
					Point p = twoDtoIso(new Point(x * TILE_HEIGHT, y
							* TILE_HEIGHT));
					drawObject(g, p, defIsoPt, objImg);
				}
			}
		}
		Image cmp = compass.get(currentDir.ordinal());
		g.drawImage(cmp, 0, PANEL_HEIGHT - cmp.getHeight(null), null);

		g.setColor(Color.GREEN);
		g.drawString(
				"Current Direction: " + currentDir.toString()
						+ " Current Map: "
						+ client.getMap().getClass().getSimpleName(), 0,
				PANEL_HEIGHT);
	}

	/**
	 * Helper method to draw a player on the graphics pane. It can draw the main
	 * character or another player.
	 * 
	 * @param g
	 *            The graphics object to draw to.
	 * @param defIsoPt
	 *            The offset point of the main character.
	 * @param plyr
	 *            The playerCharacter object to draw.
	 * @param animImg
	 *            The image to draw.
	 */
	private void drawPlayer(Graphics g, Point defIsoPt, PlayerCharacter plyr,
			Image animImg) {
		if (!plyr.equals(currentPlayer)) {
			Point plyrPt = translatePlayerPosition(plyr.getPosition(),
					currentDir);
			plyrPt = twoDtoIso(plyrPt);
			Point plyrPt2 = isoToPlayerPt(plyrPt, animImg, defIsoPt.x,
					defIsoPt.y);
			g.drawImage(animImg, plyrPt2.x, plyrPt2.y, null);
			Point stats = new Point(plyrPt2.x + 60, plyrPt2.y - 30);
			drawPlayerStats(g, stats, plyr);
		} else {// Current player in center of screen
			int px = (PANEL_WIDTH / 2) - (animImg.getWidth(null) / 2);
			int py = (PANEL_HEIGHT / 2) - (animImg.getHeight(null) - 20);
			g.drawImage(animImg, px, py, null);
			Point stats = new Point(px + 80, py);
			drawPlayerStats(g, stats, plyr);
		}
	}

	/**
	 * Helper method to draw an object on the graphics pane. It will draw any
	 * image correctly as long as width is correct.
	 * 
	 * @param g
	 *            The graphics object to draw to.
	 * @param isoPt
	 *            The calculated isometric position of the object in the
	 *            graphics pane.
	 * @param defIsoPt
	 *            The offset point of the main character.
	 * @param objImg
	 *            the image to draw.
	 */
	private void drawObject(Graphics g, Point isoPt, Point defIsoPt,
			Image objImg) {
		isoPt.y = isoPt.y + (PANEL_HEIGHT / 2) - defIsoPt.y;
		isoPt.x = isoPt.x + (PANEL_WIDTH / 2) - (TILE_WIDTH / 2) - defIsoPt.x;
		g.drawImage(objImg, isoPt.x, isoPt.y - objImg.getHeight(null)
				+ TILE_HEIGHT, objImg.getWidth(null), objImg.getHeight(null),
				null);
	}

	/**
	 * Converts an isometric point on screen the map into the correct position
	 * of a player .
	 * 
	 * @param inPt
	 *            The original isometric position.
	 * @param animPlayerImage
	 *            The image which will be used.
	 * @param x
	 *            The x origin offset.
	 * @param y
	 *            The y origin offset.
	 * @param b
	 *            True if main character.
	 * @return The actual point to draw at.
	 */
	private Point isoToPlayerPt(Point inPt, Image animPlayerImage, int x, int y) {
		Point toRet = new Point();
		toRet.x = inPt.x + (PANEL_WIDTH / 2)
				- (animPlayerImage.getWidth(null) / 2) - x;
		toRet.y = inPt.y + (PANEL_HEIGHT / 2)
				- (animPlayerImage.getHeight(null) - 20) - y;
		return toRet;
	}

	/**
	 * Draws a character's statistics above their head in the graphics pane.
	 * 
	 * @param g
	 *            The graphics object to draw to.
	 * @param p
	 *            The Point to draw the statistics at.
	 * @param plyr
	 *            The player to draw above.
	 */
	private void drawPlayerStats(Graphics g, Point p, PlayerCharacter plyr) {
		Graphics2D g2d = (Graphics2D) g;
		g.setColor(Color.WHITE);
		g.drawRect(p.x, p.y, HEALTH_BAR_SIZE, 10);
		g.setColor(Color.RED);
		int healthSize = HEALTH_BAR_SIZE * plyr.getHealth()
				/ plyr.getMaxHealth();
		// System.out.println(healthSize + " actual health " +
		// plyr.getHealth());
		g.fill3DRect(p.x, p.y, healthSize, 10, false);
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		Font replace = g2d.getFont().deriveFont(Font.BOLD).deriveFont(16f);
		g2d.setFont(replace);
		g2d.setColor(Color.WHITE);
		g2d.drawString(plyr.playerName, p.x, p.y + 30);
	}

	/**
	 * Get the current applicable image for a character from their animation
	 * set. If they do not have one yet, create it and default it to walking.
	 * 
	 * @param plyr
	 *            The player to get an image for.
	 * @return The current image frame of the character.
	 */
	private Image getCharacterImage(PlayerCharacter plyr) {
		if (animations.containsKey(plyr)) {
			return animations.get(plyr).getCurrentImage();
		}
		animations.put(plyr, new AnimationSet(plyr, plyr.getImageSource()));
		animations.get(plyr)
				.updateImage(PlayerAction.MOVE, plyr.getDirection());// default
		return animations.get(plyr).getCurrentImage();
	}

	/**
	 * Advance the current image for a character based on their action.
	 * 
	 * @param player
	 *            The player to animate.
	 * @param act
	 *            The action being performed.
	 */
	private void updateCharacterImage(PlayerCharacter player, PlayerAction act) {
		if (animations.containsKey(player)) {
			int d = (player.getDirection().ordinal() - currentDir.ordinal()) % 4;
			if (d < 0) {
				d = 4 + d;
			}
			animations.get(player).updateImage(act, Direction.values()[d]);
			return;
		}
		getCharacterImage(player);
	}

	/**
	 * Helper method to transform a 2D array of tiles based on the player's
	 * current direction.
	 * 
	 * @param original
	 *            The source 2D array to transform.
	 * @param dir
	 *            The current direction.
	 * @return The transformed array.
	 */
	private Tile[][] translateMap(Tile[][] original, Direction dir) {
		Tile[][] toRet = new Tile[original.length][];
		for (int i = 0; i < original.length; i++) {
			Tile[] orig = original[i];
			toRet[i] = new Tile[orig.length];
			System.arraycopy(orig, 0, toRet[i], 0, orig.length);
		}
		for (int z = 0; z < currentDir.ordinal(); z++) {
			final int M = toRet.length;
			final int N = toRet[0].length;
			Tile[][] tempTranslation = new Tile[N][M];
			for (int r = 0; r < M; r++) {
				for (int c = 0; c < N; c++) {
					tempTranslation[r][c] = toRet[N - 1 - c][r];
				}
			}
			toRet = tempTranslation;
		}
		return toRet;
	}

	/**
	 * Helper method to transform a 2D array of GameObjects based on the
	 * player's current direction.
	 * 
	 * @param original
	 *            The source 2D array to transform.
	 * @param dir
	 *            The current direction.
	 * @return The transformed array.
	 */
	private GameObject[][] translateMap(GameObject[][] original, Direction dir) {
		GameObject[][] toRet = new GameObject[original.length][];
		for (int i = 0; i < original.length; i++) {
			GameObject[] orig = original[i];
			toRet[i] = new GameObject[orig.length];
			System.arraycopy(orig, 0, toRet[i], 0, orig.length);
		}

		for (int z = 0; z < currentDir.ordinal(); z++) {
			final int M = toRet.length;
			final int N = toRet[0].length;
			GameObject[][] tempTranslation = new GameObject[N][M];
			for (int r = 0; r < M; r++) {
				for (int c = 0; c < N; c++) {
					tempTranslation[r][c] = toRet[N - 1 - c][r];
				}
			}
			toRet = tempTranslation;
		}
		return toRet;
	}

	/**
	 * Helper method to transform a player's position around the map based on
	 * the current direction.
	 * 
	 * @param player
	 *            The player being transformed.
	 * @param dir
	 *            The current direction.
	 * @return A new Point representing the transformed player's position.
	 */
	private Point translatePlayerPosition(Point player, Direction dir) {
		Point dPt = player;
		Point toRet = new Point();
		int centerX = (mapTiles.length * TILE_HEIGHT) / 2;
		int centerY = (mapTiles.length * TILE_HEIGHT) / 2;
		AffineTransform transform = new AffineTransform();
		double angleInRadians = (-90 * Math.PI / 180);
		angleInRadians *= dir.ordinal();
		transform.rotate(angleInRadians, centerX, centerY);
		transform.transform(dPt, toRet);
		return toRet;
	}

	/**
	 * Helper method to move a point from regular 2D space to an isometric
	 * representation. Note that this will return some Points with negative
	 * values.
	 * 
	 * @param p
	 *            The point in 2D space.
	 * @return A new point representing the point in isometric space.
	 */
	private Point twoDtoIso(Point p) {
		Point toRet = new Point();
		toRet.x = p.x - p.y;
		toRet.y = (p.x + p.y) / 2;
		return toRet;
	}
	/**
	 * Gets the current interpretation of the map objects which the
	 * Renderer is using.
	 * @return The array of GameObjects.
	 */
	public GameObject[][] getTransMapObjects(){
		return transMapObjects;
	}
	/**
	 * Gets the current interpretation of the map tiles which the
	 * Renderer is using.
	 * @return The array of Map Tiles.
	 */
	public Tile[][] getTransMapTiles(){
		return transMapTiles;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}

	// KEY BINDING ACTION HANDLING
	private static final String RIGHT = "right";
	private static final String LEFT = "left";
	private static final String W = "w";
	private static final String A = "a";
	private static final String S = "s";
	private static final String D = "d";
	private static final String F = "f";
	private static final String E = "e";
	private static final String SPACE = "space";

	private Action left = new AbstractAction(LEFT) {
		private static final long serialVersionUID = -5916758744039584878L;

		@Override
		public void actionPerformed(ActionEvent e) {
			rotateLeft();
		}
	};
	/**
	 * Translate the Tiles and Objects on the map.
	 */
	private void rotateLeft(){
		if (currentDir.ordinal() == 0) {
			currentDir = Direction.values()[3];
		} else {
			currentDir = Direction.values()[currentDir.ordinal() - 1];
		}
		transMapTiles = translateMap(mapTiles, currentDir);
		transMapObjects = translateMap(mapObjects, currentDir);
		transPlayer = translatePlayerPosition(currentPlayer.getPosition(),
				currentDir);
		updateCharacterImage(currentPlayer, PlayerAction.MOVE);
	}
	
	private Action right = new AbstractAction(RIGHT) {
		private static final long serialVersionUID = 8994900926275003106L;

		@Override
		public void actionPerformed(ActionEvent e) {
			rotateRight();
		}
	};
	/**
	 * Translate the Tiles and Objects on the map.
	 */
	private void rotateRight(){
		if (currentDir.ordinal() == 3) {
			currentDir = Direction.values()[0];
		} else {
			currentDir = Direction.values()[currentDir.ordinal() + 1];
		}
		transMapTiles = translateMap(mapTiles, currentDir);
		transMapObjects = translateMap(mapObjects, currentDir);
		transPlayer = translatePlayerPosition(currentPlayer.getPosition(),
				currentDir);
		updateCharacterImage(currentPlayer, PlayerAction.MOVE);
	}
	private Action ePress = new AbstractAction(E) {
		private static final long serialVersionUID = -1847092208693126516L;

		@Override
		public void actionPerformed(ActionEvent e) {
			client.addPacket(new Packet(PlayerAction.PICKUP, currentPlayer,
					currentPlayer.getDirection()));
		}
	};
	private Action fPress = new AbstractAction(F) {
		private static final long serialVersionUID = 7105587759667512769L;

		@Override
		public void actionPerformed(ActionEvent e) {
			if (keyPressAllowed()) {
				updateCharacterImage(currentPlayer, PlayerAction.ATTACK);
				client.addPacket(new Packet(PlayerAction.ATTACK, currentPlayer,
						currentPlayer.getDirection()));
			}
		}
	};
	private Action spacePress = new AbstractAction(SPACE) {
		private static final long serialVersionUID = -5830469364090562524L;

		@Override
		public void actionPerformed(ActionEvent e) {
			int x = 0;
			int y = 0;
			switch (currentPlayer.getDirection()) {
			case NORTH:
				x = currentPlayer.getArrayPosition().x;
				y = currentPlayer.getArrayPosition().y - 1;
				break;
			case EAST:
				x = currentPlayer.getArrayPosition().x + 1;
				y = currentPlayer.getArrayPosition().y;
				break;
			case SOUTH:
				x = currentPlayer.getArrayPosition().x;
				y = currentPlayer.getArrayPosition().y + 1;
				break;
			case WEST:
				x = currentPlayer.getArrayPosition().x - 1;
				y = currentPlayer.getArrayPosition().y;
				break;
			}
			if (isValidPoint(x, y) && mapObjects[x][y] != null) {
				chatbox.displayMessage(mapObjects[x][y].description() + "\n");
			}
		}
	};

	/**
	 * Helper method to verify if a point is within the bounds of the map array.
	 * 
	 * @param x
	 *            The x position in the array.
	 * @param y
	 *            The y position in the array.
	 * @return True if the position is valid.
	 */
	private boolean isValidPoint(int x, int y) {
		int width = mapObjects.length;
		int height = mapObjects.length;
		return x >= 0 && x < width && y >= 0 && y < height;
	}

	private Action wPress = new AbstractAction(W) {
		private static final long serialVersionUID = -1847092208693126516L;

		@Override
		public void actionPerformed(ActionEvent e) {
			moveKeyPress(DirectionKeys.w);
		}
	};
	private Action aPress = new AbstractAction(A) {
		private static final long serialVersionUID = 7105587759667512769L;

		@Override
		public void actionPerformed(ActionEvent e) {
			moveKeyPress(DirectionKeys.a);
		}
	};
	private Action sPress = new AbstractAction(S) {
		private static final long serialVersionUID = -5830469364090562524L;

		@Override
		public void actionPerformed(ActionEvent e) {
			moveKeyPress(DirectionKeys.s);
		}
	};
	private Action dPress = new AbstractAction(D) {
		private static final long serialVersionUID = 6778794819512324563L;

		@Override
		public void actionPerformed(ActionEvent e) {
			moveKeyPress(DirectionKeys.d);
		}
	};

	// END KEY BINDINGS

	/**
	 * Performs a move operation based on the key being pressed. Prevents a
	 * button being excessively pressed by not performing an action while still
	 * in cooldown.
	 * 
	 * @param key
	 *            The key being pressed.
	 */
	private void moveKeyPress(DirectionKeys key) {
		int result = (key.ordinal() + currentDir.ordinal()) % 4;
		Direction dir = Direction.values()[result];
		if (keyPressAllowed()) {
			updateCharacterImage(currentPlayer, PlayerAction.MOVE);
			currentPlayer.move(dir);
			client.addPacket(new Packet(PlayerAction.MOVE, currentPlayer, dir));
		}
	}

	/**
	 * Helper method to check if the key press is still on cool down.
	 * 
	 * @return True if the key press is allowed.
	 */
	private boolean keyPressAllowed() {
		if (currentCooldown > 0) {
			currentCooldown -= System.currentTimeMillis();
			return false;
		} else {
			currentCooldown = KEY_PRESS_INTERVAL;
			return true;
		}
	}
}