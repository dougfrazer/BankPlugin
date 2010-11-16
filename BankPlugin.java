/**
* Bank Plugin
* @author Doug Frazer
*/

import java.util.logging.Level;
import java.util.logging.Logger;

public class BankPlugin extends Plugin {

    private String name = "Bank Plugin";
    private double version = 1.1;

    static final Logger log = Logger.getLogger("Minecraft");
    private BankListener listener;

    public void enable() {
        // If we had commands we would add them here.
        etc.getInstance().addCommand("/bank", "<page> - Shows the items currently in your bank");
        etc.getInstance().addCommand("/deposit", "- Deposit an item into the bank");
        etc.getInstance().addCommand("/withdraw", "[index] <amount> - Withdraw an item from your bank");
        etc.getInstance().addCommand("/setbank", "[name] [distance] - Add a new bank location");
        etc.getInstance().addCommand("/listbanks", "- Get a list of the nearby banks");
        
        ItemCache cache = new ItemCache();
        listener = new BankListener(cache);
        log.info(this.name + " " + this.version + " enabled");
    }

    public void disable() {
        //And remove the commands here.
        etc.getInstance().removeCommand("/bank");
        
        log.info(this.name + " " + this.version + " disabled");
    }

    public void initialize() {
        // Here we add the hook we're going to use. In this case it's the command event.
        etc.getLoader().addListener(PluginLoader.Hook.COMMAND, listener, this, PluginListener.Priority.HIGH);
        etc.getLoader().addListener(PluginLoader.Hook.CRAFTINVENTORY_CHANGE, listener, this, PluginListener.Priority.HIGH);
    }
}
