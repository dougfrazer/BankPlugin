/**
 * Database for Bank Plugin
 * @author Doug Frazer
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.LinkedList;

public class BankDB extends MySQLSource {

	public BankItem[] getBankItems (Player player, int size) {
		BankItem item_array[] = new BankItem[size];
		Connection conn = null;
		PreparedStatement prepped = null;
		ResultSet results = null;
		int i = 0;

		try {
            conn = etc.getSQLConnection();
			prepped = conn.prepareStatement("SELECT * FROM minebank WHERE player=?;");
			prepped.setString(1, player.getName());
			results = prepped.executeQuery();

			while(results.next() && i < size) {
                BankItem currItem = new BankItem();
				currItem.item_id = results.getInt("item");
				currItem.quantity = results.getInt("quantity");
                currItem.player = player.getName();
                item_array[i] = currItem;
				i++;
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, "BankDB: Failed to execute SQL Query 1");
		} finally {
			try {
				if (prepped != null) prepped.close();
				if (results != null) results.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {}
		}
		return item_array;
	}

	public void addItemToBank (Player player, int item_id, int quantity) {
		Connection conn = null;
		PreparedStatement query = null;
		PreparedStatement update = null;
		ResultSet results = null;

		try {
            conn = etc.getSQLConnection();
		    query = conn.prepareStatement("SELECT * FROM minebank WHERE player=? AND item=?;");
			query.setString(1, player.getName());
			query.setInt(2, item_id);
			results = query.executeQuery();

			if(results.next()) {
				update = conn.prepareStatement("UPDATE minebank SET quantity=? WHERE player=? AND item=? LIMIT 1;");
				update.setInt(1, quantity+results.getInt("quantity"));
				update.setString(2, player.getName());
				update.setInt(3, item_id);
                update.executeUpdate();
            } else {
                // The item doesnt already exist for the player, so insert it
                update = conn.prepareStatement("INSERT INTO minebank (player, item, quantity) VALUES (?,?,?);");
                update.setString(1, player.getName());
                update.setInt(2, item_id);
                update.setInt(3, quantity);
                update.executeUpdate();
            }
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "BankDB: Failed to addItemToBank for " + player.getName());
		} finally {
			try {
				if (update != null) update.close();
                if (query != null) query.close();
				if (results != null) results.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {}
		}
    }
    
    public int withdraw(Player player, int item_id, int amount) {
        Connection conn = null;
        PreparedStatement query = null;
        PreparedStatement update = null;
        ResultSet results = null;

        try {
            conn = etc.getSQLConnection();
            query = conn.prepareStatement("SELECT quantity FROM minebank WHERE player=? AND item=?;");
            query.setString(1, player.getName());
            query.setInt(2, item_id);
            results = query.executeQuery();

            if(results.next()) {
                if(results.getInt("quantity") < amount) {
                    player.sendMessage("You only have " + results.getInt("quantity") + " of that item");
                    return 0;
                } else if (results.getInt("quantity") == amount || amount == -1) {
                    update = conn.prepareStatement("DELETE FROM minebank WHERE player=? AND item=? AND quantity=? LIMIT 1;");
                    update.setString(1, player.getName());
                    update.setInt(2, item_id);
                    update.setInt(3, results.getInt("quantity"));
                    update.executeUpdate();
                    return results.getInt("quantity");
                } else {
                    update = conn.prepareStatement("UPDATE minebank SET quantity=? WHERE player=? AND item=? LIMIT 1;");
                    update.setInt(1, results.getInt("quantity")-amount);
                    update.setString(2, player.getName());
                    update.setInt(3, item_id);
                    update.executeUpdate();
                    return amount;
                }
            } else {
                player.sendMessage("Could not find that item in your bank");
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "BankDB: Failed to withdraw item for player " +player.getName());
        } finally {
			try {
				if (update != null) update.close();
                if (query != null) query.close();
				if (results != null) results.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {}
        }
    return 0;
    }

    public int getNumItemsInBank (Player player) {
		Connection conn = null;
		PreparedStatement query = null;
		ResultSet results = null;
        int ret_val = 0;
        
        try {
            conn = etc.getSQLConnection();
            query = conn.prepareStatement("SELECT COUNT(DISTINCT item) as num_items FROM minebank WHERE player=?;");
            query.setString(1, player.getName());
            results = query.executeQuery();

            if(results.next()) {
                ret_val = results.getInt("num_items");
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE, "BankDB: Failed to get item count for player " + player.getName());
        } finally {
            try {
                if (query != null) query.close();
                if (results != null) results.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
        return ret_val;
    }

    public String getItemNameFromID(int item_id) {
        Connection conn = null;
        PreparedStatement query = null;
        ResultSet results = null;
        String ret_string = "";

        try {
            conn = etc.getSQLConnection();
            query = conn.prepareStatement("SELECT name FROM items WHERE itemid=?;");
            query.setInt(1, item_id);
            results = query.executeQuery();

            if(results.next()) {
                ret_string = results.getString("name");
            }

        } catch (SQLException e) {
            log.log(Level.SEVERE, "BankDB: Failed to get item name for itemid " + item_id);
        } finally {
            try {
                if (query != null) query.close();
                if (results != null) results.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
        return ret_string;
    }

    public LinkedList<Location> getBankLocations() {
        LinkedList<Location> locations = new LinkedList<Location>();
        Connection conn = null;
        PreparedStatement query = null;
        ResultSet results = null;

        try {
            conn = etc.getSQLConnection();
            query = conn.prepareStatement("SELECT x,y,z FROM warps WHERE name='bank';");
            results = query.executeQuery();
    
            while(results.next()) {
                Location newLoc = new Location(results.getInt("x"),
                                               results.getInt("y"),
                                               results.getInt("z"));
                locations.add(newLoc);
            }

            return locations;
        } catch (SQLException e) {
            log.log(Level.SEVERE, "BankDB: Failed to get list of banks");
        } finally {
            try {
                if (query != null) query.close();
                if (results != null) results.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
        return null;
    }

}
