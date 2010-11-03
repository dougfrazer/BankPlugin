/**
 * Bank Plugin Listener
 * @author Doug Frazer
 */

import java.util.logging.Level;
import java.util.logging.Logger;

public class BankListener extends PluginListener { 

    public boolean onCommand (Player player, String[] split) {
        BankPlayer myBank = new BankPlayer(player);
        
        // Did they enter the /bank command?
        if (split[0].equalsIgnoreCase("/bank")) {
			int i = 0;
			if(myBank.inv == null || myBank.inv.BankArray[0] == null) {
				player.sendMessage("Your bank is empty.");
			} else {
                while(myBank.inv.BankArray[i] != null) {
			        if(myBank.inv.BankArray[i].item_id != 0) {
                        myBank.inv.BankArray[i].getItemNameFromDB();
                        player.sendMessage(i + ": " + "(" + 
                                           myBank.inv.BankArray[i].quantity + 
                                           ")x " + 
                                           myBank.inv.BankArray[i].item_string);
                        i++;
                        if (i == myBank.inv.MaxItems) 
                            break;
                    }
				}
                player.sendMessage("Total bank items: " + myBank.inv.item_count);
			}
            // return true, we have completed the command
            return true;
        } else if (split[0].equalsIgnoreCase("/deposit")) { 
            myBank.deposit();
            return true;
		} 

        return false;
    }

}
