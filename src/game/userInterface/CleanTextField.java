package game.userInterface;

import java.awt.Color;

import javax.swing.JTextField;
import javax.swing.border.Border;
/**
 * This text field will have gray background color and white text color  
 * And border is removed
 * @author Jinpeng Song (songjinp)
 *
 */
public class CleanTextField extends JTextField{
	private static final long serialVersionUID = 1L;
	
	public CleanTextField(String text){
		super(text);
		this.setForeground(Color.WHITE); //Text Color White
		this.setBackground(new Color(75,75,75)); //SemiTransparent
	}
	@Override
	public void setBorder(Border border){} //remove border
	
}
