package game;

import game.Network.Server;
import game.userInterface.applicationWindow.StartingDialog;
import gameModel.gameWorld.World;

/**
 * 
 * This class starts the game
 * @author Jinpeng Song (songjinp)
 */

public class Main {
	public static void main(String[] args) {
		if(args.length == 0){
			new StartingDialog();
		} else {
			World world = new World();
			Server server = new Server(world, Integer.parseInt(args[0]));
			server.start();
		}
	}

}