package databases_project;
import java.util.*;
import java.time.*;

public class RentalManagement {
	
	public static void RentEquipment(Scanner scan) {
		System.out.println("Enter the email of the target member:");
    	String requestedEmail = scan.nextLine();
    	Member targetMember = null;
    	for (Member m : Application.members) {
    		if(m.email.equals(requestedEmail)) {
    			targetMember = m;
    		}
    	}
    	if(targetMember == null) {
    		System.out.println("The requested member was not found");
    		return;
    	}
    	try {
    		System.out.println("Enter the model number you'd like to rent: ");
    		String modelNumber = scan.nextLine();
    		
    		System.out.println("Enter the rental Start date (yyyy-mm-dd format): ");
    		LocalDate startDate = LocalDate.parse(scan.nextLine());
    		
    		System.out.println("Enter the rental end date (yyyy-mm-dd format): ");
    		LocalDate endDate = LocalDate.parse(scan.nextLine());
    		
    		/* Create the rental and assign the ID here */
    		
    		System.out.println("Your rental is processing, an ID will be provided shortly.");
    		
    	}catch(Exception ex) {
    		System.out.println("Invalid Input");
    	}
	}
	
	public static void ReturnRental(Scanner scan) {
		System.out.println("Enter the email of the target member:");
    	String requestedEmail = scan.nextLine();
    	Member targetMember = null;
    	for (Member m : Application.members) {
    		if(m.email.equals(requestedEmail)) {
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
    		
    		/* Process the rental return dates here */
    		
    		System.out.println("Your return is processing, we look forward to seeing you");
    		
    	}catch(Exception ex) {
    		System.out.println("Invalid Input");
    	}
	}

	public static void ScheduleRentalDelivery(Scanner scan) {
		System.out.println("Enter the email of the target member:");
    	String requestedEmail = scan.nextLine();
    	Member targetMember = null;
    	for (Member m : Application.members) {
    		if(m.email.equals(requestedEmail)) {
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
    		
    		System.out.println("Enter your preferred delivery date (yyyy-mm-dd format): ");
    		LocalDate deliveryDate = LocalDate.parse(scan.nextLine());
    		
    		/* Assign the drone to deliver the equipment here */
    		
    		System.out.println("Thank you for your rental, we will assign a delivery drone shortly.");
    		
    	}catch(Exception ex) {
    		System.out.println("Invalid Input");
    	}
	}
	
	public static void ScheduleRentalPickup(Scanner scan) {
		System.out.println("Enter the email of the target member:");
    	String requestedEmail = scan.nextLine();
    	Member targetMember = null;
    	for (Member m : Application.members) {
    		if(m.email.equals(requestedEmail)) {
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
