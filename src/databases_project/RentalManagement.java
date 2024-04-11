package databases_project;
import java.util.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class RentalManagement {
	
	public static void RentEquipment(Scanner scan) {
		System.out.println("Enter your Member ID:");
		int memberID = Integer.parseInt(scan.nextLine());
		String serialNumber = null;
		int newRentalId = 0;
    	try {
    		System.out.println("Enter the model number you'd like to rent: ");
    		int modelNumber = Integer.parseInt(scan.nextLine());
    		
    		System.out.println("Enter the rental Start date (yyyy-mm-dd format): ");
    		LocalDate startDate = LocalDate.parse(scan.nextLine());
    		
    		System.out.println("Enter the rental end date (yyyy-mm-dd format): ");
    		LocalDate endDate = LocalDate.parse(scan.nextLine());
    		
    		/* Create the rental and assign the ID here */
    		
    		System.out.println("Your rental is processing, an ID will be provided shortly.");
    		
    		try (PreparedStatement stmt = Application.conn.prepareStatement(
    		    "SELECT Serial_Number FROM EQUIPMENT WHERE Model_Number = ? AND Status = TRUE LIMIT 1")) 
    		{
    		    stmt.setInt(1, modelNumber);
    		    ResultSet rs = stmt.executeQuery();
    		    if (rs.next()) 
    		    {
    		        serialNumber = rs.getString("Serial_Number");
    		    } 
    		    else 
    		    {
    		        System.out.println("No available equipment found for the model number.");
    		        return;
    		    }
    		} 
    		catch (SQLException e) {
    		    System.out.println(e);
    		    return;
    		}
    		
    		try (Statement stmt = Application.conn.createStatement();
    		     ResultSet rs = stmt.executeQuery("SELECT MAX(RentalID) AS maxId FROM RENTAL")) 
    		{
    		    if (rs.next()) 
    		    {
    		        newRentalId = rs.getInt("maxId") + 1;
    		    }
    		} 
    		catch (SQLException e) 
    		{
    		    System.out.println(e);
    		    return;
    		}
    		
    		try {
    		    /* This essentially start the transaction */
    		    Application.conn.setAutoCommit(false);

    		    /* Create the rental */
    		    try (PreparedStatement stmt = Application.conn.prepareStatement(
    		        "INSERT INTO RENTAL (RentalID, Rental_Start_Date, Rental_Due_Date, Total_Fee_Charged, MemberID) VALUES (?, ?, ?,0, ?)")) 
    		    {
    		        stmt.setInt(1, newRentalId);
    		        stmt.setObject(2, startDate);
    		        stmt.setObject(3, endDate);
    		        stmt.setInt(4, memberID);
    		        stmt.executeUpdate();
    		    }

    		    /* Create the Rental_Equipment association */
    		    try (PreparedStatement stmt = Application.conn.prepareStatement(
    		        "INSERT INTO RENTAL_EQUIPMENT (RentalID, E_Serial_Number) VALUES (?, ?)")) 
    		    {
    		        stmt.setInt(1, newRentalId);
    		        stmt.setString(2, serialNumber);
    		        stmt.executeUpdate();
    		    }

    		    /* commit on success */
    		    Application.conn.commit();
    		    System.out.println("Rental Complete!");
    		    System.out.println("Your Rental ID: " + newRentalId);
    		} 
    		catch (SQLException e) 
    		{
    		    try 
    		    {
    		    	/* Roll-back on failure */
    		        Application.conn.rollback();
    		        System.out.println("Transaction is rolled back.");
    		    } 
    		    catch (SQLException ex) 
    		    {
    		        System.out.println(ex);
    		    }
    		    e.printStackTrace();
    		} 
    		finally 
    		{
    		    try 
    		    {
    		        /* Turns back on the auto commits */
    		        Application.conn.setAutoCommit(true);
    		    } 
    		    catch (SQLException e) 
    		    {
    		        System.out.println(e);
    		    }
    		}
    		
    	}
    	catch(Exception ex) 
    	{
    		System.out.println(ex);
    	}
	}
	
	public static void ReturnRental(Scanner scan) {
		System.out.println("Enter your Member ID:");
		int memberID = Integer.parseInt(scan.nextLine());
		int rentalID = 0;
    	try {
    		System.out.println("Enter your rental ID: ");
    		rentalID = Integer.parseInt(scan.nextLine());

       		try {
       		    /* This essentially start the transaction */
       		    Application.conn.setAutoCommit(false);

       		    /* Create the rental */
       		    try (PreparedStatement stmt = Application.conn.prepareStatement(
       		        "UPDATE Rental SET Return_Date = ? WHERE RentalID = ? AND MemberID = ?;")) 
       		    {
       		        stmt.setObject(1,LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
       		        stmt.setObject(2, rentalID);
       		        stmt.setInt(3, memberID);
       		        stmt.executeUpdate();
       		    }

       		    /* Create the Rental_Equipment association */
       		    try (PreparedStatement stmt = Application.conn.prepareStatement(
       		        "DELETE FROM Rental_Equipment WHERE RentalID = ?;")) 
       		    {
       		        stmt.setInt(1, rentalID);
       		        stmt.executeUpdate();
       		    }

       		    /* commit on success */
       		    Application.conn.commit();
       		    System.out.println("Rental Return Complete!");
       		} 
       		catch (SQLException e) 
       		{
       		    try 
       		    {
       		    	/* Roll-back on failure */
       		        Application.conn.rollback();
       		        System.out.println("Transaction is rolled back.");
       		    } 
       		    catch (SQLException ex) 
       		    {
       		        System.out.println(ex);
       		    }
       		    e.printStackTrace();
       		} 
       		finally 
       		{
       		    try 
       		    {
       		        /* Turns back on the auto commits */
       		        Application.conn.setAutoCommit(true);
       		    } 
       		    catch (SQLException e) 
       		    {
       		        System.out.println(e);
       		    }
       		}
    		
    	}
    	catch(Exception ex) 
    	{
    		System.out.println(ex);
    	}
	}

	public static void ScheduleRentalDelivery(Scanner scan) {
		System.out.println("Enter your Member ID:");
		int memberID = Integer.parseInt(scan.nextLine());
		int rentalID = 0;
    	try {
    		System.out.println("Enter your rental ID: ");
    		rentalID = Integer.parseInt(scan.nextLine());

       		try {
       		    /* This essentially start the transaction */
       		    Application.conn.setAutoCommit(false);
       		    
       		    try (PreparedStatement stmt = Application.conn.prepareStatement(
       		    	"SELECT d.Serial_Number FROM DRONE d, EQUIPMENT e, RENTAL_EQUIPMENT re, MEMBER m"
       		    	+ " WHERE e.Serial_Number = re.E_Serial_Number AND re.RentalID = ? AND d.Weight_Capacity < e.Weight"
       		    	+ " AND m.MemberID = ? AND m.Warehouse_Distance < (d.Distance_Autonomy / 2) "))
       		    {
       		    	
       		    }
       		    catch(SQLException ex) {
       		    	
       		    }

       		    /* Create the rental */
       		    try (PreparedStatement stmt = Application.conn.prepareStatement(
       		        "UPDATE Rental SET Return_Date = ? WHERE RentalID = ? AND MemberID = ?;")) 
       		    {
       		        stmt.setObject(1,LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
       		        stmt.setObject(2, rentalID);
       		        stmt.setInt(3, memberID);
       		        stmt.executeUpdate();
       		    }

       		    /* Create the Rental_Equipment association */
       		    try (PreparedStatement stmt = Application.conn.prepareStatement(
       		        "DELETE FROM Rental_Equipment WHERE RentalID = ?;")) 
       		    {
       		        stmt.setInt(1, rentalID);
       		        stmt.executeUpdate();
       		    }

       		    /* commit on success */
       		    Application.conn.commit();
       		    System.out.println("Rental Return Complete!");
       		} 
       		catch (SQLException e) 
       		{
       		    try 
       		    {
       		    	/* Roll-back on failure */
       		        Application.conn.rollback();
       		        System.out.println("Transaction is rolled back.");
       		    } 
       		    catch (SQLException ex) 
       		    {
       		        System.out.println(ex);
       		    }
       		    e.printStackTrace();
       		} 
       		finally 
       		{
       		    try 
       		    {
       		        /* Turns back on the auto commits */
       		        Application.conn.setAutoCommit(true);
       		    } 
       		    catch (SQLException e) 
       		    {
       		        System.out.println(e);
       		    }
       		}
    		
    	}
    	catch(Exception ex) 
    	{
    		System.out.println(ex);
    	}
	}
	
	public static void ScheduleRentalPickup(Scanner scan) {
		System.out.println("Enter the email of the target member:");
    	String requestedEmail = scan.nextLine();
    	Member targetMember = null;
    	for (Member m : Application.members) {
    		if(m.Email.equals(requestedEmail)) {
    			targetMember = m;
    		}
    	}
    	if(targetMember == null) {
    		System.out.println("The requested member was not found");
    		return;
    	}
    	try {
    		System.out.println("Enter your rental ID: ");
    		String rentalID = scan.nextLine();
    		
    		System.out.println("Enter your preferred pick up date (yyyy-mm-dd format): ");
    		LocalDate pickupDate = LocalDate.parse(scan.nextLine());
    		
    		/* Assign the drone to pick the equipment here */
    		
    		System.out.println("Thank you for your rental, we will assign a pick-up drone shortly.");
    		
    	}catch(Exception ex) {
    		System.out.println("Invalid Input");
    	}
	}

}
