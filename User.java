
public abstract class User {
    protected String userID;
    protected String name;
    protected String userType;
    protected String password;

    public User(String userID, String name, String userType, String password) {
        this.userID = userID;
        this.name = name;
        this.userType = userType;
        this.password = password;
    }

    public String getUserID() { return userID; }
    public String getName() { return name; }
    public String getUserType() { return userType; }

    public boolean checkPassword(String inputPassword) {
        return password != null && password.equals(inputPassword);
    }

    public void login() {
        System.out.println(name + " (" + userType + ") logged into Pahiram Po! successfully.");
    }

    public abstract void viewDashboard();
}

class Student extends User {
    public Student(String userID, String name, String password) {
        super(userID, name, "Student", password);
    }

    @Override
    public void viewDashboard() {
        System.out.println("Displaying Student Borrower Dashboard for: " + name);
    }
}

class StaffFaculty extends User {
    public StaffFaculty(String userID, String name, String password) {
        super(userID, name, "Staff/Faculty", password);
    }

    @Override
    public void viewDashboard() {
        System.out.println("Displaying Staff/Faculty Borrower Dashboard for: " + name);
    }
}

