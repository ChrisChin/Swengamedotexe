package gameModel.gameObjects.items;

import game.userInterface.GuiHelper;
import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.Item;
import gameModel.gameObjects.PlayerCharacter;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * 
 * @author Jiaheng Wang (wangjiah)
 *
 */
public class Potion extends Item {

	/**
	 *
	 */
	private static final long serialVersionUID = 6642080374002637961L;
	public static final int STRENGTH = 100;
	//private static Image POTION_IMG = GuiHelper.loadImage("/items/potion.png");
	private static Image POTION_MENU_IMG = GuiHelper.loadImage("items/potion_red_menu.png");
	private static ImageIcon POTION_MENU_IMGICON = GuiHelper.loadImageIcon("items/potion_red_menu.png");

	public Potion() {
		super("Potion");
	}

	@Override
	public boolean use(GameObject target) {
		if (target instanceof PlayerCharacter) {
			return ((PlayerCharacter) target).recover(STRENGTH);
		} else {
			return false;
		}
	}

	@Override
	public Image getImage() {
		return POTION_MENU_IMG;
	}

	@Override
	public String description() {
		return "It's a potion. Drink it to recover " + STRENGTH + " health.";
	}

	@Override
	public Image getMenuImage() {
		return POTION_MENU_IMG;
	}

	@Override
	public ImageIcon getMenuImageIcon() {
		return POTION_MENU_IMGICON;
	}

}
