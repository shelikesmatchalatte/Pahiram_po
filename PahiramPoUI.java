import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class PahiramPoUI extends JFrame {
    // --- Palette Extracted from "Pahiram po!" Banner ---
    private static final Color SKY_BLUE_BG       = new Color(165, 224, 245); // Bright sky blue from banner background
    private static final Color SKY_BLUE_ACCENT   = new Color(56, 163, 219);  // Crisp sky/water blue for primary buttons
    private static final Color SKY_BLUE_DEEP     = new Color(24, 114, 168);  // Deeper sky blue for hover/secondary accents
    private static final Color GRASS_GREEN_BG    = new Color(92, 179, 56);   // Vibrant lawn/leaf green
    private static final Color FOREST_GREEN_DARK = new Color(34, 92, 28);    // Deep green outline/header & dark text
    private static final Color MINT_CARD_BG      = new Color(236, 248, 224); // Soft fresh green-tinted card surface
    private static final Color SUNNY_YELLOW      = new Color(255, 226, 71);  // Bright "Pahiram po!" logo yellow
    private static final Color CREAM_RIBBON_BG   = new Color(254, 249, 206); // Warm cream ribbon/panel tint
    private static final Color LIME_BANNER_ACC   = new Color(185, 214, 58);  // Yellow-green ribbon/wave accent

    private final List<User> users = new ArrayList<>();
    private final List<Equipment> inventory = new ArrayList<>();
    private final List<BorrowingRequest> requests = new ArrayList<>();
    private final List<BorrowingRecord> records = new ArrayList<>();

    private User currentUser;
    private int reqCounter = 1;
    private int recCounter = 1;

    // "LOGIN" and "MAIN"
    private final CardLayout rootLayout = new CardLayout();
    private final JPanel rootPanel = new JPanel(rootLayout);

    // Login components
    private JTextField borrowerIdField;
    private JPasswordField borrowerPassField;
    private JComboBox<String> borrowerRoleFilter;
    private JLabel borrowerLoginStatus;

    // Login components - Admin / Custodian
    private JTextField adminIdField;
    private JPasswordField adminPassField;
    private JLabel adminLoginStatus;

    // Main UI
    private JLabel headerUserLabel;
    private JComboBox<Equipment> equipmentCombo;
    private JSpinner qtySpinner;
    private JTextField dateField;

    private JPanel borrowerRequestPanel;
    private JPanel adminRequestActions;
    private JPanel adminRecordActions;

    private DefaultTableModel inventoryModel;
    private DefaultTableModel requestModel;
    private DefaultTableModel recordModel;

    private JTable inventoryTable;
    private JTable requestTable;
    private JTable recordTable;

    public PahiramPoUI() {
        super("Pahiram po! - University Resource Borrowing & Inventory");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1120, 720);
        setLocationRelativeTo(null);

        seedData();
        buildRootUI();
        add(rootPanel);

        // Always start at the Login screen
        rootLayout.show(rootPanel, "LOGIN");
    }

    private void seedData() {
        users.add(new Student("25-00248", "Princess Joy Amador", "1234"));
        users.add(new Student("25-06905", "John Carl Omega", "1234"));
        users.add(new Student("25-01859", "Phia Taganna", "1234"));
        users.add(new StaffFaculty("FAC-101", "Ms. Fatima Marie Agdon", "1234"));
        users.add(new AdminCustodian("ADMIN-01", "IT Custodian Admin", "IT Dept", "1234"));
        users.add(new AdminCustodian("ADMIN-02", "Property Office Admin", "Property Office", "1234"));

        inventory.add(new Equipment("EQ-001", "IT Dept", "Epson LCD Projector", 3));
        inventory.add(new Equipment("EQ-002", "AV Dept", "Sony DSLR Camera", 2));
        inventory.add(new Equipment("EQ-003", "IT Dept", "Laptop Unit", 5));
        inventory.add(new Equipment("EQ-004", "Music/Org", "Portable Sound System", 2));
    }

    private void buildRootUI() {
        rootPanel.add(buildLoginScreen(), "LOGIN");
        rootPanel.add(buildMainScreen(), "MAIN");
    }

    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(fgColor);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FOREST_GREEN_DARK, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));
        return btn;
    }

    private void styleTable(JTable table) {
        table.setBackground(Color.WHITE);
        table.setForeground(FOREST_GREEN_DARK);
        table.setGridColor(GRASS_GREEN_BG);
        table.setRowHeight(24);
        table.setSelectionBackground(SUNNY_YELLOW);
        table.setSelectionForeground(FOREST_GREEN_DARK);
        table.getTableHeader().setBackground(GRASS_GREEN_BG);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
    }

    // LOGIN SCREEN
    private JPanel buildLoginScreen() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setBackground(SKY_BLUE_BG);

        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setPreferredSize(new Dimension(520, 430));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FOREST_GREEN_DARK, 3, true),
                BorderFactory.createEmptyBorder(18, 22, 18, 22)
        ));
        card.setBackground(MINT_CARD_BG);

        JPanel titleBox = new JPanel(new GridLayout(2, 1, 2, 2));
        titleBox.setOpaque(true);
        titleBox.setBackground(GRASS_GREEN_BG);
        titleBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FOREST_GREEN_DARK, 2),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel title = new JLabel("PAHIRAM PO!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setForeground(SUNNY_YELLOW);

        JLabel subtitle = new JLabel("University Resource Borrowing & Real-Time Inventory", SwingConstants.CENTER);
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        subtitle.setForeground(CREAM_RIBBON_BG);

        titleBox.add(title);
        titleBox.add(subtitle);
        card.add(titleBox, BorderLayout.NORTH);

        JTabbedPane loginTabs = new JTabbedPane();
        loginTabs.setBackground(LIME_BANNER_ACC);
        loginTabs.setForeground(FOREST_GREEN_DARK);
        loginTabs.setFont(new Font("SansSerif", Font.BOLD, 12));
        loginTabs.addTab("Student / Staff & Faculty", buildBorrowerLoginTab());
        loginTabs.addTab("Admin / Custodian", buildAdminLoginTab());
        card.add(loginTabs, BorderLayout.CENTER);

        wrapper.add(card);
        return wrapper;
    }

    private JPanel buildBorrowerLoginTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(CREAM_RIBBON_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel roleLbl = new JLabel("Role:");
        roleLbl.setForeground(FOREST_GREEN_DARK);
        roleLbl.setFont(roleLbl.getFont().deriveFont(Font.BOLD));
        panel.add(roleLbl, gbc);

        gbc.gridx = 1;
        borrowerRoleFilter = new JComboBox<>(new String[]{"Student", "Staff/Faculty"});
        borrowerRoleFilter.setBackground(Color.WHITE);
        borrowerRoleFilter.setForeground(FOREST_GREEN_DARK);
        panel.add(borrowerRoleFilter, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel idLbl = new JLabel("SR-Code / User ID:");
        idLbl.setForeground(FOREST_GREEN_DARK);
        idLbl.setFont(idLbl.getFont().deriveFont(Font.BOLD));
        panel.add(idLbl, gbc);

        gbc.gridx = 1;
        borrowerIdField = new JTextField("25-00248", 16);
        borrowerIdField.setBackground(Color.WHITE);
        borrowerIdField.setForeground(FOREST_GREEN_DARK);
        panel.add(borrowerIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passLbl = new JLabel("Password:");
        passLbl.setForeground(FOREST_GREEN_DARK);
        passLbl.setFont(passLbl.getFont().deriveFont(Font.BOLD));
        panel.add(passLbl, gbc);

        gbc.gridx = 1;
        borrowerPassField = new JPasswordField("", 16);
        borrowerPassField.setBackground(Color.WHITE);
        borrowerPassField.setForeground(FOREST_GREEN_DARK);
        panel.add(borrowerPassField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel hint = new JLabel("WELCOME! PLEASE ENJOY OUR SYSTEM", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 11));
        hint.setForeground(SKY_BLUE_DEEP);
        panel.add(hint, gbc);

        gbc.gridy = 4;
        JButton loginBtn = createStyledButton("Log In as Borrower", SKY_BLUE_ACCENT, Color.WHITE);
        panel.add(loginBtn, gbc);

        gbc.gridy = 5;
        borrowerLoginStatus = new JLabel(" ", SwingConstants.CENTER);
        borrowerLoginStatus.setForeground(new Color(183, 28, 28));
        panel.add(borrowerLoginStatus, gbc);

        loginBtn.addActionListener(e -> handleBorrowerLogin());
        borrowerPassField.addActionListener(e -> handleBorrowerLogin());

        return panel;
    }

    private JPanel buildAdminLoginTab() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(MINT_CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel adminIdLbl = new JLabel("Admin ID:");
        adminIdLbl.setForeground(FOREST_GREEN_DARK);
        adminIdLbl.setFont(adminIdLbl.getFont().deriveFont(Font.BOLD));
        panel.add(adminIdLbl, gbc);

        gbc.gridx = 1;
        adminIdField = new JTextField("ADMIN-01", 16);
        adminIdField.setBackground(Color.WHITE);
        adminIdField.setForeground(FOREST_GREEN_DARK);
        panel.add(adminIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel adminPassLbl = new JLabel("Admin Password:");
        adminPassLbl.setForeground(FOREST_GREEN_DARK);
        adminPassLbl.setFont(adminPassLbl.getFont().deriveFont(Font.BOLD));
        panel.add(adminPassLbl, gbc);

        gbc.gridx = 1;
        adminPassField = new JPasswordField("", 16);
        adminPassField.setBackground(Color.WHITE);
        adminPassField.setForeground(FOREST_GREEN_DARK);
        panel.add(adminPassField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JLabel hint = new JLabel("ARE YOU THE ADMIN?", SwingConstants.CENTER);
        hint.setFont(new Font("SansSerif", Font.BOLD | Font.ITALIC, 11));
        hint.setForeground(FOREST_GREEN_DARK);
        panel.add(hint, gbc);

        gbc.gridy = 3;
        JButton adminLoginBtn = createStyledButton("Log In to Admin Console", GRASS_GREEN_BG, SUNNY_YELLOW);
        panel.add(adminLoginBtn, gbc);

        gbc.gridy = 4;
        adminLoginStatus = new JLabel(" ", SwingConstants.CENTER);
        adminLoginStatus.setForeground(new Color(183, 28, 28));
        panel.add(adminLoginStatus, gbc);

        adminLoginBtn.addActionListener(e -> handleAdminLogin());
        adminPassField.addActionListener(e -> handleAdminLogin());

        return panel;
    }

    private void handleBorrowerLogin() {
        String selectedRole = (String) borrowerRoleFilter.getSelectedItem();
        String enteredId = borrowerIdField.getText().trim();
        String enteredPass = new String(borrowerPassField.getPassword());

        if (enteredId.isEmpty() || enteredPass.isEmpty()) {
            borrowerLoginStatus.setText("Please enter your ID and password.");
            return;
        }

        for (User u : users) {
            if (u.getUserID().equalsIgnoreCase(enteredId) && !(u instanceof AdminCustodian)) {
                if (!u.getUserType().equalsIgnoreCase(selectedRole)) {
                    borrowerLoginStatus.setText("ID belongs to " + u.getUserType() + ". Please switch Role.");
                    return;
                }
                if (!u.checkPassword(enteredPass)) {
                    borrowerLoginStatus.setText("Incorrect password for " + u.getUserID() + ".");
                    return;
                }
                borrowerLoginStatus.setText(" ");
                borrowerPassField.setText("");
                enterMainApp(u);
                return;
            }
        }
        borrowerLoginStatus.setText("Borrower account not found for ID: " + enteredId);
    }

    private void handleAdminLogin() {
        String enteredId = adminIdField.getText().trim();
        String enteredPass = new String(adminPassField.getPassword());

        if (enteredId.isEmpty() || enteredPass.isEmpty()) {
            adminLoginStatus.setText("Please enter both Admin ID and Password.");
            return;
        }

        for (User u : users) {
            if (u instanceof AdminCustodian && u.getUserID().equalsIgnoreCase(enteredId)) {
                if (!u.checkPassword(enteredPass)) {
                    adminLoginStatus.setText("Incorrect admin password.");
                    return;
                }
                adminLoginStatus.setText(" ");
                adminPassField.setText("");
                enterMainApp(u);
                return;
            }
        }
        adminLoginStatus.setText("Admin account not found for ID: " + enteredId);
    }

    private void enterMainApp(User user) {
        this.currentUser = user;
        user.login();
        user.viewDashboard();

        boolean isAdmin = user instanceof AdminCustodian;
        headerUserLabel.setText("Logged in as: " + user.getName() + "  [" + user.getUserType() + "]");

        // Role-based access difference between Student/Staff and Admin
        borrowerRequestPanel.setVisible(!isAdmin);
        adminRequestActions.setVisible(isAdmin);
        adminRecordActions.setVisible(isAdmin);

        refreshAllTables();
        rootLayout.show(rootPanel, "MAIN");
    }

    private void logoutToLoginScreen() {
        currentUser = null;
        rootLayout.show(rootPanel, "LOGIN");
    }

    // ==================== MAIN SCREEN ====================
    private JPanel buildMainScreen() {
        JPanel main = new JPanel(new BorderLayout(8, 8));
        main.setBackground(SKY_BLUE_BG);
        main.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Top bar: Logged-in user + Logout button
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(GRASS_GREEN_BG);
        topBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FOREST_GREEN_DARK, 2),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        headerUserLabel = new JLabel();
        headerUserLabel.setFont(headerUserLabel.getFont().deriveFont(Font.BOLD, 14f));
        headerUserLabel.setForeground(SUNNY_YELLOW);

        JButton logoutBtn = createStyledButton("Log Out", SKY_BLUE_ACCENT, Color.WHITE);
        logoutBtn.addActionListener(e -> logoutToLoginScreen());

        topBar.add(headerUserLabel, BorderLayout.WEST);
        topBar.add(logoutBtn, BorderLayout.EAST);

        // Borrower Request Bar (shown for Student / Staff & Faculty)
        borrowerRequestPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        borrowerRequestPanel.setBackground(CREAM_RIBBON_BG);
        borrowerRequestPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(FOREST_GREEN_DARK, 1),
                "Submit Borrowing Request"
        ));

        equipmentCombo = new JComboBox<>(inventory.toArray(new Equipment[0]));
        equipmentCombo.setBackground(Color.WHITE);
        equipmentCombo.setForeground(FOREST_GREEN_DARK);
        equipmentCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                          boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Equipment eq) {
                    setText(eq.getName() + " (" + eq.getAvailableQuantity() + "/" + eq.getTotalQuantity() + " avail)");
                }
                return this;
            }
        });

        qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        dateField = new JTextField("2026-11-21", 10);
        dateField.setBackground(Color.WHITE);
        dateField.setForeground(FOREST_GREEN_DARK);

        JButton submitReqBtn = createStyledButton("Submit Request", SKY_BLUE_ACCENT, Color.WHITE);

        JLabel eqLbl = new JLabel("Equipment:");
        eqLbl.setForeground(FOREST_GREEN_DARK);
        JLabel qtyLbl = new JLabel("Quantity:");
        qtyLbl.setForeground(FOREST_GREEN_DARK);
        JLabel dateLbl = new JLabel("Date:");
        dateLbl.setForeground(FOREST_GREEN_DARK);

        borrowerRequestPanel.add(eqLbl);
        borrowerRequestPanel.add(equipmentCombo);
        borrowerRequestPanel.add(qtyLbl);
        borrowerRequestPanel.add(qtySpinner);
        borrowerRequestPanel.add(dateLbl);
        borrowerRequestPanel.add(dateField);
        borrowerRequestPanel.add(submitReqBtn);

        JPanel northContainer = new JPanel(new BorderLayout(4, 4));
        northContainer.setOpaque(false);
        northContainer.add(topBar, BorderLayout.NORTH);
        northContainer.add(borrowerRequestPanel, BorderLayout.CENTER);
        main.add(northContainer, BorderLayout.NORTH);

        // Center tables: Inventory, Requests, Records
        JPanel centerPanel = new JPanel(new GridLayout(3, 1, 6, 6));
        centerPanel.setOpaque(false);

        inventoryModel = new DefaultTableModel(
                new String[]{"ID", "Category", "Name", "Status", "Available Qty", "Total Qty"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        inventoryTable = new JTable(inventoryModel);
        styleTable(inventoryTable);

        JPanel invPanel = new JPanel(new BorderLayout());
        invPanel.setBackground(MINT_CARD_BG);
        invPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(FOREST_GREEN_DARK, 1),
                "Equipment Inventory Catalog"
        ));
        invPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        requestModel = new DefaultTableModel(
                new String[]{"Request ID", "Borrower", "Equipment", "Qty", "Date", "Status"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        requestTable = new JTable(requestModel);
        styleTable(requestTable);

        JPanel reqPanel = new JPanel(new BorderLayout());
        reqPanel.setBackground(CREAM_RIBBON_BG);
        reqPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GRASS_GREEN_BG, 2),
                "Borrowing Requests"
        ));
        reqPanel.add(new JScrollPane(requestTable), BorderLayout.CENTER);

        adminRequestActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adminRequestActions.setBackground(CREAM_RIBBON_BG);
        JButton approveBtn = createStyledButton("Approve", GRASS_GREEN_BG, SUNNY_YELLOW);
        JButton rejectBtn = createStyledButton("Reject", SKY_BLUE_DEEP, Color.WHITE);
        adminRequestActions.add(approveBtn);
        adminRequestActions.add(rejectBtn);
        reqPanel.add(adminRequestActions, BorderLayout.SOUTH);

        recordModel = new DefaultTableModel(
                new String[]{"Record ID", "Borrower", "Equipment", "Qty", "Borrow Date", "Return Date", "Condition", "Late Fee"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        recordTable = new JTable(recordModel);
        styleTable(recordTable);

        JPanel recPanel = new JPanel(new BorderLayout());
        recPanel.setBackground(MINT_CARD_BG);
        recPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(FOREST_GREEN_DARK, 1),
                "Borrowing Records & Late Fees"
        ));
        recPanel.add(new JScrollPane(recordTable), BorderLayout.CENTER);

        adminRecordActions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        adminRecordActions.setBackground(MINT_CARD_BG);
        JButton returnOnTimeBtn = createStyledButton("Record Return (On Time)", SKY_BLUE_ACCENT, Color.WHITE);
        JButton returnLateBtn = createStyledButton("Record Late Return & Fine", GRASS_GREEN_BG, SUNNY_YELLOW);
        adminRecordActions.add(returnOnTimeBtn);
        adminRecordActions.add(returnLateBtn);
        recPanel.add(adminRecordActions, BorderLayout.SOUTH);

        centerPanel.add(invPanel);
        centerPanel.add(reqPanel);
        centerPanel.add(recPanel);
        main.add(centerPanel, BorderLayout.CENTER);

        // Listeners
        submitReqBtn.addActionListener(e -> {
            if (currentUser == null || currentUser instanceof AdminCustodian) return;
            Equipment equip = (Equipment) equipmentCombo.getSelectedItem();
            int qty = (Integer) qtySpinner.getValue();
            String date = dateField.getText().trim();

            if (equip == null || date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please choose equipment and a date.");
                return;
            }

            String reqId = String.format("REQ-%03d", reqCounter++);
            BorrowingRequest req = new BorrowingRequest(reqId, currentUser, equip, qty, date);
            requests.add(req);
            req.submitRequest();
            refreshAllTables();
        });

        approveBtn.addActionListener(e -> {
            if (!(currentUser instanceof AdminCustodian)) return;
            int row = requestTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a request to approve.");
                return;
            }
            BorrowingRequest req = requests.get(row);
            if (!"PENDING".equals(req.getStatus())) {
                JOptionPane.showMessageDialog(this, "Only PENDING requests can be approved.");
                return;
            }
            if (req.approveRequest()) {
                String recId = String.format("REC-%03d", recCounter++);
                BorrowingRecord rec = new BorrowingRecord(recId, req, req.getDate());
                records.add(rec);
            }
            refreshAllTables();
        });

        rejectBtn.addActionListener(e -> {
            if (!(currentUser instanceof AdminCustodian)) return;
            int row = requestTable.getSelectedRow();
            if (row < 0) {
                JOptionPane.showMessageDialog(this, "Select a request to reject.");
                return;
            }
            BorrowingRequest req = requests.get(row);
            if (!"PENDING".equals(req.getStatus())) {
                JOptionPane.showMessageDialog(this, "Only PENDING requests can be rejected.");
                return;
            }
            req.rejectRequest();
            refreshAllTables();
        });

        returnOnTimeBtn.addActionListener(e -> processReturn(false));
        returnLateBtn.addActionListener(e -> processReturn(true));

        return main;
    }

    private void processReturn(boolean isLate) {
        if (!(currentUser instanceof AdminCustodian)) return;
        int row = recordTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Select a borrowing record to process return.");
            return;
        }
        BorrowingRecord rec = records.get(row);
        if (!"Pending Return".equals(rec.getReturnDate())) {
            JOptionPane.showMessageDialog(this, "This record has already been returned.");
            return;
        }

        String[] conditions = {"Good", "Damaged", "Lost"};
        String condition = (String) JOptionPane.showInputDialog(
                this, "Select item condition:", "Return Condition",
                JOptionPane.PLAIN_MESSAGE, null, conditions, "Good"
        );
        if (condition == null) return;

        int daysLate = 0;
        if (isLate) {
            String input = JOptionPane.showInputDialog(this, "How many days late?", "2");
            if (input == null) return;
            try {
                daysLate = Math.max(0, Integer.parseInt(input.trim()));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid number.");
                return;
            }
            rec.checkLateReturn(true, daysLate);
        } else {
            rec.checkLateReturn(false, 0);
        }

        String retDate = JOptionPane.showInputDialog(this, "Enter Return Date (YYYY-MM-DD):", "2026-11-23");
        if (retDate == null || retDate.trim().isEmpty()) return;

        rec.recordReturn(retDate.trim(), condition);
        refreshAllTables();
    }

    private void refreshAllTables() {
        inventoryModel.setRowCount(0);
        for (Equipment eq : inventory) {
            inventoryModel.addRow(new Object[]{
                    eq.getEquipmentID(),
                    eq.getCategory(),
                    eq.getName(),
                    eq.getStatus(),
                    eq.getAvailableQuantity(),
                    eq.getTotalQuantity()
            });
        }
        equipmentCombo.repaint();

        boolean isAdmin = currentUser instanceof AdminCustodian;

        requestModel.setRowCount(0);
        for (BorrowingRequest req : requests) {
            if (isAdmin || (currentUser != null && req.getBorrower().getUserID().equals(currentUser.getUserID()))) {
                requestModel.addRow(new Object[]{
                        req.getRequestID(),
                        req.getBorrower().getName(),
                        req.getEquipment().getName(),
                        req.getRequestedQuantity(),
                        req.getDate(),
                        req.getStatus()
                });
            }
        }

        recordModel.setRowCount(0);
        for (BorrowingRecord rec : records) {
            if (isAdmin || (currentUser != null && rec.getBorrower().getUserID().equals(currentUser.getUserID()))) {
                recordModel.addRow(new Object[]{
                        rec.getRecordID(),
                        rec.getBorrower().getName(),
                        rec.getEquipment().getName(),
                        rec.getBorrowedQuantity(),
                        rec.getBorrowDate(),
                        rec.getReturnDate(),
                        rec.getItemCondition(),
                        "Php " + String.format("%.2f", rec.getLateFee())
                });
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PahiramPoUI().setVisible(true));
    }
}