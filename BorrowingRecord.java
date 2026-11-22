public class BorrowingRecord {  //in memmory java list (array)
    private String recordID;
    private User borrower;
    private Equipment equipment;
    private int borrowedQuantity;
    private String borrowDate;
    private String returnDate;
    private String itemCondition; 
    private double lateFee;

    public BorrowingRecord(String recordID, BorrowingRequest request, String borrowDate) {
        this.recordID = recordID;
        this.borrower = request.getBorrower();
        this.equipment = request.getEquipment();
        this.borrowedQuantity = request.getRequestedQuantity();
        this.borrowDate = borrowDate;
        this.returnDate = "Pending Return";
        this.itemCondition = "Good";
        this.lateFee = 0.0;
    }

    public String getRecordID() { return recordID; }
    public User getBorrower() { return borrower; }
    public Equipment getEquipment() { return equipment; }
    public int getBorrowedQuantity() { return borrowedQuantity; }
    public String getBorrowDate() { return borrowDate; }
    public String getReturnDate() { return returnDate; }
    public String getItemCondition() { return itemCondition; }
    public double getLateFee() { return lateFee; }

    public void recordReturn(String returnDate, String condition) {
        this.returnDate = returnDate;
        this.itemCondition = condition;
        equipment.restoreQuantity(borrowedQuantity);
        System.out.println("-> Return recorded for Record ID: " + recordID + ". Condition: " + condition);
    }

    public void checkLateReturn(boolean isLate, int daysLate) {
        if (isLate && daysLate > 0) {
            this.lateFee = daysLate * 50.0; // Php 50 per day simulated fine
            System.out.println("-> OVERDUE ALERT: Record " + recordID + " is late by " + daysLate + " day(s). Simulated Fine: Php " + this.lateFee);
        } else {
            this.lateFee = 0.0;
            System.out.println("-> Record " + recordID + " returned on time.");
        }
    }
}