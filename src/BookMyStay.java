import java.util.*;

/**
 * Handles booking cancellation and rollback
 */
class CancellationService {

    private RoomInventory inventory;
    private BookingHistory bookingHistory;

    // roomType → allocated IDs (same as BookingService)
    private Map<String, Set<String>> roomAllocations;

    // Track released IDs (LIFO rollback)
    private Stack<String> rollbackStack;

    public CancellationService(RoomInventory inventory,
            BookingHistory bookingHistory,
            Map<String, Set<String>> roomAllocations) {

        this.inventory = inventory;
        this.bookingHistory = bookingHistory;
        this.roomAllocations = roomAllocations;

        rollbackStack = new Stack<>();
    }

    /**
     * Cancel a booking
     */
    public void cancelBooking(String reservationId) {

        // Step 1: Validate existence
        BookingRecord record = findBooking(reservationId);

        if (record == null) {
            System.out.println("Cancellation ERROR → Reservation not found.");
            return;
        }

        String roomType = record.getRoomType();

        // Step 2: Validate allocation exists
        if (!roomAllocations.containsKey(roomType) ||
                !roomAllocations.get(roomType).contains(reservationId)) {

            System.out.println("Cancellation ERROR → Already cancelled or invalid.");
            return;
        }

        // Step 3: Rollback (controlled order)

        // Remove from allocation
        roomAllocations.get(roomType).remove(reservationId);

        // Track rollback
        rollbackStack.push(reservationId);

        // Restore inventory
        int current = inventory.getAvailability(roomType);
        inventory.updateAvailability(roomType, current + 1);

        // Update history (mark cancellation)
        markCancelled(reservationId);

        System.out.println("Booking CANCELLED → " + reservationId +
                " (" + roomType + ")");
    }

    /**
     * Find booking record
     */
    private BookingRecord findBooking(String reservationId) {

        for (BookingRecord r : bookingHistory.getAllRecords()) {
            if (r.getReservationId().equals(reservationId)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Mark booking as cancelled (simple approach)
     */
    private void markCancelled(String reservationId) {

        System.out.println("Marked in history as cancelled: " + reservationId);
    }

    /**
     * Show rollback stack
     */
    public void displayRollbackStack() {

        System.out.println("\nRollback Stack (recent cancellations):");

        for (String id : rollbackStack) {
            System.out.println(id);
        }
    }

}

    public Map<String, Set<String>> getRoomAllocations() {
        return roomAllocations;
    }

public Map<String, Set<String>> getRoomAllocations() {
    return roomAllocations;
}