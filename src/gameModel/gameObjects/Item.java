package gameModel.gameObjects;

import java.awt.Image;

import javax.swing.ImageIcon;

/**
 * Represents an item in the game. Items can be added to inventory and used. 
 * 
 * @author Jiaheng Wang (wangjiah)
 * 
 */
public abstract class Item extends ImmortalObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5632190576947343513L;

	public Item(String name) {
		super(name);
	}

	/**
	 * Uses this item on given target. Returns true if used successfully
	 * 
	 * @param target
	 *            the target to use the item on
	 * @return true if successfully used, false otherwise.
	 */
	public abstract boolean use(GameObject target);
	
	/**
	 * Returns the image that represents this item in the inventory 
	 * @return the inventory image of this item
	 */
	
	public abstract Image getMenuImage();
	
	/**
	 * Returns the image that represents this item in the inventory
	 * @return the inventory imageIcon of this item
	 */
	public abstract ImageIcon getMenuImageIcon();
}
