/**
 * BankPlayer is an extension of the Player class
 * to provide players with access to their banks
 *
 * @author Doug Frazer
 */

public class BankPlayer extends Player {

    public BankInventory inv;
    private BankDB database;
    private Player player;

    public BankPlayer(Player player) {
        database = new BankDB();
        inv = database.getBankItems(player);
        this.player = player;
    }

    public void deposit() {
        Inventory craftingTable = this.player.getCraftingTable();
        Item currItem = null;
        int i = 0;
        int num_items_deposited = 0;

        for (i=0;i<craftingTable.getArray().length;i++) {
            if(craftingTable.getItemFromSlot(i) != null) {
                // deposit item
                currItem = craftingTable.getItemFromSlot(i);
                this.database.addItemToBank(this.player, currItem.getItemId(), currItem.getAmount());

                // remove item
                craftingTable.removeItem(currItem.getSlot());
                BankItem temp = new BankItem(player.getName(), currItem);
                temp.getItemNameFromDB();
                player.sendMessage("Deposited " + currItem.getAmount() + " of " + temp.item_string + " successfully into your bank.");
                num_items_deposited++;
             }
        }

        if (num_items_deposited == 0) {
            player.sendMessage("Put items in your 2x2 crafting area to deposit them");
        } else {
            craftingTable.clearContents();
        }
        
    }

}
