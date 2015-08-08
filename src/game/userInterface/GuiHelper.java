package game.userInterface;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * This is helper class with static method and testing methods
 * 
 * @author Jinpeng Song (songjinp)
 */
public class GuiHelper {
	private static final String IMAGE_PATH = "/images/";

	public GuiHelper() {

	}

	public static void main(String[] args) {
	}

	/**
	 * Load a Image from the image folder locate at the jar file using URL
	 * 
	 * @param filename
	 *            the filename of the image to load
	 * @return the Image object with the given filename
	 */
	public static Image loadImage(String filename) {
		try {
			Image img = ImageIO.read(GuiHelper.class.getResource(IMAGE_PATH
					+ filename));
			return img;
		} catch (IOException e) {
			throw new RuntimeException("Unable to load image: " + filename);
		}
	}

	/**
	 * Load a ImageIcon from the image folder locate at the jar file using URL
	 * 
	 * @param filename
	 *            the filename of the image icon to load
	 * @return the ImageIcon object with the given filename
	 */

	public static ImageIcon loadImageIcon(String filename) {
		ImageIcon icon = new ImageIcon(GuiHelper.class.getResource(IMAGE_PATH
				+ filename));
		return icon;
	}
}
