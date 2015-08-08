package gameModel.gameObjects.items;

import java.awt.Image;

import javax.swing.ImageIcon;

import game.userInterface.GuiHelper;
import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.Item;
import gameModel.gameObjects.landscape.BronzeChest;

/**
 * A bronze key. Can be used to open bronze chests with the same keycode.
 *
 * @author Jiaheng Wang (wangjiah)
 *
 */
public class BronzeKey extends Item {

	private static final long serialVersionUID = 1341224171701755547L;
	private static Image KEY_MENU_IMG = GuiHelper.loadImage("items/key.png");
	private static ImageIcon KEY_MENU_IMGICON = GuiHelper.loadImageIcon("items/key.png");

	public BronzeKey(String keyCode) {
		super(keyCode);
	}

	@Override
	public boolean use(GameObject target) {
		if (!(target instanceof BronzeChest)) {
			return false;
		}
		BronzeChest bc = (BronzeChest) target;
		return bc.name.equals(this.name);
	}

	@Override
	public Image getImage() {
		return KEY_MENU_IMG;
	}

	@Override
	public String description() {
		return "A bronze key. It has faint markings on it reading: "
				+ this.getName();
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": " + this.getName();
	}

	@Override
	public Image getMenuImage() {
		// WARNING This is a testing image for key even it's a potion image right not.
		return KEY_MENU_IMG;
	}

	@Override
	public ImageIcon getMenuImageIcon() {
		return KEY_MENU_IMGICON;
	}

}
