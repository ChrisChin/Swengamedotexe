package game.Network;

import java.io.Serializable;

/**
 * 
 * @author Wendell (wendel), Jiaheng Wang (wangjiah)
 *
 */
public class ChatMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sender;
	String message;

	public ChatMessage(String sender, String message) {
		this.sender = sender;
		this.message = message;
	}

	/**
	 * @return the sender
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

}
