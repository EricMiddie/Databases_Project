package databases_project;
import java.util.*;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.sql.DriverManager;

public class Application {
	static int MAIN_MENU_OPTIONS_COUNT = 9;
	static int SUB_MENU_OPTIONS_COUNT = 5;
	public static ArrayList<Member> members;
	public static String DATABASE = "equipment_renting_system.db";
	public static Connection conn = null;
	
	public static void main(String[] args) {
		/* create the list of members */
		members = new ArrayList<Member>();
		conn = initialize(DATABASE);
		Scanner scan = new Scanner(System.in);

		mainMenuPrompt(scan);

		scan.close();
	}
	
	
	public static Connection initialize(String database) {
		String url = "jdbc:sqlite:" + database;
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(url);
			if(conn != null) {
				DatabaseMetaData meta = conn.getMetaData();
				System.out.println(meta.getDriverName());
				
				System.out.println("Conenction to the database was successful");
			}
			else {
				System.out.println("Connection was null");
			}
		}
		catch(SQLException ex) {
			System.out.println(ex.getMessage());
			System.out.println("Error getting Connection");
		}
		return conn;
	}
	public static void mainMenuPrompt(Scanner scan) {
		/* Method to continuously call the main menu pompt */
		while(true) {
			System.out.println("0.) Exit Program");
			System.out.println("1.) Manage Members");
			System.out.println("2.) Manage Equipment");
			System.out.println("3.) Manage Drones");
			System.out.println("4.) Rent Equipment");
			System.out.println("5.) Return Equipment on site");
			System.out.println("6.) Schedule Delivery of Equipment via Drone");
			System.out.println("7.) Schedule Return of Equipment via drone");
			System.out.println("8.) Useful Reports");
			System.out.print("Enter the number of your selection: ");
			int value = getUserInput(scan, MAIN_MENU_OPTIONS_COUNT-1);	
			
			switch(value) {
				case 1: 
					manageableSubMenuPrompt(scan, "members", new Member());
					break;
				case 2: 
					System.out.println("Not Implemented");
//					subMenuPrompt(scan, "equipment", new Equipment());
					break;
				case 3: 
					System.out.println("Not Implemented");
//					subMenuPrompt(scan, "drones", new Drone());
					break;
				case 4: 
					RentalManagement.RentEquipment(scan);
					break;
				case 5: 
					RentalManagement.ReturnRental(scan);
					break;
				case 6: 
					RentalManagement.ScheduleRentalDelivery(scan);
					break;
				case 7: 
					RentalManagement.ScheduleRentalPickup(scan);
					break;
				case 8:
					UsefulReports.usefulReportsPrompt(scan);
					break;
				default:
					return;
			}
		}
	}
	
	public static int getUserInput(Scanner scan, int maxEntry) {
		/* Method that makes sure the user has entered a valid number within a range */
		String input = scan.nextLine();
		try {
			int value = Integer.parseInt(input);
			if(value < 0 || value > maxEntry) {
				throw new NumberFormatException();
			}
			return value;
		}
		catch(NumberFormatException ex) {
			System.out.println("Please enter the number of the wanted option");
			return getUserInput(scan , maxEntry);
		}
	}
	
	public static void manageableSubMenuPrompt(Scanner scan, String subgroup, Manageable entity) {
		/* Method to interact with the "entity" types, adding, editing, or deleting */
		System.out.println("0.) Go Back");
		System.out.println("1.) Add");
		System.out.println("2.) Edit");
		System.out.println("3.) Delete");
		System.out.println("4.) Search By Email");
		System.out.print("What do you want to do for " + subgroup + ": ");
		int value = getUserInput(scan, SUB_MENU_OPTIONS_COUNT);
		if(value == 0) {
			return;
		}
		
		if(value == 1) {
			/* Create a new entity */
			entity.create(scan);
		}
		else if(value == 2) {
			/* edit an existing entity */
			entity.edit(scan);
		}
		else if(value == 3) {
			/* delete an exiting entity */
			entity.delete(scan);
		}
		else if(value == 4) {
			/* Search and display an entity */
			entity.display(scan);
		}	
	}
	

}
