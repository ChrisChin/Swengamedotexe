package gameModel.gameObjects.classes;

import game.userInterface.GuiHelper;
import gameModel.gameObjects.PlayerCharacter;

import java.awt.Image;

/**
 * 
 * @author Jiaheng Wang (wangjiah)
 *
 */
public class Swordsman extends PlayerCharacter {

	private static final long serialVersionUID = -5213734868368892688L;
	public static final int INITIAL_HEALTH = 100;
	public static final int INITIAL_ATTACK = 20;
	public static final int INITIAL_DEFENCE = 5;
	private static Image DEAD = GuiHelper.loadImage("characters/dead.png");
	private static Image ALIVE = GuiHelper
			.loadImage("characters/player-test.png");

	public Swordsman(String playerName) {
		super(INITIAL_HEALTH, INITIAL_ATTACK, INITIAL_DEFENCE, playerName);
	}

	@Override
	public Image getImage() {
		if (this.isDead()) {
			return DEAD;
		} else {
			return ALIVE;
		}
	}

}
