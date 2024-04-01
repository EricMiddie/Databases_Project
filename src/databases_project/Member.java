package databases_project;
import java.util.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Member implements Manageable{
	public String email;
	public String first_name;
	public String last_name;
	public String address;
	public String phone_number;
	public LocalDate start_date;
	
    public void create(Scanner scanner) {
        fillMemberDetails(scanner, this);
        Application.members.add(this);
    }

    public void edit(Scanner scanner) {
    	editMemberDetails(scanner);
    }

    public void delete(Scanner scanner) {
        deleteMember(scanner);
    }
    
    public void display(Scanner scanner) {
    	getMemberByEmail(scanner);
    }
    
    private static void deleteMember(Scanner scan) {
    	Member targetMember = getMemberByEmail(scan);
    	if(targetMember == null) return;
    	
    	for(int i = Application.members.size()-1; i>=0; i--) {
    		if(Application.members.get(i).email.equals(targetMember.email)) {
    			Application.members.remove(i);
    		}
    	}
    	
    	System.out.println("Removed member with email " + targetMember.email);
    }
    
    private static void editMemberDetails(Scanner scan) {
    	
    	Member targetMember = getMemberByEmail(scan);
    	if(targetMember == null) return;
    	
    	System.out.println("Which attribute would you like to edit? (Type 'done' to finish editing)");
        Field[] fields = Member.class.getDeclaredFields();
        /* Loop through the Member fields until "done" is typed */
        while (true) {
            String inputField = scan.nextLine();
            if ("done".equalsIgnoreCase(inputField)) {
            	/* check if the user entered done */
                break;
            }

            Field fieldToEdit = null;
            /* Loop through and check that what they entered matched a field */
            for (Field field : fields) {
                if (field.getName().replace('_', ' ').equalsIgnoreCase(inputField)) {
                    fieldToEdit = field;
                    break;
                }
            }

            if (fieldToEdit != null) {
            	/* A valid field was entered, so update that value with what the user enters */
                System.out.println("Enter the new value for " + fieldToEdit.getName().replace('_', ' ') + ":");
                String newValue = scan.nextLine();
                try {
                    fieldToEdit.setAccessible(true);
                    if (fieldToEdit.getType().equals(String.class)) 
                    {
                        fieldToEdit.set(targetMember, newValue);
                    } 
                    else if (fieldToEdit.getType().equals(int.class)) 
                    {
                        fieldToEdit.set(targetMember, Integer.parseInt(newValue));
                    } 
                    else if (fieldToEdit.getType().equals(boolean.class)) 
                    {
                        fieldToEdit.set(targetMember, Boolean.parseBoolean(newValue));
                    }
                    else if (fieldToEdit.getType().equals(LocalDate.class)) 
                    {
                        LocalDate value = LocalDate.parse(scan.nextLine());
                        fieldToEdit.set(targetMember, value);
                    }
                    /* Update the user that the value was accepted */
                    System.out.println("Updated " + fieldToEdit.getName().replace('_', ' ') + " to " + newValue);
                	System.out.println("Enter the next attribute or 'done' to finish editing:");
                } catch (IllegalAccessException | IllegalArgumentException e) {
                    System.out.println("Could not update the field. Please ensure the value is correct.");
                }
            } else {
                System.out.println("Field not recognized. Please enter a valid field name.");
            }
        }
    	
    }
	
	private static void fillMemberDetails(Scanner scan, Member member) {
		/* Method to populate a Member based on the name of the fields and their types */
        Field[] fields = Member.class.getDeclaredFields();
        
        /* Loop through the fields, output the name of the field and get the user's input */
        for (Field field : fields) {
        	if(field.getName() == "id") {
        		continue;
        	}
            field.setAccessible(true);

            try {
                if (field.getType().equals(String.class)) 
                {
                    System.out.println("Enter your " + field.getName().replace('_', ' ') + ": ");
                    String value = scan.nextLine();
                    field.set(member, value); 
                } 
                else if (field.getType().equals(int.class)) 
                {
                    System.out.println("Enter your " + field.getName().replace('_', ' ') + " (A Number): ");
                    int value = Integer.parseInt(scan.nextLine());
                    field.set(member, value);
                } 
                else if (field.getType().equals(boolean.class)) 
                {
                    System.out.println("Enter your " + field.getName().replace('_', ' ') + ": ");
                    boolean value = Boolean.parseBoolean(scan.nextLine());
                    field.set(member, value);
                }
                else if (field.getType().equals(LocalDate.class)) 
                {
                    System.out.println("Enter your " + field.getName().replace('_', ' ') + " (yyyy-mm-dd format): ");
                    LocalDate value = LocalDate.parse(scan.nextLine());
                    field.set(member, value);
                }
            } catch (IllegalAccessException e) {
                System.out.println("Error setting value for " + field.getName().replace('_', ' '));
            } catch (NumberFormatException e) {
                System.out.println("Invalid format for " + field.getName().replace('_', ' '));
            } catch(DateTimeParseException e) {
            	System.out.println("Invalid format for " + field.getName().replace('_', ' '));
            }
        }
       
    }
	
	private static Member getMemberByEmail(Scanner scan) {
		/* Method for prompting and finding an existing Member by their email */
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
    		return null;
    	}
    	
    	/* Print out the found member's details so that this can be used in many functions that already 
    	 * have to do this
    	 */
        System.out.println("Current member details:");
        Field[] fields = Member.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                System.out.println(field.getName().replace('_', ' ') + ": " + field.get(targetMember));
            } catch (IllegalAccessException e) {
                System.out.println("Could not access the value of " + field.getName());
            }
        }
        
        return targetMember;
	}
}
