import java.util.*;

/**
 * Manages add-on services for reservations
 */
class AddOnServiceManager {

    // reservationId → list of services
    private Map<String, List<AddOnService>> serviceMap;

    public AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    /**
     * Add service to a reservation
     */
    public void addService(String reservationId, AddOnService service) {

        serviceMap
                .computeIfAbsent(reservationId, k -> new ArrayList<>())
                .add(service);

        System.out.println("Added service: " + service.getName() +
                " to Reservation: " + reservationId);
    }

    /**
     * Calculate total cost of services
     */
    public int calculateTotalCost(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null)
            return 0;

        int total = 0;

        for (AddOnService s : services) {
            total += s.getPrice();
        }

        return total;
    }

    /**
     * Display services for a reservation
     */
    public void displayServices(String reservationId) {

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services added.");
            return;
        }

        System.out.println("\nServices for Reservation " + reservationId + ":");

        for (AddOnService s : services) {
            System.out.println("- " + s.getName() + " (₹" + s.getPrice() + ")");
        }

        System.out.println("Total Add-On Cost: ₹" +
                calculateTotalCost(reservationId));
    }
}

/**
 * Represents an optional add-on service
 */
class AddOnService {

    private String name;
    private int price;

    public AddOnService(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }}

    // Add inside BookingService class
    private Map<String, String> reservationToRoomMap = new HashMap<>();

    // After generating roomId
    reservationToRoomMap.put(roomId,guest);

public class BookMyStay {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();

        // Add booking requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Double"));

        // Process bookings
        BookingService bookingService = new BookingService(inventory, queue);
        bookingService.processBookings();

        // Assume we know generated IDs (for demo)
        String res1 = "S12345"; // example ID
        String res2 = "D67890";

        // Add-on services
        AddOnServiceManager manager = new AddOnServiceManager();

        manager.addService(res1, new AddOnService("Breakfast", 500));
        manager.addService(res1, new AddOnService("Airport Pickup", 1000));

        manager.addService(res2, new AddOnService("Extra Bed", 800));

        // Display
        manager.displayServices(res1);
        manager.displayServices(res2);
    }
}