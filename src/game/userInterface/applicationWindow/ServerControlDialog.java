package game.userInterface.applicationWindow;

import game.Network.Server;
import game.userInterface.GuiHelper;
import gameModel.gameObjects.GameObject;
import gameModel.gameObjects.Item;
import gameModel.gameObjects.NonPlayerCharacter;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameObjects.landscape.Barrel;
import gameModel.gameObjects.landscape.BronzeChest;
import gameModel.gameObjects.landscape.Tree;
import gameModel.gameWorld.GameMap;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * This dialog is use to control the server (kill server,showing map)
 * @author Jinpeng Song (songjinp)
 */

public class ServerControlDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private final int PAN_WIDTH = 300;//300
	private final int PAN_HEIGHT = 515;//200 +300 + 15(offsets)
	private final JPanel panel;
	private JLabel stopServerLabel;
	private JLabel updateMapLabel;
	private JLabel changeMapLabel;
	private Server server;
	private List<GameMap> maps;
	private int count = 0;
	private final int SQUARE_SIZE = 35;
	private final int X = 5;
	private final int Y = 200;
	private JLabel mapName = new JLabel();
	private Font font = new Font("SansSerif", Font.BOLD, 13);
	
	public ServerControlDialog(Server server) {
		super();
		setModalityType(Dialog.DEFAULT_MODALITY_TYPE);
		this.server = server;
		maps =new ArrayList<GameMap>(server.getWorld().getMaps());
		setSize(PAN_WIDTH, PAN_HEIGHT);
		panel = makePanel();
		add(panel);
		mapName.setFont(font);
		setVisible(true);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
	}

	/**
	 * Initialize the panel with button and background images
	 * 
	 * @return the new panel
	 */
	public JPanel makePanel() {
		JPanel jpan = new JPanel() {
			private static final long serialVersionUID = 1L;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(GuiHelper.loadImage("serverDialog/lavaspawn.jpg"), 0, 0,
						this);
				GameMap gm = maps.get(count);
				Map<Point, GameObject> hm = gm.getObjects();
				mapName.setText(gm.getClass().getSimpleName());
				/*Draw the mini map use to check server status
				  Tree = green
				  Player = blue
				  Enemy = red
			      Barrel/Chest/Item = yellow*/
				for(Map.Entry<Point, GameObject> entry : hm.entrySet()){
					Point point = entry.getKey();
					GameObject gameObject = entry.getValue();
					if(gameObject instanceof Barrel || gameObject instanceof BronzeChest
							|| gameObject instanceof Item)
						g.setColor(Color.YELLOW.darker());
					else if(gameObject instanceof NonPlayerCharacter)
						g.setColor(Color.RED);
					else if(gameObject instanceof Tree)
						g.setColor(Color.GREEN);
					else if(gameObject instanceof PlayerCharacter)
						g.setColor(Color.BLUE);
					else g.setColor(Color.BLACK);
					g.fillRect((point.x*(SQUARE_SIZE+1))+X, (point.y*(SQUARE_SIZE+1))+Y, 
							SQUARE_SIZE, SQUARE_SIZE);
				}
			}
		};
		jpan.setLayout(null);
		

		stopServerLabel = new JLabel(STOPSERVER);
		updateMapLabel = new JLabel(UPDATEMAP);
		changeMapLabel = new JLabel(CHANGEMAP);
		
		jpan.add(stopServerLabel);
		jpan.add(updateMapLabel);
		jpan.add(changeMapLabel);
		jpan.add(mapName);
		
		stopServerLabel.addMouseListener(new MouseListener(){
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {stopServerLabel.setIcon(STOPSERVER);}
			public void mouseEntered(MouseEvent e) {stopServerLabel.setIcon(STOPSERVER_HOVER);}
			public void mouseClicked(MouseEvent e) {
				server.stopRunning();// stop the server
				System.exit(0);
			}
		});	
		
		updateMapLabel.addMouseListener(new MouseListener(){
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {updateMapLabel.setIcon(UPDATEMAP);}
			public void mouseEntered(MouseEvent e) {updateMapLabel.setIcon(UPDATEMAP_HOVER);}
			public void mouseClicked(MouseEvent e) {
				panel.repaint();
			}
		});	
		changeMapLabel.addMouseListener(new MouseListener(){
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {changeMapLabel.setIcon(CHANGEMAP);}
			public void mouseEntered(MouseEvent e) {changeMapLabel.setIcon(CHANGEMAP_HOVER);}
			public void mouseClicked(MouseEvent e) {
				if(count+1 > maps.size()-1){
					count = 0;
				}else 
					count++;
				panel.repaint();
			}
		});	
		
		stopServerLabel.setBounds(100,40,100,40);
		updateMapLabel.setBounds(100,80,100,40);
		changeMapLabel.setBounds(100,120,100,40);
		mapName.setBounds(100,315,100,40);
		return jpan;
	}

	/*
	 * ImageIcon for hover effects
	 */
	private ImageIcon UPDATEMAP = GuiHelper.loadImageIcon("serverDialog/update_map.png");
	private ImageIcon UPDATEMAP_HOVER = GuiHelper.loadImageIcon("serverDialog/update_map_hover.png");
	private ImageIcon CHANGEMAP = GuiHelper.loadImageIcon("serverDialog/change_map.png");
	private ImageIcon CHANGEMAP_HOVER = GuiHelper.loadImageIcon("serverDialog/change_map_hover.png");
	private ImageIcon STOPSERVER = GuiHelper.loadImageIcon("serverDialog/stop_server.png");
	private ImageIcon STOPSERVER_HOVER = GuiHelper.loadImageIcon("serverDialog/stop_server_hover.png");
	

}
