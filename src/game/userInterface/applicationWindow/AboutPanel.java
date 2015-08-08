package game.userInterface.applicationWindow;

import game.userInterface.GuiHelper;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.Timer;


public class AboutPanel extends JPanel implements ActionListener{
	private static final long serialVersionUID = 654436404855252566L;
	private int x = 0;
	Image text = GuiHelper.loadImage("about/names.png");
	Image colours = GuiHelper.loadImage("about/colours.png");
	
	public AboutPanel(){
		Timer timer = new Timer(5, this);
		timer.start();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(colours, ++x, 0, null);
		if(x>=0){
			g.drawImage(colours, x-colours.getWidth(null), 0, null);
		}
		if(x==colours.getWidth(null)){
			x=0;
		}
		g.drawImage(text, 0, 0, null);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		this.repaint();		
	}
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(text.getWidth(null), text.getHeight(null));
	}
	
}
