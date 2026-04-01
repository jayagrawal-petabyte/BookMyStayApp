import java.util.LinkedList;
import java.util.Queue;

/**
 * BookingQueue handles incoming booking requests in FIFO order
 */
class BookingQueue {

    private Queue<Reservation> queue;

    public BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add request
    public void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.getGuestName() +
                " (" + reservation.getRoomType() + ")");
    }

    // View next request (without removing)
    public Reservation peekRequest() {
        return queue.peek();
    }

    // Remove next request (for future processing)
    public Reservation getNextRequest() {
        return queue.poll();
    }

    // Display all requests
    public void displayQueue() {
        System.out.println("\nBooking Request Queue:");

        for (Reservation r : queue) {
            System.out.println(r.getGuestName() + " → " + r.getRoomType());
        }
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

/**
 * Reservation represents a booking request
 */
class Reservation {

    private String guestName;
    private String roomType;

    public Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }
}

/**
 * Main Application - Book My Stay
 */
public class BookMyStay {

    public static void main(String[] args) {

        BookingQueue bookingQueue = new BookingQueue();

        // Simulating booking requests (arrival order matters)
        bookingQueue.addRequest(new Reservation("Alice", "Single"));
        bookingQueue.addRequest(new Reservation("Bob", "Double"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite"));

        // Display queue
        bookingQueue.displayQueue();

        // Peek first request (no removal)
        System.out.println("\nNext request to process: " +
                bookingQueue.peekRequest().getGuestName());
    }
}