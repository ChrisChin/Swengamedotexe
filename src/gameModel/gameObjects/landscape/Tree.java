package gameModel.gameObjects.landscape;

import java.awt.Image;

import game.userInterface.GuiHelper;
import gameModel.gameObjects.ImmortalObject;

/**
 * 
 * @author Jiaheng Wang (wangjiah)
 *
 */
public class Tree extends ImmortalObject {
	/**
	 *
	 */
	private static final long serialVersionUID = 2586094759351033859L;
	private static Image TREE_IMG = GuiHelper.loadImage("landscape/tree.png");

	public Tree() {
		super("Tree");
	}

	@Override
	public Image getImage() {
		// TODO Auto-generated method stub
		return TREE_IMG;
	}

	@Override
	public String description() {
		return "It's a tree.";
	}

}
