/**
 * Bank Plugin Listener
 * @author Doug Frazer
 */

import java.util.logging.Level;
import java.util.logging.Logger;

public class BankListener extends PluginListener { 

    public boolean onCommand (Player player, String[] split) {
        BankPlayer myBank = new BankPlayer(player);

        if (split[0].equalsIgnoreCase("/bank") || 
            split[0].equalsIgnoreCase("/withdraw") || 
            split[0].equalsIgnoreCase("/deposit")) { 
            if (!player.canUseCommand(split[0])) {
                return false;
            }

            // Check their distance from the bank
            if (!myBank.checkDistanceFromBank()) {
                player.sendMessage("You must be closer to the bank to interact with it.");
                player.sendMessage("Warp to the bank using /warp bank.");
                return true;
            }
        }
        
        if (split[0].equalsIgnoreCase("/bank")) {
            // Figure out what page they want to see
            int page = 1;
            if (split.length == 2) {
                // they specified a page
                try { page = Integer.parseInt(split[1]); }
                catch (NumberFormatException e) {
                    player.sendMessage("Syntax: /bank <page number>");
                }
            }

            page = page - 1;
            page = page*myBank.inv.MaxItemsPerPage;
            int i = 0;
            if(myBank.inv == null || myBank.inv.BankArray[0] == null) {
                player.sendMessage("Your bank is empty.");
                return true;
            } else {
                while(myBank.inv.BankArray[page+i] != null) {
                    if(myBank.inv.BankArray[page+i].item_id != 0) {
                        int curr_index = page + i;
                        myBank.inv.BankArray[page+i].getItemNameFromDB();
                        player.sendMessage(Colors.Green + curr_index + ") " + Colors.Rose +
                                           myBank.inv.BankArray[page+i].quantity + 
                                           "x " + 
                                           myBank.inv.BankArray[page+i].item_string);
                        i++;
                        if (i == myBank.inv.MaxItemsPerPage) 
                            break;
                    }
                }
                int curr_page = page/myBank.inv.MaxItemsPerPage + 1;
                int max_pages = (myBank.inv.item_count + myBank.inv.MaxItemsPerPage - 1)/myBank.inv.MaxItemsPerPage;
                if (curr_page <= max_pages)
                    player.sendMessage(Colors.LightBlue + "Total bank items: " + myBank.inv.item_count + Colors.Blue +
                                       " [ Page " + curr_page + " of " + max_pages + " ]");
                else
                    player.sendMessage("You only have " + max_pages + " pages of items.");
            }

            return true;

        } else if (split[0].equalsIgnoreCase("/deposit")) { 
            if (split.length == 1) { 
                myBank.deposit();
                return true;
            } else {
                player.sendMessage("Syntax: /deposit");
                return true;
            }
		} else if (split[0].equalsIgnoreCase("/withdraw")) {
            int index = 0;
            int amount = 0;

            if (split.length == 2) {
                // no amount specified
                if (split[1].equals("")) {
                    player.sendMessage("Syntax: /withdraw <index> <amount>");
                    return true;
                } else { 
                    try {
                        index = Integer.parseInt(split[1]);
                    } catch (NumberFormatException e) {
                        player.sendMessage("Syntax: /withdraw <index> <amount>");
                        return true;
                    }
                    amount = -1;
                }
            } else if (split.length == 3) {
                // index and amount specified, pass these values on
                try {
                    amount = Integer.parseInt(split[2]);
                    index = Integer.parseInt(split[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage("Syntax: /withdraw <index> <amount>");
                    return true;
                }
            } else {
                player.sendMessage("Syntax: /withdraw <index> <amount>");
                return true;
            }
            
            myBank.withdraw(index, amount);
            return true;
            }
        return false;
    }

}
