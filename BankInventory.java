/**
 * Item array to store items in Bank for BankPlugin
 * @author Doug Frazer
 */

public class BankInventory extends ItemArray {
	
	private Player player;
	private MaxItems = 16;

	public enum value {
		ITEM_NAME,
		ITEM_QUANTITY,
		ITEM_MAX
	}
	
	public String[][] BankArray;

	public BankInvetory(Player player) {
		BankArray = new String[MaxItems][ITEM_MAX]
		this.player = player;
	}

}
