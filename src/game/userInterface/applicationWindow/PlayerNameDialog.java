package game.userInterface.applicationWindow;

import game.userInterface.CleanTextField;
import game.userInterface.GuiHelper;

import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * This Dialog is used to get the user player the player name will be 
 * stored in the playerName when enter key or the button is pressed
 * 
 * @author Jinpeng Song (songjinp)
 *
 */
public class PlayerNameDialog extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private final int PAN_WIDTH = 300;
	private final int PAN_HEIGHT = 200;
	private final JPanel panel;
	private JTextField text;
	private JTextField ip;
	private JTextField port;
	private String playerName;
	private String ipAddress;
	private int portNum;
	private JLabel ipinfo;
	private JLabel portinfo;
	private JLabel enterLabel;
	
	private ImageIcon ENTER = GuiHelper.loadImageIcon("startingDialog/enter.png");
	private ImageIcon ENTER_HOVER = GuiHelper.loadImageIcon("startingDialog/enter_hover.png");

	
	public PlayerNameDialog() {
		super();
		setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		setSize(PAN_WIDTH, PAN_HEIGHT);
		panel = makePanel();
		add(panel);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	/**
	 * Initialize the panel with button and background images
	 * 
	 * @return the new panel
	 */
	private JPanel makePanel() {
		JPanel jpan = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(GuiHelper.loadImage("serverDialog/t1.png"), 0, 0,
						this);
			}
		};

		text = new CleanTextField("Replace with your name");
		ip = new CleanTextField("127.0.0.1");
		port = new CleanTextField("8080");
		
		ipinfo = new JLabel("IP Address");
		portinfo = new JLabel("Port Number");
		enterLabel = new JLabel(ENTER);
		
		jpan.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 30, 0, 0);
		jpan.add(text, gbc);
		text.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseExited(MouseEvent e){}
			public void mouseEntered(MouseEvent e){}
			public void mouseClicked(MouseEvent e){
				text.setText("");
				text.setFocusable(true);}
		});
		
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.insets = new Insets(0, 0, 5, 25);
		jpan.add(enterLabel,gbc);
		enterLabel.addMouseListener(new MouseListener(){
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {enterLabel.setIcon(ENTER);}
			public void mouseEntered(MouseEvent e) {enterLabel.setIcon(ENTER_HOVER);}
			public void mouseClicked(MouseEvent e) {checkUserInputs();}
		});	
		
		gbc.gridx = 0;
		gbc.gridy = 1;
		jpan.add(ipinfo,gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 1;
		jpan.add(ip, gbc);
		
		gbc.gridx = 0;
		gbc.gridy = 2;
		jpan.add(portinfo,gbc);
		
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.weightx = 0.5;
		jpan.add(port, gbc);
		
		return jpan;
	}

	/**
	 * Return the player name that the user has choose
	 * 
	 * @return playerName
	 */
	public String getPlayerName() {
		return playerName;
	}
	
	
	/**
	 * Return the port the user entered
	 * @return	
	 * 			port number
	 */
	public int getPort(){
		return portNum;
	}
	
	
	/**
	 * Retrurn the ip address the user entered
	 * @return	
	 * 			ip address
	 */
	public String getIPAddress(){
		return ipAddress;
	}

	
	/**
	 * Check if the user has enter the correct input
	 * if true the set the field and remove the 
	 * dialog 
	 */
	private void checkUserInputs(){
		if (ChatBox.validMessage(text.getText())){
		try{
			portNum = Integer.parseInt(port.getText());
			ipAddress = ip.getText();
			playerName = text.getText();
			dispose();
		}catch(NumberFormatException e){}
		}
	}
}
