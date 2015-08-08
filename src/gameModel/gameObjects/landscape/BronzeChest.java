package gameModel.gameObjects.landscape;

import java.awt.Image;

import game.userInterface.GuiHelper;
import gameModel.gameObjects.ImmortalObject;

/**
 * 
 * @author Jiaheng Wang (wangjiah)
 *
 */
public class BronzeChest extends ImmortalObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8163853136157572465L;
	private static final Image img = GuiHelper.loadImage("landscape/chest.png");
	
	public BronzeChest(String keyCode) {
		super(keyCode);
	}

	@Override
	public Image getImage() {
		return img;
	}

	@Override
	public String description() {
		return "A bronze chest. It has some faint markings on it reading "
				+ this.getName();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.getName();
	}
}
