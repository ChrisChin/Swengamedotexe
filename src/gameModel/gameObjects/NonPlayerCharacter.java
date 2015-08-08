package gameModel.gameObjects;

/**
 * Represents a NPC in the game. Currently no implementation for friendly NPCs,
 * so all concrete implementations of this class are expected to be enemies in
 * the game.
 * 
 * The NPC will attack surrounding targets if it is aggresive. A non-aggresive
 * NPC should become aggresive if attacked.
 * 
 * @author Jiaheng Wang (wangjiah)
 * 
 */
public abstract class NonPlayerCharacter extends MortalObject {

	private static final long serialVersionUID = 3412410510637800476L;
	protected boolean isAggresive;
	protected long attackDelay = 3000;
	protected long cooldown = 0;

	public NonPlayerCharacter(int maxHP, int ATK, int DEF) {
		super(maxHP, ATK, DEF);
	}

	/**
	 * Tells you if the NPC is aggressive (attacks you)
	 * 
	 * @return true if it is aggressive, false otherwise
	 */
	public boolean isAggresive() {
		return this.isAggresive;
	}

	@Override
	protected void damage(int damage) {
		this.isAggresive = true;
		super.damage(damage);
	}

	/**
	 * Reduces the cooldown of the NPC by the amount given
	 * 
	 * @param amount
	 *            the amount to reduce the cooldown by
	 */
	public void tick(long amount) {
		cooldown -= amount;
	}

	/**
	 * Tells you if the NPC is ready to attack
	 * @return true if NPC is ready, false otherwise
	 */
	public boolean isReady() {
		return (cooldown <= 0);
	}

	/**
	 * Resets the NPC's attack cooldown. Should be called after attacking
	 */
	public void resetCooldown() {
		this.cooldown = this.attackDelay;
	}
}
