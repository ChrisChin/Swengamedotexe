package gameModel.gameObjects;

/**
 * Represents an ImmortalObject in the game. Immortal Objects cannot be attacked
 * and cannot die. Common implementations of ImmortalObjects include landscape
 * and items.
 * 
 * @author Jiaheng Wang (wangjiah) , Chris Chin (chinchri1)
 */
public abstract class ImmortalObject implements GameObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2556883665153537386L;
	
	public final String name;

	public ImmortalObject(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImmortalObject other = (ImmortalObject) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
