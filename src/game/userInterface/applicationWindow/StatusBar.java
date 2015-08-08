package game.userInterface.applicationWindow;

import game.Network.Client;
import game.Network.Packet;
import game.userInterface.GuiHelper;
import game.userInterface.RendererWindow.Renderer;
import gameModel.gameObjects.Item;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameObjects.classes.Swordsman;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * Use for displaying players status (Health, current character)
 *  @author Jinpeng Song (songjinp), Geoffrey Longuet (longuegeof)
 */
public class StatusBar extends JPanel {
	private static final long serialVersionUID = 1L;
	private static final int HEALTH_BAR_SIZE = 180;
	private Image image = GuiHelper.loadImage("gui/GUI.png");
	private PlayerCharacter character;
	private PlayerCharacter healthCharacter;
	private Client client;
	private final int MENU_IMAGE_SIZE = 60;
	private final int OFFSET = 10;
	private final int START_X = 220;
	private final int START_Y = 40;
	private String playerName;
	private List<JLabel> itemsImages = new ArrayList<JLabel>();
	private ImageIcon useInfo = GuiHelper.loadImageIcon("items/use-infov2.png");
	private Image S_ICON = GuiHelper.loadImage("gui/knight.png");
	private Image F_ICON = GuiHelper.loadImage("gui/farmer.png");
	private ChatBox chatbox;
	
	public StatusBar(ChatBox chatbox, Client client, PlayerCharacter chara){
		this.playerName = chara.playerName;
		this.character = chara;
		this.healthCharacter = chara;
		this.client = client;
		this.chatbox = chatbox;
		updateGui();
		setLayout(null);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Renderer.BG_COLOR);
		g.fillRect(0, 0, image.getWidth(null), image.getHeight(null));
		if(character instanceof Swordsman){ //draw the swordsman icon 
			g.drawImage(S_ICON, 60, 40, null);
		}else{ //draw the farmersicon 
			g.drawImage(F_ICON, 60, 40, null);
		}
		int healthSize = HEALTH_BAR_SIZE * healthCharacter.getHealth() / healthCharacter.getMaxHealth();
		g.setColor(Color.RED);
		g.fill3DRect(20, 10, healthSize, 20, true);
		g.drawImage(image, 0, 0 ,this);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Font replace = g2d.getFont().deriveFont(Font.BOLD).deriveFont(18f);
		g2d.setFont(replace);
		g2d.setColor(Color.WHITE);
		g2d.drawString(playerName, 20, 160);
	}


	/**
	 * Update the current players information
	 * @param character
	 * 					the new PlayerCharacter
	 */
	public void updatePlayer(PlayerCharacter character){
		this.character = character;
		this.playerName = character.playerName;
		updateGui();
		this.validate();
		this.repaint();
	}
	
	/**
	 * Update the current players health
	 * @param character
	 * 					the new PlayerCharacter
	 */
	public void updateHealth(PlayerCharacter character){
		this.healthCharacter = character;
		this.repaint();
	}
	
	/**
	 * Update and redraw the image (icon in labels) in the panel
	 */
	public void updateGui(){
		for(JLabel jl : itemsImages){
			jl.getParent().remove(jl);
		}
		itemsImages.clear();
		final List<Item> inven = character.getInventoryItems();
		for(int i = 0; i < inven.size(); i++){
			int pos = i;
			int yOffset = 0;
			if(i > 3){
				pos  = pos - 4;
				yOffset = MENU_IMAGE_SIZE+OFFSET;
			}
			final int LabelPos = i;
			final JLabel jl = new JLabel(inven.get(i).getMenuImageIcon());
			jl.setBounds(START_X + (MENU_IMAGE_SIZE+OFFSET)*pos, START_Y + yOffset,
					MENU_IMAGE_SIZE, MENU_IMAGE_SIZE);
			jl.addMouseListener(new MouseListener() {
				private boolean check = true;
				private int yOff;
				public void mouseReleased(MouseEvent e) {}
				public void mousePressed(MouseEvent e) {}
				@Override
				public void mouseExited(MouseEvent e) {
					jl.setIcon(inven.get(LabelPos).getMenuImageIcon());
					check = true;
				}
				@Override
				public void mouseEntered(MouseEvent e) {jl.setIcon(useInfo);}
				@Override
				public void mouseClicked(MouseEvent e) {
					if(check == true){
						yOff = START_Y;
						if(LabelPos > 3)
							yOff -= 3;
						int Height = yOff + (MENU_IMAGE_SIZE/2);
						if (e.getY() > yOff && e.getY() < Height) {
							chatbox.displayingSystemMessage(inven.get(LabelPos).description());
						}else {
							chatbox.displayingSystemMessage("Trying to Use");
							//update to server
							client.addPacket(new Packet(LabelPos));
						}
						check = false;
					}
				}
			});
			add(jl);
			itemsImages.add(jl);
		}
	}

}
