package databases_project;

import java.util.Scanner;

public class UsefulReports {
	
	private static int REPORT_OPTIONS_COUNT = 6;

	
	public static void usefulReportsPrompt(Scanner scan) {
		/* Method to continuously call the main menu prompt */
		while(true) {
			System.out.println("0.) Go Back");
			System.out.println("1.) Find the total number of equipment items rented by a member.");
			System.out.println("2.) Find the most populat item.");
			System.out.println("3.) Find the most popular manufacturer.");
			System.out.println("4.) Find the most popular drone.");
			System.out.println("5.) Find the member who has rented the most items.");
			System.out.println("6.) Find equipment name based on type.");
			System.out.print("Enter the number of your selection: ");
			int value = getUserInput(scan, REPORT_OPTIONS_COUNT-1);	
			
			switch(value) {
				case 1: 
					
					break;
				case 2: 
					

					break;
				case 3: 
					

					break;
				case 4: 
					
					break;
				case 5: 
					
					break;
				case 6: 
					
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
}

