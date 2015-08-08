package gameModel.gameObjects;

/**
 * Represents a Mortal (alive) object in the game. Mortal objects can attack and
 * can be attack. If attacked and health reaches 0 or lower, the object is
 * considered dead, and should be removed from the game (set to null)
 * 
 * @author Jiaheng Wang (wangjiah) , Chris Chin (chinchri1)
 * 
 */
public abstract class MortalObject implements GameObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7902462916002089548L;
	private int health;
	private int maxHealth;
	private int attack;
	private int defence;

	public MortalObject(int maxHP, int ATK, int DEF) {
		this.health = this.maxHealth = maxHP;
		this.attack = ATK;
		this.defence = DEF;
	}

	/**
	 * Makes this object attack any other GameObject. Assumes parameter is not
	 * null
	 * 
	 * @param obj
	 *            the object to attack
	 * @return true if attack occured
	 */
	public final boolean attack(GameObject obj) {
		assert (obj != null);
		if (!(obj instanceof MortalObject)) {
			return false;
		}
		MortalObject mo = (MortalObject) obj;
		mo.damage(this.attack - mo.defence);
		return true;
	}

	/**
	 * Damages this object by the amount given
	 * 
	 * @param damage
	 *            amount of damage to cause
	 */
	protected void damage(int damage) {
		this.health -= damage;
	}

	/**
	 * Recovers this object's hp by given amount
	 * 
	 * @param amount
	 *            amount to recover by
	 * @return true if any amount recovered (may be less than amount given)
	 */
	public boolean recover(int amount) {
		if (this.health >= this.maxHealth) {
			return false;
		}
		this.health = Math.min(this.health + amount, this.maxHealth);
		return true;
	}

	/**
	 * Returns whether the MortalObject is dead
	 * 
	 * @return true if the object is dead, false otherwise
	 */
	public boolean isDead() {
		return this.health <= 0;
	}

	/**
	 * Returns the health
	 * 
	 * @return health the health of this MortalObject
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Sets the health to the new health
	 * 
	 * @param health
	 *            the new health
	 */
	public void setHealth(int health) {
		this.health = health;
	}

	/**
	 * Gets the max health
	 * 
	 * @return the max health
	 */
	public int getMaxHealth() {
		return maxHealth;
	}

	/**
	 * Sets the max health to the new max health
	 * 
	 * @param maxHealth
	 *            the new maxHealth
	 */
	public void setMaxHealth(int maxHealth) {
		this.maxHealth = maxHealth;
	}

	/**
	 * Gets the attack
	 * 
	 * @return attack the attack power of this MortalObject
	 */
	public int getAttack() {
		return attack;
	}

	/**
	 * Sets the attack to the new attack
	 * 
	 * @param attack
	 *            the attack power to set to
	 */
	public void setAttack(int attack) {
		this.attack = attack;
	}

	/**
	 * Gets the defence
	 * 
	 * @return defence the defence points of this MortalObject
	 */
	public int getDefence() {
		return defence;
	}

	/**
	 * Sets the defence to the new defence
	 * 
	 * @param defence the new defence of this MortalObject
	 */
	public void setDefence(int defence) {
		this.defence = defence;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
