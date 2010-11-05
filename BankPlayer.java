/**
 * BankPlayer is an extension of the Player class
 * to provide players with access to their banks
 *
 * @author Doug Frazer
 */

import java.util.LinkedList;
import java.lang.Math;

public class BankPlayer extends Player {

    public BankInventory inv;
    private Player player;
    private final int MaxXFromBank = 8;
    private final int MaxYFromBank = 8;
    private final int MaxZFromBank = 8;

    public BankPlayer(Player player) {
        inv = new BankInventory(player);
        this.player = player;
    }

    public void deposit() {
        // Start at the beginning of inventory
        Inventory charCrafting = player.getCraftingTable();
        Item currItem = null;
        int i = 0;
        int num_items_deposited = 0;

        for (i=0;i<charCrafting.getArray().length;i++) {
            if(charCrafting.getItemFromSlot(i) != null) {
                // deposit item
                currItem = charCrafting.getItemFromSlot(i);
                this.inv.addToBankInventory(player, currItem.getItemId(), currItem.getAmount());

                // remove item
                BankItem temp = new BankItem(player.getName(), currItem);
                charCrafting.removeItem(currItem.getSlot());
                charCrafting.updateInventory();
                temp.getItemNameFromDB();
                player.sendMessage("Deposited " + currItem.getAmount() + " of " + temp.item_string + " successfully into your bank.");
                num_items_deposited++;
            }
        }

        if (num_items_deposited == 0) {
            player.sendMessage("Put items in your crafting square.");
        }
    }

    public void withdraw(int index, int amount) {
        int amt_returned = 0;
        if (this.inv.BankArray[index] != null) {
            if(this.inv.BankArray[index].item_id != 0) {
                amt_returned = this.inv.removeFromBank(this.player, this.inv.BankArray[index].item_id, amount);
            }
            player.giveItem(this.inv.BankArray[index].item_id, amt_returned);
            this.inv.BankArray[index].getItemNameFromDB();
            if (amt_returned > 0)
                player.sendMessage("Successfully withdrew " + amt_returned + " of " + this.inv.BankArray[index].item_string);
        }
    }

    public boolean checkDistanceFromBank() {
        BankDB database = new BankDB();
        LinkedList<Location> locs;
        locs = database.getBankLocations();
        Location currLoc = this.player.getLocation();
        int i=0;

        if (locs == null || locs.size() == 0) {
            this.player.sendMessage("locs == null");
            return false;
        }
        while(i < locs.size()) {
            Location bankLoc = locs.get(i);
        /*    this.player.sendMessage("X: " + Math.abs(currLoc.x - bankLoc.x) + 
                                    " Y: " + Math.abs(currLoc.y - bankLoc.y) +
                                    " Z: " + Math.abs(currLoc.z - bankLoc.z));
        */
            if(Math.abs(currLoc.x - bankLoc.x) < MaxXFromBank &&
               Math.abs(currLoc.y - bankLoc.y) < MaxYFromBank &&
               Math.abs(currLoc.z - bankLoc.z) < MaxZFromBank)
                return true;
            i++;
        }
        return false;
    }

}
