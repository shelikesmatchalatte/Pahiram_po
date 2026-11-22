// BorrowingRequest.java
public class BorrowingRequest {
    private String requestID;
    private User borrower;       // Connection to User
    private Equipment equipment; // Connection to Equipment
    private int requestedQuantity;
    private String date;
    private String status;       // PENDING, APPROVED, REJECTED

    public BorrowingRequest(String requestID, User borrower, Equipment equipment, int requestedQuantity, String date) {
        this.requestID = requestID;
        this.borrower = borrower;
        this.equipment = equipment;
        this.requestedQuantity = requestedQuantity;
        this.date = date;
        this.status = "PENDING";
    }

    public String getRequestID() { return requestID; }
    public User getBorrower() { return borrower; }
    public Equipment getEquipment() { return equipment; }
    public int getRequestedQuantity() { return requestedQuantity; }
    public String getDate() { return date; }
    public String getStatus() { return status; }

    public void submitRequest() {
        System.out.println("Request " + requestID + " submitted by " + borrower.getName() + 
                           " for " + requestedQuantity + " unit(s) of " + equipment.getName() + ".");
    }

    public boolean approveRequest() {
        if (equipment.checkAvailability(requestedQuantity)) {
            status = "APPROVED";
            equipment.deductQuantity(requestedQuantity);
            System.out.println("-> Request " + requestID + " has been APPROVED.");
            return true;
        } else {
            status = "REJECTED";
            System.out.println("-> Request " + requestID + " REJECTED: Insufficient quantity available.");
            return false;
        }
    }

    public void rejectRequest() {
        status = "REJECTED";
        System.out.println("-> Request " + requestID + " has been REJECTED.");
    }
}