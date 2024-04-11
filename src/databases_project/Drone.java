package databases_project;
import java.util.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import sql_package.SQL;

public class Drone implements Manageable{
	public static String Serial_Number;
	public static String Model_Number;
	public static String Manufacturer;
	public static int Year_Manufactured;
	public static String Name;
	public static int Max_Speed;
	public static int Weight_Capacity;
	public static int Distance_Autonomy;
	public static String Location;
	public static String W_Address;
	public static int Order_Number;
	
	
	/* the main table this class manages */
	private static String mainTable = "DRONE";
	private static String PRIMARY_KEY = "Serial_Number";
	
	/* This is set private so that it's ignored in the reflection */
	private static Map<String, String> fieldToDisplayNameMap = Map.of(
            "W_Address", "Warehouse Address",
            "Status", "Availability Status"
            
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
        fillDroneDetails(scanner, this);
    }

    public void edit(Scanner scanner) {
    	editDroneDetails(scanner);
    }

    public void delete(Scanner scanner) {
        deleteDrone(scanner);
    }
    
    public void display(Scanner scanner) {
    	findDrone(scanner);
    }
    
    private static void findDrone(Scanner scan) {
    	System.out.println("Enter the Serial Number:");
    	String requestedSN = scan.nextLine();
    	
    	SQL.ps_SearchDrone(requestedSN);
    }
    
    private static void deleteDrone(Scanner scan) {
    	System.out.println("Enter the Serial Number:");
    	String droneSN = scan.nextLine();
    	
    	SQL.ps_RemoveDrone(droneSN);
    	
    }
    
    public static String getFieldNamesAsString() {
        Field[] fields = Member.class.getFields();
        ArrayList<String> fieldNames = new ArrayList<String>();

        for (int i = 0; i < fields.length; i++) {
        	if(fields[i].getName() != PRIMARY_KEY) {
        		fieldNames.add(getFieldDisplayName(fields[i].getName()));        		
        	}
        }

        return String.join(", ", fieldNames);
    }
    
    private static void editDroneDetails(Scanner scan) {
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
            
            if(key == "Serial_Number") {
            	System.out.println("Cannot change the Serial Number");
            }
            else {
            	System.out.println("Enter the new value for " + inputField + ":");
            	String newValue = scan.nextLine();
            	changes.put(key, newValue);            	
            }

        }

        if (!changes.isEmpty()) {
            SQL.ps_EditManageable(requestedID, changes, PRIMARY_KEY);
        } else {
            System.out.println("No changes to update.");
        }
    }
	
	private static void fillDroneDetails(Scanner scan, Drone d) {
		/* Method to populate a Drone based on the name of the fields and their types */
        Field[] fields = Drone.class.getFields();
        
        /* Loop through the fields, output the name of the field and get the user's input */
        for (Field field : fields) {
            field.setAccessible(true);

            try {
                if (field.getType().equals(String.class)) 
                {
                    System.out.println("Enter your " + getFieldDisplayName(field.getName()).replace('_', ' ') + ": ");
                    String value = SQL.sanitizeInput(scan.nextLine());
                    field.set(d, value); 
                } 
                else if (field.getType().equals(int.class)) 
                {
                    System.out.println("Enter your " +  getFieldDisplayName(field.getName()).replace('_', ' ') + " (A Number): ");
                    int value = Integer.parseInt(scan.nextLine());
                    field.set(d, value);
                } 
                else if (field.getType().equals(boolean.class)) 
                {
                    System.out.println("Enter your " +  getFieldDisplayName(field.getName()).replace('_', ' ') + " (True or False): ");
                    boolean value = Boolean.parseBoolean(SQL.sanitizeInput(scan.nextLine()));
                    field.set(d, value);
                }
                else if (field.getType().equals(LocalDate.class)) 
                {
                    System.out.println("Enter your " +  getFieldDisplayName(field.getName()).replace('_', ' ') + " (yyyy-mm-dd format): ");
                    LocalDate value = LocalDate.parse(scan.nextLine());
                    field.set(d, value);
                }
            } catch (IllegalAccessException e) {
                System.out.println("Error setting value for " +  getFieldDisplayName(field.getName()).replace('_', ' '));
            } catch (NumberFormatException e) {
                System.out.println("Invalid format for " +  getFieldDisplayName(field.getName()).replace('_', ' '));
            } catch(DateTimeParseException e) {
            	System.out.println("Invalid format for " +  getFieldDisplayName(field.getName()).replace('_', ' '));
            }
        }
        
        SQL.ps_AddManageable(d, mainTable);
       
    }
}
