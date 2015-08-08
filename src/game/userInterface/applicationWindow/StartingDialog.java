package game.userInterface.applicationWindow;

import game.Network.Client;
import game.Network.Server;
import game.userInterface.GuiHelper;
import gameModel.gameWorld.World;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * This a dialog that make user select the game mode (run sever, start game ,
 * option)
 *
 * @author Jinpeng Song (songjinp)
 *
 */
public class StartingDialog extends JDialog implements WindowListener{

	private static final long serialVersionUID = 1L;
	private JPanel panel;
	private static JDialog dialog;
	private static boolean check = true;
	private final int WIDTH = 1324;
	private final int HEIGHT = 725;
	private JLabel close = new JLabel(CLOSE);
	private JLabel start = new JLabel(START);
	private JLabel info = new JLabel(INFO);
	private JLabel servers = new JLabel(SERVER);

	public StartingDialog() {
		panel = makePanel();
		add(panel);
		setSize(WIDTH, HEIGHT);
		setUpRule();
		setUndecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
		addWindowListener(this);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}


	/**
	 * Initialize the dialog that is use for displaying rules for the game
	 */
	private void setUpRule () {
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
	 * Make a panel that is used to hold the background image.
	 * @return the new Panel made
	 */
	private JPanel makePanel() {
		JPanel jpan = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(bgImage, 0, 0, this);
			}
		};
		jpan.setLayout(null);
		start.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {
				start.setIcon(START);
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				start.setIcon(START_HOVER);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				PlayerNameDialog pnd = new PlayerNameDialog();
				String playerName  = pnd.getPlayerName();
				int port  = pnd.getPort();
				String ip = pnd.getIPAddress();
				Client client = new Client(playerName, ip, port);
				client.start();
				dispose();
			}
		});
		info.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {
				info.setIcon(INFO);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				info.setIcon(INFO_HOVER);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				showRule();
			}
		});
		servers.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {
				servers.setIcon(SERVER);
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				servers.setIcon(SERVER_HOVER);
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				Server server;
				ServerDialog sd = new ServerDialog();
				String path = sd.getPath();
				//System.out.println(path);
				int port  = sd.getPort();
				if(path != null){
					server = new Server(port,path);
					server.start();
				} else {
					World world = new World();
					server = new Server(world, port);
					server.start();
				}
				dispose();
				new ServerControlDialog(server);
			}
		});
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

		jpan.add(start);
		jpan.add(info);
		jpan.add(servers);
		jpan.add(close);

		servers.setBounds(950,200,300,150);
		start.setBounds(950,350,300,150);
		info.setBounds(950,500,300,150);
		close.setBounds(WIDTH-50,0,50,50);
		return jpan;
	}


	/**
	 * Below is the Window Listener deal with closing only
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		int option = JOptionPane
				.showConfirmDialog(this, "Quitting from Game?", "Quit",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if (option == JOptionPane.YES_OPTION) {
			System.exit(0); // Quit the game
		}
	}
	public void windowOpened(WindowEvent e) {}
	public void windowClosed(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}


	/**
	 * Display a dialog showing the rule of how to play the game.
	 * Note: Only one rule dialog can exist (at a time)
	 */
	public static void showRule(){
		if(check == true){
			JLabel text = new JLabel(GuiHelper.loadImageIcon("startingDialog/info_page.png"));
			dialog.setSize(500, 650);
			dialog.add(text);
			dialog.setResizable(false);
			dialog.setVisible(true);
		}
	}
	/*
	 * Image for background and hover effect
	 */
	private final static Image bgImage = GuiHelper.loadImage("startingDialog/BG.png");
	private final static ImageIcon INFO = GuiHelper.loadImageIcon("startingDialog/info.png");
	private final static ImageIcon INFO_HOVER = GuiHelper.loadImageIcon("startingDialog/info_hover.png");
	private final static ImageIcon START = GuiHelper.loadImageIcon("startingDialog/start.png");
	private final static ImageIcon START_HOVER = GuiHelper.loadImageIcon("startingDialog/start_hover.png");
	private final static ImageIcon SERVER = GuiHelper.loadImageIcon("startingDialog/server.png");
	private final static ImageIcon SERVER_HOVER = GuiHelper.loadImageIcon("startingDialog/server_hover.png");
	private final static ImageIcon CLOSE = GuiHelper.loadImageIcon("startingDialog/close.png");
	private final static ImageIcon CLOSE_HOVER = GuiHelper.loadImageIcon("startingDialog/close_hover.png");



}
