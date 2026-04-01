import java.util.*;

public class BookMyStay {

    public static void main(String[] args) {

        RoomInventory inventory = new RoomInventory();
        BookingQueue queue = new BookingQueue();
        BookingHistory history = new BookingHistory();

        Set<String> validTypes = new HashSet<>();
        validTypes.add("Single");

        BookingValidator validator = new BookingValidator(validTypes);

        // 🔥 Only 1 room available → race condition test
        inventory.updateAvailability("Single", 1);

        // Multiple requests for SAME room
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Single"));
        queue.addRequest(new Reservation("Charlie", "Single"));

        BookingService service = new BookingService(inventory, queue, history, validator);

        // Create threads
        Thread t1 = new Thread(new BookingTask(service), "Thread-1");
        Thread t2 = new Thread(new BookingTask(service), "Thread-2");
        Thread t3 = new Thread(new BookingTask(service), "Thread-3");

        // Start threads
        t1.start();
        t2.start();
        t3.start();
    }

}

    public void processSingleRequest() {

        Reservation request;

        // synchronized queue access
        synchronized (bookingQueue) {
            if (bookingQueue.isEmpty())
                return;
            request = bookingQueue.getNextRequest();
        }

        try {
            validator.validate(request, inventory);

            String roomType = request.getRoomType();
            String guest = request.getGuestName();

            // 🔥 CRITICAL SECTION (inventory lock)
            synchronized (inventory) {

                int available = inventory.getAvailability(roomType);

                if (available <= 0) {
                    System.out.println(Thread.currentThread().getName() +
                            " → Booking failed for " + guest);
                    return;
                }

                String roomId = generateUniqueRoomId(roomType);

                allocatedRoomIds.add(roomId);

                roomAllocations
                        .computeIfAbsent(roomType, k -> new HashSet<>())
                        .add(roomId);

                inventory.updateAvailability(roomType, available - 1);

                bookingHistory.addRecord(
                        new BookingRecord(roomId, guest, roomType));

                System.out.println(Thread.currentThread().getName() +
                        " → CONFIRMED " + guest +
                        " | Room: " + roomId);
            }

        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

/**
 * Represents a booking task executed by a thread
 */
class BookingTask implements Runnable {

    private BookingService bookingService;

    public BookingTask(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    public void run() {
        bookingService.processSingleRequest();
    }
}

    synchronized(inventory)

    {
    // check + allocate + update
}