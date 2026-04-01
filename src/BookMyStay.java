import java.util.HashMap;
import java.util.Map;

class Room {

    private String type;
    private int price;
    private String amenities;

    public Room(String type, int price, String amenities) {
        this.type = type;
        this.price = price;
        this.amenities = amenities;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public String getAmenities() {
        return amenities;
    }
}

class SearchService {

    private RoomInventory inventory;
    private Map<String, Room> roomData;

    public SearchService(RoomInventory inventory, Map<String, Room> roomData) {
        this.inventory = inventory;
        this.roomData = roomData;
    }

    public void searchAvailableRooms() {

        System.out.println("\nAvailable Rooms:\n");

        for (String roomType : roomData.keySet()) {

            int available = inventory.getAvailability(roomType);

            // Defensive check: only show available rooms
            if (available > 0) {

                Room room = roomData.get(roomType);

                System.out.println("Type: " + room.getType());
                System.out.println("Price: ₹" + room.getPrice());
                System.out.println("Amenities: " + room.getAmenities());
                System.out.println("Available: " + available);
                System.out.println("------------------------");
            }
        }
    }
}

/**
 * Main Application - Book My Stay
 */
public class BookMyStay {

    public static void main(String[] args) {

        // Inventory (UC3)
        RoomInventory inventory = new RoomInventory();

        // Room details (Domain model)
        Map<String, Room> roomData = new HashMap<>();

        roomData.put("Single", new Room("Single", 2000, "WiFi, AC"));
        roomData.put("Double", new Room("Double", 3500, "WiFi, AC, TV"));
        roomData.put("Suite", new Room("Suite", 7000, "WiFi, AC, TV, Mini Bar"));

        // Search Service (UC4)
        SearchService searchService = new SearchService(inventory, roomData);

        // Perform search (READ ONLY)
        searchService.searchAvailableRooms();
    }
}