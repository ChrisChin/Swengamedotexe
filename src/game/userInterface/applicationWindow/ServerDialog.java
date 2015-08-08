package game.userInterface.applicationWindow;

import game.dataStorage.XMLFileFilter;
import game.userInterface.CleanTextField;
import game.userInterface.GuiHelper;

import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
/**
 * This Dialog is used to let the use select the server mode that
 * they want (load,create new)
 * @author Jinpeng Song (songjinp)
 */

public class ServerDialog extends JDialog{
	private static final long serialVersionUID = 1L;
	private final int PAN_WIDTH = 300;
	private final int PAN_HEIGHT = 200;
	private final JPanel panel;
	private JLabel runServerLabel;
	private JLabel loadServerLabel;
	private ImageIcon RUN_SERVER = GuiHelper.loadImageIcon("startingDialog/run_server.png");
	private ImageIcon RUN_SERVER_HOVER = GuiHelper.loadImageIcon("startingDialog/run_server_hover.png");
	private ImageIcon LOAD_SERVER = GuiHelper.loadImageIcon("startingDialog/load_server.png");
	private ImageIcon LOAD_SERVER_HOVER = GuiHelper.loadImageIcon("startingDialog/load_server_hover.png");
	private JFileChooser jfc;
	private String path;
	private int portNum;
	private JLabel portinfo;
	private JTextField port;
	
	public ServerDialog() {
		super();
		setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		setSize(PAN_WIDTH, PAN_HEIGHT);
		panel = makePanel();
		add(panel);
		jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		FileFilter xmlFilter = new XMLFileFilter();
		jfc.addChoosableFileFilter(xmlFilter);
        jfc.setAcceptAllFileFilterUsed(false);
        jfc.setFileFilter(xmlFilter);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	/**
	 * Initialize the panel with button and background images
	 * @return 
	 * 			the new panel 
	 */
	public JPanel makePanel(){
		JPanel jpan = new JPanel(){
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(GuiHelper.loadImage("serverDialog/t3.jpg"), 0, 0, this);
			}	
		};
		jpan.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.insets = new Insets(0, 60, 0, 0);
		runServerLabel = new JLabel(RUN_SERVER);
		jpan.add(runServerLabel,gbc);
		runServerLabel.addMouseListener(new MouseListener(){
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {runServerLabel.setIcon(RUN_SERVER);}
			public void mouseEntered(MouseEvent e) {runServerLabel.setIcon(RUN_SERVER_HOVER);}
			public void mouseClicked(MouseEvent e) {
				try{
					portNum = Integer.parseInt(port.getText());
					dispose();
				}catch (NumberFormatException nfe){}
			}
		});	
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.insets = new Insets(0, 60, 0, 0);
		loadServerLabel = new JLabel(LOAD_SERVER);
		jpan.add(loadServerLabel,gbc);
		loadServerLabel.addMouseListener(new MouseListener(){
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {loadServerLabel.setIcon(LOAD_SERVER);}
			public void mouseEntered(MouseEvent e) {loadServerLabel.setIcon(LOAD_SERVER_HOVER);}
			public void mouseClicked(MouseEvent e) {
				try{
					portNum = Integer.parseInt(port.getText());
					int returnVal = jfc.showOpenDialog(null);
			        if (returnVal == JFileChooser.APPROVE_OPTION) {
			            File file = jfc.getSelectedFile();
			            path = file.getAbsolutePath();
			            dispose();
			        } else {
			        	JOptionPane.showMessageDialog(null, 
			        			"No file is selected a new server will be created", "Attention",
			    				JOptionPane.OK_OPTION);
			        	dispose();
			        }
					dispose();
				}catch (NumberFormatException nfe){}
			}
		});	
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.insets = new Insets(20, 0, 0, 40);
		port = new CleanTextField("8080");
		jpan.add(port,gbc);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.insets = new Insets(20, 40, 0, 0);
		portinfo = new JLabel("Port Number");
		jpan.add(portinfo,gbc);
		return jpan;
	}
	
	/**
	 * Return the path that the server file is located
	 * null if no file is being selected
	 * @return path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Return the port the user entered
	 * @return	
	 * 			port number
	 */
	public int getPort(){
		return portNum;
	}

	
	
}
