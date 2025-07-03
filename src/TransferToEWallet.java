/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import java.awt.Color;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;

/**
 *
 * @author danie
 */
public class TransferToEWallet extends javax.swing.JFrame {

    private String senderAccountNumber;
    private String senderAccountName;
    private Bank_Transfer parentPage;

    private double currentBalance = 100000.00; // Initial Current Balance
    private double fees = 0.00; // Fee amount

    Connection conn;

    public TransferToEWallet(String accountNumber, String accountName, double balance, Bank_Transfer parentPage) {

        this.conn = javaconnect.ConnectDb();

        initComponents();
        setLocationRelativeTo(null);
        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state

        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        setTitle("Payroll Swift"); // ✅ Custom window title

        setupEWalletNumberInput(); // Restrict e-wallet number input to valid format
        setupAmountInputListener(); // Restrict amount input to integers
        updateCurrentBalanceDisplay(); // Set initial balance

        navigation_banktransfer.setEnabled(false);
        navigation_banktransfer.setForeground(Color.GRAY);

        this.senderAccountNumber = accountNumber;
        this.senderAccountName = accountName;
        this.currentBalance = balance;
        this.parentPage = parentPage;

        connect_accnumber.setText(senderAccountNumber); // Automate bank account number

        currenrtbalance.setText("Current Balance: ₱" + new DecimalFormat("#,##0.00").format(currentBalance)); // Display balance
        setCurrentDate(); // Automate date

        sendmoney_rbtn.setEnabled(false);

        setLatestUserId();
        bank_userid.setEnabled(false);

        updateConnectedBankAccount();
        connect_accnumber.setEditable(false);

        Utils.setDigitLimit(ewalletnumber, 11);
    }

