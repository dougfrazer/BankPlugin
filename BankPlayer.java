/**
 * BankPlayer is an extension of the Player class
 * to provide players with access to their banks
 *
 * @author Doug Frazer
 */

import java.util.LinkedList;
import java.lang.Math;
import java.util.logging.Level;

public class BankPlayer extends Player {

    public BankInventory inv;
    private Player player;

    public BankPlayer(Player player) {
        inv = new BankInventory(player);
        this.player = player;
    }

    public BankItem[] deposit() {
        // Start at the beginning of inventory
        BankDB database = new BankDB();
        database.setInventoryLock(player, true);

        Inventory charCrafting = player.getCraftingTable();
        BankItem[] ret = new BankItem[charCrafting.getArray().length];
        Item currItem = null;
        int i = 0;
        int num_items_deposited = 0;

        for (i=0;i<charCrafting.getArray().length;i++) {
            if(charCrafting.getItemFromSlot(i) != null) {
                currItem = charCrafting.getItemFromSlot(i);
                BankItem temp = new BankItem(player.getName(), currItem);
                this.inv.addToBankInventory(player, currItem.getItemId(), currItem.getAmount());
                ret[i] = temp;
                num_items_deposited++;
            }
        }
        if (num_items_deposited == 0) return null;

        // Enter an infinite loop to try and delete items;
        charCrafting.clearContents();
        charCrafting.updateInventory();

        database.setInventoryLock(player, false);
        return ret;
    }

    public BankItem withdraw(int index, int amount) {
        int amt_returned = 0;
        BankItem currItem;
        try { currItem = this.inv.BankArray.get(index); }
        catch (IndexOutOfBoundsException e) {
            return null;
        }

        if(currItem.item_id != 0) {
            amt_returned = this.inv.removeFromBank(this.player, currItem.item_id, amount);
        }
        player.giveItem(currItem.item_id, amt_returned);
        currItem.quantity = amt_returned;
        return currItem;
    }

    public boolean checkDistanceFromBank() {
        BankDB database = new BankDB();
        LinkedList<BankLocation> locs;
        locs = database.getBankLocations();
        Location currLoc = this.player.getLocation();
        int i=0;

        if (locs == null || locs.size() == 0) {
            this.player.sendMessage("locs == null");
            return false;
        }
        while(i < locs.size()) {
            BankLocation bank = locs.get(i);
            double distance = Math.sqrt(Math.pow(bank.x - currLoc.x,2) +
                                        Math.pow(bank.y - currLoc.y,2) +
                                        Math.pow(bank.z - currLoc.z,2));
            if (distance < bank.distance) return true;
            i++;
        }
        return false;
    }

    public LinkedList<BankLocation> getBanks() {
        BankDB database = new BankDB();
        return database.getBankLocations();
    }

    public boolean setBank(String s, double d) {
        BankLocation b = new BankLocation(s);
        BankDB database = new BankDB();

        b.x = player.getLocation().x;
        b.y = player.getLocation().y;
        b.z = player.getLocation().z;
        b.distance = d;
        
        return database.setBank(b);
    }

}
