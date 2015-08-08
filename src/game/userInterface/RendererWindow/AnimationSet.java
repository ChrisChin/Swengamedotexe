package game.userInterface.RendererWindow;

import java.awt.Image;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import game.userInterface.GuiHelper;
import game.userInterface.RendererWindow.Renderer.Direction;
import gameModel.gameObjects.MortalObject;
import gameModel.gameWorld.GameMap.PlayerAction;

/**
 * Manages a character's appearance based on their action.
 * @author Geoffrey Longuet (longuegeof)
 *
 */
public class AnimationSet {	
	private Map<PlayerAction, Map<Direction, ArrayList<Image>>> mappings;
	private int walkCount = 0 ;
	private int attackCount = 0;
	public static final int WALK_ANIM_FRAMES = 9;
	public static final int ATTACK_ANIM_FRAMES = 17;
	private static final Image NO_RESOURCE_AVAILABLE = GuiHelper.loadImage("characters/dummy.png");
	private Image currentFrame;
	
	/**
	 * Constructs an AnimationSet object to manage a player's image based on
	 * their current action.
	 * @param character The character to animate.
	 * @param imagesLoc The string file root of the player's animations.
	 */
	public AnimationSet(MortalObject character, String imagesLoc){
		mappings = new HashMap<PlayerAction, Map<Direction, ArrayList<Image>>>();
		
		//WALKING
		Map<Direction, ArrayList<Image>> walkingMap = new HashMap<Direction, ArrayList<Image>>();
		for(int i=1; i<=4; i++){
			Direction currDir = Direction.values()[i%4];
			ArrayList<Image> images = new ArrayList<Image>();//images in this direction
			for(int j=0; j<=WALK_ANIM_FRAMES; j++){
				String fileString = imagesLoc + String.format("Walk/walk_%d%04d.png", i, j);
				//System.out.println("Trying to get: " + fileString);
				images.add(GuiHelper.loadImage(fileString));
			}
			walkingMap.put(currDir, images);
		}
		mappings.put(PlayerAction.MOVE, walkingMap);
		
		//ATTACKING
		Map<Direction, ArrayList<Image>> attackingMap = new HashMap<Direction, ArrayList<Image>>();
		for(int i=1; i<=4; i++){
			Direction currDir = Direction.values()[i%4];
			ArrayList<Image> images = new ArrayList<Image>();//images in this direction
			for(int j=0; j<=ATTACK_ANIM_FRAMES; j++){
				String fileString = imagesLoc + String.format("Attack/Attack_%d%04d.png", i, j);
				images.add(GuiHelper.loadImage(fileString));
			}
			attackingMap.put(currDir, images);
		}
		mappings.put(PlayerAction.ATTACK, attackingMap);
	}
	
	/**
	 * Returns the current image of the player.
	 * @return the current image of the player.
	 */
	public Image getCurrentImage(){
		return currentFrame;
	}
	
	/**
	 * Advance the player's frame by one and set it to the current image.
	 * @param act The PlayerAction in progress
	 * @param dir The direction of the character image to get.
	 * @return The image which is updated and now the currentImage. 
	 */
	public Image updateImage(PlayerAction act, Direction dir){
		Image toRet = NO_RESOURCE_AVAILABLE;
		Map<Direction, ArrayList<Image>> tmp = mappings.get(act); 
		ArrayList<Image> tmp2 = tmp.get(dir);
		if(act == PlayerAction.ATTACK){
			toRet = tmp2.get(attackCount);
		}else if(act == PlayerAction.MOVE){
			toRet = tmp2.get(walkCount);
		}
		increment(act);
		this.currentFrame = toRet;
		return toRet;
	}

	/**
	 * Add 1 to the current animation frame and loop around if the end is reached. 
	 * @param act The action type to increment.
	 */
	private void increment(PlayerAction act) {
		switch (act) {
		case MOVE:
			if(walkCount==WALK_ANIM_FRAMES-1){
				walkCount=0;
				break;
			}
			walkCount++;
			break;
		case ATTACK:
			if(attackCount==ATTACK_ANIM_FRAMES-1){
				attackCount=0;
				break;
			}
			attackCount++;
			break;
		default:
			break;
		}
		
	}

	
}
