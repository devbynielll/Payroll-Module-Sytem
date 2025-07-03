

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author danie
 */
public class Bank_Transfer extends javax.swing.JFrame {

    private EMP_Dashboard empDashboard; // Reference to EMP_Dashboard
    private double currentBalance = 0.0; // Current balance in the bank account
    private int referenceCounter = 100; // Start reference ID from REF100

    Connection conn;

    public Bank_Transfer(EMP_Dashboard empDashboard) {

        this.conn = javaconnect.ConnectDb();

        this.empDashboard = empDashboard; // Store the reference to EMP_Dashboard
        initComponents();
        setLocationRelativeTo(null);
        setCurrentDate();

        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state

        navigation_banktransfer.setEnabled(false);
        navigation_banktransfer.setForeground(Color.GRAY);

        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        setTitle("Payroll Swift"); // ✅ Custom window title

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE); // Only dispose of this window

        updateBankBalanceDisplay();
        loadLatestUserCardDetails();
        populateBankTransferHistoryTable();
    }

    public void populateBankTransferHistoryTable() {
        PreparedStatement pstLogin = null;
        PreparedStatement pstTransfer = null;
        ResultSet rsLogin = null;
        ResultSet rsTransfer = null;

        DefaultTableModel model = (DefaultTableModel) Transaction_Database.getModel(); // Replace with your JTable name
        model.setRowCount(0); // Clear existing rows

        try {
            // Step 1: Get latest logged-in UserID from Login table
            String loginQuery = "SELECT UserID FROM Login ORDER BY LoginTime DESC LIMIT 1";
            pstLogin = conn.prepareStatement(loginQuery);
            rsLogin = pstLogin.executeQuery();

            if (!rsLogin.next()) {
                JOptionPane.showMessageDialog(this, "No login records found.");
                return;
            }

            String userID = rsLogin.getString("UserID");

            // Step 2: Fetch bank transfer history for this UserID
            String transferQuery = "SELECT Date, REF, RecipName, RecipNum, Accnum, Amount, Status "
                    + "FROM BankTransfer WHERE UserID = ? ORDER BY Date DESC";
            pstTransfer = conn.prepareStatement(transferQuery);
            pstTransfer.setString(1, userID);
            rsTransfer = pstTransfer.executeQuery();

            while (rsTransfer.next()) {
                String date = rsTransfer.getString("Date");
                String ref = rsTransfer.getString("REF");
                String recipName = rsTransfer.getString("RecipName");
                String recipNum = rsTransfer.getString("RecipNum");
                String accNum = rsTransfer.getString("Accnum");
                double amount = rsTransfer.getDouble("Amount");
                String status = rsTransfer.getString("Status");

                model.addRow(new Object[]{
                    date, ref, recipName, recipNum, accNum, "₱" + String.format("%,.2f", amount), status
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading bank transfer history: " + e.getMessage());
        } finally {
            try {
                if (rsLogin != null) {
                    rsLogin.close();
                }
                if (rsTransfer != null) {
                    rsTransfer.close();
                }
                if (pstLogin != null) {
                    pstLogin.close();
                }
                if (pstTransfer != null) {
                    pstTransfer.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    private void loadLatestUserCardDetails() {
        PreparedStatement pstLogin = null;
        PreparedStatement pstBank = null;
        PreparedStatement pstName = null;
        ResultSet rsLogin = null;
        ResultSet rsBank = null;
        ResultSet rsName = null;

        try {
            // 1. Get the latest UserID from Login table
            String getUserSql = "SELECT UserID FROM Login ORDER BY LoginTime DESC LIMIT 1";
            pstLogin = conn.prepareStatement(getUserSql);
            rsLogin = pstLogin.executeQuery();

            if (rsLogin.next()) {
                String latestUserID = rsLogin.getString("UserID");

                // 2. Get Account Number from BankDetails
                String bankSql = "SELECT AccountNum FROM BankDetails WHERE UserID = ?";
                pstBank = conn.prepareStatement(bankSql);
                pstBank.setString(1, latestUserID);
                rsBank = pstBank.executeQuery();

                if (rsBank.next()) {
                    String accountNum = rsBank.getString("AccountNum");
                    String formattedAccNum = accountNum.replaceAll(".{4}(?!$)", "$0 ");
                    card_accnumber.setText(formattedAccNum);

                } else {
                    card_accnumber.setText("No account found");
                }

                // 3. Get Full Name from PersonalData
                String nameSql = "SELECT FirstName, LastName FROM PersonalData WHERE UserID = ?";
                pstName = conn.prepareStatement(nameSql);
                pstName.setString(1, latestUserID);
                rsName = pstName.executeQuery();

                if (rsName.next()) {
                    String fullName = rsName.getString("FirstName") + " " + rsName.getString("LastName");
                    card_accname.setText(fullName);
                } else {
                    card_accname.setText("Name not found");
                }

            } else {
                JOptionPane.showMessageDialog(this, "No logged in user found.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading card details: " + ex.getMessage());
        } finally {
            try {
                if (rsLogin != null) {
                    rsLogin.close();
                }
                if (rsBank != null) {
                    rsBank.close();
                }
                if (rsName != null) {
                    rsName.close();
                }
                if (pstLogin != null) {
                    pstLogin.close();
                }
                if (pstBank != null) {
                    pstBank.close();
                }
                if (pstName != null) {
                    pstName.close();
                }
            } catch (SQLException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
        }
    }

    private void updateTotalIncomeToDashboard() {
        try {
            double currentBalance = Double.parseDouble(balance.getText().replace("₱", "").replace(",", ""));
            empDashboard.setTotalIncome(currentBalance);
        } catch (NumberFormatException e) {
            System.out.println("Invalid balance format.");
        }
    }

    public void updateBankBalanceDisplay() {
        try {
            // Step 1: Get the latest logged-in UserID from Login table using LoginTime
            String loginQuery = "SELECT UserID FROM Login ORDER BY LoginTime DESC LIMIT 1";
            PreparedStatement loginStmt = conn.prepareStatement(loginQuery);
            ResultSet loginRs = loginStmt.executeQuery();

            String userId = null;

            if (loginRs.next()) {
                userId = loginRs.getString("UserID");
            } else {
                JOptionPane.showMessageDialog(this, "No login records found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            loginRs.close();
            loginStmt.close();

            // Step 2: Use the UserID to get TotalIncome from EmployeeIncome table
            String incomeQuery = "SELECT TotalIncome FROM EmployeeIncome WHERE UserID = ?";
            PreparedStatement incomeStmt = conn.prepareStatement(incomeQuery);
            incomeStmt.setString(1, userId);
            ResultSet incomeRs = incomeStmt.executeQuery();

            if (incomeRs.next()) {
                double totalIncome = incomeRs.getDouble("TotalIncome");
                balance.setText("₱" + String.format("%,.2f", totalIncome));
            } else {
                balance.setText("₱0.00"); // if no income record found
                System.err.println("No income record found for UserID: " + userId);
            }

            incomeRs.close();
            incomeStmt.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching balance:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setCurrentBalance(double balance) {
        this.currentBalance = balance;
        updateBalanceDisplay();
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        currentdate.setText(sdf.format(new Date()));
    }

    // Update the balance in the UI
    private void updateBalanceDisplay() {
        currentBalance = SharedData.getTotalIncome(); // Keep balance in sync
        balance.setText("₱" + String.format("%,.2f", currentBalance));
    }

    public void syncBalanceWithTotalIncome() {
        currentBalance = SharedData.getTotalIncome();
        updateBalanceDisplay(); // Refresh UI
    }

    // Deduct the balance (called after a transaction)
    public void deductBalance(double amount) {
        // Sync with the latest total income
        currentBalance = SharedData.getTotalIncome();

        // Ensure there are sufficient funds before deducting
        if (currentBalance >= amount) {
            currentBalance -= amount;
            SharedData.setTotalIncome(currentBalance); // Update total income once
            updateBalanceDisplay(); // Refresh UI
            if (empDashboard != null) {
                empDashboard.updateTotalIncomeDisplay(); // Refresh income display
            }
        } else {
            System.out.println("Error: Insufficient funds!");
            JOptionPane.showMessageDialog(this, "Insufficient balance!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public double getCurrentBalance() {
        return currentBalance;
    }

// Helper method to parse money from text
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background_white = new javax.swing.JPanel();
        leftpanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        navigation_dashboard = new javax.swing.JLabel();
        navigation_ewallet = new javax.swing.JLabel();
        navigation_banktransfer = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        credits = new javax.swing.JLabel();
        card_panel = new javax.swing.JPanel();
        card_accnumber = new javax.swing.JLabel();
        card_accname = new javax.swing.JLabel();
        card_expirationdate = new javax.swing.JLabel();
        card_debitlabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        banktransfer_title = new javax.swing.JLabel();
        myCard_label = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        transferhistory_label = new javax.swing.JLabel();
        transfertowallet_btn = new javax.swing.JButton();
        balance = new javax.swing.JLabel();
        transfertobank_btn = new javax.swing.JButton();
        balance_textlabel = new javax.swing.JLabel();
        philippines = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        date_label = new javax.swing.JLabel();
        currentdate = new javax.swing.JLabel();
        return_banktransferpage = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        Transaction_Database = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        background_white.setBackground(new java.awt.Color(255, 255, 255));

        leftpanel.setBackground(new java.awt.Color(0, 51, 51));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AYROLL WIFT (2) (3).png"))); // NOI18N
        jLabel2.setText(" ");

        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));

        navigation_dashboard.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_dashboard.setForeground(new java.awt.Color(255, 255, 255));
        navigation_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image.png"))); // NOI18N
        navigation_dashboard.setText("  Dashboard");
        navigation_dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_dashboardMouseClicked(evt);
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

        navigation_banktransfer.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_banktransfer.setForeground(new java.awt.Color(255, 255, 255));
        navigation_banktransfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bank smol.png"))); // NOI18N
        navigation_banktransfer.setText("  Bank Transfer");
        navigation_banktransfer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_banktransferMouseClicked(evt);
            }
        });

        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));

        credits.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        credits.setForeground(new java.awt.Color(255, 255, 255));
        credits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        credits.setText("© 2025 Payroll Swift");

        javax.swing.GroupLayout leftpanelLayout = new javax.swing.GroupLayout(leftpanel);
        leftpanel.setLayout(leftpanelLayout);
        leftpanelLayout.setHorizontalGroup(
            leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftpanelLayout.createSequentialGroup()
                .addGroup(leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(leftpanelLayout.createSequentialGroup()
                            .addGap(54, 54, 54)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(leftpanelLayout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addGroup(leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(credits, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                                    .addComponent(jSeparator4)))))
                    .addGroup(leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(navigation_ewallet, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(navigation_banktransfer, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(navigation_dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(17, Short.MAX_VALUE))
        );
        leftpanelLayout.setVerticalGroup(
            leftpanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftpanelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(navigation_dashboard)
                .addGap(27, 27, 27)
                .addComponent(navigation_ewallet)
                .addGap(26, 26, 26)
                .addComponent(navigation_banktransfer)
                .addGap(26, 26, 26)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(credits)
                .addGap(22, 22, 22))
        );

        card_panel.setBackground(new java.awt.Color(0, 102, 102));
        card_panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.white, java.awt.Color.white, java.awt.Color.gray, java.awt.Color.gray));

        card_accnumber.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        card_accnumber.setForeground(new java.awt.Color(255, 255, 255));
        card_accnumber.setText("1200 2300 3400 1234");

        card_accname.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        card_accname.setForeground(new java.awt.Color(255, 255, 255));
        card_accname.setText("Account Name");

        card_expirationdate.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        card_expirationdate.setForeground(new java.awt.Color(255, 255, 255));
        card_expirationdate.setText("3/2027");

        card_debitlabel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 12)); // NOI18N
        card_debitlabel.setForeground(new java.awt.Color(255, 255, 255));
        card_debitlabel.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        card_debitlabel.setText("Debit");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/card (1).png"))); // NOI18N
        jLabel1.setText(" ");

        javax.swing.GroupLayout card_panelLayout = new javax.swing.GroupLayout(card_panel);
        card_panel.setLayout(card_panelLayout);
        card_panelLayout.setHorizontalGroup(
            card_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_panelLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(card_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(card_panelLayout.createSequentialGroup()
                        .addComponent(card_accname, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(card_panelLayout.createSequentialGroup()
                        .addGroup(card_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(card_accnumber)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 64, Short.MAX_VALUE)
                        .addGroup(card_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(card_debitlabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(card_expirationdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(22, 22, 22))))
        );
        card_panelLayout.setVerticalGroup(
            card_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(card_panelLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(card_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card_debitlabel)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                .addGroup(card_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(card_accnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(card_expirationdate))
                .addGap(4, 4, 4)
                .addComponent(card_accname)
                .addGap(16, 16, 16))
        );

        banktransfer_title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        banktransfer_title.setForeground(new java.awt.Color(0, 0, 0));
        banktransfer_title.setText("BANK TRANSFER");

        myCard_label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        myCard_label.setForeground(new java.awt.Color(0, 0, 0));
        myCard_label.setText("My Card");

        transferhistory_label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        transferhistory_label.setForeground(new java.awt.Color(0, 0, 0));
        transferhistory_label.setText("Transfer History");

        transfertowallet_btn.setBackground(new java.awt.Color(0, 51, 51));
        transfertowallet_btn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        transfertowallet_btn.setForeground(new java.awt.Color(255, 255, 255));
        transfertowallet_btn.setText("Transfer to E-Wallet");
        transfertowallet_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transfertowallet_btnActionPerformed(evt);
            }
        });

        balance.setFont(new java.awt.Font("Segoe UI Semibold", 0, 40)); // NOI18N
        balance.setForeground(new java.awt.Color(0, 0, 0));
        balance.setText("₱0.00");

        transfertobank_btn.setBackground(new java.awt.Color(0, 51, 51));
        transfertobank_btn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        transfertobank_btn.setForeground(new java.awt.Color(255, 255, 255));
        transfertobank_btn.setText("Transfer to Bank");
        transfertobank_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                transfertobank_btnMouseClicked(evt);
            }
        });
        transfertobank_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                transfertobank_btnActionPerformed(evt);
            }
        });

        balance_textlabel.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        balance_textlabel.setForeground(new java.awt.Color(0, 0, 0));
        balance_textlabel.setText("Current Balance");

        philippines.setFont(new java.awt.Font("Segoe UI Semibold", 1, 18)); // NOI18N
        philippines.setForeground(new java.awt.Color(0, 0, 0));
        philippines.setText("PHL");

        date_label.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        date_label.setForeground(new java.awt.Color(0, 0, 0));
        date_label.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        date_label.setText("Date:");

        currentdate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        currentdate.setForeground(new java.awt.Color(0, 0, 0));
        currentdate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        currentdate.setText("01/01/2025");

        return_banktransferpage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/replay (1).png"))); // NOI18N
        return_banktransferpage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                return_banktransferpageMouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 51, 51));

        Transaction_Database.setBackground(new java.awt.Color(0, 105, 105));
        Transaction_Database.setForeground(new java.awt.Color(255, 255, 255));
        Transaction_Database.setModel(new javax.swing.table.DefaultTableModel(
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
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Date", "Reference ID", "Recipient Name ", "Recipient Number", "Account Number", "Amount", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Transaction_Database.setEnabled(false);
        Transaction_Database.setGridColor(new java.awt.Color(0, 115, 115));
        Transaction_Database.setRowHeight(40);
        Transaction_Database.setShowGrid(false);
        Transaction_Database.setShowHorizontalLines(true);
        Transaction_Database.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(Transaction_Database);
        if (Transaction_Database.getColumnModel().getColumnCount() > 0) {
            Transaction_Database.getColumnModel().getColumn(0).setResizable(false);
            Transaction_Database.getColumnModel().getColumn(0).setPreferredWidth(50);
            Transaction_Database.getColumnModel().getColumn(1).setResizable(false);
            Transaction_Database.getColumnModel().getColumn(1).setPreferredWidth(50);
            Transaction_Database.getColumnModel().getColumn(2).setResizable(false);
            Transaction_Database.getColumnModel().getColumn(2).setPreferredWidth(80);
            Transaction_Database.getColumnModel().getColumn(3).setResizable(false);
            Transaction_Database.getColumnModel().getColumn(3).setPreferredWidth(100);
            Transaction_Database.getColumnModel().getColumn(4).setResizable(false);
            Transaction_Database.getColumnModel().getColumn(4).setPreferredWidth(100);
            Transaction_Database.getColumnModel().getColumn(5).setResizable(false);
            Transaction_Database.getColumnModel().getColumn(5).setPreferredWidth(50);
            Transaction_Database.getColumnModel().getColumn(6).setResizable(false);
            Transaction_Database.getColumnModel().getColumn(6).setPreferredWidth(50);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 220, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout background_whiteLayout = new javax.swing.GroupLayout(background_white);
        background_white.setLayout(background_whiteLayout);
        background_whiteLayout.setHorizontalGroup(
            background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(background_whiteLayout.createSequentialGroup()
                .addComponent(leftpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(background_whiteLayout.createSequentialGroup()
                        .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(background_whiteLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(background_whiteLayout.createSequentialGroup()
                                .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(background_whiteLayout.createSequentialGroup()
                                        .addGap(32, 32, 32)
                                        .addComponent(banktransfer_title, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(background_whiteLayout.createSequentialGroup()
                                        .addGap(31, 31, 31)
                                        .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(myCard_label, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(card_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(transferhistory_label, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(26, 26, 26)
                                        .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(background_whiteLayout.createSequentialGroup()
                                                .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(philippines, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(balance, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(balance_textlabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addComponent(date_label, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(background_whiteLayout.createSequentialGroup()
                                                .addComponent(transfertobank_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(transfertowallet_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(28, 28, 28)))
                        .addGap(16, 16, 16))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, background_whiteLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, background_whiteLayout.createSequentialGroup()
                                .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(return_banktransferpage, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 717, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, background_whiteLayout.createSequentialGroup()
                                .addComponent(currentdate, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(45, 45, 45))))))
        );
        background_whiteLayout.setVerticalGroup(
            background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(background_whiteLayout.createSequentialGroup()
                .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(background_whiteLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(banktransfer_title, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(background_whiteLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(return_banktransferpage, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(myCard_label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(background_whiteLayout.createSequentialGroup()
                        .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(balance_textlabel)
                            .addComponent(date_label))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(balance, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(currentdate))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(philippines)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(background_whiteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(transfertobank_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(transfertowallet_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(card_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addComponent(transferhistory_label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background_white, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background_white, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void transfertobank_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transfertobank_btnActionPerformed
        TransferToBank transferToBank = new TransferToBank(
                card_accnumber.getText(), // Get the account number from the card
                card_accname.getText(), // Get the account name from the card
                Double.parseDouble(balance.getText().replace("₱", "").replace(",", "")), // Get current balance
                this // Pass the reference to the current page
        );
        transferToBank.setVisible(true);
        this.dispose(); // Close the current Bank_Transfer page

    }//GEN-LAST:event_transfertobank_btnActionPerformed

    private void transfertobank_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_transfertobank_btnMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_transfertobank_btnMouseClicked

    private void transfertowallet_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_transfertowallet_btnActionPerformed

        TransferToEWallet transferToEWallet = new TransferToEWallet(
                card_accnumber.getText(), // Get the account number from the card
                card_accname.getText(), // Get the account name from the card
                Double.parseDouble(balance.getText().replace("₱", "").replace(",", "")), // Get current balance
                this // Pass the reference to the current page
        );
        transferToEWallet.setVisible(true); // Show the TransferToEWallet page
        this.dispose(); // Close the current Bank_Transfer page

    }//GEN-LAST:event_transfertowallet_btnActionPerformed

    private void return_banktransferpageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_return_banktransferpageMouseClicked

        EMP_Dashboard dashboard = new EMP_Dashboard(null);
        dashboard.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_return_banktransferpageMouseClicked

    private void navigation_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_dashboardMouseClicked
        // TODO add your handling code here:
        EMP_Dashboard dashboard = new EMP_Dashboard(null);
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_navigation_dashboardMouseClicked

    private void navigation_ewalletMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_ewalletMouseClicked
        // TODO add your handling code here:
        EWallet_Page ewallet = new EWallet_Page(null);
        ewallet.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_navigation_ewalletMouseClicked

    private void navigation_banktransferMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_banktransferMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_navigation_banktransferMouseClicked

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
            java.util.logging.Logger.getLogger(Bank_Transfer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Bank_Transfer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Bank_Transfer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Bank_Transfer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            // Create a Bank_Transfer instance
            // Create an EMP_Dashboard instance
            EMP_Dashboard empDashboard = new EMP_Dashboard(null);

            // Create a Bank_Transfer instance and pass the EMP_Dashboard reference
            Bank_Transfer bankTransfer = new Bank_Transfer(empDashboard);

            // Set the Bank_Transfer reference in EMP_Dashboard
            empDashboard.setBankTransferPage(bankTransfer);

            // Display the EMP_Dashboard
            empDashboard.setVisible(true);

        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Transaction_Database;
    private javax.swing.JPanel background_white;
    private javax.swing.JLabel balance;
    private javax.swing.JLabel balance_textlabel;
    private javax.swing.JLabel banktransfer_title;
    private javax.swing.JLabel card_accname;
    private javax.swing.JLabel card_accnumber;
    private javax.swing.JLabel card_debitlabel;
    private javax.swing.JLabel card_expirationdate;
    private javax.swing.JPanel card_panel;
    private javax.swing.JLabel credits;
    private javax.swing.JLabel currentdate;
    private javax.swing.JLabel date_label;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPanel leftpanel;
    private javax.swing.JLabel myCard_label;
    private javax.swing.JLabel navigation_banktransfer;
    private javax.swing.JLabel navigation_dashboard;
    private javax.swing.JLabel navigation_ewallet;
    private javax.swing.JLabel philippines;
    private javax.swing.JLabel return_banktransferpage;
    private javax.swing.JLabel transferhistory_label;
    private javax.swing.JButton transfertobank_btn;
    private javax.swing.JButton transfertowallet_btn;
    // End of variables declaration//GEN-END:variables
}
