package databases_project;

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
				System.out.println("2.) Find the most popular item.");
				System.out.println("3.) Find the most popular manufacturer.");
				System.out.println("4.) Find the most popular drone.");
				System.out.println("5.) Find the member who has rented the most items.");
				System.out.println("6.) Find equipment name based on type.");
				System.out.print("Enter the number of your selection: ");
				int value = getUserInput(scan, REPORT_OPTIONS_COUNT-1);	
				
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
					sql = "SELECT e.Description, \r\n"
							+ " COUNT(re.E_Serial_Number) AS Times_Rented, \r\n"
							+ " SUM(julianday(r.Return_Date) - julianday(r.Rental_Start_Date)) AS Running_Rented_Time\r\n"
							+ " FROM EQUIPMENT e\r\n"
							+ " JOIN RENTAL_EQUIPMENT re ON e.Serial_Number = re.E_Serial_Number\r\n"
							+ " JOIN RENTAL r ON re.RentalID = r.RentalID\r\n"
							+ " GROUP BY e.Serial_Number\r\n"
							+ " ORDER BY Running_Rented_Time DESC, Times_Rented DESC;";
					SQL.ps_ExecuteQuery(sql);
					break;
				case 3: 
					sql = "SELECT e.Manufacturer, COUNT(re.E_Serial_Number) AS Total_Rented\r\n"
							+ " FROM EQUIPMENT e\r\n"
							+ " JOIN RENTAL_EQUIPMENT re ON e.Serial_Number = re.E_Serial_Number\r\n"
							+ " GROUP BY e.Manufacturer\r\n"
							+ " ORDER BY Total_Rented DESC\r\n"
							+ " LIMIT 1; ";
					SQL.ps_ExecuteQuery(sql);
					break;
				case 4: 
					System.out.println("Not implemented yet");
					sql = "";
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
					sql = "SELECT D.Manufacturer, D.Name, D.Serial_Number, D.Year_Manufactured\r\n"
							+ " FROM DRONE D\r\n"
							+ " WHERE D.Year_Manufactured > 2022\r\n"
							+ " ORDER BY D.Manufacturer;";
					SQL.ps_ExecuteQuery(sql);
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

