package sql_package;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import databases_project.Application;

import databases_project.Manageable;

public class SQL {
	
	
	public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }

        String whitelistPattern = "[a-zA-Z0-9\\\\s,.!?@()-]+";

        StringBuilder sanitized = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (String.valueOf(c).matches(whitelistPattern)) {
                sanitized.append(c);
            }
        }

        return sanitized.toString();
    }
	
	private static void outputResult(ResultSet result, ResultSetMetaData rsmd) {
		try {
			int columnCount = rsmd.getColumnCount();
			for (int i = 1; i <= columnCount; i++) {
				String value = rsmd.getColumnName(i);
				System.out.print(value);
				if (i < columnCount) System.out.print(",  ");
			}
			System.out.print("\n");
			while (result.next()) {
				for (int i = 1; i <= columnCount; i++) {
					String columnValue = result.getString(i);
					System.out.print(columnValue);
					if (i < columnCount) System.out.print(",  ");
				}
				System.out.print("\n");
			}
			result.close();			
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public static void ps_ExecuteQuery(String sql) {
		try {
			PreparedStatement stmt = Application.conn.prepareStatement(sql);
			ResultSet result = stmt.executeQuery();
            ResultSetMetaData rsmd = result.getMetaData();
            outputResult(result, rsmd);
            stmt.close();
		}
		catch(SQLException ex) {
			
		}
	}
	
	public static void ps_ExecuteQueryOnMember(String sql, int MemberID) {
		try {
			PreparedStatement stmt = Application.conn.prepareStatement(sql);		
			stmt.setInt(1, MemberID);
			ResultSet result = stmt.executeQuery();
            ResultSetMetaData rsmd = result.getMetaData();
            outputResult(result, rsmd);
            stmt.close();
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
	public static void ps_SearchMember(int memberID) {
		try {
			String sql = "SELECT * FROM Member WHERE MemberID = ?";
			PreparedStatement stmt = Application.conn.prepareStatement(sql);		
			stmt.setInt(1, memberID);
			ResultSet result = stmt.executeQuery();
            ResultSetMetaData rsmd = result.getMetaData();
            outputResult(result, rsmd);
            stmt.close();
		}
		catch(SQLException ex){
			
		}
		
	}
	
	public static void ps_SearchEquipment(String equipmentSN) {
		try {
			String sql = "SELECT * FROM EQUIPMENT WHERE Serial_Number = ?";
			PreparedStatement stmt = Application.conn.prepareStatement(sql);		
			stmt.setString(1, equipmentSN);
			ResultSet result = stmt.executeQuery();
            ResultSetMetaData rsmd = result.getMetaData();
            outputResult(result, rsmd);
            stmt.close();
		}
		catch(SQLException ex){
			
		}
		
	}
	
	public static void ps_SearchDrone(String droneSN) {
		try {
			String sql = "SELECT * FROM DRONE WHERE Serial_Number = ?";
			PreparedStatement stmt = Application.conn.prepareStatement(sql);		
			stmt.setString(1, droneSN);
			ResultSet result = stmt.executeQuery();
            ResultSetMetaData rsmd = result.getMetaData();
            outputResult(result, rsmd);
            stmt.close();
		}
		catch(SQLException ex){
			
		}
		
	}
	
	public static void ps_AddManageable(Manageable m, String table) {
        List<Object> values = new ArrayList<>();
        StringBuilder sql = new StringBuilder("INSERT INTO "+table+" (");
        StringBuilder placeholders = new StringBuilder(") VALUES (");
        Field[] fields = m.getClass().getFields();
        boolean isFirst = true;

        for (Field field : fields) {

            if (!isFirst) {
                sql.append(", ");
                placeholders.append(", ");
            } else {
                isFirst = false;
            }

            sql.append(field.getName());
            placeholders.append("?");

            field.setAccessible(true);
            try {
				values.add(field.get(m));
			} catch (Exception e) {

			} 
        }

        sql.append(placeholders.toString()).append(")");

        try {
        	PreparedStatement stmt = Application.conn.prepareStatement(sql.toString());
            for (int i = 0; i < values.size(); i++) {
                stmt.setObject(i + 1, values.get(i));
            }

            int affectedRows = stmt.executeUpdate();
            System.out.println(affectedRows + " row(s) updated.");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("An error occurred updating the Entity: " + e.getMessage());
        }
	}
	
	public static void ps_RemoveMember(int memberID) {

		
		String sql = "DELETE FROM Member WHERE MemberID = ?;";
		try {
			PreparedStatement stmt = Application.conn.prepareStatement(sql);	
			stmt.setInt(1, memberID);
			stmt.executeUpdate();
            stmt.close();
		}
		catch(SQLException ex) {
			System.out.println("Error removing member with Id: " + memberID);
		}
	}
	
	public static void ps_RemoveEquipment(String serialNumber) {

		
		String sql = "DELETE FROM EQUIPMENT WHERE Serial_Number = ?;";
		try {
			PreparedStatement stmt = Application.conn.prepareStatement(sql);	
			stmt.setString(1, serialNumber);
			stmt.executeUpdate();
            stmt.close();
		}
		catch(SQLException ex) {
			System.out.println("Error removing equipment with Serial Number: " + serialNumber);
		}
	}
	
	public static void ps_RemoveDrone(String serialNumber) {

		
		String sql = "DELETE FROM DRONE WHERE Serial_Number = ?;";
		try {
			PreparedStatement stmt = Application.conn.prepareStatement(sql);	
			stmt.setString(1, serialNumber);
			stmt.executeUpdate();
            stmt.close();
		}
		catch(SQLException ex) {
			System.out.println("Error removing drone with Serial Number: " + serialNumber);
		}
	}
	
	public static void ps_EditManageable(Object keyValue, Map<String, Object> changes, String primaryKey, String table) {
        if (changes.isEmpty()) {
            System.out.println("No changes provided.");
            return;
        }

        /* Start building the SQL query for the update */
        StringBuilder sql = new StringBuilder("UPDATE "+table+" SET ");
        Set<String> fields = changes.keySet();
        
        /* Need to add one because email is not listed as a change */
        Object[] values = new Object[changes.size() + 1];
        
        /* Loop through each field and append the change to the SQL query */
        int i = 0;
        for (String field : fields) {
            sql.append(i > 0 ? ", " : "").append(field).append(" = ?");
            values[i++] = changes.get(field);
        }
        sql.append(" WHERE "+primaryKey+" = ?;");
        values[i] = keyValue;

        /* Build the PreparedStatement */
        try{
        	PreparedStatement stmt = Application.conn.prepareStatement(sql.toString());
        	/* We don't know the type, so just set the object */
        	/* Might change this later to make it a Map<String, Dictionary<String, Object>> with the conversions*/
            for (int j = 0; j < values.length; j++) {
                stmt.setObject(j + 1, values[j]);
            }
            
            /* Execute the update */
            int affectedRows = stmt.executeUpdate();
            System.out.println(affectedRows + " row(s) updated.");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("An error occurred updating the Member: " + e.getMessage());
        }
    }

}
