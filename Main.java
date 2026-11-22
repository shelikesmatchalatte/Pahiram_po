// Main.java
public class Main {
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  PAHIRAM PO! - Integrated University Resource    ");
        System.out.println("  Borrowing and Real-Time Inventory Platform      ");
        System.out.println("==================================================\n");

        // 1. Create a Student user 
        User student = new Student("25-00248", "Princess Joy Amador");
        student.login();
        student.viewDashboard();
        System.out.println();

        // 2. Initialize Equipment Catalog with Quantities
        Equipment projector = new Equipment("EQ-001", "IT Dept", "Epson LCD Projector", 3);
        
        System.out.println("--- Initial Equipment Catalog ---");
        projector.displayDetails();
        System.out.println();

        // 3. User Submits a Borrowing Request for 2 units
        System.out.println("--- Submitting Borrowing Request ---");
        BorrowingRequest request = new BorrowingRequest("REQ-001", student, projector, 2, "2026-11-18");
        request.submitRequest();
        System.out.println();

        // 4. Custodian Approves Request
        System.out.println("--- Processing Approval ---");
        if (request.approveRequest()) {
            // 5. Generate Borrowing Record
            BorrowingRecord record = new BorrowingRecord("REC-001", request, "2026-11-18");

            System.out.println("\n--- Updated Equipment Catalog (After Approval) ---");
            projector.displayDetails();
            System.out.println();

            // 6. Simulate Return and Late Penalty Check
            System.out.println("--- Processing Return & Penalties ---");
            record.checkLateReturn(true, 2); // 2 days late simulation
            record.recordReturn("2026-11-22", "Good");
            System.out.println();

            System.out.println("--- Final Equipment Catalog (After Item Return) ---");
            projector.displayDetails();
        }
    }
}