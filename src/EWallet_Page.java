/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.awt.Color;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author danie
 */
public class EWallet_Page extends javax.swing.JFrame {

    private EMP_Dashboard empDashboard; // Reference to EMP_Dashboard
    Connection conn;

    public EWallet_Page(EMP_Dashboard empDashboard) {

        this.conn = javaconnect.ConnectDb();

        this.empDashboard = empDashboard;
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state

        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        setTitle("Payroll Swift"); // ✅ Custom window title

        navigation_ewallet.setEnabled(false);
        navigation_ewallet.setForeground(Color.GRAY);

        // Ensure balance starts at ₱0
        if (SharedData.getCurrentBalance() == 0.0) {
            SharedData.setCurrentBalance(0.0);
        }

        updateBalanceDisplay();
        ewallet_database.setModel(SharedData.transactionTableModel); // Link transaction table

        loadLatestUserBalance();
        loadLatestUserDetails();
        populateEwalletTransactionHistoryTable();

    }

    public void populateEwalletTransactionHistoryTable() {
        PreparedStatement pstLogin = null;
        PreparedStatement pstEwallet = null;
        ResultSet rsLogin = null;
        ResultSet rsEwallet = null;

        DefaultTableModel model = (DefaultTableModel) ewallet_database.getModel(); // Replace with your JTable variable name
        model.setRowCount(0); // Clear existing rows

        try {
            // Step 1: Get the latest logged-in UserID from Login table
            String loginQuery = "SELECT UserID FROM Login ORDER BY LoginTime DESC LIMIT 1";
            pstLogin = conn.prepareStatement(loginQuery);
            rsLogin = pstLogin.executeQuery();

            if (!rsLogin.next()) {
                JOptionPane.showMessageDialog(this, "No login records found.");
                return;
            }

            String userID = rsLogin.getString("UserID");

            // Step 2: Fetch e-wallet transactions for this UserID
            String ewalletQuery = "SELECT Date, REF, RecipName, RecipNum, AccNum, Amount, Status "
                    + "FROM EwalletTable WHERE UserID = ? ORDER BY Date DESC";
            pstEwallet = conn.prepareStatement(ewalletQuery);
            pstEwallet.setString(1, userID);
            rsEwallet = pstEwallet.executeQuery();

            while (rsEwallet.next()) {
                String date = rsEwallet.getString("Date");
                String ref = rsEwallet.getString("REF");
                String recipName = rsEwallet.getString("RecipName");
                String recipNum = rsEwallet.getString("RecipNum");
                String accNum = rsEwallet.getString("AccNum");
                double amount = rsEwallet.getDouble("Amount");
                String status = rsEwallet.getString("Status");

                model.addRow(new Object[]{
                    date, ref, recipName, recipNum, accNum, "₱" + String.format("%,.2f", amount), status
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading e-wallet transaction history: " + e.getMessage());
        } finally {
            try {
                if (rsLogin != null) {
                    rsLogin.close();
                }
                if (rsEwallet != null) {
                    rsEwallet.close();
                }
                if (pstLogin != null) {
                    pstLogin.close();
                }
                if (pstEwallet != null) {
                    pstEwallet.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    public void updateBalance(double amount) {
        SharedData.addToCurrentBalance(amount); // Add cash-in amount to e-wallet balance
        updateBalanceDisplay(); // Refresh the UI to show the updated balance
    }

    public void updateBalanceDisplay() {
        balance.setText("₱" + String.format("%,.2f", SharedData.getCurrentBalance()));
    }

    private String formatMoney(double amount) {
        return String.format("%,.2f", amount);
    }

    private double parseMoney(String amount) {
        try {
            return Double.parseDouble(amount.replace("₱", "").replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0; // Default to 0.0 if parsing fails
        }
    }

    private void loadLatestUserBalance() {
        String latestUserId = "";
        String sql = "SELECT UserID FROM Login ORDER BY LoginTime DESC LIMIT 1";

        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                latestUserId = rs.getString("UserID");

                // Fetch CashedIn value from EWallet_Cash for the latest user
                String balanceSql = "SELECT CashedIn FROM EWallet_Cash WHERE UserID = ?";
                try (PreparedStatement balancePst = conn.prepareStatement(balanceSql)) {
                    balancePst.setString(1, latestUserId);
                    try (ResultSet balanceRs = balancePst.executeQuery()) {
                        if (balanceRs.next()) {
                            double cashedIn = balanceRs.getDouble("CashedIn");
                            SharedData.setCurrentBalance(cashedIn);
                        } else {
                            SharedData.setCurrentBalance(0.0); // No balance found
                        }
                    }
                }
            } else {
                SharedData.setCurrentBalance(0.0); // No logged-in user
            }

            updateBalanceDisplay(); // Update the label regardless

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to load balance from EWallet_Cash.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadLatestUserDetails() {
        String latestUserId = "";
        String sqlLogin = "SELECT UserID FROM Login ORDER BY LoginTime DESC LIMIT 1";

        try (PreparedStatement pstLogin = conn.prepareStatement(sqlLogin); ResultSet rsLogin = pstLogin.executeQuery()) {

            if (rsLogin.next()) {
                latestUserId = rsLogin.getString("UserID");

                // Query to get FirstName, LastName, Contact_Num from PersonalData
                String sqlPersonal = "SELECT FirstName, LastName, Contact_Num FROM PersonalData WHERE UserID = ?";
                try (PreparedStatement pstPersonal = conn.prepareStatement(sqlPersonal)) {
                    pstPersonal.setString(1, latestUserId);
                    try (ResultSet rsPersonal = pstPersonal.executeQuery()) {
                        if (rsPersonal.next()) {
                            String firstName = rsPersonal.getString("FirstName");
                            String lastName = rsPersonal.getString("LastName");
                            String phoneNumber = rsPersonal.getString("Contact_Num");

                            // Set full name as "FirstName LastName"
                            ewallet_fullname.setText(firstName + " " + lastName);
                            ewallet_phonenumber.setText("0" + phoneNumber);
                        } else {
                            // No personal data found for user
                            ewallet_fullname.setText("");
                            ewallet_phonenumber.setText("");
                        }
                    }
                }

            } else {
                // No user logged in found
                ewallet_fullname.setText("");
                ewallet_phonenumber.setText("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch latest user details.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        leftpanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        credits = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        navigation_banktransfer = new javax.swing.JLabel();
        navigation_ewallet = new javax.swing.JLabel();
        navigation_dashboard = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        returnto_banktransferpage = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        ewallet_title = new javax.swing.JLabel();
        ewallet_panel = new javax.swing.JPanel();
        availbalance_label = new javax.swing.JLabel();
        balance = new javax.swing.JLabel();
        send = new javax.swing.JButton();
        ewallet_fullname = new javax.swing.JLabel();
        ewallet_phonenumber = new javax.swing.JLabel();
        cashin = new javax.swing.JButton();
        transferhistory_label = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ewallet_database = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        leftpanel.setBackground(new java.awt.Color(0, 51, 51));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AYROLL WIFT (2) (3).png"))); // NOI18N
        jLabel2.setText(" ");

        credits.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        credits.setForeground(new java.awt.Color(255, 255, 255));
        credits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        credits.setText("© 2025 Payroll Swift");

        jSeparator5.setForeground(new java.awt.Color(255, 255, 255));

        navigation_banktransfer.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_banktransfer.setForeground(new java.awt.Color(255, 255, 255));
        navigation_banktransfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bank smol.png"))); // NOI18N
        navigation_banktransfer.setText("  Bank Transfer");
        navigation_banktransfer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_banktransferMouseClicked(evt);
            }
        });

        navigation_ewallet.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_ewallet.setForeground(new java.awt.Color(255, 255, 255));
        navigation_ewallet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image (1) (1).png"))); // NOI18N
        navigation_ewallet.setText("  E-Wallet");
        navigation_ewallet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_ewalletMouseClicked(evt);
            }
        });

        navigation_dashboard.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_dashboard.setForeground(new java.awt.Color(255, 255, 255));
        navigation_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image.png"))); // NOI18N
        navigation_dashboard.setText("  Dashboard");
        navigation_dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_dashboardMouseClicked(evt);
            }
        });

        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout leftpanelLayout = new javax.swing.GroupLayout(leftpanel);
        leftpanel.setLayout(leftpanelLayout);
        leftpanelLayout.setHorizontalGroup(
            leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftpanelLayout.createSequentialGroup()
                .addGroup(leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftpanelLayout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftpanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(credits, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jSeparator6)
                                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(navigation_ewallet, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(navigation_banktransfer, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(navigation_dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        leftpanelLayout.setVerticalGroup(
            leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftpanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(navigation_dashboard)
                .addGap(27, 27, 27)
                .addComponent(navigation_ewallet)
                .addGap(26, 26, 26)
                .addComponent(navigation_banktransfer)
                .addGap(26, 26, 26)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(credits)
                .addGap(20, 20, 20))
        );

        returnto_banktransferpage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/replay (1).png"))); // NOI18N
        returnto_banktransferpage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnto_banktransferpageMouseClicked(evt);
            }
        });

        ewallet_title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ewallet_title.setForeground(new java.awt.Color(0, 0, 0));
        ewallet_title.setText("E-WALLET");

        ewallet_panel.setBackground(new java.awt.Color(0, 51, 51));
        ewallet_panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.gray, java.awt.Color.gray));

        availbalance_label.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        availbalance_label.setForeground(new java.awt.Color(255, 255, 255));
        availbalance_label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        availbalance_label.setText("Available Balance");

        balance.setFont(new java.awt.Font("Segoe UI Semibold", 0, 50)); // NOI18N
        balance.setForeground(new java.awt.Color(255, 255, 255));
        balance.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        balance.setText("₱0.00");

        send.setBackground(new java.awt.Color(0, 51, 51));
        send.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        send.setForeground(new java.awt.Color(255, 255, 255));
        send.setText("Send");
        send.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendActionPerformed(evt);
            }
        });

        ewallet_fullname.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        ewallet_fullname.setForeground(new java.awt.Color(255, 255, 255));
        ewallet_fullname.setText("Full Name");

        ewallet_phonenumber.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        ewallet_phonenumber.setForeground(new java.awt.Color(255, 255, 255));
        ewallet_phonenumber.setText("09123456789");

        cashin.setBackground(new java.awt.Color(0, 51, 51));
        cashin.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        cashin.setForeground(new java.awt.Color(255, 255, 255));
        cashin.setText("+ Cash in");
        cashin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cashinActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ewallet_panelLayout = new javax.swing.GroupLayout(ewallet_panel);
        ewallet_panel.setLayout(ewallet_panelLayout);
        ewallet_panelLayout.setHorizontalGroup(
            ewallet_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ewallet_panelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(ewallet_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ewallet_fullname)
                    .addComponent(ewallet_phonenumber, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(availbalance_label, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ewallet_panelLayout.createSequentialGroup()
                .addContainerGap(77, Short.MAX_VALUE)
                .addGroup(ewallet_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ewallet_panelLayout.createSequentialGroup()
                        .addComponent(send, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cashin, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(125, 125, 125))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ewallet_panelLayout.createSequentialGroup()
                        .addComponent(balance, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(93, 93, 93))))
        );
        ewallet_panelLayout.setVerticalGroup(
            ewallet_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ewallet_panelLayout.createSequentialGroup()
                .addGroup(ewallet_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(ewallet_panelLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(availbalance_label)
                        .addGap(12, 12, 12))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ewallet_panelLayout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(ewallet_fullname, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ewallet_phonenumber, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)))
                .addComponent(balance, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(ewallet_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(send, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cashin, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        transferhistory_label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        transferhistory_label.setForeground(new java.awt.Color(0, 0, 0));
        transferhistory_label.setText("Transaction History");

        jPanel2.setBackground(new java.awt.Color(0, 51, 51));

        ewallet_database.setBackground(new java.awt.Color(0, 102, 102));
        ewallet_database.setForeground(new java.awt.Color(255, 255, 255));
        ewallet_database.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Time", "Reference ID", "Recipient Name", "Platform", "Amount", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ewallet_database.setEnabled(false);
        ewallet_database.setGridColor(new java.awt.Color(0, 114, 114));
        ewallet_database.setRowHeight(40);
        ewallet_database.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(ewallet_database);
        if (ewallet_database.getColumnModel().getColumnCount() > 0) {
            ewallet_database.getColumnModel().getColumn(0).setResizable(false);
            ewallet_database.getColumnModel().getColumn(1).setResizable(false);
            ewallet_database.getColumnModel().getColumn(2).setResizable(false);
            ewallet_database.getColumnModel().getColumn(3).setResizable(false);
            ewallet_database.getColumnModel().getColumn(4).setResizable(false);
            ewallet_database.getColumnModel().getColumn(5).setResizable(false);
            ewallet_database.getColumnModel().getColumn(6).setResizable(false);
        }

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(8, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 695, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(leftpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(98, 98, 98)
                        .addComponent(ewallet_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(102, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(ewallet_title, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(returnto_banktransferpage)
                        .addGap(38, 38, 38))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSeparator1)
                                .addGap(29, 29, 29))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(transferhistory_label, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ewallet_title, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                    .addComponent(returnto_banktransferpage, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(ewallet_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(transferhistory_label)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
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

    private void returnto_banktransferpageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_returnto_banktransferpageMouseClicked
        EMP_Dashboard dashboard = new EMP_Dashboard(null);
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_returnto_banktransferpageMouseClicked

    private void sendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendActionPerformed
        // TODO add your handling code here:
        EWallet_Send sendPage = new EWallet_Send(empDashboard, this); // pass dashboard and current page
        sendPage.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_sendActionPerformed

    private void cashinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cashinActionPerformed
        // TODO add your handling code here:

        EWallet_Cashin cashin = new EWallet_Cashin(empDashboard, this);
        cashin.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_cashinActionPerformed

    private void navigation_banktransferMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_banktransferMouseClicked
        // TODO add your handling code here:
        Bank_Transfer banktransfer = new Bank_Transfer(null);
        banktransfer.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_navigation_banktransferMouseClicked

    private void navigation_ewalletMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_ewalletMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_navigation_ewalletMouseClicked

    private void navigation_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_dashboardMouseClicked
        // TODO add your handling code here:
        EMP_Dashboard dashboard = new EMP_Dashboard(null);
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_navigation_dashboardMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(EWallet_Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EWallet_Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EWallet_Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EWallet_Page.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EWallet_Page(null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel availbalance_label;
    private javax.swing.JLabel balance;
    private javax.swing.JButton cashin;
    private javax.swing.JLabel credits;
    private javax.swing.JTable ewallet_database;
    private javax.swing.JLabel ewallet_fullname;
    private javax.swing.JPanel ewallet_panel;
    private javax.swing.JLabel ewallet_phonenumber;
    private javax.swing.JLabel ewallet_title;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPanel leftpanel;
    private javax.swing.JLabel navigation_banktransfer;
    private javax.swing.JLabel navigation_dashboard;
    private javax.swing.JLabel navigation_ewallet;
    private javax.swing.JLabel returnto_banktransferpage;
    private javax.swing.JButton send;
    private javax.swing.JLabel transferhistory_label;
    // End of variables declaration//GEN-END:variables
}
