// AdminCustodian.java
public class AdminCustodian extends User {
    private String department;

    public AdminCustodian(String userID, String name, String department, String password) {
        super(userID, name, "Admin/Custodian", password);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }

    @Override
    public void viewDashboard() {
        System.out.println("Displaying Admin/Custodian Console (" + department + ") for: " + name);
    }
}