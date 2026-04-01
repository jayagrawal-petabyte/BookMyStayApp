import java.io.*;
import java.util.*;

class BookingRecord implements java.io.Serializable
class RoomInventory implements java.io.Serializable
class BookingHistory implements java.io.Serializable



/**
 * Handles saving and loading system state
 */
class PersistenceService {

    private static final String FILE_NAME = "system_state.dat";

    /**
     * Save system state
     */
    public void save(RoomInventory inventory, BookingHistory history) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            out.writeObject(inventory);
            out.writeObject(history);

            System.out.println("✅ System state SAVED");

        } catch (IOException e) {
            System.out.println("❌ Save failed: " + e.getMessage());
        }
    }

    /**
     * Load system state
     */
    public Object[] load() {

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            RoomInventory inventory = (RoomInventory) in.readObject();
            BookingHistory history = (BookingHistory) in.readObject();

            System.out.println("✅ System state LOADED");

            return new Object[]{inventory, history};

        } catch (FileNotFoundException e) {
            System.out.println("⚠ No previous data found. Starting fresh.");
        } catch (Exception e) {
            System.out.println("❌ Load failed: " + e.getMessage());
        }

        return null;
    }
}

public class BookMyStay {

    public static void main(String[] args) {

        PersistenceService persistence = new PersistenceService();

        RoomInventory inventory;
        BookingHistory history;

        // 🔥 LOAD STATE
        Object[] data = persistence.load();

        if (data != null) {
            inventory = (RoomInventory) data[0];
            history = (BookingHistory) data[1];
        } else {
            inventory = new RoomInventory();
            history = new BookingHistory();
        }

        BookingQueue queue = new BookingQueue();

        Set<String> validTypes = new HashSet<>();
        validTypes.add("Single");
        validTypes.add("Double");
        validTypes.add("Suite");

        BookingValidator validator = new BookingValidator(validTypes);

        // Add new requests
        queue.addRequest(new Reservation("Alice", "Single"));
        queue.addRequest(new Reservation("Bob", "Suite"));

        BookingService service =
                new BookingService(inventory, queue, history, validator);

        service.processBookings();

        history.displayHistory();

        // 🔥 SAVE STATE BEFORE EXIT
        persistence.save(inventory, history);
    }
}