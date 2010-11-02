/**
 * Bank Plugin Listener
 * @author Doug Frazer
 */


public class BankListener extends PluginListener { 

	public BankInventory inv = null;
	private BankDB database = new BankDB(); 

    public void disable() {
        // This gets run when the mod gets disabled so we can do cleanup
        // We just notify the console though...
        id.a.log(Level.INFO, "Bank Plugin disabled");
    }

    public void enable() {
        // This gets run when the mod gets enabled
        id.a.log(Level.INFO, "Bank Plugin enabled");
    }

    public boolean onCommand (Player player, String[] split) {
        if (split[0].equalsIgnoreCase("/bank")) {
			int i = 0;
   			inv = database.getBankItems(player);
			if(inv = null) {
				player.sendMessage("Your bank is empty.");
			} else {
				while(inv.BankArray[iter][ITEM_NAME] != 0) {
					player.sendMessage("item_id=" + 
									   BankArray[i][ITEM_NAME] + 
									   " quantity " + 
									   BankArray[i][ITEM_QUANTITY]);
					i++;
				}
			}
            // return true, we have completed the command
            return true;
        } else if (split[0].equalsIgnoreCase("/deposit") 
			if(split[1].equals("") || split[2].equals("")) {
				player.sendMessage("Syntax: /deposit <item_id> <quantity>");
				return true;
			} else {
				int item_id = 0;
				int quantity = 0;
				
				try {
					item_id = Integer.parseInt(split[1]);
					quantity = Integer.parseInt(split[2]);
				} catch (NumberFormatException) {
					player.sendMessage("Syntax: /deposit <item_id> <quantity>");
				}

				database.addItemToBank(player, item_id, quantity);
			}

		} else {
            // return false if you want this command to be parsed
            return false;
        }
    }

}
