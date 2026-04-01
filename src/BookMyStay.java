import java.util.HashMap;
import java.util.Map;

/**
 * Manages centralized room inventory
 */
class RoomInventory {

    private Map<String, Integer> inventory;

    public RoomInventory() {
        inventory = new HashMap<>();

        // Initialize room types
        inventory.put("Single", 10);
        inventory.put("Double", 5);
        inventory.put("Suite", 2);
    }

    public int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    public void updateAvailability(String roomType, int count) {
        if (!inventory.containsKey(roomType)) {
            System.out.println("Room type does not exist.");
            return;
        }

        if (count < 0) {
            System.out.println("Invalid count.");
            return;
        }

        inventory.put(roomType, count);
    }

    public void addRoomType(String roomType, int count) {
        if (count < 0) {
            System.out.println("Invalid count.");
            return;
        }
        inventory.put(roomType, count);
    }

    public void displayInventory() {
        System.out.println("Current Room Inventory:");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " Rooms: " + entry.getValue());
        }
    }
}

/**
 * Main class for Book My Stay Application
 */
public class BookMyStay {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();

        // Initial state
        inventory.displayInventory();

        // Check availability
        System.out.println("\nAvailable Single Rooms: " +
                inventory.getAvailability("Single"));

        // Update availability
        inventory.updateAvailability("Single", 8);

        // Add new type
        inventory.addRoomType("Deluxe", 4);

        // Final state
        System.out.println("\nAfter Updates:");
        inventory.displayInventory();
    }
}