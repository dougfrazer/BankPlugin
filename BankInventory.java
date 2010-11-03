/**
 * Item array to store items in Bank for BankPlugin
 * @author Doug Frazer
 */

public class BankInventory extends ItemArray {
	
	public final int MaxItems = 4;
    public int item_count;

	public BankItem[] BankArray;

	public BankInventory() {
		BankArray = new BankItem[MaxItems];
	    item_count = 0;
    }
    
    public hh[] getArray() {
        return new hh[0];
    }

}
