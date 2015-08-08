package unitTests;

import static org.junit.Assert.fail;
import game.userInterface.GuiHelper;

import org.junit.Test;

/**
 * The unit testing is used to check all the image that is used by dialog 
 * is located at the right package 
 * @author Jinpeng Song (songjinp)
 *
 */
public class GuiImageTest {
	
	@Test
	public void GUIImagesTest(){
		try{
			GuiHelper.loadImage("gui/GUI.png");
			GuiHelper.loadImage("gui/farmer.png");
			GuiHelper.loadImage("gui/knight.png");	
		}catch (Exception e){
			fail("fail to load images" + e.getMessage());
		}
	}
	@Test
	public void UserDialogImagesTest(){
		try{
			GuiHelper.loadImage("userdialog/background.png");
			GuiHelper.loadImage("userdialog/farmer_hover.png");
			GuiHelper.loadImage("userdialog/farmer.png");	
			GuiHelper.loadImage("userdialog/inputName.png");
			GuiHelper.loadImage("userdialog/swordsman_hover.png");
			GuiHelper.loadImage("userdialog/swordsman.png");	
		}catch (Exception e){
			fail("fail to load images"+ e.getMessage());
		}
	}
	@Test
	public void StartingDialogImagesTest(){
		try{
			GuiHelper.loadImage("startingdialog/BG.png");
			GuiHelper.loadImage("startingdialog/close_hover.png");
			GuiHelper.loadImage("startingdialog/close.png");	
			GuiHelper.loadImage("startingdialog/enter.png");
			GuiHelper.loadImage("startingdialog/enter_hover.png");
			GuiHelper.loadImage("startingdialog/info.png");	
			GuiHelper.loadImage("startingdialog/info_hover.png");
			GuiHelper.loadImage("startingdialog/info_page.png");
			GuiHelper.loadImage("startingdialog/load_server.png");
			GuiHelper.loadImage("startingdialog/load_server_hover.png");	
			GuiHelper.loadImage("startingdialog/run_server.png");
			GuiHelper.loadImage("startingdialog/run_server_hover.png");
			GuiHelper.loadImage("startingdialog/server.png");
			GuiHelper.loadImage("startingdialog/server_hover.png");
			GuiHelper.loadImage("startingdialog/start.png");
			GuiHelper.loadImage("startingdialog/start_hover.png");
		}catch (Exception e){
			fail("fail to load images"+ e.getMessage());
		}
	}
	@Test
	public void ServerDialogImagesTest(){
		try{
			GuiHelper.loadImage("serverDialog/change_map_hover.png");
			GuiHelper.loadImage("serverDialog/change_map.png");
			GuiHelper.loadImage("serverDialog/dialog-close_hover.png");	
			GuiHelper.loadImage("serverDialog/dialog-close.png");
			GuiHelper.loadImage("serverDialog/lavaspawn.jpg");
			GuiHelper.loadImage("serverDialog/stop_server.png");	
			GuiHelper.loadImage("serverDialog/stop_server_hover.png");
			GuiHelper.loadImage("serverDialog/t1.png");
			GuiHelper.loadImage("serverDialog/t2.jpg");
			GuiHelper.loadImage("serverDialog/t3.jpg");	
			GuiHelper.loadImage("serverDialog/update_map.png");
			GuiHelper.loadImage("serverDialog/update_map_hover.png");
		}catch (Exception e){
			fail("fail to load images"+ e.getMessage());
		}
	}
	
	

}
