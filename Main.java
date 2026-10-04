package com.restaurant;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.restaurant.dao.FoodDAO;
import com.restaurant.model.Food;
import com.restaurant.dao.CustomerDAO;
import com.restaurant.model.Customer;
import com.restaurant.dao.TableDAO;
import com.restaurant.model.RestaurantTable;
import com.restaurant.dao.OrderDAO;
import com.restaurant.model.Order;

public class Main {

    // ============================================
    //  COLORS (fixed cards)
    // ============================================
    private static final Color ACCENT     = new Color(255, 111, 66);
    private static final Color CARD_FOOD  = new Color(255, 138, 101);
    private static final Color CARD_TABLE = new Color(66, 165, 245);
    private static final Color CARD_CUST  = new Color(102, 187, 106);
    private static final Color CARD_ORDER = new Color(171, 130, 255);
    private static final Color WHITE      = Color.WHITE;
    private static final Font UI_FONT     = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BOLD_FONT   = new Font("Segoe UI", Font.BOLD, 14);

    // ============================================
    //  THEME COLORS (light / dark)
    // ============================================
    private static boolean darkMode = false;
    private static Color BG, PANEL, HEADER, TEXT, TEXT_DIM, FIELD_BG, BORDER, GRID;

    static {
        applyTheme();
    }

    private static void applyTheme() {
        if (darkMode) {
            BG       = new Color(25, 26, 38);
            PANEL    = new Color(38, 39, 56);
            HEADER   = new Color(18, 19, 29);
            TEXT     = new Color(235, 236, 245);
            TEXT_DIM = new Color(150, 152, 172);
            FIELD_BG = new Color(52, 53, 72);
            BORDER   = new Color(70, 72, 95);
            GRID     = new Color(60, 62, 82);
        } else {
            BG       = new Color(245, 246, 250);
            PANEL    = Color.WHITE;
            HEADER   = new Color(38, 39, 54);
            TEXT     = new Color(35, 36, 50);
            TEXT_DIM = new Color(130, 132, 148);
            FIELD_BG = Color.WHITE;
            BORDER   = new Color(210, 212, 225);
            GRID     = new Color(230, 231, 240);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createLogin);
    }

    // ============================================
    //  CUSTOM ROUNDED BUTTON (hover effect)
    // ============================================
    static class StyledButton extends JButton {
        private final Color baseColor;
        private boolean hover = false;

        StyledButton(String text, Color color) {
            super(text);
            this.baseColor = color;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setForeground(WHITE);
            setFont(BOLD_FONT);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) { hover = true;  repaint(); }
                @Override
                public void mouseExited(MouseEvent e)  { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(hover ? baseColor.darker() : baseColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ============================================
    //  HELPER: styled input field
    // ============================================
    private static JTextField styledField() {
        JTextField field = new JTextField();
        field.setFont(UI_FONT);
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        return field;
    }

    private static JPasswordField styledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(UI_FONT);
        field.setBackground(FIELD_BG);
        field.setForeground(TEXT);
        field.setCaretColor(TEXT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(8, 10, 8, 10)));
        return field;
    }

    private static JLabel formLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(BOLD_FONT);
        label.setForeground(TEXT);
        return label;
    }

