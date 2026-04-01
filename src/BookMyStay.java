import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * BookingService handles reservation confirmation and room allocation
 */
class BookingService {

    private RoomInventory inventory;
    private BookingQueue bookingQueue;

    // Track allocated room IDs (global uniqueness)
    private Set<String> allocatedRoomIds;

    // Map room type → allocated room IDs
    private HashMap<String, Set<String>> roomAllocations;

    public BookingService(RoomInventory inventory, BookingQueue bookingQueue) {
        this.inventory = inventory;
        this.bookingQueue = bookingQueue;

        allocatedRoomIds = new HashSet<>();
        roomAllocations = new HashMap<>();
    }

    /**
     * Process all booking requests in FIFO order
     */
    public void processBookings() {

        while (!bookingQueue.isEmpty()) {

            Reservation request = bookingQueue.getNextRequest();

            String roomType = request.getRoomType();
            String guest = request.getGuestName();

            int available = inventory.getAvailability(roomType);

            // Check availability
            if (available <= 0) {
                System.out.println("Booking failed for " + guest +
                        " → No rooms available (" + roomType + ")");
                continue;
            }

            // Generate unique room ID
            String roomId = generateUniqueRoomId(roomType);

            // Allocate room (atomic logic)
            allocatedRoomIds.add(roomId);

            roomAllocations
                    .computeIfAbsent(roomType, k -> new HashSet<>())
                    .add(roomId);

            // Update inventory immediately
            inventory.updateAvailability(roomType, available - 1);

            // Confirm booking
            System.out.println("Booking CONFIRMED for " + guest +
                    " → Room ID: " + roomId + " (" + roomType + ")");
        }
    }

    /**
     * Generate unique room ID
     */
    private String generateUniqueRoomId(String roomType) {
        String roomId;

        do {
            roomId = roomType.substring(0, 1).toUpperCase() +
                    UUID.randomUUID().toString().substring(0, 5);
        } while (allocatedRoomIds.contains(roomId));

        return roomId;
    }

    /**
     * Display all allocations
     */
    public void displayAllocations() {
        System.out.println("\nRoom Allocations:");

        for (String type : roomAllocations.keySet()) {
            System.out.println(type + " → " + roomAllocations.get(type));
        }
    }
}

public class BookMyStay {

    public static void main(String[] args) {

        // UC3: Inventory
        RoomInventory inventory = new RoomInventory();

        // UC4: Room data
        Map<String, Room> roomData = new HashMap<>();
        roomData.put("Single", new Room("Single", 2000, "WiFi"));
        roomData.put("Double", new Room("Double", 3500, "WiFi, TV"));
        roomData.put("Suite", new Room("Suite", 7000, "Luxury"));

        // UC5: Queue
        BookingQueue queue = new BookingQueue();

        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Suite"));
        queue.addRequest(new Reservation("David", "Suite"));
        queue.addRequest(new Reservation("Eve", "Suite")); // may fail

        // UC6: Booking
        BookingService bookingService = new BookingService(inventory, queue);

        bookingService.processBookings();

        bookingService.displayAllocations();

        // Final inventory
        System.out.println("\nFinal Inventory:");
        inventory.displayInventory();
    }
}