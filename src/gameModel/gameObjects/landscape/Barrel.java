package gameModel.gameObjects.landscape;

import java.awt.Image;

import game.userInterface.GuiHelper;
import gameModel.gameObjects.MortalObject;

/**
 * 
 * @author Jiaheng Wang (wangjiah)
 *
 */
public class Barrel extends MortalObject {


	private static final long serialVersionUID = -224660058857598943L;
	private static Image img = GuiHelper.loadImage("landscape/barrel.png");
	public Barrel() {
		super(1, 0, 0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Image getImage() {
		return img;
	}

	@Override
	public String description() {
		return "It's a barrel. There may be some goodies inside.";
	}


}
