import java.time.LocalDate;

enum EquipmentStatus {
    AVAILABLE, RESERVED, BORROWED, UNDER_MAINTENANCE
}

abstract class User {
    private String userID;
    private String name;
    private String userType;

    User(String userID, String name, String userType) {
        this.userID = userID;
        this.name = name;
        this.userType = userType;
    }

    boolean login(String id) {
        return userID.equals(id);
    }

    void viewProfile() {
        System.out.println("ID: " + userID + " | Name: " + name + " | Type: " + userType);
    }

    String getUserID() { return userID; }
    String getName() { return name; }
    String getUserType() { return userType; }
}

class Student extends User {
    Student(String userID, String name) {
        super(userID, name, "Student");
    }
}

class StaffFaculty extends User {
    StaffFaculty(String userID, String name) {
        super(userID, name, "Staff/Faculty");
    }
}

class Equipment {
    private String equipmentID;
    private String name;
    private String category;
    private EquipmentStatus status;

    Equipment(String equipmentID, String name, String category) {
        this.equipmentID = equipmentID;
        this.name = name;
        this.category = category;
        this.status = EquipmentStatus.AVAILABLE;
    }

    boolean checkAvailability() {
        return status == EquipmentStatus.AVAILABLE;
    }

    void updateStatus(EquipmentStatus newStatus) {
        this.status = newStatus;
    }

    String getEquipmentID() { return equipmentID; }
    String getName() { return name; }
    String getCategory() { return category; }
    EquipmentStatus getStatus() { return status; }

    @Override
    public String toString() {
        return equipmentID + " | " + name + " | " + category + " | " + status;
    }
}

class BorrowingRequest {
    private static int counter = 0;

    private int requestID;
    private String userID;
    private String equipmentID;
    private LocalDate date;
    private String status;

    BorrowingRequest(String userID, String equipmentID, LocalDate date) {
        this.requestID = ++counter;
        this.userID = userID;
        this.equipmentID = equipmentID;
        this.date = date;
        this.status = "NEW";
    }

    void submitRequest() { this.status = "PENDING"; }
    void approveRequest() { this.status = "APPROVED"; }
    void rejectRequest() { this.status = "REJECTED"; }

    String getStatus() { return status; }

    @Override
    public String toString() {
        return "Request #" + requestID + " | " + userID + " | " + equipmentID + " | " + date + " | " + status;
    }
}

class BorrowingRecord {
    private static int counter = 0;

    private int recordID;
    private String userID;
    private String equipmentID;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String condition;

    BorrowingRecord(String userID, String equipmentID, LocalDate borrowDate, LocalDate dueDate) {
        this.recordID = ++counter;
        this.userID = userID;
        this.equipmentID = equipmentID;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
    }

    void recordReturn(LocalDate returnDate, String condition) {
        this.returnDate = returnDate;
        this.condition = condition;
    }

    boolean checkLateReturn() {
        LocalDate checkDate = (returnDate != null) ? returnDate : LocalDate.now();
        return checkDate.isAfter(dueDate);
    }

    @Override
    public String toString() {
        return "Record #" + recordID + " | " + userID + " | " + equipmentID + " | borrowed " + borrowDate
                + " | due " + dueDate + " | returned " + (returnDate == null ? "not yet" : returnDate)
                + " | condition " + (condition == null ? "-" : condition);
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("=== PAHIRAM PO! (Week 3 - Basic Classes) ===\n");

        // Users
        Student student = new Student("25-00248", "Princess Joy Amador");
        StaffFaculty faculty = new StaffFaculty("FAC-001", "Prof. Santos");
        student.viewProfile();
        faculty.viewProfile();

        // Equipment
        Equipment projector = new Equipment("EQ-001", "Epson LCD Projector", "IT");
        Equipment camera = new Equipment("EQ-002", "Sony Camera", "AV");
        camera.updateStatus(EquipmentStatus.RESERVED);

        System.out.println("\n--- Equipment ---");
        System.out.println(projector + " -> available? " + projector.checkAvailability());
        System.out.println(camera + " -> available? " + camera.checkAvailability());

        // Borrowing request
        System.out.println("\n--- Borrowing Request ---");
        BorrowingRequest request = new BorrowingRequest(student.getUserID(), projector.getEquipmentID(),
                LocalDate.of(2026, 11, 20));
        request.submitRequest();
        System.out.println(request);
        request.approveRequest();
        System.out.println(request);

        // Borrowing record
        System.out.println("\n--- Borrowing Record ---");
        BorrowingRecord record = new BorrowingRecord(student.getUserID(), projector.getEquipmentID(),
                LocalDate.of(2026, 11, 20), LocalDate.of(2026, 11, 21));
        record.recordReturn(LocalDate.of(2026, 11, 23), "GOOD");
        System.out.println(record);
        System.out.println("Late return? " + record.checkLateReturn());
    }
} 