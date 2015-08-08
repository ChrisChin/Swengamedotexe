package gameModel.gameObjects.classes;

import java.awt.Image;
import gameModel.gameObjects.PlayerCharacter;

/**
 * 
 * @author Jiaheng Wang (wangjiah)
 *
 */
public class PigFarmer extends PlayerCharacter {

	private static final long serialVersionUID = 1189930264032142935L;
	public static final int INITIAL_HEALTH = 9999;
	public static final int INITIAL_ATTACK = 999;
	public static final int INITIAL_DEFENCE = 999;
	
	public PigFarmer(String playerName) {
		super(INITIAL_HEALTH, INITIAL_ATTACK, INITIAL_DEFENCE, playerName);
	}

	@Override
	public Image getImage() {
		return null;
	}

}
