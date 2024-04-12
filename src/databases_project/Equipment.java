package databases_project;
import java.util.*;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import sql_package.SQL;

public class Equipment implements Manageable{
	public String Serial_Number;
	public int Model_Number;
	public String Equipment_Type;
	public String Description;
	public String Manufacturer;
	public int Year_Manufactured;
	public int Weight;
	public int Height;
	public int Width;
	public int Length;
	public LocalDate Warranty_Expiration_Date;
	
	public String W_Address;
	public int Order_Number;
	
	public boolean Status;
	public String Location;
	
	
	/* the main table this class manages */
	private static String mainTable = "EQUIPMENT";
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
        fillEquipmentDetails(scanner, this);
    }

    public void edit(Scanner scanner) {
    	editEquipmentDetails(scanner);
    }

    public void delete(Scanner scanner) {
        deleteEquipment(scanner);
    }
    
    public void display(Scanner scanner) {
    	findEquipment(scanner);
    }
    
    private static void findEquipment(Scanner scan) {
    	System.out.println("Enter the Serial Number:");
    	String requestedSN = scan.nextLine();
    	
    	SQL.ps_SearchEquipment(requestedSN);
    }
    
    private static void deleteEquipment(Scanner scan) {
    	System.out.println("Enter the Serial Number:");
    	String equipmentSN = scan.nextLine();
    	
    	SQL.ps_RemoveEquipment(equipmentSN);
    	
    }
    
    public static String getFieldNamesAsString() {
        Field[] fields = Equipment.class.getFields();
        ArrayList<String> fieldNames = new ArrayList<String>();

        for (int i = 0; i < fields.length; i++) {
        	if(fields[i].getName() != "Serial_Number") {
        		fieldNames.add(getFieldDisplayName(fields[i].getName()));        		
        	}
        }

        return String.join(", ", fieldNames);
    }
    
    private static void editEquipmentDetails(Scanner scan) {
        System.out.println("Enter the Serial Number:");
    	String requestedSN = scan.nextLine();
        
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
            SQL.ps_EditManageable(requestedSN, changes, PRIMARY_KEY, mainTable);
        } else {
            System.out.println("No changes to update.");
        }
    }
	
	private static void fillEquipmentDetails(Scanner scan, Equipment eq) {
		/* Method to populate a Equipment based on the name of the fields and their types */
        Field[] fields = Equipment.class.getFields();
        
        /* Loop through the fields, output the name of the field and get the user's input */
        for (Field field : fields) {
            field.setAccessible(true);

            try {
                if (field.getType().equals(String.class)) 
                {
                    System.out.println("Enter your " + getFieldDisplayName(field.getName()).replace('_', ' ') + ": ");
                    String value = SQL.sanitizeInput(scan.nextLine());
                    field.set(eq, value); 
                } 
                else if (field.getType().equals(int.class)) 
                {
                    System.out.println("Enter your " +  getFieldDisplayName(field.getName()).replace('_', ' ') + " (A Number): ");
                    int value = Integer.parseInt(scan.nextLine());
                    field.set(eq, value);
                } 
                else if (field.getType().equals(boolean.class)) 
                {
                    System.out.println("Enter your " +  getFieldDisplayName(field.getName()).replace('_', ' ') + " (1 [True] or 0 [False]): ");
                    boolean value = Boolean.parseBoolean(SQL.sanitizeInput(scan.nextLine()));
                    field.set(eq, value);
                }
                else if (field.getType().equals(LocalDate.class)) 
                {
                    System.out.println("Enter your " +  getFieldDisplayName(field.getName()).replace('_', ' ') + " (yyyy-mm-dd format): ");
                    LocalDate value = LocalDate.parse(scan.nextLine());
                    field.set(eq, value);
                }
            } catch (IllegalAccessException e) {
                System.out.println("Error setting value for " +  getFieldDisplayName(field.getName()).replace('_', ' '));
            } catch (NumberFormatException e) {
                System.out.println("Invalid format for " +  getFieldDisplayName(field.getName()).replace('_', ' '));
            } catch(DateTimeParseException e) {
            	System.out.println("Invalid format for " +  getFieldDisplayName(field.getName()).replace('_', ' '));
            }
        }
        
        SQL.ps_AddManageable(eq, mainTable);
       
    }
}
