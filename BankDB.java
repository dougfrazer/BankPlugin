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

public class BankDB extends MySQLSource {

	public BankInventory getBankItems (Player player) {
		BankInventory inv = new BankInventory();
		Connection conn = null;
		PreparedStatement prepped = null;
		ResultSet results = null;
		int i = 0;

		try {
            conn = etc.getSQLConnection();
			prepped = conn.prepareStatement("SELECT * FROM minebank WHERE player=?;");
			prepped.setString(1, player.getName());
			results = prepped.executeQuery();

			while(results.next() && i < inv.MaxItems) {
                BankItem currItem = new BankItem();
				currItem.item_id = results.getInt("item");
				currItem.quantity = results.getInt("quantity");
                currItem.player = player.getName();
                inv.BankArray[i] = currItem;
				i++;
			}

            inv.item_count = getNumItemsInBank(player);

		} catch (SQLException ex) {
			log.log(Level.SEVERE, "BankDB: Failed to execute SQL Query 1");
		} finally {
			try {
				if (prepped != null) prepped.close();
				if (results != null) results.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {}
		}
		return inv;
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
			log.log(Level.SEVERE, "BankDB: Failed to execute SQL Query 2");
		} finally {
			try {
				if (update != null) update.close();
                if (query != null) query.close();
				if (results != null) results.close();
				if (conn != null) conn.close();
			} catch (SQLException e) {}
		}
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

}
