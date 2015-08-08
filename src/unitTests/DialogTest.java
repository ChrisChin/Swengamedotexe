package unitTests;

import static org.junit.Assert.fail;
import game.userInterface.applicationWindow.CharacterDialog;
import game.userInterface.applicationWindow.ChatBox;
import game.userInterface.applicationWindow.PlayerNameDialog;
import gameModel.gameObjects.classes.PigFarmer;
import gameModel.gameObjects.classes.Swordsman;

import org.junit.Test;
/**
 * @author Jinpeng Song (songjinp)
 */
public class DialogTest {
	@Test
	public void characterDialogTest(){
		CharacterDialog cd = new CharacterDialog("tom");
		if(cd.getNewPlayer() == null){
			fail("The new Player should be null");
		}
		if(!(cd.getNewPlayer() instanceof Swordsman|| cd.getNewPlayer() instanceof PigFarmer)){
			fail("The new player should be swordsman or pig farmer");
		}
		if(cd.getName().equals("tom")){
			fail("The name should stays the same");
		}
	}
	
	@Test
	public void chatBoxTest(){
		String s1 = "\n";
		String s2 = "\0";
		String s3 = "      ";
		String s4 = "";
		String s5 = "tom";
		String s6 = "t om";
		String s7 = "o   t   o";
		String s8 = "eajteijai";
		if(ChatBox.validMessage(s1)){fail("Can't have new line character");}
		if(ChatBox.validMessage(s2)){fail("Can't have null charcter");}
		if(ChatBox.validMessage(s3)){fail("Can't have space only");}
		if(ChatBox.validMessage(s4)){fail("Can't have empty string");}
		
		if(!ChatBox.validMessage(s5)){fail("Name should be valid");}
		if(!ChatBox.validMessage(s6)){fail("Name should be valid");}
		if(!ChatBox.validMessage(s7)){fail("Name should be valid");}
		if(!ChatBox.validMessage(s8)){fail("Name should be valid");}
	}
	

	@Test
	public void playerNameTest(){
		PlayerNameDialog png = new PlayerNameDialog();
		String playerName = "Replace with your name";
		String IPAddress = "127.0.0.1";
		int port = 8080;
		if(!png.getPlayerName().equals(playerName)){fail();}
		if (png.getPort() != port) {fail();}
		if(!png.getIPAddress().equals(IPAddress)){fail();}
		
	}
}
