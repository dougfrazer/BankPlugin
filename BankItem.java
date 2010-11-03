/**
 * Bank Item class for BankPlugin
 *
 * Essentially a structure definition
 *
 * @author Doug Frazer
 */

public class BankItem {
    public int quantity;
    public int item_id;
    public String player;
    public String item_string;

    public BankItem() {
        this.quantity = 0;
        this.player = "";
        this.item_id = 0;
        this.item_string = "";
    }

    public BankItem(String s, int i, int j) {
        this.player = s;
        this.item_id = i;
        this.quantity = j;
    }

    public BankItem(String s, Item i) {
        this.player = s;
        this.item_id = i.getItemId();
        this.quantity = i.getAmount();
    }

    public void getItemNameFromDB() {
        BankDB database = new BankDB();
        item_string = database.getItemNameFromID(this.item_id);
    }
}
