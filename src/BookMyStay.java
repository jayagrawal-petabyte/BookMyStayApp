import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * Maintains booking history (ordered)
 */
class BookingHistory {

    private List<BookingRecord> history;

    public BookingHistory() {
        history = new ArrayList<>();
    }

    // Add confirmed booking
    public void addRecord(BookingRecord record) {
        history.add(record);
    }

    // Get all records (read-only access)
    public List<BookingRecord> getAllRecords() {
        return new ArrayList<>(history); // defensive copy
    }

    // Display history
    public void displayHistory() {
        System.out.println("\nBooking History:");

        for (BookingRecord r : history) {
            System.out.println(r.getReservationId() + " | " +
                    r.getGuestName() + " | " +
                    r.getRoomType());
        }
    }
}

/**
 * Represents a confirmed booking record
 */
class BookingRecord {

    private String reservationId;
    private String guestName;
    private String roomType;

    public BookingRecord(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

class BookingReportService {

    public void generateSummary(List<BookingRecord> records) {

        Map<String, Integer> roomCount = new HashMap<>();

        for (BookingRecord r : records) {
            roomCount.put(r.getRoomType(),
                    roomCount.getOrDefault(r.getRoomType(), 0) + 1);
        }

        System.out.println("\nBooking Summary Report:");

        for (String type : roomCount.keySet()) {
            System.out.println(type + " → " + roomCount.get(type) + " bookings");
        }
    }}

    private BookingHistory bookingHistory;

public BookingService(RoomInventory inventory,
                      BookingQueue bookingQueue,
                      BookingHistory bookingHistory) {

    this.inventory = inventory;
    this.bookingQueue = bookingQueue;
    this.bookingHistory = bookingHistory;

    allocatedRoomIds = new HashSet<>();
    roomAllocations = new HashMap<>();
}

    // Add to history
    bookingHistory.addRecord(new BookingRecord(roomId,guest,roomType));

public class BookMyStay {

    public static void main(String[] args) {

        // Core components
        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        BookingHistory history = new BookingHistory();

        // Add requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Double"));
        queue.addRequest(new Reservation("Charlie", "Suite"));

        // Process bookings
        BookingService bookingService =
                new BookingService(inventory, queue, history);

        bookingService.processBookings();

        // Display history
        history.displayHistory();

        // Generate report
        BookingReportService reportService = new BookingReportService();
        reportService.generateSummary(history.getAllRecords());
    }
}