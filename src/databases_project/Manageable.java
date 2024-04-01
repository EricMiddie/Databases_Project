package databases_project;
import java.util.*;

public interface Manageable {
    void create(Scanner scanner);
    void edit(Scanner scanner);
    void delete(Scanner scanner);
    void display(Scanner scanner);
}
