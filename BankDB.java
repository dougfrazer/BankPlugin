/**
 * Database for Bank Plugin
 * @author Doug Frazer
 */

public class BankDB extends MySQLSource {

	public BankInventory getBankItems (Player player) {
		BankInventory inv = new BankInventory();
		Connection conn = null;
		PreparedStatement prepped = null;
		ResultSet results = null;
		int i = 0;

		try (
			prepped = conn.prepareStatement("SELECT * FROM minebank WHERE player=?;");
			prepped.setString(1, player.getName());
			results = prepped.executeQuery();

			while(results.next()) {
				inv.BankArray[i][ITEM_NAME] = results.getInt("item");
				inv.BankArray[i][ITEM_QUANTITY] = results.getInt("quantity");
				i++;
			}
		} catch (SQLException ex) {
			log.log(Level.SEVERE, "BankDB: Failed to execute SQL Query 1");
		} finally {
			try {
				if (prepped != null)
					prepped.close();
				if (results != null)
					results.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {}
		}
		return inv;
	}

	public void addItemToBank (Player player, int item_id, int quantity) {
		Connection conn = null;
		PreparedStatement prepped = null;
		PreparedStatement update = null;
		ResultSet results = null;

		try (
			prepped = conn.prepareStatement("SELECT * FROM minebank WHERE player=? AND item=?;");
			prepped.setString(1, player.getName());
			prepped.setString(2, item_id);
			results = prepped.executeQuery();

			if(results.next()) {
				if(results.getInt("quantity") > 0) {
					update = conn.prepareStatement("UPDATE minebank SET quantity=? WHERE player=? AND item=? LIMIT 1;");
					update.setInt(1, quantity);
					update.setString(2, player.getName());
					update.setInt(3, item_id);
				} else {
					update = conn.prepareStatement("INSERT INTO minebank (player, item, quantity) VALUE = (?,?,?);");
					update.setString(1, player.getName());
					update.setInt(2, item_id);
					update.setInt(3, quantity);
				}
				update.executeUpdate();
			}

		} catch (SQLException ex) {
			log.log(Level.SEVERE, "BankDB: Failed to execute SQL Query 2");
		} finally {
			try {
				if (prepped != null)
					prepped.close();
				if (results != null)
					results.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {}
		}
}
