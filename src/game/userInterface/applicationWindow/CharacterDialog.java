package game.userInterface.applicationWindow;

import game.userInterface.GuiHelper;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameObjects.classes.PigFarmer;
import gameModel.gameObjects.classes.Swordsman;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * This Dialog is used to let the use select the character that they want
 * stored into in the field, When  the button is pressed
 * @author Jinpeng Song (songjinp)
 *
 */
public class CharacterDialog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	private final int WIDTH = 1324;
	private final int HEIGHT = 725;
	private PlayerCharacter player;
	private JTextField nameField;
	private JPanel panel;
	private JLabel close = new JLabel(CLOSE);
	private JLabel farmer = new JLabel(FARMER_HOVER);
	private JLabel swordsman = new JLabel(SWORDSMAN);
	private JLabel question = new JLabel(QUESTION);
	private String name; 
	private Font font = new Font("SansSerif", Font.BOLD, 20);
	//false for farmer and true for swordsman
	private static boolean check = false; 

	
	public CharacterDialog(String name) {
		super();
		setModalityType(Dialog.DEFAULT_MODALITY_TYPE);//pause execution until dispose
		this.name = name;
		panel = makePanel();
		add(panel);
		setSize(WIDTH, HEIGHT);
		initKeyBindings();
		setLocationRelativeTo(null);
		setUndecorated(true);
		setVisible(true);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * Initialize the panel with button and background images
	 * @return 
	 * 			the new panel 
	 */
	private JPanel makePanel(){
		JPanel panel = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(bgImage, 0, 0, this);
			}	
		};
		panel.setLayout(null);
		close.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {
				close.setIcon(CLOSE);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				close.setIcon(CLOSE_HOVER);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				int option = JOptionPane.showConfirmDialog(null, "Quitting from Game?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (option == JOptionPane.YES_OPTION) {
					System.exit(0); // Quit the game
				}
			}
		});
		farmer.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
					farmer.setIcon(FARMER_HOVER);
					swordsman.setIcon(SWORDSMAN);
					check = true;
			}
			@Override
			public void mouseClicked(MouseEvent e) {
					player = new PigFarmer(nameField.getText());
					dispose();
			}
		});
		swordsman.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mouseEntered(MouseEvent e) {
					swordsman.setIcon(SWORDSMAN_HOVER);
					farmer.setIcon(FARMER);
					check = false;
			}
			@Override
			public void mouseClicked(MouseEvent e) {
					player = new Swordsman(nameField.getText());
					dispose();
			}
		});
		
		nameField = new JTextField();
		nameField.setText(name);
		nameField.setEditable(false);
		nameField.setBackground(new Color(0,0,0,128));
		nameField.setForeground(new Color(255,255,255));
		nameField.setFont(font);
		nameField.setFocusable(false);
		
		question.setBounds(410, 70, 510, 45);
		close.setBounds(WIDTH-50,0,50,50);
		nameField.setBounds(540, 125, 300, 50);
		farmer.setBounds(312, 250, 200, 350);
		swordsman.setBounds(812, 250, 200, 350);
		panel.add(close);
		panel.add(farmer);
		panel.add(swordsman);
		panel.add(nameField);
		panel.add(question);
		return panel;
	}
	
	/**
	 * Images for different character icons (hover/non-hover)
	 */
	private final static Image bgImage = GuiHelper.loadImage("userdialog/background.png");
	private final static ImageIcon CLOSE = GuiHelper.loadImageIcon("startingDialog/close.png");
	private final static ImageIcon CLOSE_HOVER = GuiHelper.loadImageIcon("startingDialog/close_hover.png"); 
	private final static ImageIcon FARMER = GuiHelper.loadImageIcon("userdialog/farmer.png");
	private final static ImageIcon FARMER_HOVER = GuiHelper.loadImageIcon("userdialog/farmer_hover.png");
	private final static ImageIcon SWORDSMAN = GuiHelper.loadImageIcon("userdialog/swordsman.png");
	private final static ImageIcon SWORDSMAN_HOVER = GuiHelper.loadImageIcon("userdialog/swordsman_hover.png");
	private final static ImageIcon QUESTION = GuiHelper.loadImageIcon("userdialog/inputName.png");
	
	/**
	 * Return the newly made PlayerCharacter base on the player name 
	 * provide from the user.
	 * @return the playerCharacter
	 */
	public PlayerCharacter getNewPlayer(){
		return player;
	}
	
	private final static String ENTER = "Enter";
	private final static String LEFT = "Left";
	private final static String RIGHT = "Right";
	/**
	 * Set up key binding for user key input (enter,left,right)
	 */
	private void initKeyBindings() {
		InputMap inputMap = getRootPane().getInputMap();
		ActionMap actionMap = getRootPane().getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), ENTER);
		actionMap.put(ENTER, enterPress);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT);
		actionMap.put(LEFT, leftPress);
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT);
		actionMap.put(RIGHT, rightPress);
	}
	
	/*
	 * Actions for enter, left and right key press 
	 */
	private Action enterPress = new AbstractAction(ENTER) {
		private static final long serialVersionUID = 1L;
		@Override
		public void actionPerformed(ActionEvent e) {
				if(check == true){player = new Swordsman(nameField.getText());}
				else{player = new PigFarmer(nameField.getText());}
				dispose();
		}
	};
	
	private Action leftPress = new AbstractAction(LEFT) {
		private static final long serialVersionUID = 1L;
		@Override
		public void actionPerformed(ActionEvent e) {
			farmer.setIcon(FARMER_HOVER);
			swordsman.setIcon(SWORDSMAN);
			check = false;
		}
	};
	
	private Action rightPress = new AbstractAction(RIGHT) {
		private static final long serialVersionUID = 1L;
		@Override
		public void actionPerformed(ActionEvent e) {
				swordsman.setIcon(SWORDSMAN_HOVER);
				farmer.setIcon(FARMER);
				check = true;
		}
	};
	

}
