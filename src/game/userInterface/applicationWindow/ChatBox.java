package game.userInterface.applicationWindow;

import game.Network.ChatMessage;
import game.Network.Client;
import game.Network.Packet;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

/**
 * This class represents chat box,it takes user string input then display in the
 * textArea.
 * 
 * @author Jinpeng Song (songjinp)
 * 
 */
public class ChatBox extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JButton enterButton;
	private JTextField messageBox;
	private JTextArea chatBox;
	private JScrollPane scrollPane;
	private final int MESSAGE_LENGTH = 10;
	private String playerName;
	private Client client;
	private final static String ENTER = "Enter";
	private Font font = new Font("SansSerif", Font.BOLD, 13);

	/**
	 * @param client the client
	 * @param playerName the name of the player
	 */
	public ChatBox(Client client, String playerName) {
		setLayout(setUpLayout());
		setUp();
		setFocusable(false);
		initKeyBindings();
		this.client = client;
		this.playerName = playerName;
	}

	/**
	 * set up the TextArea,buttons and messageBox
	 */
	private void setUp() {
		chatBox = new JTextArea();
		chatBox.setBackground(new Color(0x777777));
		chatBox.setForeground(new Color(255, 255, 255));
		chatBox.setFont(font);
		scrollPane = new JScrollPane(chatBox);
		GridBagConstraints gbcSP = new GridBagConstraints();
		gbcSP.gridheight = 22;
		gbcSP.gridwidth = 7;
		gbcSP.insets = new Insets(0, 0, 5, 5);
		gbcSP.fill = GridBagConstraints.BOTH;
		gbcSP.gridx = 0;
		gbcSP.gridy = 0;
		add(scrollPane, gbcSP);
		chatBox.setEditable(false);
		chatBox.setLineWrap(true);

		messageBox = new JTextField(MESSAGE_LENGTH);
		GridBagConstraints gbcTF = new GridBagConstraints();
		gbcTF.gridwidth = 5;
		gbcTF.insets = new Insets(0, 0, 0, 5);
		gbcTF.fill = GridBagConstraints.HORIZONTAL;
		gbcTF.gridx = 0;
		gbcTF.gridy = 22;
		add(messageBox, gbcTF);

		enterButton = new JButton("Enter");
		GridBagConstraints gbcEnter = new GridBagConstraints();
		gbcEnter.gridwidth = 2;
		gbcEnter.gridx = 5;
		gbcEnter.gridy = 22;
		add(enterButton, gbcEnter);
		enterButton.setActionCommand("Enter");
		enterButton.addActionListener(this);
		enterButton.setFocusable(false);
	}

	/**
	 * Create a new gridBagLayout (This grid is made in window builder)
	 * 
	 * @return The gridBagLayout
	 */
	private GridBagLayout setUpLayout() {
		GridBagLayout gbl = new GridBagLayout();
		gbl.columnWidths = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl.columnWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				Double.MIN_VALUE };
		gbl.rowWeights = new double[] { 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
				0.0, 0.0, 0.0, Double.MIN_VALUE };
		return gbl;
	}

	/**
	 * Below is dealing with action performed
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String action = e.getActionCommand();
		switch (action) {
		// if user press enter button update chat box send to sever
		case "Enter":
			String message = messageBox.getText();
			sendSendToServer(message);
			messageBox.setText("");// empty message box
			break;
		default:
			break;
		}
	}

	/**
	 * Display the message on the textarea
	 * 
	 * @param chats
	 *            message need to be displayed
	 */
	public void displayMessage(String chats) {
		if (!validMessage(chats)) {
			return;
		}
		chatBox.append(chats); // Update chat box
	}

	/**
	 * Display the system to the textarea
	 * 
	 * @param chats
	 *            message need to be displayed
	 */
	public void displayingSystemMessage(String chats) {
		chatBox.append("System: "+ chats +"\n"); // Update chat box
	}

	/**
	 * Display the message and send the message to the server
	 * 
	 * @param chats
	 *            messageneed to be display and send
	 */
	public void sendSendToServer(String chats) {
		if (!validMessage(chats)) {
			return;
		}
		client.addPacket(new Packet(new ChatMessage(playerName, chats)));
	}
	
	/**
	 * Set up key binding for your user key input (enter key) 
	 */
	private void initKeyBindings() {
		int WINDOWFOCUS = JComponent.WHEN_IN_FOCUSED_WINDOW;
		InputMap inputMap = this.getInputMap(WINDOWFOCUS);
		ActionMap actionMap = this.getActionMap();
		inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), ENTER);
		actionMap.put(ENTER, enterPress);
	}

	private Action enterPress = new AbstractAction(ENTER) {
		private static final long serialVersionUID = 4291742528448071471L;

		@Override
		public void actionPerformed(ActionEvent e) {
			String message = messageBox.getText();
			sendSendToServer(message);
			messageBox.setText("");// empty message box
		}
	};

	/**
	 * Tests if a message is valid or not
	 * 
	 * Author: Jiaheng Wang (wangjiah)
	 * 
	 * @param message
	 *            The message to validate
	 * @return true if message is valid, false otherwise
	 */
	public static boolean validMessage(String message) {
		if (message.length() == 0) {
			return false;
		}
		for (char c : message.toCharArray()) {
			if (c != ' ' && c != '\0' && c != '\n') {
				return true;
			}
		}
		return false;
	}
}
