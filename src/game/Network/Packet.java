package game.Network;

import game.userInterface.RendererWindow.Renderer.Direction;
import gameModel.gameObjects.PlayerCharacter;
import gameModel.gameWorld.GameMap;
import gameModel.gameWorld.GameMap.PlayerAction;

import java.io.Serializable;

/**
 * Packet class contains player information which will be exchanged over
 * connection between server and client.
 * 
 * @author Wendell (wendel), Jiaheng Wang (wangjiah)
 * 
 */
public class Packet implements Serializable {

	public static enum Commands {
		ADD_NEW_PLAYER, GET_PLAYER_FROM_NAME, DUPLICATE_ONLINE_PLAYER, INITIAL_PULL
	}

	/**
	 * ID is defined to distinguish one Packet to another
	 */
	private static final long serialVersionUID = -1283538281852458374L;
	public PlayerCharacter playerChara;
	public ChatMessage chatMsg;
	public GameMap map;
	public String playerName;
	public Commands command;
	public int inventIndex = -1;

	/**
	 * ATTENTION! the location GameMap class and Renderer class should not be
	 * modified in both server and client in order the two fields below be able
	 * to resolve their types
	 */
	public GameMap.PlayerAction action;
	public Direction direction;

	/**
	 * Constructor for sending a packet with only a map
	 * 
	 * @param map
	 *            the GameMap to send
	 */
	public Packet(GameMap map) {
		this.map = map;
	}

	/**
	 * Constructor for sending a packet with both a player and the map. Should
	 * be used to update the client
	 * 
	 * @param pc
	 *            the PlayerCharacter to send
	 * @param map
	 *            the GameMap to send
	 */
	public Packet(PlayerCharacter pc, GameMap map) {
		this.map = map;
		this.playerChara = pc;
	}

	/**
	 * Constructor for sending a packet with only a player. Should be used by
	 * the server when the client requests a player.
	 * 
	 * @param pc
	 *            the PlayerCharacter to send
	 */
	public Packet(PlayerCharacter pc) {
		this.playerChara = pc;
	}

	/**
	 * Constructor for sending a chat message
	 * 
	 * @param cm
	 *            the ChatMessage to send
	 */
	public Packet(ChatMessage cm) {
		this.chatMsg = cm;
	}

	/**
	 * Constructor for sending an action
	 * 
	 * @param action
	 *            the PlayerAction to send
	 * @param pc
	 *            the PlayerCharacter to send
	 * @param direction
	 *            the Direction to send
	 */
	public Packet(PlayerAction action, PlayerCharacter pc, Direction direction) {
		this.playerChara = pc;
		this.playerName = pc.playerName;
		this.action = action;
		this.direction = direction;
	}

	/**
	 * Constructor for sending the GET_PLAYER_FROM_NAME command
	 * 
	 * @param command
	 *            the command to send
	 * @param playerName
	 *            the playerName to send
	 */
	public Packet(Commands command, String playerName) {
		this.playerName = playerName;
		this.command = command;
		assert (command == Commands.GET_PLAYER_FROM_NAME);
	}

	/**
	 * Constructor for sending the ADD_NEW_PLAYER command
	 * 
	 * @param command
	 *            the command to send
	 * @param pc
	 *            the playerCharacter to send
	 */
	public Packet(Commands command, PlayerCharacter pc) {
		this.playerChara = pc;
		this.command = command;
		assert (command == Commands.ADD_NEW_PLAYER);
	}

	/**
	 * Constructor for commands with no arguments
	 * 
	 * @param cmd
	 *            the command to send
	 */
	public Packet(Commands cmd) {
		this.command = cmd;
	}

	/**
	 * Constructor for using items. Only one parameter being the index of the
	 * item in the player's inventory
	 * 
	 * @param inventoryIndex
	 *            the index of the item in the inventory
	 */
	public Packet(int inventoryIndex) {
		this.inventIndex = inventoryIndex;
	}

	/**
	 * Constructor for a blank packet.
	 */
	public Packet() {
	}

	/**
	 * Determine if this packet is a message packet.
	 * 
	 * @return true if this packet is message packet, false otherwise
	 */
	public boolean hasMessage() {
		return chatMsg != null;
	}

	public boolean hasItem() {
		return inventIndex > -1;
	}

	/**
	 * Determine if this packet is only the packet carrying the world object
	 * 
	 * @return true if this packet contains world object, false otherwise
	 */
	public boolean hasMap() {
		return map != null;
	}

	/**
	 * Determine if this packet contains command message
	 * 
	 * @return true if this packet contains command message, false otherwise
	 */
	public boolean hasCommand() {
		return command != null;
	}

	/**
	 * Determine if this packet is empty
	 * 
	 * @return true if this packet is empty, false otherwise
	 */
	public boolean isEmpty() {
		if (playerChara != null)
			return false;
		if (chatMsg != null)
			return false;
		if (map != null)
			return false;
		if (playerName != null)
			return false;
		if (command != null)
			return false;
		if (action != null)
			return false;
		if (direction != null)
			return false;

		return true;
	}

	/**
	 * Determine if this packet is for event. It is usually purposed for
	 * renderer
	 * 
	 * @return true is this is event packet, false otherwise
	 */
	public boolean isAction() {
		return action != null;
	}

	@Override
	public String toString() {
		return "Packet object, " + " isMessagePacket?: " + hasMessage()
				+ " isEmpty?: " + isEmpty() + " isMap?: " + hasMap();
	}
}
