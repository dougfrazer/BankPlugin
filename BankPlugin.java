/**
* Bank Plugin
* @author Doug Frazer
*/

// This is how we setup the listener
public class BankPlugin extends Plugin {

    private String name = "Bank Plugin";
    private int version = "0.1";

    static final Logger log = Logger.getLogger("Minecraft");
    static final BankListener listener;

    public void enable() {
        // If we had commands we would add them here.
        etc.getInstance().addCommand("/bank", "Shows the items currently in your bank");
        etc.getInstance().addCommand("/deposit", "Deposit an item into the bank");
        
        listener = new BankListener();
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
    }
}
