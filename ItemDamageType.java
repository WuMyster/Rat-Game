/**
 * How item loses health
 * @author 2010573
 *
 */
public enum ItemDamageType {
	/**
	 * Every tick the item will lose 1 hp.
	 */
	DOT, //Gas, Bomb
	
	/**
	 * Every time the item interacts with a Rat, it will lose 1 hp.
	 */
	USE; //1hp: Poison, Sex change; >1hp: Stop sign, Death rat
}
