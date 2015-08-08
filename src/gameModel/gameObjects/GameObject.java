package gameModel.gameObjects;

import java.awt.Image;
import java.io.Serializable;

/**
 * Represents an object in the game.
 * 
 * @author Jiaheng Wang (wangjiah)
 * 
 */
public interface GameObject extends Serializable {

	/**
	 * Returns an image to render for this object
	 * 
	 * @return an image to render for this object
	 */
	public Image getImage();

	/**
	 * Returns a description of this object
	 * 
	 * @return a description of this object
	 */
	public String description();

}
