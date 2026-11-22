package ui;

import backend.PahiramService;
import database.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.Objects;

public class PahiramUI {
    private static PahiramService service = new PahiramService();
    private static JFrame frame;

    // ================= MAIN METHOD =================
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            setFonts();
            showLogin();
        });
    }

    private static void setFonts() {
        Font normal = new Font("SansSerif", Font.PLAIN, 18);
        Font bold = new Font("SansSerif", Font.BOLD, 18);

        UIManager.put("Label.font", normal);
        UIManager.put("Button.font", bold);
        UIManager.put("TextField.font", normal);
        UIManager.put("PasswordField.font", normal);
        UIManager.put("ComboBox.font", normal);
        UIManager.put("Table.font", normal);
        UIManager.put("TableHeader.font", bold);
        UIManager.put("TabbedPane.font", bold);
        UIManager.put("OptionPane.messageFont", normal);
        UIManager.put("OptionPane.buttonFont", bold);
    }

    public static void showLogin() {
        frame = new JFrame("Pahiram po! | University Resource Borrowing Platform");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1050, 760);
        frame.setMinimumSize(new Dimension(850, 650));
        frame.setLocationRelativeTo(null);

        JPanel root = new JPanel(new GridBagLayout());
        root.setBackground(new Color(244, 249, 246));

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(190, 215, 202), 2),
                new EmptyBorder(30, 45, 30, 45)));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(9, 10, 9, 10);
        c.fill = GridBagConstraints.HORIZONTAL;

        c.gridx = 0; c.gridy = 0; c.gridwidth = 2;
        JLabel title = new JLabel("PAHIRAM PO!", SwingConstants.CENTER);
        title.setFont(new Font("SansSerif", Font.BOLD, 42));
        title.setForeground(new Color(30, 100, 75));
        card.add(title, c);

        c.gridy++;
        card.add(new JLabel("Integrated University Resource Borrowing Platform", SwingConstants.CENTER), c);

        c.gridwidth = 1; c.gridy++; c.gridx = 0;
        card.add(new JLabel("Full Name:"), c);
        c.gridx = 1;
        JTextField nameField = new JTextField(22);
        card.add(nameField, c);

        c.gridy++; c.gridx = 0;
        card.add(new JLabel("User Type:"), c);
        c.gridx = 1;
        JComboBox<String> roleBox = new JComboBox<>(new String[]{"Student", "Staff / Faculty", "Resource Custodian (Admin)"});
        card.add(roleBox, c);

        c.gridy++; c.gridx = 0;
        JLabel idLabel = new JLabel("SR-Code:");
        card.add(idLabel, c);
        c.gridx = 1;
        JTextField idField = new JTextField(22);
        card.add(idField, c);

        c.gridy++; c.gridx = 0;
        JLabel passLabel = new JLabel("Admin Password:");
        card.add(passLabel, c);
        c.gridx = 1;
        JPasswordField passField = new JPasswordField(22);
        card.add(passField, c);

        c.gridy++; c.gridx = 0; c.gridwidth = 2;
        JLabel note = new JLabel("Students must enter an SR-Code. Staff/Faculty must enter a Staff ID.", SwingConstants.CENTER);
        note.setForeground(new Color(75, 95, 85));
        card.add(note, c);

        roleBox.addActionListener(e -> {
            int role = roleBox.getSelectedIndex();
            boolean admin = role == 2;
            idLabel.setText(role == 1 ? "Staff ID:" : "SR-Code:");
            idLabel.setVisible(!admin);
            idField.setVisible(!admin);
            note.setVisible(!admin);
            passLabel.setVisible(admin);
            passField.setVisible(admin);
            card.revalidate();
            card.repaint();
        });

        passLabel.setVisible(false);
        passField.setVisible(false);

        c.gridy++;
        JButton loginBtn = new JButton("CONTINUE");
        styleButton(loginBtn);
        card.add(loginBtn, c);

        loginBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                int role = roleBox.getSelectedIndex();
                if (role == 2) {
                    service.loginAdmin(name, new String(passField.getPassword()));
                } else {
                    service.loginUser(name, role, idField.getText().trim());
                }
                showDashboard();
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        root.add(card);
        frame.setContentPane(root);
        frame.setVisible(true);
    }

    public static void showDashboard() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(18, 22, 22, 22));
        root.setBackground(new Color(244, 249, 246));

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);

        User currentUser = service.getCurrentUser();
        String identity = currentUser instanceof Custodian
                ? "Resource Custodian"
                : currentUser.getRole() + " | " + currentUser.getIdentifier();

        JLabel welcome = new JLabel("Welcome, " + currentUser.name + "   •   " + identity);
        welcome.setFont(new Font("SansSerif", Font.BOLD, 22));

        JButton logout = new JButton("Log out");
        logout.addActionListener(e -> {
            service.logout();
            showLogin();
        });

        top.add(welcome, BorderLayout.CENTER);
        top.add(logout, BorderLayout.EAST);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Equipment Catalog", catalogPanel());

        if (currentUser instanceof Custodian) {
            tabs.addTab("Admin Dashboard", adminPanel());
            tabs.addTab("Manage Equipment", manageEquipmentPanel());
            tabs.addTab("Manage Requests", adminRequestsPanel());
            tabs.addTab("All Records", adminRecordsPanel());
            tabs.addTab("User Accounts", usersPanel());
        } else {
            tabs.addTab("Request to Borrow", borrowPanel());
            tabs.addTab("My Requests / History", myRequestsPanel());
            tabs.addTab("My Borrowing History", myHistoryPanel());
        }

        root.add(top, BorderLayout.NORTH);
        root.add(tabs, BorderLayout.CENTER);

        frame.setContentPane(root);
        frame.setTitle("Pahiram po! | " + currentUser.name);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.revalidate();
        frame.repaint();
    }

    private static JPanel catalogPanel() {
        String[] cols = {"Equipment ID", "Equipment / Resource", "Category", "Department", "Available Quantity", "Status"};
        DefaultTableModel m = model(cols);
        for (Equipment x : service.getData().equipment) {
            m.addRow(new Object[]{x.id, x.name, x.category, x.department, x.availableQty, x.availableQty > 0 ? "Available" : "Unavailable"});
        }
        return tablePanel(m, null);
    }

    private static JPanel borrowPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(new EmptyBorder(25, 35, 25, 35));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(9, 9, 9, 9);
        c.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<Equipment> itemBox = new JComboBox<>();
        for (Equipment x : service.getData().equipment) {
            if (x.availableQty > 0) itemBox.addItem(x);
        }

        JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        JTextField purpose = new JTextField(25);
        JTextField pickup = new JTextField(LocalDate.now().toString(), 15);
        JTextField due = new JTextField(LocalDate.now().plusDays(3).toString(), 15);

        User u = service.getCurrentUser();
        p.add(new JLabel("Borrower:"), c); c.gridx = 1; p.add(new JLabel(u.name), c);
        c.gridy = 1; c.gridx = 0; p.add(new JLabel("User Type:"), c); c.gridx = 1; p.add(new JLabel(u.getRole()), c);
        c.gridy = 2; c.gridx = 0; p.add(new JLabel(u instanceof Student ? "SR-Code:" : "Staff ID:"), c); c.gridx = 1; p.add(new JLabel(u.getIdentifier()), c);

        c.gridy = 3; c.gridx = 0; p.add(new JLabel("Equipment:"), c); c.gridx = 1; p.add(itemBox, c);
        c.gridy = 4; c.gridx = 0; p.add(new JLabel("Quantity:"), c); c.gridx = 1; p.add(qty, c);
        c.gridy = 5; c.gridx = 0; p.add(new JLabel("Purpose:"), c); c.gridx = 1; p.add(purpose, c);
        c.gridy = 6; c.gridx = 0; p.add(new JLabel("Pick-up Date (YYYY-MM-DD):"), c); c.gridx = 1; p.add(pickup, c);
        c.gridy = 7; c.gridx = 0; p.add(new JLabel("Return Date (YYYY-MM-DD):"), c); c.gridx = 1; p.add(due, c);

        c.gridy = 8; c.gridx = 0; c.gridwidth = 2;
        JButton submit = new JButton("Submit Borrowing Request");
        styleButton(submit);
        p.add(submit, c);

        submit.addActionListener(e -> {
            try {
                String reqId = service.submitRequest((Equipment) itemBox.getSelectedItem(), (Integer) qty.getValue(), purpose.getText(), pickup.getText(), due.getText());
                JOptionPane.showMessageDialog(frame, "Your request has been saved.\nRequest ID: " + reqId + "\nStatus: PENDING");
                showDashboard();
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        return p;
    }

    private static JPanel myRequestsPanel() {
        DefaultTableModel m = model(new String[]{"Request ID", "Full Name", "User Type", "SR-Code / Staff ID", "Equipment", "Qty", "Pick-up", "Return", "Purpose", "Status"});
        User u = service.getCurrentUser();
        for (BorrowingRequest r : service.getData().requests) {
            if (isMine(r.userId, r.borrowerRole, r.borrowerIdentifier, u)) {
                m.addRow(new Object[]{r.id, r.borrowerName, r.borrowerRole, r.borrowerIdentifier, r.equipmentName(service.getData()), r.quantity, r.pickupDate, r.returnDate, r.purpose, r.status});
            }
        }
        return tablePanel(m, "Shows all requests.");
    }

    private static JPanel myHistoryPanel() {
        DefaultTableModel m = model(new String[]{"Request ID", "Record ID", "Full Name", "User Type", "SR-Code / Staff ID", "Equipment", "Qty", "Borrow date", "Due date", "Status", "Penalty"});
        User u = service.getCurrentUser();
        for (BorrowingRequest q : service.getData().requests) {
            if (isMine(q.userId, q.borrowerRole, q.borrowerIdentifier, u)) {
                BorrowingRecord rec = service.getData().recordForRequest(q.id);
                m.addRow(new Object[]{q.id, rec == null ? "—" : rec.id, q.borrowerName, q.borrowerRole, q.borrowerIdentifier, q.equipmentName(service.getData()), q.quantity, q.pickupDate, q.returnDate, rec == null ? "Request: " + q.status : rec.condition, rec == null ? "—" : "₱" + String.format("%.2f", rec.penalty)});
            }
        }
        return tablePanel(m, "Your borrowing history.");
    }

    private static boolean isMine(String userId, String role, String identifier, User currentUser) {
        if (Objects.equals(userId, currentUser.id)) return true;
        return Objects.equals(role, currentUser.getRole()) && identifier != null && identifier.equalsIgnoreCase(currentUser.getIdentifier());
    }

    private static JPanel adminPanel() {
        JPanel p = new JPanel(new GridLayout(2, 2, 20, 20));
        p.setBorder(new EmptyBorder(35, 35, 35, 35));
        DataManager d = service.getData();
        long pending = d.requests.stream().filter(r -> "PENDING".equals(r.status)).count();
        long active = d.records.stream().filter(r -> "BORROWED".equals(r.condition)).count();
        int stock = d.equipment.stream().mapToInt(x -> x.availableQty).sum();

        p.add(stat("PENDING REQUESTS", pending));
        p.add(stat("ACTIVE BORROWINGS", active));
        p.add(stat("AVAILABLE ITEMS", stock));
        p.add(stat("REGISTERED USERS", d.users.size()));
        return p;
    }

    private static JPanel stat(String label, long n) {
        JPanel p = new JPanel(new GridLayout(2, 1));
        p.setBorder(BorderFactory.createLineBorder(new Color(170, 205, 185), 2));
        JLabel a = new JLabel(label, SwingConstants.CENTER); a.setFont(new Font("SansSerif", Font.BOLD, 22));
        JLabel b = new JLabel("" + n, SwingConstants.CENTER); b.setFont(new Font("SansSerif", Font.BOLD, 40));
        p.add(a); p.add(b);
        return p;
    }

    private static JPanel adminRequestsPanel() {
        DefaultTableModel m = model(new String[]{"Request ID", "Borrower Name", "User Type", "SR-Code / Staff ID", "Equipment", "Qty", "Pick-up", "Return", "Purpose", "Status"});
        for (BorrowingRequest r : service.getData().requests) {
            m.addRow(new Object[]{r.id, r.borrowerName, r.borrowerRole, r.borrowerIdentifier, r.equipmentName(service.getData()), r.quantity, r.pickupDate, r.returnDate, r.purpose, r.status});
        }
        JTable t = new JTable(m);
        styleTable(t);
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        JButton approve = new JButton("Approve Selected");
        JButton reject = new JButton("Reject Selected");
        approve.addActionListener(e -> handleDecision(t, true));
        reject.addActionListener(e -> handleDecision(t, false));
        buttons.add(approve); buttons.add(reject);
        p.add(buttons, BorderLayout.SOUTH);
        return p;
    }

    private static void handleDecision(JTable t, boolean approve) {
        int row = t.getSelectedRow();
        if (row < 0) { error("Select a request first."); return; }
        try {
            service.decideRequest("" + t.getValueAt(row, 0), approve);
            JOptionPane.showMessageDialog(frame, approve ? "Request approved." : "Request rejected.");
            showDashboard();
        } catch (Exception ex) {
            error(ex.getMessage());
        }
    }

    private static JPanel adminRecordsPanel() {
        DefaultTableModel m = model(new String[]{"Record ID", "Request ID", "Borrower Name", "User Type", "SR-Code / Staff ID", "Equipment", "Qty", "Borrow date", "Due date", "Condition", "Penalty"});
        for (BorrowingRecord r : service.getData().records) {
            m.addRow(new Object[]{r.id, r.requestId, r.borrowerName, r.borrowerRole, r.borrowerIdentifier, r.equipmentName(service.getData()), r.quantity, r.borrowDate, r.dueDate, r.condition, "₱" + String.format("%.2f", r.penalty)});
        }
        JTable t = new JTable(m);
        styleTable(t);
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(t), BorderLayout.CENTER);

        JButton returned = new JButton("Mark Selected as Returned");
        returned.addActionListener(e -> {
            int row = t.getSelectedRow();
            if (row < 0) { error("Select a record first."); return; }
            try {
                String msg = service.returnRecord("" + t.getValueAt(row, 0));
                JOptionPane.showMessageDialog(frame, msg);
                showDashboard();
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });
        p.add(returned, BorderLayout.SOUTH);
        return p;
    }

    private static JPanel manageEquipmentPanel() {
        JPanel p = new JPanel(new BorderLayout());
        JPanel f = new JPanel(new GridLayout(0, 2, 10, 10));
        f.setBorder(new EmptyBorder(20, 30, 20, 30));

        JTextField name = new JTextField();
        JTextField cat = new JTextField();
        JTextField dept = new JTextField();
        JSpinner qty = new JSpinner(new SpinnerNumberModel(1, 0, 10000, 1));

        f.add(new JLabel("Equipment name:")); f.add(name);
        f.add(new JLabel("Category:")); f.add(cat);
        f.add(new JLabel("Department:")); f.add(dept);
        f.add(new JLabel("Quantity:")); f.add(qty);

        JButton add = new JButton("Add Equipment");
        add.addActionListener(e -> {
            try {
                service.addEquipment(name.getText(), cat.getText(), dept.getText(), (Integer) qty.getValue());
                JOptionPane.showMessageDialog(frame, "Equipment added.");
                showDashboard();
            } catch (Exception ex) {
                error(ex.getMessage());
            }
        });

        p.add(f, BorderLayout.CENTER);
        p.add(add, BorderLayout.SOUTH);
        return p;
    }

    private static JPanel usersPanel() {
        DefaultTableModel m = model(new String[]{"User ID", "Full Name", "User Type", "SR-Code / Staff ID"});
        for (User u : service.getData().users) {
            m.addRow(new Object[]{u.id, u.name, u.getRole(), u.getIdentifier()});
        }
        return tablePanel(m, "Registered user accounts.");
    }

    private static JPanel tablePanel(DefaultTableModel m, String note) {
        JTable t = new JTable(m);
        styleTable(t);
        JPanel p = new JPanel(new BorderLayout(5, 5));
        p.add(new JScrollPane(t), BorderLayout.CENTER);
        if (note != null) p.add(new JLabel(note, SwingConstants.CENTER), BorderLayout.SOUTH);
        return p;
    }

    private static DefaultTableModel model(String[] cols) {
        return new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
    }

    private static void styleTable(JTable t) {
        t.setRowHeight(34);
        t.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        t.setFillsViewportHeight(true);
        t.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        t.setAutoCreateRowSorter(true);
    }

    private static void styleButton(JButton b) {
        b.setFont(new Font("SansSerif", Font.BOLD, 20));
        b.setBackground(new Color(35, 115, 82));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setPreferredSize(new Dimension(280, 48));
    }

    private static void error(String s) {
        JOptionPane.showMessageDialog(frame, s, "Please check", JOptionPane.WARNING_MESSAGE);
    }
}