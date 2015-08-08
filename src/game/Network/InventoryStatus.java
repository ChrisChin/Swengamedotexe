package game.Network;

import java.io.Serializable;

/**
 * This object is used to send over the index of the item being used to the
 * server
 * 
 * @author Jinpeng Song (songjinp)
 *
 */
public class InventoryStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private String playerName;
	private int index;

	public InventoryStatus(String playerName, int index) {
		this.playerName = playerName;
		this.index = index;
	}

	/**
	 * Get the player name
	 * 
	 * @return player name
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * Get the used items index
	 * 
	 * @return item index
	 */
	public int getIndex() {
		return index;
	}

}
