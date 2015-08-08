package game.userInterface;

import game.Network.ChatMessage;
import game.Network.Client;
import game.userInterface.RendererWindow.Renderer;
import game.userInterface.applicationWindow.AboutPanel;
import game.userInterface.applicationWindow.CharacterDialog;
import game.userInterface.applicationWindow.ChatBox;
import game.userInterface.applicationWindow.StartingDialog;
import game.userInterface.applicationWindow.StatusBar;
import gameModel.gameObjects.PlayerCharacter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 * This is the main frame of the game use represent the graphics output of the
 * game.s
 * 
 * @author Jinpeng Song (songjinp)
 * 
 */

public class GameFrame extends JFrame implements ActionListener,
		WindowListener, MouseListener {
	
	private static final long serialVersionUID = 1L;
	private static final int STATUS_WIDTH = 640;
	private static final int STATUS_HEIGHT = 190;
	private static final int CHAT_WIDTH = 300;
	private static final int CHAT_HEIGHT = 700;
	private static String PLAYER_NAME;
	private JMenuBar menuBar;
	private ChatBox chatbox;
	private Renderer renderer;
	private StatusBar statusBar;
	private Client client;
	private static boolean check = true;
	private static JDialog dialog;
	
	public GameFrame(Client client, Renderer renderer) {
		GameFrame.PLAYER_NAME = client.getPc().playerName;
		this.client = client;
		this.renderer = renderer;
		this.addMouseListener(this);
		menuBar = makeMenuBar();
		setJMenuBar(menuBar);
		setUpPanels();
		setUpAboutDialog();
		renderer.setBounds(0, 0, Renderer.PANEL_WIDTH, Renderer.PANEL_HEIGHT);
		add(renderer);
		setLayout(null);
		this.getContentPane().setBackground(new Color(75, 75, 75));
		pack();
		setVisible(true);
		setResizable(false);
		addWindowListener(this);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	}
	
	/**
	 * initialize the MenuBar for the game
	 * 
	 * @return the jmenu bar
	 */
	private JMenuBar makeMenuBar() {
		JMenuBar jmb = new JMenuBar();

		/* File menu and menu items */
		JMenu fileMenu = new JMenu("File");
		JMenuItem exitJMI = new JMenuItem("Exit");

		exitJMI.setActionCommand("Exit");
		exitJMI.addActionListener(this);
		fileMenu.add(exitJMI);
		jmb.add(fileMenu);

		/* Help menu and menu items */
		JMenu helpMenu = new JMenu("Help");
		JMenuItem aboutJMI = new JMenuItem("About");
		JMenuItem ruleJMI = new JMenuItem("Rule");

		aboutJMI.setActionCommand("About");
		aboutJMI.addActionListener(this);
		ruleJMI.setActionCommand("Rule");
		ruleJMI.addActionListener(this);
		helpMenu.add(aboutJMI);
		helpMenu.addSeparator();
		helpMenu.add(ruleJMI);
		jmb.add(helpMenu);

		return jmb;
	}

	/**
	 * initialize statusBar,chat box and renderer
	 */
	private void setUpPanels() {
		chatbox = new ChatBox(client, PLAYER_NAME);
		chatbox.setBounds(Renderer.PANEL_WIDTH, 0, CHAT_WIDTH, CHAT_HEIGHT);
		add(chatbox);
		statusBar = new StatusBar(chatbox, client, client.getPc());
		statusBar.setBounds((Renderer.PANEL_WIDTH - STATUS_WIDTH) / 2,
				Renderer.PANEL_HEIGHT, STATUS_WIDTH, STATUS_HEIGHT);
		add(statusBar);

	}

	/**
	 * Show a JOptionPane ask user to confirm to quit
	 */
	private void exitFromGame() {
		int option = JOptionPane
				.showConfirmDialog(this, "Quitting from Game?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (option == JOptionPane.YES_OPTION) {
			System.exit(0); // Quit the game
		}
	}

	/**
	 * Get the panel that represent the status bar;
	 * 
	 * @return the canvas
	 */
	public StatusBar getCanvas() {
		return statusBar;
	}

	/*
	 * Below is the overriding action performed calling the right method base of
	 * the action command
	 */

	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		switch (action) {
		case "Exit":
			exitFromGame();
			break;
		case "About":
			showAboutDialog();
			break;
		case "Rule":
			StartingDialog.showRule();
			break;
		default:
			break;
		}
	}
	
	/**
	 * Return the chatbox use to display system and user message
	 * @return
	 * 		  chatbox 
	 */
	public ChatBox getChatBox(){
		return chatbox;
	}
	
	/**
	 * Append message receive from the client
	 * 
	 * @param messages
	 *            messages to be append
	 * 
	 */
	public void updateMessage(ChatMessage messages) {
		String line = messages.getSender() + ": " + messages.getMessage()
				+ "\n";
		chatbox.displayMessage(line);
		chatbox.repaint();
	}

	/**
	 * Make a dialog to enter a name and make a player base on the name
	 * 
	 * @return The new player made
	 */
	public static PlayerCharacter askPlayChara() {
		CharacterDialog uni = new CharacterDialog("COCO");
		PlayerCharacter s = uni.getNewPlayer();
		while (s == null) {
			s = uni.getNewPlayer();
		}
		return s;
	}
	
	/**
	 * Set up a dialog that is used to present about information 
	 */
	private void setUpAboutDialog() {
		dialog = new JDialog();
		dialog.addWindowListener(new WindowListener() {
			public void windowOpened(WindowEvent e) {check = false;}
			public void windowIconified(WindowEvent e) {}
			public void windowDeiconified(WindowEvent e) {}
			public void windowDeactivated(WindowEvent e) {}
			public void windowClosing(WindowEvent e) {check = true;}
			public void windowClosed(WindowEvent e) {}
			public void windowActivated(WindowEvent e) {}
		});
	}
	
	/**
	 * Show the dialog that is used to present about informations
	 */
	public static void showAboutDialog(){
		if(check == true){
			AboutPanel ap = new AboutPanel();
			dialog.setSize(700, 650);
			dialog.add(ap);
			dialog.setResizable(false);
			dialog.setVisible(true);
		}
	}
	
	/*
	 * set the default size for the frame
	 */
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(1324, 750);
	}

	/*
	 * Below is the Window Listener deal with closing only
	 */
	@Override
	public void windowClosing(WindowEvent e) {exitFromGame();}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}

	/*
	 * Below is dealing with mouse event
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getX() < Renderer.PANEL_WIDTH && e.getY() < Renderer.PANEL_HEIGHT)
			renderer.requestFocus();
	}
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}

}
