package databases_project;

import java.util.ArrayList;
import java.util.Scanner;

import sql_package.SQL;

public class UsefulReports {
	
	private static int REPORT_OPTIONS_COUNT = 6;

	
	public static void usefulReportsPrompt(Scanner scan) {
		/* Method for selecting the useful reports */
		try {
			
			while(true) {
				System.out.println("0.) Go Back");
				System.out.println("1.) Find the total number of equipment items rented by a member.");
				System.out.println("2.) Find the most popular items.");
				System.out.println("3.) Find the most popular manufacturers.");
				System.out.println("4.) Find the most popular drones.");
				System.out.println("5.) Find the members who have rented the most items.");
				System.out.println("6.) Find equipment name based on type.");
				System.out.print("Enter the number of your selection: ");
				int value = getUserInput(scan, REPORT_OPTIONS_COUNT);	
				
				String sql = "";
				switch(value) {
				case 1: 
					sql = "SELECT M.First_Name, M.Last_Name, COUNT(DISTINCT R.Pickup_Drone) AS num_of_drones\r\n"
							+ " FROM MEMBER M\r\n"
							+ " JOIN RENTAL R ON M.MemberID = R.MemberID\r\n"
							+ " JOIN RENTAL_EQUIPMENT RE ON R.RentalID = RE.RentalID\r\n"
							+ " GROUP BY M.MemberID\r\n"
							+ " LIMIT 1;";
					SQL.ps_ExecuteQuery(sql);
					break;
				case 2: 
					sql = "SELECT E.Serial_Number,\r\n"
							+ "       E.Description, \r\n"
							+ "       COUNT(RE.E_Serial_Number) AS Times_Rented, \r\n"
							+ "       SUM(julianday(R.Return_Date) - julianday(R.Rental_Start_Date)) AS Running_Rented_Time\r\n"
							+ " FROM EQUIPMENT E\r\n"
							+ " JOIN RENTAL_EQUIPMENT RE ON E.Serial_Number = RE.E_Serial_Number\r\n"
							+ " JOIN RENTAL R ON RE.RentalID = R.RentalID\r\n"
							+ " GROUP BY E.Serial_Number\r\n"
							+ " ORDER BY Running_Rented_Time DESC, Times_Rented DESC;";
					SQL.ps_ExecuteQuery(sql);
					break;
				case 3: 
					sql = "SELECT E.Manufacturer, COUNT(RE.E_Serial_Number) AS Total_Rented\r\n"
							+ " FROM EQUIPMENT E\r\n"
							+ " JOIN RENTAL_EQUIPMENT RE ON E.Serial_Number = RE.E_Serial_Number\r\n"
							+ " GROUP BY E.Manufacturer\r\n"
							+ " ORDER BY Total_Rented DESC\r\n"
							+ " LIMIT 1; ";
					SQL.ps_ExecuteQuery(sql);
					break;
				case 4: 
					sql = "SELECT D.Serial_Number, D.Name, COUNT(R.RentalID) AS Total_Deliveries, SUM(Warehouse_Distance) AS Total_Miles_Flown \r\n"
							+ " FROM RENTAL R \r\n"
							+ " JOIN DRONE D ON R.Delivery_Drone = D.Serial_Number \r\n"
							+ " JOIN MEMBER M ON R.MemberID = M.MemberID \r\n"
							+ " JOIN WAREHOUSE W ON D.W_Address = W.Address \r\n"
							+ " WHERE M.MemberID = R.MemberID \r\n"
							+ " GROUP BY D.Serial_Number\r\n"
							+ " ORDER BY Total_Deliveries DESC, Total_Miles_Flown DESC;";
					SQL.ps_ExecuteQuery(sql);
					break;
				case 5: 
					sql = "SELECT M.First_Name, M.Last_Name, COUNT(RE.E_Serial_Number) AS Rental_Count\r\n"
							+ " FROM MEMBER M\r\n"
							+ " JOIN RENTAL R ON M.MemberID = R.MemberID\r\n"
							+ " JOIN RENTAL_EQUIPMENT RE ON R.RentalID = RE.RentalID\r\n"
							+ " GROUP BY M.MemberID\r\n"
							+ " ORDER BY Rental_Count DESC\r\n"
							+ " LIMIT 1;";
					SQL.ps_ExecuteQuery(sql);
					break;
				case 6: 
					try {
						ArrayList<Object> userResponse = new ArrayList<Object>();
						System.out.println("Made before what year: ");
						userResponse.add(0, Integer.parseInt(scan.nextLine()));
						sql = "SELECT * FROM EQUIPMENT E WHERE E.Year_Manufactured < ? ORDER BY E.Equipment_Type";
						SQL.ps_ExecuteSearchQuery(sql, userResponse);					
					}
					catch(Exception ex) {
						System.out.println("An Error Occured");
					}
					break;
				default:
					return;
				}
			}
		}
		catch(Exception ex) {
			
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
}

