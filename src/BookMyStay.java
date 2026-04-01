import java.util.*;
import java.util.Set;

/**
 * Custom exception for invalid booking scenarios
 */
class InvalidBookingException extends Exception {

    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * Validates booking input and system state
 */
class BookingValidator {

    private Set<String> validRoomTypes;

    public BookingValidator(Set<String> validRoomTypes) {
        this.validRoomTypes = validRoomTypes;
    }

    /**
     * Validate booking request
     */
    public void validate(Reservation request, RoomInventory inventory)
            throws InvalidBookingException {

        // Null checks
        if (request == null) {
            throw new InvalidBookingException("Booking request is null.");
        }

        if (request.getGuestName() == null || request.getGuestName().isEmpty()) {
            throw new InvalidBookingException("Guest name is invalid.");
        }

        if (request.getRoomType() == null || request.getRoomType().isEmpty()) {
            throw new InvalidBookingException("Room type is invalid.");
        }

        // Validate room type
        if (!validRoomTypes.contains(request.getRoomType())) {
            throw new InvalidBookingException(
                    "Invalid room type: " + request.getRoomType());
        }

        // Validate availability
        int available = inventory.getAvailability(request.getRoomType());

        if (available < 0) {
            throw new InvalidBookingException(
                    "System error: Negative inventory detected.");
        }
    }}

    private BookingValidator validator;

public BookingService(RoomInventory inventory,
                      BookingQueue bookingQueue,
                      BookingHistory bookingHistory,
                      BookingValidator validator) {

    this.inventory = inventory;
    this.bookingQueue = bookingQueue;
    this.bookingHistory = bookingHistory;
    this.validator = validator;

    allocatedRoomIds = new HashSet<>();
    roomAllocations = new HashMap<>();
}

    public void processBookings() {

        while (!bookingQueue.isEmpty()) {

            Reservation request = bookingQueue.getNextRequest();

            try {
                // ✅ FAIL-FAST validation
                validator.validate(request, inventory);

                String roomType = request.getRoomType();
                String guest = request.getGuestName();

                int available = inventory.getAvailability(roomType);

                if (available <= 0) {
                    System.out.println("Booking failed for " + guest +
                            " → No rooms available (" + roomType + ")");
                    continue;
                }

                // Generate ID
                String roomId = generateUniqueRoomId(roomType);

                allocatedRoomIds.add(roomId);

                roomAllocations
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                // Update inventory
                inventory.updateAvailability(roomType, available - 1);

                // Add to history
                bookingHistory.addRecord(
                        new BookingRecord(roomId, guest, roomType));

                System.out.println("Booking CONFIRMED for " + guest +
                        " → Room ID: " + roomId + " (" + roomType + ")");

            } catch (InvalidBookingException e) {
                // ✅ Graceful failure
                System.out.println("Booking ERROR → " + e.getMessage());
            }
        }
    }

public class BookMyStay {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        BookingHistory history = new BookingHistory();

        // Valid room types
        Set<String> validRoomTypes = new HashSet<>();
        validRoomTypes.add("Single");
        validRoomTypes.add("Double");
        validRoomTypes.add("Suite");

        BookingValidator validator = new BookingValidator(validRoomTypes);

        // Add requests (including invalid cases)
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "InvalidType")); // ❌ invalid
        queue.addRequest(new Reservation("", "Double")); // ❌ invalid name
        queue.addRequest(new Reservation("Charlie", "Suite"));

        BookingService bookingService =
                new BookingService(inventory, queue, history, validator);

        bookingService.processBookings();

        history.displayHistory();
    }
}