    private void updateConnectedBankAccount() {
        String userID = bank_userid.getText().trim(); // Example: "EMP143"

        PreparedStatement pstBank = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT AccountNum FROM BankDetails WHERE UserID = ?";
            pstBank = conn.prepareStatement(sql);
            pstBank.setString(1, userID);

            rs = pstBank.executeQuery();

            if (rs.next()) {
                String accountNum = rs.getString("AccountNum");
                String formattedAccNum = accountNum.replaceAll(".{4}(?!$)", "$0 ");
                connect_accnumber.setText(formattedAccNum);

            } else {
                connect_accnumber.setText("Account not found.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error fetching bank account number: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstBank != null) {
                    pstBank.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
    }

    public TransferToEWallet() {
        initComponents();
        setLocationRelativeTo(null);
        // Provide default values for required variables
        this.senderAccountNumber = "";
        this.senderAccountName = "";
        this.currentBalance = 0.00;
        this.parentPage = null; // No parent page in this context
        currenrtbalance.setText("₱" + new DecimalFormat("#,##0.00").format(currentBalance));
        setCurrentDate();

    }

    private void setLatestUserId() {
        String latestUserId = "";
        String sql = "SELECT UserID FROM Login ORDER BY LoginTime DESC LIMIT 1";

        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {

            if (rs.next()) {
                latestUserId = rs.getString("UserID");
                bank_userid.setText(latestUserId);
            } else {
                bank_userid.setText(""); // No user found
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch latest user ID.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        currentdate.setText(sdf.format(new Date()));
    }

    // Update the current balance display
    private void updateCurrentBalanceDisplay() {
        currenrtbalance.setText("Current Balance: ₱" + new DecimalFormat("#,##0.00").format(currentBalance));
    }

    // Restrict e-wallet number input to 11 digits starting with "09"
// Restrict e-wallet number input to 11 digits starting with "09"
    private void setupEWalletNumberInput() {
        ewalletnumber.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                validateEWalletNumber();
            }

            public void removeUpdate(DocumentEvent e) {
                validateEWalletNumber();
            }

            public void changedUpdate(DocumentEvent e) {
                validateEWalletNumber();
            }
        });
    }

    private void validateEWalletNumber() {
        String input = ewalletnumber.getText().trim();

        // Allow partial inputs like "0" or "09" without showing an error
        if (input.length() == 0 || input.equals("0") || input.equals("09")) {
            return;
        }

        // Show warning dialog if the input deviates from "09" and contains invalid characters
        if (!input.matches("^09\\d{0,9}$")) { // Allow only numbers starting with "09" and up to 11 digits
            JOptionPane.showMessageDialog(this, "E-Wallet number must start with '09' and contain only digits.", "Warning", JOptionPane.WARNING_MESSAGE);
            // Remove invalid characters
            ewalletnumber.setText(input.replaceAll("[^0-9]", ""));
        }
    }

    // Restrict amount input to integers only
    private void setupAmountInputListener() {
        amount.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                restrictAmountInput();
            }

            public void removeUpdate(DocumentEvent e) {
                restrictAmountInput();
            }

            public void changedUpdate(DocumentEvent e) {
                restrictAmountInput();
            }
        });
    }

    private void restrictAmountInput() {
        String input = amount.getText().trim();
        if (!input.matches("\\d*")) { // Allow only digits
            JOptionPane.showMessageDialog(this, "Amount must be a valid integer.", "Error", JOptionPane.ERROR_MESSAGE);
            amount.setText(input.replaceAll("[^\\d]", "")); // Remove non-digits
        }
    }

    private void resetFields() {
        ewalletnumber.setText(""); // Clear e-wallet number
        ewalletaccname.setText(""); // Clear e-wallet account name
        amount.setText(""); // Clear amount
        referencenotes.setText(""); // Clear reference notes
        setCurrentDate(); // Reset the date to the current date
        fees = 0.00; // Reset fees
        feesLabel.setText("Fees: ₱0.00"); // Reset fees display
        total__review.setText("Total Deduction: ₱0.00"); // Reset total deduction display
    }

    // Calculate fees dynamically based on guidelines
    private double calculateFees(double transferAmount) {
        double flatFee = 15.00; // Default flat fee
        double percentageFee = transferAmount * 0.015; // 1.5% of transaction amount
        return Math.max(flatFee, percentageFee); // Choose the higher fee
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
        leftpanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        navigation_dashboard = new javax.swing.JLabel();
        navigation_ewallet = new javax.swing.JLabel();
        navigation_banktransfer = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        credits = new javax.swing.JLabel();
        transferdetails_label = new javax.swing.JLabel();
        banktransfer_title = new javax.swing.JLabel();
        return_banktransferpage = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        bankaccountnumberTXT_LABEL = new javax.swing.JLabel();
        connect_accnumber = new javax.swing.JTextField();
        bank_userid = new javax.swing.JTextField();
        bankaccountnumberTXT_LABEL1 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        ewalletnumberTXT_LABEL = new javax.swing.JLabel();
        ewalletnumber = new javax.swing.JTextField();
        ewalletaccname = new javax.swing.JTextField();
        ewalletaccnameTXT_LABEL = new javax.swing.JLabel();
        platform = new javax.swing.JComboBox<>();
        platformTXT_LABEL = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        referencenotesTXT_LABEL1 = new javax.swing.JLabel();
        referencenotes = new javax.swing.JTextField();
        amount = new javax.swing.JTextField();
        recipientaccnumberTXT_LABEL2 = new javax.swing.JLabel();
        currentdate = new javax.swing.JTextField();
        recipientaccnumberTXT_LABEL3 = new javax.swing.JLabel();
        execute_btn = new javax.swing.JButton();
        notes = new javax.swing.JLabel();
        transactionreview_label = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        date_review = new javax.swing.JLabel();
        amount_review = new javax.swing.JLabel();
        referencenotes_review = new javax.swing.JLabel();
        platform_review = new javax.swing.JLabel();
        ewalletaccname_review = new javax.swing.JLabel();
        ewalletnumber_review = new javax.swing.JLabel();
        bankaccname_review = new javax.swing.JLabel();
        bankaccnumber_review = new javax.swing.JLabel();
        feesLabel = new javax.swing.JLabel();
        total__review = new javax.swing.JLabel();
        sendmoney_rbtn = new javax.swing.JButton();
        currenrtbalance = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        leftpanel2.setBackground(new java.awt.Color(0, 51, 51));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AYROLL WIFT (2) (3).png"))); // NOI18N
        jLabel2.setText(" ");

        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));

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

        jSeparator5.setForeground(new java.awt.Color(255, 255, 255));

        credits.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        credits.setForeground(new java.awt.Color(255, 255, 255));
        credits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        credits.setText("© 2025 Payroll Swift");

        javax.swing.GroupLayout leftpanel2Layout = new javax.swing.GroupLayout(leftpanel2);
        leftpanel2.setLayout(leftpanel2Layout);
        leftpanel2Layout.setHorizontalGroup(
            leftpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftpanel2Layout.createSequentialGroup()
                .addGroup(leftpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(leftpanel2Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(leftpanel2Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(leftpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(leftpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(credits, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(leftpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jSeparator6)
                                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(leftpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(navigation_ewallet, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(navigation_banktransfer, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(navigation_dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        leftpanel2Layout.setVerticalGroup(
            leftpanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(leftpanel2Layout.createSequentialGroup()
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
                .addGap(18, 18, 18))
        );

        transferdetails_label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        transferdetails_label.setForeground(new java.awt.Color(0, 0, 0));
        transferdetails_label.setText("Transfer Details");

        banktransfer_title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        banktransfer_title.setForeground(new java.awt.Color(0, 0, 0));
        banktransfer_title.setText("BANK TO E-WALLET TRANSFER");

        return_banktransferpage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/replay (1).png"))); // NOI18N
        return_banktransferpage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                return_banktransferpageMouseClicked(evt);
            }
        });

        bankaccountnumberTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        bankaccountnumberTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        bankaccountnumberTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        bankaccountnumberTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        bankaccountnumberTXT_LABEL.setText("Bank Account Number");

        connect_accnumber.setBackground(new java.awt.Color(255, 255, 255));
        connect_accnumber.setForeground(new java.awt.Color(0, 0, 0));

        bank_userid.setBackground(new java.awt.Color(255, 255, 255));
        bank_userid.setForeground(new java.awt.Color(0, 0, 0));
        bank_userid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bank_useridActionPerformed(evt);
            }
        });

        bankaccountnumberTXT_LABEL1.setBackground(new java.awt.Color(0, 0, 0));
        bankaccountnumberTXT_LABEL1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        bankaccountnumberTXT_LABEL1.setForeground(new java.awt.Color(0, 0, 0));
        bankaccountnumberTXT_LABEL1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        bankaccountnumberTXT_LABEL1.setText("User ID");

        ewalletnumberTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        ewalletnumberTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        ewalletnumberTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        ewalletnumberTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ewalletnumberTXT_LABEL.setText("E-Wallet Number");

        ewalletnumber.setBackground(new java.awt.Color(255, 255, 255));
        ewalletnumber.setForeground(new java.awt.Color(0, 0, 0));

        ewalletaccname.setBackground(new java.awt.Color(255, 255, 255));
        ewalletaccname.setForeground(new java.awt.Color(0, 0, 0));

        ewalletaccnameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        ewalletaccnameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        ewalletaccnameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        ewalletaccnameTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ewalletaccnameTXT_LABEL.setText("E-Wallet Account Name");

        platform.setBackground(new java.awt.Color(255, 255, 255));
        platform.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        platform.setForeground(new java.awt.Color(0, 0, 51));
        platform.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GCash", "Maya (PayMaya)", "GOtyme Bank", "Coins.ph", "ShopeePay", "GrabPay", "JuanCash" }));
        platform.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                platformActionPerformed(evt);
            }
        });

        platformTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        platformTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        platformTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        platformTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        platformTXT_LABEL.setText("\tPlatform");

        referencenotesTXT_LABEL1.setBackground(new java.awt.Color(0, 0, 0));
        referencenotesTXT_LABEL1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        referencenotesTXT_LABEL1.setForeground(new java.awt.Color(0, 0, 0));
        referencenotesTXT_LABEL1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        referencenotesTXT_LABEL1.setText("Reference/Notes");

        referencenotes.setBackground(new java.awt.Color(255, 255, 255));
        referencenotes.setForeground(new java.awt.Color(0, 0, 0));

        amount.setBackground(new java.awt.Color(255, 255, 255));
        amount.setForeground(new java.awt.Color(0, 0, 0));

        recipientaccnumberTXT_LABEL2.setBackground(new java.awt.Color(0, 0, 0));
        recipientaccnumberTXT_LABEL2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientaccnumberTXT_LABEL2.setForeground(new java.awt.Color(0, 0, 0));
        recipientaccnumberTXT_LABEL2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        recipientaccnumberTXT_LABEL2.setText("Amount");

        currentdate.setBackground(new java.awt.Color(255, 255, 255));
        currentdate.setForeground(new java.awt.Color(0, 0, 0));

        recipientaccnumberTXT_LABEL3.setBackground(new java.awt.Color(0, 0, 0));
        recipientaccnumberTXT_LABEL3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientaccnumberTXT_LABEL3.setForeground(new java.awt.Color(0, 0, 0));
        recipientaccnumberTXT_LABEL3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        recipientaccnumberTXT_LABEL3.setText("Date");

        execute_btn.setBackground(new java.awt.Color(0, 51, 51));
        execute_btn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        execute_btn.setForeground(new java.awt.Color(255, 255, 255));
        execute_btn.setText("Execute");
        execute_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                execute_btnMouseClicked(evt);
            }
        });

        notes.setBackground(new java.awt.Color(0, 0, 0));
        notes.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        notes.setForeground(new java.awt.Color(0, 0, 0));
        notes.setText("Notes: Make sure the wallet number and platform are correct..");

        transactionreview_label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        transactionreview_label.setForeground(new java.awt.Color(0, 0, 0));
        transactionreview_label.setText("Transaction Review");

        date_review.setBackground(new java.awt.Color(0, 0, 0));
        date_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        date_review.setForeground(new java.awt.Color(0, 0, 0));
        date_review.setText("Date:");

        amount_review.setBackground(new java.awt.Color(0, 0, 0));
        amount_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        amount_review.setForeground(new java.awt.Color(0, 0, 0));
        amount_review.setText("Amount:");

        referencenotes_review.setBackground(new java.awt.Color(0, 0, 0));
        referencenotes_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        referencenotes_review.setForeground(new java.awt.Color(0, 0, 0));
        referencenotes_review.setText("Reference/Notes:");

        platform_review.setBackground(new java.awt.Color(0, 0, 0));
        platform_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        platform_review.setForeground(new java.awt.Color(0, 0, 0));
        platform_review.setText("Platform:");

        ewalletaccname_review.setBackground(new java.awt.Color(0, 0, 0));
        ewalletaccname_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        ewalletaccname_review.setForeground(new java.awt.Color(0, 0, 0));
        ewalletaccname_review.setText("E-Wallet Account Name:");

        ewalletnumber_review.setBackground(new java.awt.Color(0, 0, 0));
        ewalletnumber_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        ewalletnumber_review.setForeground(new java.awt.Color(0, 0, 0));
        ewalletnumber_review.setText("E-Wallet Number:");

        bankaccname_review.setBackground(new java.awt.Color(0, 0, 0));
        bankaccname_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        bankaccname_review.setForeground(new java.awt.Color(0, 0, 0));
        bankaccname_review.setText("User ID:");

        bankaccnumber_review.setBackground(new java.awt.Color(0, 0, 0));
        bankaccnumber_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        bankaccnumber_review.setForeground(new java.awt.Color(0, 0, 0));
        bankaccnumber_review.setText("Bank Account Number: ");

        feesLabel.setBackground(new java.awt.Color(0, 0, 0));
        feesLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        feesLabel.setForeground(new java.awt.Color(0, 0, 0));
        feesLabel.setText("Fees:");

        total__review.setBackground(new java.awt.Color(0, 0, 0));
        total__review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        total__review.setForeground(new java.awt.Color(0, 0, 0));
        total__review.setText("Total:");

        sendmoney_rbtn.setBackground(new java.awt.Color(0, 51, 51));
        sendmoney_rbtn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        sendmoney_rbtn.setForeground(new java.awt.Color(255, 255, 255));
        sendmoney_rbtn.setText("Send Money");
        sendmoney_rbtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendmoney_rbtnMouseClicked(evt);
            }
        });
        sendmoney_rbtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendmoney_rbtnActionPerformed(evt);
            }
        });

        currenrtbalance.setBackground(new java.awt.Color(0, 0, 0));
        currenrtbalance.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        currenrtbalance.setForeground(new java.awt.Color(0, 0, 0));
        currenrtbalance.setText("Current Balance:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(leftpanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(bankaccountnumberTXT_LABEL1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(bankaccountnumberTXT_LABEL, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(platformTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(7, 7, 7)
                                                .addComponent(ewalletaccnameTXT_LABEL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                                .addGap(47, 47, 47)
                                                .addComponent(ewalletnumberTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(bankaccnumber_review, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(bankaccname_review, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(ewalletaccname_review, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(platform_review, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(referencenotes_review, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(254, 254, 254)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(currenrtbalance, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                        .addContainerGap())))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(banktransfer_title, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(250, 250, 250)
                                .addComponent(return_banktransferpage, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(107, 107, 107)
                                        .addComponent(recipientaccnumberTXT_LABEL3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(total__review, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(feesLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addComponent(notes))))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(transferdetails_label, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(connect_accnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bank_userid, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(ewalletaccname, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(ewalletnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(platform, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(execute_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(currentdate, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(recipientaccnumberTXT_LABEL2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(referencenotesTXT_LABEL1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(referencenotes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(amount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(transactionreview_label, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(145, 145, 145))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(amount_review, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(date_review, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(ewalletnumber_review, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(sendmoney_rbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftpanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(return_banktransferpage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(banktransfer_title, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transferdetails_label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transactionreview_label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bankaccnumber_review)
                        .addGap(18, 18, 18)
                        .addComponent(bankaccname_review)
                        .addGap(18, 18, 18)
                        .addComponent(ewalletnumber_review)
                        .addGap(18, 18, 18)
                        .addComponent(ewalletaccname_review)
                        .addGap(17, 17, 17)
                        .addComponent(platform_review)
                        .addGap(18, 18, 18)
                        .addComponent(referencenotes_review)
                        .addGap(18, 18, 18)
                        .addComponent(amount_review))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(connect_accnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bankaccountnumberTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bank_userid, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bankaccountnumberTXT_LABEL1))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ewalletnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ewalletnumberTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ewalletaccname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ewalletaccnameTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(platform, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(platformTXT_LABEL))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(referencenotes, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(referencenotesTXT_LABEL1)
                            .addComponent(date_review))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recipientaccnumberTXT_LABEL2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(currentdate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recipientaccnumberTXT_LABEL3)))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(feesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(total__review)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(execute_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendmoney_rbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(notes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(currenrtbalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(23, 23, 23))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void return_banktransferpageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_return_banktransferpageMouseClicked
        if (parentPage != null) {
            parentPage.setVisible(true); // Show the existing parent page
            this.dispose(); // Close the current TransferToBank page
        }
    }//GEN-LAST:event_return_banktransferpageMouseClicked

    private void platformActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_platformActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_platformActionPerformed

    private void execute_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_execute_btnMouseClicked
        // Validate inputs
        String walletNumberInput = ewalletnumber.getText().trim();
        String walletNameInput = ewalletaccname.getText().trim();
        String referenceNotesInput = referencenotes.getText().trim();
        String amountInput = amount.getText().trim();

        if (walletNumberInput.isEmpty() || !walletNumberInput.matches("^09\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "Invalid E-Wallet Number. It must start with '09' and contain exactly 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (walletNameInput.isEmpty() || !walletNameInput.matches("[a-zA-Z ]+")) {
            JOptionPane.showMessageDialog(this, "Invalid E-Wallet Account Name. Only alphabetic characters are allowed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (amountInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Amount cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double transferAmount = Double.parseDouble(amountInput); // Parse the amount
        fees = calculateFees(transferAmount);
        double totalDeduction = transferAmount + fees;

        // Check for insufficient balance
        if (totalDeduction > currentBalance) {
            JOptionPane.showMessageDialog(this, "Insufficient balance. Please enter a smaller amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display transaction review
        bankaccnumber_review.setText("Bank Account Number: " + senderAccountNumber);
        bankaccname_review.setText("Bank Account Name: " + bank_userid.getText());
        ewalletnumber_review.setText("E-Wallet Number: " + walletNumberInput);
        ewalletaccname_review.setText("E-Wallet Account Name: " + walletNameInput);
        platform_review.setText("Platform: " + platform.getSelectedItem().toString());
        referencenotes_review.setText("Reference/Notes: " + referenceNotesInput);
        date_review.setText("Date: " + currentdate.getText());
        amount_review.setText("Amount: ₱" + new DecimalFormat("#,##0.00").format(transferAmount));
        feesLabel.setText("Fees: ₱" + new DecimalFormat("#,##0.00").format(fees));
        total__review.setText("Total: ₱" + new DecimalFormat("#,##0.00").format(totalDeduction));

        sendmoney_rbtn.setEnabled(true);

    }//GEN-LAST:event_execute_btnMouseClicked

    // Helper method inside your class
    private boolean deductBalanceFromDB(Connection conn, String userID, double amountToDeduct) {
        try {
            String sql = "UPDATE EmployeeIncome SET TotalIncome = TotalIncome - ? WHERE UserID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, amountToDeduct);
            pst.setString(2, userID);

            int rowsAffected = pst.executeUpdate();
            pst.close();

            if (rowsAffected == 0) {
                System.err.println("UserID not found or deduction failed.");
                return false;
            }

            // If auto-commit is false, commit explicitly:
            // conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void sendmoney_rbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendmoney_rbtnMouseClicked
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to send money?", "Confirm Transaction", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {
            String currentUserID = bank_userid.getText().trim();  // <-- Get UserID from input field

            String walletNumberInput = ewalletnumber.getText().trim();
            String walletNameInput = ewalletaccname.getText().trim();
            String amountInput = amount.getText().trim();
            String referenceNotesInput = referencenotes.getText().trim();

            // Validation checks
            if (currentUserID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter your UserID.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (walletNumberInput.isEmpty() || walletNameInput.isEmpty() || amountInput.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!walletNumberInput.matches("^09\\d{9}$")) {
                JOptionPane.showMessageDialog(this, "Invalid E-Wallet Number. It must start with '09' and contain exactly 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!walletNameInput.matches("[a-zA-Z ]+")) {
                JOptionPane.showMessageDialog(this, "Invalid E-Wallet Account Name. Only alphabetic characters are allowed.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double transferAmount;
            try {
                transferAmount = Double.parseDouble(amountInput);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Amount must be a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            fees = calculateFees(transferAmount);
            double totalDeduction = transferAmount + fees;

            if (totalDeduction > currentBalance) {
                JOptionPane.showMessageDialog(this, "Insufficient balance. Please enter a smaller amount.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Deduct from DB using the UserID from the text field
            if (!deductBalanceFromDB(conn, currentUserID, totalDeduction)) {
                JOptionPane.showMessageDialog(this, "Failed to deduct balance from database. Transaction cancelled.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Deduct balance locally only if DB update was successful
            currentBalance -= totalDeduction;
            currenrtbalance.setText("Current Balance: ₱" + new DecimalFormat("#,##0.00").format(currentBalance));

            try {
                String currentDate = java.time.LocalDateTime.now().toString(); // You may format this if needed
                String latestRef = "REF" + System.currentTimeMillis(); // Use timestamp for unique REF

                // Insert new transaction into BankTransfer
                String insertQuery = "INSERT INTO BankTransfer (UserID, Date, REF, RecipName, RecipNum, Accnum, Amount, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement insertPst = conn.prepareStatement(insertQuery);
                insertPst.setString(1, currentUserID);
                insertPst.setString(2, currentDate);
                insertPst.setString(3, latestRef);
                insertPst.setString(4, walletNameInput);
                insertPst.setString(5, walletNumberInput);
                insertPst.setString(6, connect_accnumber.getText().trim());
                insertPst.setDouble(7, transferAmount);
                insertPst.setString(8, "Success");

                int rowsInserted = insertPst.executeUpdate();
                insertPst.close();

                parentPage.populateBankTransferHistoryTable();
                parentPage.updateBankBalanceDisplay();

                if (rowsInserted <= 0) {
                    JOptionPane.showMessageDialog(this, "Transaction succeeded, but failed to log into BankTransfer table.", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Failed to insert BankTransfer record: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this, "Transaction Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

            sendmoney_rbtn.setEnabled(false);

            resetReview();

            resetFields();
        }
    }//GEN-LAST:event_sendmoney_rbtnMouseClicked

    private void navigation_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_dashboardMouseClicked
        EMP_Dashboard dashboard = new EMP_Dashboard(null);
        dashboard.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_navigation_dashboardMouseClicked

    private void navigation_ewalletMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_ewalletMouseClicked
        EWallet_Page ewallet = new EWallet_Page(null);
        ewallet.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_navigation_ewalletMouseClicked

    private void navigation_banktransferMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_banktransferMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_navigation_banktransferMouseClicked

    private void sendmoney_rbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendmoney_rbtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sendmoney_rbtnActionPerformed

    private void bank_useridActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bank_useridActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bank_useridActionPerformed

    private void resetReview() {
        bankaccnumber_review.setText("Bank Account Number:");
        bankaccname_review.setText("Bank Account Name:");
        ewalletnumber_review.setText("E-Wallet Number:");
        ewalletaccname_review.setText("E-Wallet Account Name:");
        platform_review.setText("Platform:");
        referencenotes_review.setText("Reference/Notes:");
        date_review.setText("Date:");
        amount_review.setText("Amount:");
        feesLabel.setText("Fees:");
        total__review.setText("Total:");
    }

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
            java.util.logging.Logger.getLogger(TransferToEWallet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransferToEWallet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransferToEWallet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransferToEWallet.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new TransferToEWallet(
                        "1234 5678 9012 3456", // Dummy account number
                        "John Doe", // Dummy account name
                        100000.00, // Dummy balance
                        null // No parent page for testing
                ).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amount;
    private javax.swing.JLabel amount_review;
    private javax.swing.JTextField bank_userid;
    private javax.swing.JLabel bankaccname_review;
    private javax.swing.JLabel bankaccnumber_review;
    private javax.swing.JLabel bankaccountnumberTXT_LABEL;
    private javax.swing.JLabel bankaccountnumberTXT_LABEL1;
    private javax.swing.JLabel banktransfer_title;
    private javax.swing.JTextField connect_accnumber;
    private javax.swing.JLabel credits;
    private javax.swing.JLabel currenrtbalance;
    private javax.swing.JTextField currentdate;
    private javax.swing.JLabel date_review;
    private javax.swing.JTextField ewalletaccname;
    private javax.swing.JLabel ewalletaccnameTXT_LABEL;
    private javax.swing.JLabel ewalletaccname_review;
    private javax.swing.JTextField ewalletnumber;
    private javax.swing.JLabel ewalletnumberTXT_LABEL;
    private javax.swing.JLabel ewalletnumber_review;
    private javax.swing.JButton execute_btn;
    private javax.swing.JLabel feesLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPanel leftpanel2;
    private javax.swing.JLabel navigation_banktransfer;
    private javax.swing.JLabel navigation_dashboard;
    private javax.swing.JLabel navigation_ewallet;
    private javax.swing.JLabel notes;
    private javax.swing.JComboBox<String> platform;
    private javax.swing.JLabel platformTXT_LABEL;
    private javax.swing.JLabel platform_review;
    private javax.swing.JLabel recipientaccnumberTXT_LABEL2;
    private javax.swing.JLabel recipientaccnumberTXT_LABEL3;
    private javax.swing.JTextField referencenotes;
    private javax.swing.JLabel referencenotesTXT_LABEL1;
    private javax.swing.JLabel referencenotes_review;
    private javax.swing.JLabel return_banktransferpage;
    private javax.swing.JButton sendmoney_rbtn;
    private javax.swing.JLabel total__review;
    private javax.swing.JLabel transactionreview_label;
    private javax.swing.JLabel transferdetails_label;
    // End of variables declaration//GEN-END:variables
}
