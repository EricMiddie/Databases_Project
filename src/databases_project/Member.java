package databases_project;
import java.util.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import sql_package.SQL;

public class Member implements Manageable{
	public int MemberID;
	public String Email;
	public String First_Name;
	public String Last_Name;
	public String Address;
	public String Phone;
	public boolean Status;
	public int Warehouse_Distance;
	public String W_Address;
	public LocalDate Start_Date;
	
	/* This is set private so that it's ignored in the reflection */
	private static Map<String, String> fieldToDisplayNameMap = Map.of(
            "W_Address", "Warehouse Address",
            "Status", "Member Status"
            
    );
	
	public static String getFieldDisplayName(String input) {

		return fieldToDisplayNameMap.getOrDefault(input, input);
	}
	
	public static String getKeyFromDisplayName(String value) {
        for (Map.Entry<String, String> entry : fieldToDisplayNameMap.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return value;
	}
	

	
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
    	findMember(scanner);
    }
    
    private static void findMember(Scanner scan) {
    	System.out.println("Enter the member's ID:");
    	int requestedID = Integer.parseInt(scan.nextLine());
    	
    	SQL.ps_SearchMember(requestedID);
    }
    
    private static void deleteMember(Scanner scan) {
    	System.out.println("Enter the member's ID:");
    	int requestedID = Integer.parseInt(scan.nextLine());
    	
    	SQL.ps_RemoveMember(requestedID);
    	
    }
    
    public static String getFieldNamesAsString() {
        Field[] fields = Member.class.getFields();
        ArrayList<String> fieldNames = new ArrayList<String>();

        for (int i = 0; i < fields.length; i++) {
        	if(fields[i].getName() != "MemberID") {
        		fieldNames.add(getFieldDisplayName(fields[i].getName()));        		
        	}
        }

        return String.join(", ", fieldNames);
    }
    
    private static void editMemberDetails(Scanner scan) {
        System.out.println("Enter the member's ID:");
    	int requestedID = Integer.parseInt(scan.nextLine());
        
        Map<String, Object> changes = new HashMap<>();
        System.out.println("Fields to change: " + getFieldNamesAsString());

        while (true) {
        	System.out.println("Which attribute would you like to edit? (Type 'done' to finish editing)");
            String inputField = scan.nextLine().trim();
            if ("done".equalsIgnoreCase(inputField)) {
                break;
            }
            
            String key = getKeyFromDisplayName(inputField);
            
            if(key == "MemberID") {
            	System.out.println("Cannot change your ID");
            }
            else {
            	System.out.println("Enter the new value for " + inputField + ":");
            	String newValue = scan.nextLine();
            	changes.put(key, newValue);            	
            }

        }

        if (!changes.isEmpty()) {
            SQL.ps_EditMember(requestedID, changes);
        } else {
            System.out.println("No changes to update.");
        }
    }
	
	private static void fillMemberDetails(Scanner scan, Member member) {
		/* Method to populate a Member based on the name of the fields and their types */
        Field[] fields = Member.class.getFields();
        
        /* Loop through the fields, output the name of the field and get the user's input */
        for (Field field : fields) {
        	if(field.getName() == "id") {
        		continue;
        	}
            field.setAccessible(true);

            try {
                if (field.getType().equals(String.class)) 
                {
                    System.out.println("Enter your " + getFieldDisplayName(field.getName()).replace('_', ' ') + ": ");
                    String value = SQL.sanitizeInput(scan.nextLine());
                    field.set(member, value); 
                } 
                else if (field.getType().equals(int.class)) 
                {
                    System.out.println("Enter your " +  getFieldDisplayName(field.getName()).replace('_', ' ') + " (A Number): ");
                    int value = Integer.parseInt(scan.nextLine());
                    field.set(member, value);
                } 
                else if (field.getType().equals(boolean.class)) 
                {
                    System.out.println("Enter your " +  getFieldDisplayName(field.getName()).replace('_', ' ') + " (True or False): ");
                    boolean value = Boolean.parseBoolean(SQL.sanitizeInput(scan.nextLine()));
                    field.set(member, value);
                }
                else if (field.getType().equals(LocalDate.class)) 
                {
                    System.out.println("Enter your " +  getFieldDisplayName(field.getName()).replace('_', ' ') + " (yyyy-mm-dd format): ");
                    LocalDate value = LocalDate.parse(scan.nextLine());
                    field.set(member, value);
                }
            } catch (IllegalAccessException e) {
                System.out.println("Error setting value for " +  getFieldDisplayName(field.getName()).replace('_', ' '));
            } catch (NumberFormatException e) {
                System.out.println("Invalid format for " +  getFieldDisplayName(field.getName()).replace('_', ' '));
            } catch(DateTimeParseException e) {
            	System.out.println("Invalid format for " +  getFieldDisplayName(field.getName()).replace('_', ' '));
            }
        }
        
        SQL.ps_AddMember(member);
       
    }
}
