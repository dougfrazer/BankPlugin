/**
 * Bank Plugin Listener
 * @author Doug Frazer
 */


public class BankListener extends PluginListener { 

    final static int num_slots = 16;

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
                        

            // return true, we have completed the command
            return true;
        } else {
            // return false if you want this command to be parsed
            return false;
        }
    }

}
