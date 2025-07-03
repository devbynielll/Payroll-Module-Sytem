

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author danie
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import payrollsystem.PayrollData;
import payrollsystem.PayrollRecord;
import java.text.DecimalFormat;
import java.awt.*;
import javax.swing.table.DefaultTableModel;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import defaultpackage.PaySlip;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class EMP_Dashboard extends javax.swing.JFrame {

    PreparedStatement pst1;
    PreparedStatement pst2;
    PreparedStatement pst3;
    PreparedStatement pst4;
    ResultSet rs;
    Connection conn;

    private String currentUserID; // set this once when user logs in
    private Bank_Transfer bankTransferPage; // Reference to Bank_Transfer
    private boolean isIncomeVisible = true; // Track visibility of income
    private double totalIncome;

    public EMP_Dashboard(Bank_Transfer bankTransferPage) {

        this.conn = javaconnect.ConnectDb();

        this.bankTransferPage = bankTransferPage;
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state

        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        setTitle("Payroll Swift"); // ✅ Custom window title

        // Link the shared payroll data model to the payrollHistory_Database table
        payrollHistory_Database.setModel(PayrollData.payrollTableModel);

        notifications_table.setModel(SharedData.getNotificationTableModel());

        startDateTimeUpdater();

        currentUserID = getLatestLoggedInUserID();

        updateTotalIncomeDisplay(); // ✅ Ensures it displays correctly on reload
        updateLoggedInUserID();
        updateLeftPanelInfo();
        loadDashboardInfoFromLeftPanel();

        populatePayrollHistoryTable();
        loadSalaryNotifications();

    }

    public EMP_Dashboard() {
        this(null);
    }

    private void loadDashboardInfoFromLeftPanel() {
        try {
            // Extract EMP ID from "Employee ID: EMP143"
            String fullText = id_leftpanel.getText().trim();
            String empId = fullText.substring(fullText.lastIndexOf(":") + 1).trim(); // EMP143

            // --- Get Contact Number from PersonalData ---
            String sqlPersonal = "SELECT Contact_Num FROM PersonalData WHERE UserID = ?";
            try (PreparedStatement pst = conn.prepareStatement(sqlPersonal)) {
                pst.setString(1, empId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        dashboard_ewalletnum.setText("0" + rs.getString("Contact_Num"));
                    } else {
                        dashboard_ewalletnum.setText("");
                    }
                }
            }

            // --- Get Bank Account and Bank Name from BankDetails ---
            String sqlBank = "SELECT AccountNum, BankName FROM BankDetails WHERE UserID = ?";
            try (PreparedStatement pst = conn.prepareStatement(sqlBank)) {
                pst.setString(1, empId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        String rawNumber = rs.getString("AccountNum"); // e.g. "1111111111111111"
                        String formattedNumber = rawNumber.replaceAll(".{4}(?!$)", "$0 ");
                        dashboard_bankaccnum.setText(formattedNumber);

                        dashboard_bankname.setText(rs.getString("BankName"));
                    } else {
                        dashboard_bankaccnum.setText("");
                        dashboard_bankname.setText("");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load dashboard info.", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Invalid Employee ID format.", "Format Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateLoggedInUserID() {
        String userId = null;

        String sql = "SELECT UserID FROM Login ORDER BY LoginTime DESC LIMIT 1";

        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                userId = rs.getString("UserID");
            }

            if (userId != null) {
                this.currentUserID = userId;
                id_leftpanel.setText("Employee ID: " + userId);
            } else {
                currentUserID = null;
                id_leftpanel.setText("Employee ID: N/A");
            }
        } catch (SQLException e) {
            System.err.println("Failed to fetch logged in user: " + e.getMessage());
            id_leftpanel.setText("Employee ID: Error");
        }
    }

    public String getLatestLoggedInUserID() {
        String latestUserID = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT UserID FROM Login ORDER BY TimeStamp DESC LIMIT 1";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            if (rs.next()) {
                latestUserID = rs.getString("UserID");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching latest logged-in user: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return latestUserID;
    }

    public String getCurrentEmployeeID() {
        String labelText = id_leftpanel.getText(); // "Employee ID: EMP143"
        if (labelText != null && labelText.startsWith("Employee ID: ")) {
            return labelText.substring("Employee ID: ".length()).trim();
        }
        return null; // or "" if you prefer
    }

    public void setCurrentUserID(String userID) {

        this.currentUserID = userID;
        updateTotalIncomeDisplay();  // update income display after setting user
    }

    public void updateTotalIncomeDisplay() {
        if (currentUserID == null) {
            currentUserID = getLatestLoggedInUserID();
        }

        if (currentUserID == null) {
            System.out.println("Skipping update: currentUserID is null");
            return;
        }

        ResultSet rs = null;
        PreparedStatement pst = null;

        System.out.println("Updating total income for userID: " + currentUserID);

        try {
            String sql = "SELECT TotalIncome FROM EmployeeIncome WHERE UserID = ? ORDER BY TimeStamp DESC LIMIT 1";
            pst = conn.prepareStatement(sql);
            pst.setString(1, currentUserID);

            rs = pst.executeQuery();

            if (rs.next()) {
                double storedIncome = rs.getDouble("TotalIncome");
                SharedData.setTotalIncome(storedIncome);

                DecimalFormat df = new DecimalFormat("#,##0.00");
                totalincome.setText("₱" + df.format(storedIncome));
            } else {
                SharedData.setTotalIncome(0.00);
                DecimalFormat df = new DecimalFormat("#,##0.00");
                totalincome.setText("₱" + df.format(0.00));
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error retrieving total income: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
        }
    }

    private void populatePayrollHistoryTable() {
        String rawID = id_leftpanel.getText().trim(); // Example: "Employee ID: EMP143"
        String userID = rawID.replace("Employee ID: ", "").trim();

        PreparedStatement pstHistory = null;
        PreparedStatement pstSalary = null;
        ResultSet rsHistory = null;
        ResultSet rsSalary = null;

        DefaultTableModel model = (DefaultTableModel) payrollHistory_Database.getModel();
        model.setRowCount(0); // Clear existing data

        try {
            // ✅ Get Payroll Period from PayrollHistory
            String sqlHistory = "SELECT PayrollPeriod FROM PayrollHistory WHERE UserID = ? ORDER BY TimeStamp DESC";
            pstHistory = conn.prepareStatement(sqlHistory);
            pstHistory.setString(1, userID);
            rsHistory = pstHistory.executeQuery();

            // ✅ Get GrossPay and TotalDeduc from SentSalary
            String sqlSalary = "SELECT GrossPay, TotalDeduc FROM SentSalary WHERE UserID = ? ORDER BY TimeStamp  DESC";
            pstSalary = conn.prepareStatement(sqlSalary);
            pstSalary.setString(1, userID);
            rsSalary = pstSalary.executeQuery();

            // ✅ Populate rows in the JTable
            while (rsHistory.next() && rsSalary.next()) {
                String payrollPeriod = rsHistory.getString("PayrollPeriod");
                String payDate = java.time.LocalDate.now().toString(); // Current date
                double grossPay = rsSalary.getDouble("GrossPay");
                double deductions = rsSalary.getDouble("TotalDeduc");
                double netPay = grossPay - deductions;
                String payMethod = "Bank Transfer";
                String status = "Success";

                model.addRow(new Object[]{
                    payrollPeriod, payDate, grossPay, deductions, netPay, payMethod, status
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error populating payroll history: " + e.getMessage());
        } finally {
            try {
                if (rsHistory != null) {
                    rsHistory.close();
                }
                if (rsSalary != null) {
                    rsSalary.close();
                }
                if (pstHistory != null) {
                    pstHistory.close();
                }
                if (pstSalary != null) {
                    pstSalary.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    public void loadSalaryNotifications() {
        String rawID = id_leftpanel.getText().trim(); // Example: "Employee ID: EMP143"
        String userID = rawID.replace("Employee ID: ", "").trim();

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT TimeStamp, NetPay FROM SentSalary WHERE UserID = ? ORDER BY TimeStamp DESC";
            pst = conn.prepareStatement(sql);
            pst.setString(1, userID);
            rs = pst.executeQuery();

            DefaultTableModel model = (DefaultTableModel) notifications_table.getModel();
            model.setRowCount(0); // Clear existing notifications

            while (rs.next()) {
                String dateTime = rs.getString("TimeStamp");
                double netPay = rs.getDouble("NetPay");
                String message = "₱" + netPay + " has been deposited.";

                model.addRow(new Object[]{dateTime, message});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to load salary notifications: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    private void updateLeftPanelInfo() {
        // Extract the UserID from id_leftpanel (e.g., "Employee ID: EMP143" -> EMP143)
        // Get the UserID from id_leftpanel, e.g., "Employee ID: EMP143"
        String rawID = id_leftpanel.getText().trim();
        String userID = rawID.replace("Employee ID: ", "").trim();

        PreparedStatement pstPersonal = null;
        PreparedStatement pstEmployment = null;
        ResultSet rs = null;

        try {
            // ✅ PersonalData Query
            String personalSQL = "SELECT FirstName, LastName, DOB, Contact_Num, EmailAdd FROM PersonalData WHERE UserID = ?";
            pstPersonal = conn.prepareStatement(personalSQL);
            pstPersonal.setString(1, userID);
            rs = pstPersonal.executeQuery();

            if (rs.next()) {
                String fullName = rs.getString("FirstName") + " " + rs.getString("LastName");
                name_leftpanel.setText("Name: " + fullName);
                birthday_leftpanel.setText("Birthday: " + rs.getString("DOB"));
                contact_leftpanel.setText("Contact: " + rs.getString("Contact_Num"));
                email_leftpanel.setText("Email: " + rs.getString("EmailAdd"));
            }
            rs.close();
            pstPersonal.close();

            // ✅ EmploymentInformation Query
            String employmentSQL = "SELECT JobPos, Department, DateHired, EmpStat FROM EmploymentInformation WHERE UserID = ?";
            pstEmployment = conn.prepareStatement(employmentSQL);
            pstEmployment.setString(1, userID);
            rs = pstEmployment.executeQuery();

            if (rs.next()) {
                jobposition_leftpanel.setText("Job Position: " + rs.getString("JobPos"));
                department_leftpanel.setText("Department: " + rs.getString("Department"));
                datehired_leftpanel.setText("Date Hired: " + rs.getString("DateHired"));
                empstatus_leftpanel.setText("Employment Status: " + rs.getString("EmpStat"));
                payschedule_leftpanel.setText("Pay Schedule: Monthly");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching employee info: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstPersonal != null) {
                    pstPersonal.close();
                }
                if (pstEmployment != null) {
                    pstEmployment.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public void setTotalIncome(double income) {
        this.totalIncome = income;
        if (totalincome != null) {
            totalincome.setText("₱" + String.format("%,.2f", income));
        }
    }

// Deduct a specified amount from total income
    public void deductFromTotalIncome(double amount) {
        if (SharedData.getTotalIncome() >= amount) {
            SharedData.setTotalIncome(SharedData.getTotalIncome() - amount);
        } else {
            System.out.println("Error: Insufficient funds!");
        }
        updateTotalIncomeDisplay();

    }

    private void startDateTimeUpdater() {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MMMM dd, yyyy"); // Example: May 16, 2025
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm:ss a");    // Example: 03:45:20 PM

        Timer timer = new Timer(1000, new ActionListener() { // Update every second
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDateTime now = LocalDateTime.now();
                currentdate.setText(dateFormat.format(now));
                currenttime.setText(timeFormat.format(now));
            }
        });
        timer.start();
    }

    public Bank_Transfer getBankTransferPage() {
        return bankTransferPage;
    }

    public String getTotalIncomeText() {
        return "₱" + formatMoney(SharedData.getTotalIncome());
    }

    // Helper methods for parsing and formatting money
    private String formatMoney(double amount) {
        return String.format("%,.2f", amount);
    }

    private double parseMoney(String amount) {
        try {
            return Double.parseDouble(amount.replace("₱", "").replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    // Toggle visibility of total income
    private void toggleIncomeVisibility() {
        if (isIncomeVisible) {
            // Hide the income (replace with asterisks)
            totalincome.setText("₱***,***");
        } else {
            // Show the actual income (fetch from SharedData)
            updateTotalIncomeDisplay();
        }
        isIncomeVisible = !isIncomeVisible; // Toggle visibility state
    }

    public void setInitialTotalIncome(double income) {
        SharedData.setTotalIncome(income); // Set the initial total income in SharedData
        updateTotalIncomeDisplay(); // Update the UI
    }

    private double currentTotalIncome = 0.0; // Track the current total income

    public void setBankTransferPage(Bank_Transfer bankTransferPage) {
        this.bankTransferPage = bankTransferPage;
    }

    public void addPayrollHistory(String payrollPeriod, String payDate, String grossPay, String totalDeductions, String netPay, String payMethod, String status) {
        DefaultTableModel model = (DefaultTableModel) payrollHistory_Database.getModel();
        model.addRow(new Object[]{payrollPeriod, payDate, grossPay, totalDeductions, netPay, payMethod, status});

        // Recalculate total income after adding a new row
        double netPayValue = parseMoney(netPay);
        SharedData.setTotalIncome(SharedData.getTotalIncome() + netPayValue);
        updateTotalIncomeDisplay();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        employePicture = new javax.swing.JLabel();
        name_leftpanel = new javax.swing.JLabel();
        id_leftpanel = new javax.swing.JLabel();
        birthday_leftpanel = new javax.swing.JLabel();
        contact_leftpanel = new javax.swing.JLabel();
        email_leftpanel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jobposition_leftpanel = new javax.swing.JLabel();
        department_leftpanel = new javax.swing.JLabel();
        datehired_leftpanel = new javax.swing.JLabel();
        empstatus_leftpanel = new javax.swing.JLabel();
        payschedule_leftpanel = new javax.swing.JLabel();
        logout_btn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        employeedashboard_title = new javax.swing.JLabel();
        incomePanel = new RoundedPanel(15, new Color(0, 51, 51));
        totalincome_TXTLABEL = new javax.swing.JLabel();
        totalincome = new javax.swing.JLabel();
        eyeHide = new javax.swing.JLabel();
        ewalletPanel = new javax.swing.JPanel();
        ewallet_TXTLABEL = new javax.swing.JLabel();
        dashboard_ewalletnum = new javax.swing.JLabel();
        ewallet_platform = new javax.swing.JLabel();
        banktransferPanel = new javax.swing.JPanel();
        banktransfer_TXTLABEL = new javax.swing.JLabel();
        dashboard_bankaccnum = new javax.swing.JLabel();
        dashboard_bankname = new javax.swing.JLabel();
        notifications = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        notifications_table = new javax.swing.JTable();
        payrollHistory_txt = new javax.swing.JLabel();
        notification_TXTLABEL = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        payrollHistory_Database = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        currenttime = new javax.swing.JLabel();
        currentdate = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jMenu1.setText("jMenu1");

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        employePicture.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        employePicture.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AIAH 1X1.jpg"))); // NOI18N

        name_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        name_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        name_leftpanel.setText("Name: ");

        id_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        id_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        id_leftpanel.setText("Employee ID: ");

        birthday_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        birthday_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        birthday_leftpanel.setText("Birthday: ");

        contact_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        contact_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        contact_leftpanel.setText("Contact: ");

        email_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        email_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        email_leftpanel.setText("Email: ");

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));

        jobposition_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        jobposition_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        jobposition_leftpanel.setText("Job Position: ");

        department_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        department_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        department_leftpanel.setText("Department: ");

        datehired_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        datehired_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        datehired_leftpanel.setText("Date Hired: ");

        empstatus_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        empstatus_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        empstatus_leftpanel.setText("Emp. Status: ");

        payschedule_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        payschedule_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        payschedule_leftpanel.setText("Pay Schedule: ");

        logout_btn.setBackground(new java.awt.Color(0, 51, 51));
        logout_btn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        logout_btn.setForeground(new java.awt.Color(255, 255, 255));
        logout_btn.setText("Logout");
        logout_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout_btnActionPerformed(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AYROLL WIFT (2) (3).png"))); // NOI18N
        jLabel2.setText(" ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addComponent(employePicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(name_leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(id_leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(email_leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(contact_leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(birthday_leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(logout_btn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                            .addComponent(jobposition_leftpanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(datehired_leftpanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(empstatus_leftpanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(payschedule_leftpanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(department_leftpanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(0, 10, Short.MAX_VALUE)))
                .addGap(13, 13, 13))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(employePicture, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(name_leftpanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(id_leftpanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(birthday_leftpanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contact_leftpanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(email_leftpanel)
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jobposition_leftpanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(department_leftpanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(datehired_leftpanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(empstatus_leftpanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(payschedule_leftpanel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 101, Short.MAX_VALUE)
                .addComponent(logout_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        employeedashboard_title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        employeedashboard_title.setForeground(new java.awt.Color(0, 0, 0));
        employeedashboard_title.setText("EMPLOYEE DASHBOARD");

        incomePanel.setBackground(new java.awt.Color(0, 51, 51));
        incomePanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.gray, java.awt.Color.gray));

        totalincome_TXTLABEL.setBackground(new java.awt.Color(255, 255, 255));
        totalincome_TXTLABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        totalincome_TXTLABEL.setForeground(new java.awt.Color(255, 255, 255));
        totalincome_TXTLABEL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalincome_TXTLABEL.setText("Total Income");

        totalincome.setFont(new java.awt.Font("Segoe UI Semibold", 0, 30)); // NOI18N
        totalincome.setForeground(new java.awt.Color(255, 255, 255));
        totalincome.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        totalincome.setText("₱***,***");

        eyeHide.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eyeHide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image (2).png"))); // NOI18N
        eyeHide.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image (2).png"))); // Ensure the path is correct
        eyeHide.setToolTipText("Click to hide/show income");
        eyeHide.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                toggleIncomeVisibility();
            }
        });

        javax.swing.GroupLayout incomePanelLayout = new javax.swing.GroupLayout(incomePanel);
        incomePanel.setLayout(incomePanelLayout);
        incomePanelLayout.setHorizontalGroup(
            incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(incomePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalincome, javax.swing.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                    .addGroup(incomePanelLayout.createSequentialGroup()
                        .addComponent(totalincome_TXTLABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(eyeHide, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        incomePanelLayout.setVerticalGroup(
            incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(incomePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(incomePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(totalincome_TXTLABEL)
                    .addComponent(eyeHide, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(totalincome, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ewalletPanel.setBackground(new java.awt.Color(0, 51, 51));
        ewalletPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.gray, java.awt.Color.gray));
        ewalletPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ewalletPanelMouseClicked(evt);
            }
        });

        ewallet_TXTLABEL.setBackground(new java.awt.Color(255, 255, 255));
        ewallet_TXTLABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        ewallet_TXTLABEL.setForeground(new java.awt.Color(255, 255, 255));
        ewallet_TXTLABEL.setText("E-Wallet");

        dashboard_ewalletnum.setFont(new java.awt.Font("Segoe UI Historic", 1, 19)); // NOI18N
        dashboard_ewalletnum.setForeground(new java.awt.Color(255, 255, 255));
        dashboard_ewalletnum.setText("0919-392-8186");

        ewallet_platform.setFont(new java.awt.Font("Segoe UI Symbol", 1, 13)); // NOI18N
        ewallet_platform.setForeground(new java.awt.Color(255, 255, 255));
        ewallet_platform.setText("GCASH");

        javax.swing.GroupLayout ewalletPanelLayout = new javax.swing.GroupLayout(ewalletPanel);
        ewalletPanel.setLayout(ewalletPanelLayout);
        ewalletPanelLayout.setHorizontalGroup(
            ewalletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ewalletPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ewalletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ewalletPanelLayout.createSequentialGroup()
                        .addComponent(ewallet_TXTLABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(150, Short.MAX_VALUE))
                    .addGroup(ewalletPanelLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(ewalletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ewallet_platform, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(ewalletPanelLayout.createSequentialGroup()
                                .addComponent(dashboard_ewalletnum, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        ewalletPanelLayout.setVerticalGroup(
            ewalletPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ewalletPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ewallet_TXTLABEL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashboard_ewalletnum, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(ewallet_platform)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        banktransferPanel.setBackground(new java.awt.Color(0, 51, 51));
        banktransferPanel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.gray, java.awt.Color.gray));
        banktransferPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                banktransferPanelMouseClicked(evt);
            }
        });

        banktransfer_TXTLABEL.setBackground(new java.awt.Color(255, 255, 255));
        banktransfer_TXTLABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        banktransfer_TXTLABEL.setForeground(new java.awt.Color(255, 255, 255));
        banktransfer_TXTLABEL.setText("Bank Transfer");

        dashboard_bankaccnum.setFont(new java.awt.Font("Segoe UI Historic", 1, 18)); // NOI18N
        dashboard_bankaccnum.setForeground(new java.awt.Color(255, 255, 255));
        dashboard_bankaccnum.setText("1200 2300 3400 1234");

        dashboard_bankname.setFont(new java.awt.Font("Segoe UI Symbol", 1, 13)); // NOI18N
        dashboard_bankname.setForeground(new java.awt.Color(255, 255, 255));
        dashboard_bankname.setText("PNB");

        javax.swing.GroupLayout banktransferPanelLayout = new javax.swing.GroupLayout(banktransferPanel);
        banktransferPanel.setLayout(banktransferPanelLayout);
        banktransferPanelLayout.setHorizontalGroup(
            banktransferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(banktransferPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(banktransfer_TXTLABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(110, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, banktransferPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(banktransferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dashboard_bankname, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(banktransferPanelLayout.createSequentialGroup()
                        .addComponent(dashboard_bankaccnum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        banktransferPanelLayout.setVerticalGroup(
            banktransferPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(banktransferPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(banktransfer_TXTLABEL)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashboard_bankaccnum, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(dashboard_bankname)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        notifications.setBackground(new java.awt.Color(0, 102, 102));

        notifications_table.setBackground(new java.awt.Color(0, 102, 102));
        notifications_table.setForeground(new java.awt.Color(255, 255, 255));
        notifications_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Date / Time", "Subject"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        notifications_table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        notifications_table.setEnabled(false);
        notifications_table.setGridColor(new java.awt.Color(0, 115, 115));
        notifications_table.setRowHeight(50);
        notifications_table.setShowGrid(false);
        notifications_table.setShowHorizontalLines(true);
        jScrollPane3.setViewportView(notifications_table);
        if (notifications_table.getColumnModel().getColumnCount() > 0) {
            notifications_table.getColumnModel().getColumn(0).setResizable(false);
            notifications_table.getColumnModel().getColumn(0).setPreferredWidth(100);
            notifications_table.getColumnModel().getColumn(1).setResizable(false);
            notifications_table.getColumnModel().getColumn(1).setPreferredWidth(200);
        }

        javax.swing.GroupLayout notificationsLayout = new javax.swing.GroupLayout(notifications);
        notifications.setLayout(notificationsLayout);
        notificationsLayout.setHorizontalGroup(
            notificationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notificationsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 389, Short.MAX_VALUE)
                .addContainerGap())
        );
        notificationsLayout.setVerticalGroup(
            notificationsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notificationsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 643, Short.MAX_VALUE)
                .addContainerGap())
        );

        payrollHistory_txt.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        payrollHistory_txt.setForeground(new java.awt.Color(0, 0, 0));
        payrollHistory_txt.setText("Payroll History");

        notification_TXTLABEL.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        notification_TXTLABEL.setForeground(new java.awt.Color(0, 0, 0));
        notification_TXTLABEL.setText("Notifications");

        jPanel3.setBackground(new java.awt.Color(0, 51, 51));

        payrollHistory_Database.setBackground(new java.awt.Color(0, 51, 51));
        payrollHistory_Database.setForeground(new java.awt.Color(255, 255, 255));
        payrollHistory_Database.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Payroll Period", "Pay Date", "Gross Pay", "Deductions", "Net Pay", "Pay Method", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        payrollHistory_Database.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        payrollHistory_Database.setEnabled(false);
        payrollHistory_Database.setGridColor(new java.awt.Color(0, 65, 65));
        payrollHistory_Database.setRowHeight(50);
        payrollHistory_Database.setSelectionForeground(new java.awt.Color(102, 102, 102));
        payrollHistory_Database.setShowHorizontalLines(true);
        payrollHistory_Database.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                payrollHistory_DatabaseMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(payrollHistory_Database);
        if (payrollHistory_Database.getColumnModel().getColumnCount() > 0) {
            payrollHistory_Database.getColumnModel().getColumn(0).setResizable(false);
            payrollHistory_Database.getColumnModel().getColumn(0).setPreferredWidth(150);
            payrollHistory_Database.getColumnModel().getColumn(1).setResizable(false);
            payrollHistory_Database.getColumnModel().getColumn(2).setResizable(false);
            payrollHistory_Database.getColumnModel().getColumn(3).setResizable(false);
            payrollHistory_Database.getColumnModel().getColumn(4).setResizable(false);
            payrollHistory_Database.getColumnModel().getColumn(5).setResizable(false);
            payrollHistory_Database.getColumnModel().getColumn(5).setPreferredWidth(100);
            payrollHistory_Database.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jLabel1.setFont(new java.awt.Font("Segoe UI", 2, 13)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel1.setText("Click the table to view your PaySlip.");

        currenttime.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        currenttime.setForeground(new java.awt.Color(0, 0, 0));
        currenttime.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        currenttime.setText("00:00:00");

        currentdate.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        currentdate.setForeground(new java.awt.Color(0, 0, 0));
        currentdate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        currentdate.setText("APRIL 12, 2025");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(currentdate, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(currenttime, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(incomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(ewalletPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(banktransferPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addComponent(payrollHistory_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(13, 13, 13))
                                .addComponent(employeedashboard_title, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(notification_TXTLABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(notifications, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jSeparator1)))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currenttime, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currentdate, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(employeedashboard_title, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(notification_TXTLABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ewalletPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(banktransferPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(incomePanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(payrollHistory_txt, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(notifications, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void logout_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout_btnActionPerformed

        // Display a confirmation dialog
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Confirm Logout", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (response == JOptionPane.YES_OPTION) {
            // Save state to SharedData
            SharedData.setTotalIncome(SharedData.getTotalIncome());
            SharedData.setCurrentBalance(SharedData.getCurrentBalance());

            this.dispose(); // Close the current window
            new Login().setVisible(true); // Open the login window
        }
        // If NO_OPTION is selected, the dialog will simply close and no further action will be taken

    }//GEN-LAST:event_logout_btnActionPerformed


    private void banktransferPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_banktransferPanelMouseClicked
        openBankTransferPage(); // Navigate to Bank_Transfer page
    }//GEN-LAST:event_banktransferPanelMouseClicked

    private void ewalletPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ewalletPanelMouseClicked
        // TODO add your handling code here:
        EWallet_Page eWalletPage = new EWallet_Page(this); // Create an instance of EWallet_Page
        eWalletPage.setVisible(true); // Make the EWallet_Page visible
        this.dispose(); // Close the current page (optional)
    }//GEN-LAST:event_ewalletPanelMouseClicked

    private void payrollHistory_DatabaseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_payrollHistory_DatabaseMouseClicked

        PaySlip payslip = new PaySlip(conn);
        payslip.setVisible(true);
    }//GEN-LAST:event_payrollHistory_DatabaseMouseClicked

    private void openBankTransferPage() {
        if (bankTransferPage == null) {
            bankTransferPage = new Bank_Transfer(this); // Pass this EMP_Dashboard instance
        }
        bankTransferPage.setVisible(true); // Show Bank_Transfer page
        this.dispose(); // Close the current EMP_Dashboard
    }

    public static void main(String args[]) {

        java.awt.EventQueue.invokeLater(() -> {
            // Create a Bank_Transfer instance
            EMP_Dashboard empDashboard = new EMP_Dashboard(null);
            empDashboard.setInitialTotalIncome(0.0); // Example: Set initial income

            // Create a Bank_Transfer instance and pass the EMP_Dashboard reference
            Bank_Transfer bankTransfer = new Bank_Transfer(empDashboard);

            // Set the Bank_Transfer reference in EMP_Dashboard
            empDashboard.setBankTransferPage(bankTransfer);

            // Display the EMP_Dashboard
            empDashboard.setVisible(true);
        });
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel banktransferPanel;
    private javax.swing.JLabel banktransfer_TXTLABEL;
    private javax.swing.JLabel birthday_leftpanel;
    private javax.swing.JLabel contact_leftpanel;
    private javax.swing.JLabel currentdate;
    private javax.swing.JLabel currenttime;
    private javax.swing.JLabel dashboard_bankaccnum;
    private javax.swing.JLabel dashboard_bankname;
    private javax.swing.JLabel dashboard_ewalletnum;
    private javax.swing.JLabel datehired_leftpanel;
    private javax.swing.JLabel department_leftpanel;
    private javax.swing.JLabel email_leftpanel;
    private javax.swing.JLabel employePicture;
    private javax.swing.JLabel employeedashboard_title;
    private javax.swing.JLabel empstatus_leftpanel;
    private javax.swing.JPanel ewalletPanel;
    private javax.swing.JLabel ewallet_TXTLABEL;
    private javax.swing.JLabel ewallet_platform;
    private javax.swing.JLabel eyeHide;
    private javax.swing.JLabel id_leftpanel;
    private javax.swing.JPanel incomePanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel jobposition_leftpanel;
    private javax.swing.JButton logout_btn;
    private javax.swing.JLabel name_leftpanel;
    private javax.swing.JLabel notification_TXTLABEL;
    private javax.swing.JPanel notifications;
    private javax.swing.JTable notifications_table;
    private javax.swing.JTable payrollHistory_Database;
    private javax.swing.JLabel payrollHistory_txt;
    private javax.swing.JLabel payschedule_leftpanel;
    private javax.swing.JLabel totalincome;
    private javax.swing.JLabel totalincome_TXTLABEL;
    // End of variables declaration//GEN-END:variables
}
