//phia
public class Equipment {
    private String equipmentID;
    private String category;
    private String name;
    private String status; // AVAILABLE, RESERVED, BORROWED, UNDER MAINTENANCE
    private int totalQuantity;
    private int availableQuantity;

    public Equipment(String equipmentID, String category, String name, int totalQuantity) {
        this.equipmentID = equipmentID;
        this.category = category;
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.availableQuantity = totalQuantity;
        this.status = "AVAILABLE";
    }

    public String getEquipmentID() { return equipmentID; }
    public String getCategory() { return category; }
    public String getName() { return name; }
    public String getStatus() { return status; }
    public int getTotalQuantity() { return totalQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }

    public boolean checkAvailability(int requestedQty) {
        return availableQuantity >= requestedQty && status.equals("AVAILABLE");
    }

    public void updateStatus(String status) {
        this.status = status;
    }

    public void deductQuantity(int qty) {
        if (qty <= availableQuantity) {
            availableQuantity -= qty;
            if (availableQuantity == 0) {
                status = "BORROWED";
            }
        }
    }

    public void restoreQuantity(int qty) {
        availableQuantity += qty;
        if (availableQuantity > 0) {
            status = "AVAILABLE";
        }
    }

    public void displayDetails() {
        System.out.println("[" + equipmentID + "] " + name + " (" + category + ") | Status: " + status + 
                           " | Available: " + availableQuantity + "/" + totalQuantity);
    }
}