    private static JPanel formPanel(Component... rows) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL);
        for (Component row : rows) {
            panel.add(row);
            panel.add(Box.createVerticalStrut(8));
        }
        panel.setBorder(new EmptyBorder(15, 15, 5, 15));
        return panel;
    }

    // ============================================
    //  HELPER: styled JTable dialog
    // ============================================
    private static JTable styledTable(Object[][] data, String[] columns) {
        JTable table = new JTable(data, columns);
        table.setRowHeight(30);
        table.setFont(UI_FONT);
        table.setBackground(PANEL);
        table.setForeground(TEXT);
        table.setGridColor(GRID);
        table.setSelectionBackground(ACCENT);
        table.setSelectionForeground(WHITE);
        table.setShowVerticalLines(false);
        table.getTableHeader().setFont(BOLD_FONT);
        table.getTableHeader().setBackground(HEADER);
        table.getTableHeader().setForeground(WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(0, 36));
        table.setEnabled(false);
        return table;
    }

    private static void showTableDialog(JFrame frame, String title,
                                        String[] columns, Object[][] data) {
        if (data.length == 0) {
            JOptionPane.showMessageDialog(frame,
                    "No records found yet.\nAdd some data first!",
                    title, JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        JTable table = styledTable(data, columns);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setPreferredSize(new Dimension(520, 320));
        scroll.getViewport().setBackground(PANEL);
        scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));

        JOptionPane.showMessageDialog(frame, scroll, title,
                JOptionPane.PLAIN_MESSAGE);
    }

    // ============================================
    //  LOGIN SCREEN
    // ============================================
    private static void createLogin() {

        JFrame frame = new JFrame("Restaurant Handling System - Login");
        frame.setSize(460, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG);
        frame.setLayout(new GridBagLayout());

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(35, 45, 35, 45)));

        JLabel logo = new JLabel("🍽️");
        logo.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 52));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel title = new JLabel("RestoChef");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Restaurant Handling System");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_DIM);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextField usernameField = styledField();
        usernameField.setMaximumSize(new Dimension(280, 40));
        JPasswordField passwordField = styledPasswordField();
        passwordField.setMaximumSize(new Dimension(280, 40));

        JLabel userLabel = formLabel("Username");
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel passLabel = formLabel("Password");
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        StyledButton loginButton = new StyledButton("LOGIN", ACCENT);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(280, 44));
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 15));

        card.add(logo);
        card.add(Box.createVerticalStrut(8));
        card.add(title);
        card.add(Box.createVerticalStrut(2));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(28));
        card.add(userLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(14));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(4));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(24));
        card.add(loginButton);

        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (username.equals("RestoChef") && password.equals("Taste@2026")) {
                frame.dispose();
                createDashboard();
            } else {
                shake(card);
                JOptionPane.showMessageDialog(frame,
                        "Invalid username or password!",
                        "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(card);
        frame.setVisible(true);
    }

    // shake animation on wrong login
    private static void shake(JComponent comp) {
        final Point original = comp.getLocation();
        new Timer(10, new java.awt.event.ActionListener() {
            int count = 0;
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                int offset = (count % 2 == 0) ? -6 : 6;
                comp.setLocation(original.x + offset, original.y);
                if (++count > 8) {
                    comp.setLocation(original);
                    ((Timer) e.getSource()).stop();
                }
            }
        }).start();
    }

    // ============================================
    //  DASHBOARD
    // ============================================
    private static void createDashboard() {

        JFrame frame = new JFrame("Restaurant Handling System");
        frame.setSize(980, 640);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.getContentPane().setBackground(BG);
        frame.setLayout(new BorderLayout());

        // ---------- HEADER ----------
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER);
        header.setBorder(new EmptyBorder(10, 25, 10, 25));

        JLabel title = new JLabel("🍽  Restaurant Handling System");
        title.setForeground(WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 19));

        StyledButton themeButton = new StyledButton(
                darkMode ? "☀  Light Mode" : "🌙  Dark Mode",
                new Color(90, 92, 118));
        themeButton.setPreferredSize(new Dimension(125, 30));
        themeButton.addActionListener(e -> {
            darkMode = !darkMode;
            applyTheme();
            frame.dispose();
            createDashboard();
        });

        StyledButton logoutButton = new StyledButton("Logout",
                new Color(120, 122, 140));
        logoutButton.setPreferredSize(new Dimension(90, 30));
        logoutButton.addActionListener(e -> {
            frame.dispose();
            createLogin();
        });

        JPanel headerRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        headerRight.setBackground(HEADER);
        headerRight.add(themeButton);
        headerRight.add(logoutButton);

        header.add(title, BorderLayout.WEST);
        header.add(headerRight, BorderLayout.EAST);

        // ---------- LIVE STATS ----------
        JPanel statsBar = new JPanel(new BorderLayout(15, 0));
        statsBar.setBackground(BG);
        statsBar.setBorder(new EmptyBorder(15, 40, 5, 40));
        statsBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel statsGrid = new JPanel(new GridLayout(1, 4, 18, 0));
        statsGrid.setBackground(BG);

        JLabel salesValue = statCard(statsGrid, "₹0", "💰 Total Sales", ACCENT);
        JLabel orderValue = statCard(statsGrid, "0", "🧾 Total Orders", CARD_ORDER);
        JLabel foodValue  = statCard(statsGrid, "0", "🍕 Food Items", CARD_FOOD);
        JLabel custValue  = statCard(statsGrid, "0", "👤 Customers", CARD_CUST);

        StyledButton refreshButton = new StyledButton("🔄", new Color(90, 92, 118));
        refreshButton.setPreferredSize(new Dimension(40, 34));
        refreshButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));

        statsBar.add(statsGrid, BorderLayout.CENTER);
        statsBar.add(refreshButton, BorderLayout.EAST);

        Runnable updateStats = () -> {
            try {
                double sales = 0;
                List<Order> orders = new OrderDAO().getAllOrders();
                for (Order o : orders) sales += o.getTotalAmount();
                salesValue.setText("₹" + sales);
                orderValue.setText(String.valueOf(orders.size()));
                foodValue.setText(String.valueOf(new FoodDAO().getAllFood().size()));
                custValue.setText(String.valueOf(new CustomerDAO().getAllCustomers().size()));
            } catch (Exception ignored) { }
        };
        refreshButton.addActionListener(e -> updateStats.run());
        updateStats.run();

        // ---------- CONTENT (cards) ----------
        JPanel content = new JPanel(new GridLayout(2, 2, 24, 24));
        content.setBackground(BG);
        content.setBorder(new EmptyBorder(8, 40, 35, 40));

        StyledButton foodButton = makeCard("🍕", "Food Menu",
                "View & add food items", CARD_FOOD);
        StyledButton tableButton = makeCard("🪑", "Tables",
                "Manage restaurant tables", CARD_TABLE);
        StyledButton customerButton = makeCard("👤", "Customers",
                "Manage customer details", CARD_CUST);
        StyledButton orderButton = makeCard("🧾", "Orders & Billing",
                "Take orders, generate bills", CARD_ORDER);

        foodButton.addActionListener(e -> foodMenu(frame));
        tableButton.addActionListener(e -> tableMenu(frame));
        customerButton.addActionListener(e -> customerMenu(frame));
        orderButton.addActionListener(e -> orderMenu(frame));

        content.add(foodButton);
        content.add(tableButton);
        content.add(customerButton);
        content.add(orderButton);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(BG);
        centerPanel.add(statsBar);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(content);

        frame.add(header, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    // one stat card; returns the value label so it can be updated
    private static JLabel statCard(JPanel parent, String value,
                                   String caption, Color valueColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                new EmptyBorder(6, 14, 6, 14)));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 19));
        valueLabel.setForeground(valueColor);

        JLabel captionLabel = new JLabel(caption);
        captionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        captionLabel.setForeground(TEXT_DIM);

        card.add(valueLabel, BorderLayout.CENTER);
        card.add(captionLabel, BorderLayout.SOUTH);
        parent.add(card);
        return valueLabel;
    }

    private static StyledButton makeCard(String emoji, String name,
                                         String subtitle, Color color) {
        StyledButton btn = new StyledButton(
                "<html><center><span style='font-size:42px'>" + emoji + "</span><br>"
                        + "<font size='+2'><b>" + name + "</b></font><br>"
                        + "<font size='-1'>" + subtitle + "</font></center></html>",
                color);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return btn;
    }

    // ============================================
    //  FOOD MENU
    // ============================================
    private static void foodMenu(JFrame frame) {
        String[] options = {"📋 View Food Menu", "➕ Add Food"};
        int choice = JOptionPane.showOptionDialog(frame,
                "What would you like to do?", "Food Menu",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            List<Food> list = new FoodDAO().getAllFood();
            Object[][] data = new Object[list.size()][4];
            for (int i = 0; i < list.size(); i++) {
                Food f = list.get(i);
                data[i] = new Object[]{f.getId(), f.getName(), "₹" + f.getPrice(), f.getCategory()};
            }
            showTableDialog(frame, "🍕 Food Menu",
                    new String[]{"ID", "Name", "Price", "Category"}, data);

        } else if (choice == 1) {
            JTextField nameField = styledField();
            JTextField priceField = styledField();
            JTextField categoryField = styledField();

            JPanel panel = formPanel(
                    formLabel("Food Name:"), nameField,
                    formLabel("Price (₹):"), priceField,
                    formLabel("Category:"), categoryField);

            int result = JOptionPane.showConfirmDialog(frame, panel,
                    "Add New Food", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    Food food = new Food(0, nameField.getText().trim(),
                            Double.parseDouble(priceField.getText().trim()),
                            categoryField.getText().trim());
                    new FoodDAO().addFood(food);
                    JOptionPane.showMessageDialog(frame,
                            "Food added successfully! ✅");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid price.",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // ============================================
    //  TABLES
    // ============================================
    private static void tableMenu(JFrame frame) {
        String[] options = {"📋 View Tables", "➕ Add Table"};
        int choice = JOptionPane.showOptionDialog(frame,
                "What would you like to do?", "Restaurant Tables",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            List<RestaurantTable> list = new TableDAO().getAllTables();
            Object[][] data = new Object[list.size()][3];
            for (int i = 0; i < list.size(); i++) {
                RestaurantTable t = list.get(i);
                data[i] = new Object[]{t.getId(), t.getTableNumber(), t.getStatus()};
            }
            showTableDialog(frame, "🪑 Restaurant Tables",
                    new String[]{"ID", "Table Number", "Status"}, data);

        } else if (choice == 1) {
            JTextField numberField = styledField();
            JTextField statusField = styledField();
            statusField.setText("Available");

            JPanel panel = formPanel(
                    formLabel("Table Number:"), numberField,
                    formLabel("Status:"), statusField);

            int result = JOptionPane.showConfirmDialog(frame, panel,
                    "Add New Table", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                try {
                    RestaurantTable table = new RestaurantTable(0,
                            Integer.parseInt(numberField.getText().trim()),
                            statusField.getText().trim());
                    new TableDAO().addTable(table);
                    JOptionPane.showMessageDialog(frame,
                            "Table added successfully! ✅");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter a valid table number.",
                            "Invalid Input", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // ============================================
    //  CUSTOMERS
    // ============================================
    private static void customerMenu(JFrame frame) {
        String[] options = {"📋 View Customers", "➕ Add Customer"};
        int choice = JOptionPane.showOptionDialog(frame,
                "What would you like to do?", "Customer Management",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            List<Customer> list = new CustomerDAO().getAllCustomers();
            Object[][] data = new Object[list.size()][3];
            for (int i = 0; i < list.size(); i++) {
                Customer c = list.get(i);
                data[i] = new Object[]{c.getId(), c.getName(), c.getPhone()};
            }
            showTableDialog(frame, "👤 Customer Details",
                    new String[]{"ID", "Name", "Phone"}, data);

        } else if (choice == 1) {
            JTextField nameField = styledField();
            JTextField phoneField = styledField();

            JPanel panel = formPanel(
                    formLabel("Customer Name:"), nameField,
                    formLabel("Phone Number:"), phoneField);

            int result = JOptionPane.showConfirmDialog(frame, panel,
                    "Add New Customer", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                if (name.isEmpty() || phone.isEmpty()) {
                    JOptionPane.showMessageDialog(frame,
                            "Please enter all details.");
                } else {
                    new CustomerDAO().addCustomer(new Customer(0, name, phone));
                    JOptionPane.showMessageDialog(frame,
                            "Customer added successfully! ✅");
                }
            }
        }
    }

    // ============================================
    //  ORDERS & BILLING
    // ============================================
    private static void orderMenu(JFrame frame) {
        String[] options = {"📋 View Orders", "➕ Add New Order", "🧾 Generate Bill"};
        int choice = JOptionPane.showOptionDialog(frame,
                "What would you like to do?", "Orders & Billing",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);

        if (choice == 0) {
            List<Order> list = new OrderDAO().getAllOrders();
            Object[][] data = new Object[list.size()][5];
            double grandTotal = 0;
            for (int i = 0; i < list.size(); i++) {
                Order o = list.get(i);
                data[i] = new Object[]{o.getId(), o.getTableNumber(),
                        o.getFoodName(), o.getQuantity(), "₹" + o.getTotalAmount()};
                grandTotal += o.getTotalAmount();
            }

            if (data.length == 0) {
                JOptionPane.showMessageDialog(frame,
                        "No orders found yet.", "Orders & Billing",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            JTable table = styledTable(data,
                    new String[]{"ID", "Table", "Food", "Qty", "Amount"});
            JLabel totalLabel = new JLabel("   TOTAL BILL:  ₹" + grandTotal);
            totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
            totalLabel.setForeground(ACCENT);
            totalLabel.setBorder(new EmptyBorder(12, 0, 0, 0));

            JPanel wrap = new JPanel(new BorderLayout());
            wrap.setBackground(PANEL);
            JScrollPane scroll = new JScrollPane(table);
            scroll.setPreferredSize(new Dimension(540, 300));
            scroll.getViewport().setBackground(PANEL);
            wrap.add(scroll, BorderLayout.CENTER);
            wrap.add(totalLabel, BorderLayout.SOUTH);
            wrap.setBorder(new EmptyBorder(10, 10, 10, 10));

            JOptionPane.showMessageDialog(frame, wrap,
                    "🧾 Orders & Billing", JOptionPane.PLAIN_MESSAGE);

        } else if (choice == 1) {
            addOrderDialog(frame);
        } else if (choice == 2) {
            generateBill(frame);
        }
    }

    private static void addOrderDialog(JFrame frame) {
        List<Food> foods = new FoodDAO().getAllFood();

        if (foods.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Food menu is empty! Add food items first.",
                    "No Food Found", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String[] foodNames = new String[foods.size()];
        for (int i = 0; i < foods.size(); i++) foodNames[i] = foods.get(i).getName();

        JTextField tableField = styledField();
        JComboBox<String> foodCombo = new JComboBox<>(foodNames);
        foodCombo.setFont(UI_FONT);
        foodCombo.setBackground(FIELD_BG);
        foodCombo.setForeground(TEXT);
        JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 50, 1));
        quantitySpinner.setFont(UI_FONT);

        JLabel totalLabel = new JLabel("₹0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(ACCENT);

        Runnable updateTotal = () -> {
            try {
                int qty = (Integer) quantitySpinner.getValue();
                String selected = (String) foodCombo.getSelectedItem();
                for (Food f : foods) {
                    if (f.getName().equals(selected)) {
                        totalLabel.setText("₹" + (f.getPrice() * qty));
                        return;
                    }
                }
            } catch (Exception ignored) { }
        };
        foodCombo.addActionListener(e -> updateTotal.run());
        quantitySpinner.addChangeListener(e -> updateTotal.run());
        updateTotal.run();

        JPanel panel = formPanel(
                formLabel("Table Number:"), tableField,
                formLabel("Food Item:"), foodCombo,
                formLabel("Quantity:"), quantitySpinner,
                formLabel("Total Amount:"), totalLabel);

        int result = JOptionPane.showConfirmDialog(frame, panel,
                "Add New Order", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                int tableNumber = Integer.parseInt(tableField.getText().trim());
                String foodName = (String) foodCombo.getSelectedItem();
                int quantity = (Integer) quantitySpinner.getValue();

                double totalAmount = 0;
                for (Food f : foods) {
                    if (f.getName().equals(foodName)) {
                        totalAmount = f.getPrice() * quantity;
                        break;
                    }
                }

                new OrderDAO().addOrder(
                        new Order(0, tableNumber, foodName, quantity, totalAmount));

                JOptionPane.showMessageDialog(frame,
                        "Order added successfully! ✅\nBill: ₹" + totalAmount);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame,
                        "Please enter a valid table number.",
                        "Invalid Input", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ============================================
    //  SMART BILL GENERATOR  (GST + save as file)
    // ============================================
    private static void generateBill(JFrame frame) {
        JTextField tableField = styledField();
        JPanel inputPanel = formPanel(formLabel("Table Number:"), tableField);

        int r = JOptionPane.showConfirmDialog(frame, inputPanel,
                "Generate Bill", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (r != JOptionPane.OK_OPTION) return;

        int tableNo;
        try {
            tableNo = Integer.parseInt(tableField.getText().trim());
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame,
                    "Please enter a valid table number.",
                    "Invalid Input", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // collect orders of this table
        List<Order> tableOrders = new ArrayList<>();
        for (Order o : new OrderDAO().getAllOrders()) {
            if (o.getTableNumber() == tableNo) tableOrders.add(o);
        }

        if (tableOrders.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "No orders found for Table " + tableNo + "!",
                    "Generate Bill", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // build bill text
        String line   = "------------------------------------------";
        String thin   = "--------------------------------------------";
        StringBuilder bill = new StringBuilder();
        bill.append("\n");
        bill.append("              🍽️  RESTOCHEF\n");
        bill.append("        Restaurant Handling System\n");
        bill.append(line).append("\n");
        bill.append("        Bill for Table No: ").append(tableNo).append("\n");
        bill.append(line).append("\n");
        bill.append(String.format("  %-24s %-6s %10s%n", "Item", "Qty", "Amount"));
        bill.append(thin).append("\n");

        double subtotal = 0;
        for (Order o : tableOrders) {
            bill.append(String.format("  %-24s %-6s %10.2f%n",
                    o.getFoodName(), "x" + o.getQuantity(), o.getTotalAmount()));
            subtotal += o.getTotalAmount();
        }
        double gst = subtotal * 0.05;
        double grand = subtotal + gst;

        bill.append(thin).append("\n");
        bill.append(String.format("  %-32s %10.2f%n", "Subtotal:", subtotal));
        bill.append(String.format("  %-32s %10.2f%n", "GST (5%):", gst));
        bill.append(line).append("\n");
        bill.append(String.format("  %-32s %10.2f%n", "GRAND TOTAL:", grand));
        bill.append(line).append("\n");
        bill.append("        Thank you! Visit again 😊\n");

        JTextArea billArea = new JTextArea(bill.toString());
        billArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        billArea.setEditable(false);
        billArea.setBackground(PANEL);
        billArea.setForeground(TEXT);
        billArea.setCaretColor(TEXT);
        billArea.setBorder(new EmptyBorder(10, 12, 10, 12));

        JScrollPane scroll = new JScrollPane(billArea);
        scroll.setPreferredSize(new Dimension(430, 420));
        scroll.setBorder(BorderFactory.createLineBorder(BORDER, 1, true));

        String[] billOptions = {"💾 Save Bill", "Close"};
        int action = JOptionPane.showOptionDialog(frame, scroll,
                "🧾 Bill - Table " + tableNo,
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, billOptions, billOptions[0]);

        if (action == 0) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Save Bill");
            chooser.setSelectedFile(new File("Bill_Table" + tableNo + ".txt"));
            int sr = chooser.showSaveDialog(frame);
            if (sr == JFileChooser.APPROVE_OPTION) {
                try (FileWriter fw = new FileWriter(chooser.getSelectedFile())) {
                    fw.write(bill.toString());
                    JOptionPane.showMessageDialog(frame,
                            "Bill saved successfully! ✅\n\n" +
                                    chooser.getSelectedFile().getAbsolutePath(),
                            "Saved", JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(frame,
                            "Could not save bill: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}