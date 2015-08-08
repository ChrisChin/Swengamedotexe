package gameModel.gameObjects.monsters;

import java.awt.Image;

import game.userInterface.GuiHelper;
import gameModel.gameObjects.NonPlayerCharacter;

/**
 * 
 * @author Jiaheng Wang (wangjiah)
 *
 */
public class Slime extends NonPlayerCharacter {
	/**
	 *
	 */
	private static final long serialVersionUID = 1503553645511806569L;
	private static Image SLIME_IMG = GuiHelper.loadImage("monsters/slime.png");

	public Slime() {
		super(1000, 10, 5);
		this.isAggresive = false;
	}

	@Override
	public Image getImage() {
		return SLIME_IMG;
	}

	@Override
	public String description() {
		return "A common slime. Weak as s...";
	}

}
