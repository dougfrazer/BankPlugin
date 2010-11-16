/**
 * Item array to store items in Bank for BankPlugin
 * @author Doug Frazer
 */

import java.util.LinkedList;

public class BankInventory extends ItemArray {
	
    public final int MaxItemsPerPage = 6;
    public int item_count;
    private BankDB database;

	public LinkedList<BankItem> BankArray;

    public BankInventory (Player p) {
        database = new BankDB();
        BankArray = database.getBankItems(p);
        item_count = database.getNumItemsInBank(p);
    }

    public void addToBankInventory(Player player, int item_id, int amount) {
        this.database.addItemToBank(player, item_id, amount);
        this.item_count = database.getNumItemsInBank(player);
    }

    public int removeFromBank(Player player, int item_id, int amount) {
        int amt_returned = 0;
        amt_returned = database.withdraw(player, item_id, amount);  
        this.item_count = database.getNumItemsInBank(player);
        return amt_returned;
    }

    public hj[] getArray() {
        return new hj[0];
    }

    public LinkedList<BankItem> getBank(Player p) {
        return database.getBankItems(p);
    }
}
