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
public class TransferToBank extends javax.swing.JFrame {

    Connection conn;

    private String senderAccountNumber;
    private String senderAccountName;
    private Bank_Transfer parentPage;
    private double currentBalance = 100000.00; // Initial Current Balance
    private double fees = 0.00; // Fee amount
    private EMP_Dashboard empDashboard;
    private String userID;  // You must assign this somewhere in your class or constructor

    public TransferToBank(String accountNumber, String accountName, double balance, Bank_Transfer parentPage) {

        this.conn = javaconnect.ConnectDb();

        initComponents();
        setLocationRelativeTo(null);
        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state

        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        setTitle("Payroll Swift"); // ✅ Custom window title

        navigation_banktransfer.setEnabled(false);
        navigation_banktransfer.setForeground(Color.GRAY);

        setupRestrictedAccountNumberInput(connect_accnumber); // Restrict account number input
        setupRestrictedAccountNumberInput(recipientaccnumber); // Restrict recipient account number input
        setupAmountInputListener(); // Restrict amount input to integers
        updateCurrentBalanceDisplay(); // Set initial balance

        this.senderAccountNumber = accountNumber;
        this.senderAccountName = accountName;
        this.currentBalance = balance;
        this.parentPage = parentPage;

        connect_accnumber.setText(senderAccountNumber);
        bank_userid.setText(senderAccountName);
        currenrtbalance.setText("Current Balance: ₱" + new DecimalFormat("#,##0.00").format(currentBalance));
        setCurrentDate();

        sendmoney_rbtn.setEnabled(false);

        setLatestUserId();
        bank_userid.setEnabled(false);

        updateConnectedBankAccount();
        connect_accnumber.setEditable(false);

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

    // Add this default constructor to the TransferToBank class
    public TransferToBank() {
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

    // Automatically set the current date in the date field
    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date.setText(sdf.format(new Date()));
    }

    // Update the current balance display
    private void updateCurrentBalanceDisplay() {
        currenrtbalance.setText("Current Balance: ₱" + new DecimalFormat("#,##0.00").format(currentBalance));
    }

    // Restrict account number input to digits and spaces only
    private void setupRestrictedAccountNumberInput(javax.swing.JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                restrictAccountNumberInput(textField);
            }

            public void removeUpdate(DocumentEvent e) {
                restrictAccountNumberInput(textField);
            }

            public void changedUpdate(DocumentEvent e) {
                restrictAccountNumberInput(textField);
            }
        });
    }

    private void restrictAccountNumberInput(javax.swing.JTextField textField) {
        String input = textField.getText();
        if (!input.matches("[\\d ]*")) { // Allow only digits and spaces
            JOptionPane.showMessageDialog(this, "Account number must contain only digits and spaces.", "Error", JOptionPane.ERROR_MESSAGE);
            textField.setText(input.replaceAll("[^\\d ]", "")); // Remove invalid characters
        }

        // Limit account number to 16 digits (excluding spaces)
        String digitsOnly = input.replaceAll(" ", ""); // Remove spaces for validation
        if (digitsOnly.length() > 16) {
            JOptionPane.showMessageDialog(this, "Account number cannot exceed 16 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            textField.setText(digitsOnly.substring(0, 16)); // Truncate to 16 digits
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

    // Validate name (only alphabetic characters)
    private boolean isValidName(String name) {
        return name.matches("[a-zA-Z ]+");
    }

    // Validate account number format (user-entered with spaces)
    private boolean isValidAccountNumber(String accountNumber) {
        return accountNumber.matches("\\d{4} \\d{4} \\d{4} \\d{4}");
    }

    // Reformat account number for review
    private String formatAccountNumberForReview(String accountNumber) {
        return accountNumber.replace(" ", "-"); // Replace spaces with dashes
    }

    private void resetFields() {
        recipientaccnumber.setText(""); // Clear recipient account number
        recipientaccname.setText(""); // Clear recipient account name
        amount.setText(""); // Clear amount
        title.setText(""); // Clear title
        setCurrentDate(); // Reset the date to the current date
        fees = 0.00; // Reset fees
        feesLabel.setText("Fees: ₱0.00"); // Reset fees display
        total__review.setText("Total Deduction: ₱0.00"); // Reset total deduction display

    }

    // Calculate fees dynamically based on guidelines
    private double calculateFees(double transferAmount) {
        double flatFee = 25.00; // Default flat fee
        double percentageFee = transferAmount * 0.01; // 1% of transaction amount
        double realTimeFee = Math.max(20.00, transferAmount * 0.02); // 20 PHP or 2% of transaction amount

        // Choose the fee dynamically (based on guidelines)
        return Math.max(flatFee, Math.max(percentageFee, realTimeFee));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        background = new javax.swing.JPanel();
        leftpanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        credits = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        navigation_banktransfer = new javax.swing.JLabel();
        navigation_ewallet = new javax.swing.JLabel();
        navigation_dashboard = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        banktransfer_title = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        transactionreview_label = new javax.swing.JLabel();
        transferdetails_label = new javax.swing.JLabel();
        accountnumberTXT_LABEL = new javax.swing.JLabel();
        connect_accnumber = new javax.swing.JTextField();
        bank_userid = new javax.swing.JTextField();
        accountnameTXT_LABEL = new javax.swing.JLabel();
        recipientaccnumber = new javax.swing.JTextField();
        recipientaccnumberTXT_LABEL = new javax.swing.JLabel();
        recipientaccname = new javax.swing.JTextField();
        recipientaccnameTXT_LABEL = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        countryTXT_LABEL = new javax.swing.JLabel();
        country = new javax.swing.JComboBox<>();
        jSeparator3 = new javax.swing.JSeparator();
        title = new javax.swing.JTextField();
        titleTXT_LABEL = new javax.swing.JLabel();
        amount = new javax.swing.JTextField();
        amountTXT_LABEL = new javax.swing.JLabel();
        date = new javax.swing.JTextField();
        dateTXT_LABEL = new javax.swing.JLabel();
        notes = new javax.swing.JLabel();
        execute_btn = new javax.swing.JButton();
        accnumber_review = new javax.swing.JLabel();
        bankaccname_review = new javax.swing.JLabel();
        recipientaccnumber_review = new javax.swing.JLabel();
        recipientaccname_review = new javax.swing.JLabel();
        country_review = new javax.swing.JLabel();
        title_review = new javax.swing.JLabel();
        amount_review = new javax.swing.JLabel();
        date_review = new javax.swing.JLabel();
        sendmoney_rbtn = new javax.swing.JButton();
        feesLabel = new javax.swing.JLabel();
        total__review = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        currenrtbalance = new javax.swing.JLabel();
        return_banktransferpage = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        background.setBackground(new java.awt.Color(255, 255, 255));

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
                        .addGap(15, 15, 15)
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
                .addContainerGap(17, Short.MAX_VALUE))
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
                .addComponent(credits, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12))
        );

        banktransfer_title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        banktransfer_title.setForeground(new java.awt.Color(0, 0, 0));
        banktransfer_title.setText("BANK TO BANK TRANSFER");

        transactionreview_label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        transactionreview_label.setForeground(new java.awt.Color(0, 0, 0));
        transactionreview_label.setText("Transaction Review");

        transferdetails_label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        transferdetails_label.setForeground(new java.awt.Color(0, 0, 0));
        transferdetails_label.setText("Transfer Details");

        accountnumberTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        accountnumberTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        accountnumberTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        accountnumberTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        accountnumberTXT_LABEL.setText("Bank Account Number");

        connect_accnumber.setBackground(new java.awt.Color(255, 255, 255));
        connect_accnumber.setForeground(new java.awt.Color(0, 0, 0));

        bank_userid.setBackground(new java.awt.Color(255, 255, 255));
        bank_userid.setForeground(new java.awt.Color(0, 0, 0));

        accountnameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        accountnameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        accountnameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        accountnameTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        accountnameTXT_LABEL.setText("User ID:");

        recipientaccnumber.setBackground(new java.awt.Color(255, 255, 255));
        recipientaccnumber.setForeground(new java.awt.Color(0, 0, 0));

        recipientaccnumberTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        recipientaccnumberTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientaccnumberTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        recipientaccnumberTXT_LABEL.setText("Recipient Acc. Number");

        recipientaccname.setBackground(new java.awt.Color(255, 255, 255));
        recipientaccname.setForeground(new java.awt.Color(0, 0, 0));

        recipientaccnameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        recipientaccnameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientaccnameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        recipientaccnameTXT_LABEL.setText("Recipient Acc. Name");

        countryTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        countryTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        countryTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        countryTXT_LABEL.setText("Country");

        country.setBackground(new java.awt.Color(255, 255, 255));
        country.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        country.setForeground(new java.awt.Color(0, 0, 51));
        country.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Afghanistan", "Albania", "Algeria", "Andorra", "Angola", "Antigua and Barbuda", "Argentina", "Armenia", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana", "Brazil", "Brunei", "Bulgaria", "Burkina Faso", "Burundi", "Cabo Verde", "Cambodia", "Cameroon", "Canada", "Central African Republic", "Chad", "Chile", "China", "Colombia", "Comoros", "Congo (Congo-Brazzaville)", "Congo (Democratic Republic)", "Costa Rica", "Croatia", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Eswatini", "Ethiopia", "Fiji", "Finland", "France", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Greece", "Grenada", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Honduras", "Hungary", "Iceland", "India", "Indonesia", "Iran", "Iraq", "Ireland", "Israel", "Italy", "Ivory Coast", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea (North)", "Korea (South)", "Kuwait", "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein", "Lithuania", "Luxembourg", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mexico", "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Morocco", "Mozambique", "Myanmar (Burma)", "Namibia", "Nauru", "Nepal", "Netherlands", "New Zealand", "Nicaragua", "Niger", "Nigeria", "North Macedonia", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Poland", "Portugal", "Qatar", "Romania", "Russia", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone", "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Sudan", "Spain", "Sri Lanka", "Sudan", "Suriname", "Sweden", "Switzerland", "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates (UAE)", "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Vanuatu", "Vatican City", "Venezuela", "Vietnam", "Yemen", "Zambia", "Zimbabwe" }));
        country.setSelectedIndex(137);
        country.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryActionPerformed(evt);
            }
        });

        title.setBackground(new java.awt.Color(255, 255, 255));
        title.setForeground(new java.awt.Color(0, 0, 0));

        titleTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        titleTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        titleTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        titleTXT_LABEL.setText("Title");

        amount.setBackground(new java.awt.Color(255, 255, 255));
        amount.setForeground(new java.awt.Color(0, 0, 0));

        amountTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        amountTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        amountTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        amountTXT_LABEL.setText("Amount");

        date.setBackground(new java.awt.Color(255, 255, 255));
        date.setForeground(new java.awt.Color(0, 0, 0));

        dateTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        dateTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        dateTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        dateTXT_LABEL.setText("Date");

        notes.setBackground(new java.awt.Color(0, 0, 0));
        notes.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        notes.setForeground(new java.awt.Color(0, 0, 0));
        notes.setText("Notes: Transfers are irreversible. Confirm details before proceeding.");

        execute_btn.setBackground(new java.awt.Color(0, 51, 51));
        execute_btn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        execute_btn.setForeground(new java.awt.Color(255, 255, 255));
        execute_btn.setText("Execute");
        execute_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                execute_btnMouseClicked(evt);
            }
        });

        accnumber_review.setBackground(new java.awt.Color(0, 0, 0));
        accnumber_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        accnumber_review.setForeground(new java.awt.Color(0, 0, 0));
        accnumber_review.setText("Bank Account Number: ");

        bankaccname_review.setBackground(new java.awt.Color(0, 0, 0));
        bankaccname_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        bankaccname_review.setForeground(new java.awt.Color(0, 0, 0));
        bankaccname_review.setText("User ID:");

        recipientaccnumber_review.setBackground(new java.awt.Color(0, 0, 0));
        recipientaccnumber_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientaccnumber_review.setForeground(new java.awt.Color(0, 0, 0));
        recipientaccnumber_review.setText("Recipient Acc. Number:");

        recipientaccname_review.setBackground(new java.awt.Color(0, 0, 0));
        recipientaccname_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientaccname_review.setForeground(new java.awt.Color(0, 0, 0));
        recipientaccname_review.setText("Recipient Acc. Name:");

        country_review.setBackground(new java.awt.Color(0, 0, 0));
        country_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        country_review.setForeground(new java.awt.Color(0, 0, 0));
        country_review.setText("Country:");

        title_review.setBackground(new java.awt.Color(0, 0, 0));
        title_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        title_review.setForeground(new java.awt.Color(0, 0, 0));
        title_review.setText("Title:");

        amount_review.setBackground(new java.awt.Color(0, 0, 0));
        amount_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        amount_review.setForeground(new java.awt.Color(0, 0, 0));
        amount_review.setText("Amount:");

        date_review.setBackground(new java.awt.Color(0, 0, 0));
        date_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        date_review.setForeground(new java.awt.Color(0, 0, 0));
        date_review.setText("Date:");

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

        feesLabel.setBackground(new java.awt.Color(0, 0, 0));
        feesLabel.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        feesLabel.setForeground(new java.awt.Color(0, 0, 0));
        feesLabel.setText("Fees:");

        total__review.setBackground(new java.awt.Color(0, 0, 0));
        total__review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        total__review.setForeground(new java.awt.Color(0, 0, 0));
        total__review.setText("Total:");

        currenrtbalance.setBackground(new java.awt.Color(0, 0, 0));
        currenrtbalance.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        currenrtbalance.setForeground(new java.awt.Color(0, 0, 0));
        currenrtbalance.setText("Current Balance:");

        return_banktransferpage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/replay (1).png"))); // NOI18N
        return_banktransferpage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                return_banktransferpageMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout backgroundLayout = new javax.swing.GroupLayout(background);
        background.setLayout(backgroundLayout);
        backgroundLayout.setHorizontalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addComponent(leftpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                        .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(backgroundLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(backgroundLayout.createSequentialGroup()
                                            .addComponent(recipientaccnumberTXT_LABEL)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(recipientaccnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(backgroundLayout.createSequentialGroup()
                                            .addComponent(recipientaccnameTXT_LABEL)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(recipientaccname, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, backgroundLayout.createSequentialGroup()
                                            .addGap(94, 94, 94)
                                            .addComponent(countryTXT_LABEL)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(country, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(transferdetails_label, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(backgroundLayout.createSequentialGroup()
                                            .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(amountTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(titleTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(dateTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(execute_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addComponent(notes))))
                            .addGroup(backgroundLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(accountnumberTXT_LABEL, javax.swing.GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE)
                                    .addComponent(accountnameTXT_LABEL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(bank_userid, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(connect_accnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                                .addComponent(transactionreview_label, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(144, 144, 144))
                            .addComponent(accnumber_review, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bankaccname_review, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 354, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                                .addComponent(recipientaccnumber_review, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                                .addComponent(country_review, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                                .addComponent(title_review, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                                .addComponent(amount_review, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                                .addComponent(date_review, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                                .addComponent(feesLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                                .addComponent(recipientaccname_review, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(backgroundLayout.createSequentialGroup()
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                                .addComponent(sendmoney_rbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(51, 51, 51))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, backgroundLayout.createSequentialGroup()
                                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(currenrtbalance, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(total__review, javax.swing.GroupLayout.PREFERRED_SIZE, 348, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())))
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, backgroundLayout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addComponent(banktransfer_title, javax.swing.GroupLayout.PREFERRED_SIZE, 442, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(return_banktransferpage, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, backgroundLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 753, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        backgroundLayout.setVerticalGroup(
            backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(backgroundLayout.createSequentialGroup()
                .addContainerGap(9, Short.MAX_VALUE)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(return_banktransferpage, javax.swing.GroupLayout.DEFAULT_SIZE, 57, Short.MAX_VALUE)
                    .addComponent(banktransfer_title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(backgroundLayout.createSequentialGroup()
                                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(transferdetails_label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(transactionreview_label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(14, 14, 14)
                                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(connect_accnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(accountnumberTXT_LABEL)
                                    .addComponent(accnumber_review))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(bank_userid, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(accountnameTXT_LABEL))
                                .addGap(18, 18, 18)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(backgroundLayout.createSequentialGroup()
                                .addComponent(bankaccname_review)
                                .addGap(18, 18, 18)
                                .addComponent(recipientaccnumber_review)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(recipientaccnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recipientaccnumberTXT_LABEL)
                            .addComponent(recipientaccname_review))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(recipientaccname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recipientaccnameTXT_LABEL)
                            .addComponent(country_review))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(country, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(countryTXT_LABEL)
                            .addComponent(title_review))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(amount_review))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(date_review, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(title, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(titleTXT_LABEL)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(amountTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(date, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dateTXT_LABEL)))
                    .addGroup(backgroundLayout.createSequentialGroup()
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(feesLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(total__review)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sendmoney_rbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(execute_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(backgroundLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(currenrtbalance, javax.swing.GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE)
                    .addComponent(notes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(background, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void countryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_countryActionPerformed

    private void execute_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_execute_btnMouseClicked
        // Validate inputs

        String connectaccnumberInput = connect_accnumber.getText().trim();
        String recipientAccNumberInput = recipientaccnumber.getText().trim();
        String recipientAccNameInput = recipientaccname.getText().trim();
        String amountInput = amount.getText().trim();

        // Account Number Validation
        // Recipient Account Number Validation
        if (!isValidAccountNumber(recipientAccNumberInput)) {
            JOptionPane.showMessageDialog(this, "Invalid Recipient Account Number. Format must be '1234 1234 1234 1234' with spaces.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidName(recipientAccNameInput)) {
            JOptionPane.showMessageDialog(this, "Invalid Recipient Account Name. Only alphabetic characters are allowed.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Amount Validation
        if (amountInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Amount cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double transferAmount = Double.parseDouble(amountInput); // Parse the amount

        // Calculate fees and total to be deducted
        fees = calculateFees(transferAmount);
        double totalDeduction = transferAmount + fees;

        // Check for insufficient balance
        if (totalDeduction > currentBalance) {
            JOptionPane.showMessageDialog(this, "Insufficient balance. Please enter a smaller amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Display transaction review
        accnumber_review.setText("Bank Account Number: " + formatAccountNumberForReview(connectaccnumberInput));
        bankaccname_review.setText("Bank Account Name: " + bank_userid.getText());
        recipientaccnumber_review.setText("Recipient Acc. Number: " + formatAccountNumberForReview(recipientAccNumberInput));
        recipientaccname_review.setText("Recipient Acc. Name: " + recipientAccNameInput);
        country_review.setText("Country: " + country.getSelectedItem().toString());
        title_review.setText("Title: " + title.getText().trim());
        date_review.setText("Date: " + date.getText().trim());
        amount_review.setText("Amount: ₱" + new DecimalFormat("#,##0.00").format(transferAmount));
        feesLabel.setText("Fees: ₱" + new DecimalFormat("#,##0.00").format(fees));
        total__review.setText("Total: ₱" + new DecimalFormat("#,##0.00").format(totalDeduction));

        sendmoney_rbtn.setEnabled(true);
    }//GEN-LAST:event_execute_btnMouseClicked

    private boolean deductBalanceFromDB(Connection conn, String userID, double amountToDeduct) {
        try {
            String sql = "UPDATE EmployeeIncome SET TotalIncome = TotalIncome - ? WHERE UserID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, amountToDeduct);
            pst.setString(2, userID);

            int rowsAffected = pst.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);
            pst.close();

            if (rowsAffected == 0) {
                System.err.println("UserID not found or deduction failed.");
                return false;
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void sendmoney_rbtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendmoney_rbtnMouseClicked
        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to send money?", "Confirm Transaction", JOptionPane.YES_NO_OPTION);

        if (confirmation != JOptionPane.YES_OPTION) {
            return;
        }

        // Sender info (from input or logged in user)
        String senderUserID = bank_userid.getText().trim();  // sender's UserID
        String senderAccountNumber = connect_accnumber.getText().trim();  // optional, if you use it

        // Recipient info
        String recipientName = recipientaccname.getText().trim();
        String recipientNumber = recipientaccnumber.getText().trim();

        String amountInput = amount.getText().trim();

        // Validate inputs
        if (senderUserID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter your UserID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (recipientName.isEmpty() || recipientNumber.isEmpty() || amountInput.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double transferAmount;
        try {
            transferAmount = Double.parseDouble(amountInput);
            if (transferAmount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be positive.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Calculate fees before checking balance
        double fees = calculateFees(transferAmount);
        double totalDeduction = transferAmount + fees;

        if (totalDeduction > currentBalance) {
            JOptionPane.showMessageDialog(this, "Insufficient balance.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Deduct balance from DB using sender's UserID
        boolean success = deductBalanceFromDB(conn, senderUserID, totalDeduction);
        if (!success) {
            JOptionPane.showMessageDialog(this, "Transaction failed. Could not update balance in database.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Deduct balance locally and update UI
        currentBalance -= totalDeduction;
        currenrtbalance.setText("Current Balance: ₱" + new DecimalFormat("#,##0.00").format(currentBalance));

        // INSERT transaction record to BankTransfer table
        try {
            String currentDate = java.time.LocalDateTime.now().toString(); // You may format this if needed
            String latestRef = "REF" + System.currentTimeMillis(); // Use timestamp for unique REF

            // Insert transaction into BankTransfer table
            String insertQuery = "INSERT INTO BankTransfer (UserID, Date, REF, RecipName, RecipNum, Accnum, Amount, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
            insertStmt.setString(1, senderUserID);
            insertStmt.setString(2, currentDate);
            insertStmt.setString(3, latestRef);
            insertStmt.setString(4, recipientName);
            insertStmt.setString(5, recipientNumber);
            insertStmt.setString(6, senderAccountNumber);
            insertStmt.setDouble(7, transferAmount);
            insertStmt.setString(8, "Success");

            int rows = insertStmt.executeUpdate();
            insertStmt.close();

            if (rows <= 0) {
                JOptionPane.showMessageDialog(this, "Transaction succeeded, but logging failed.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to record transaction in BankTransfer table:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        parentPage.updateBankBalanceDisplay();
        parentPage.populateBankTransferHistoryTable();

        JOptionPane.showMessageDialog(this, "Transaction Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Disable send button and reset forms
        sendmoney_rbtn.setEnabled(false);
        resetReview();
        resetFields();
    }//GEN-LAST:event_sendmoney_rbtnMouseClicked

    private void resetReview() {
        // Reset review labels (or any other UI elements used for review)
        accnumber_review.setText("Bank Account Number:");
        bankaccname_review.setText("Bank Account Name:");
        recipientaccnumber_review.setText("Recipient Acc. Number:");
        recipientaccname_review.setText("Recipient Acc. Name:");
        country_review.setText("Country: ");
        title_review.setText("Title: ");
        amount_review.setText("Amount:");
        date_review.setText("Date: ");
        feesLabel.setText("Fees:");
        total__review.setText("Total:");
    }

    private void return_banktransferpageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_return_banktransferpageMouseClicked
        if (parentPage != null) {
            parentPage.setVisible(true); // Show the existing parent page
            this.dispose(); // Close the current TransferToBank page
        } else {
            // In case parentPage is null, handle gracefully
            JOptionPane.showMessageDialog(this, "Parent page not available.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_return_banktransferpageMouseClicked

    private void navigation_banktransferMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_banktransferMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_navigation_banktransferMouseClicked

    private void navigation_ewalletMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_ewalletMouseClicked
        EWallet_Page ewallet = new EWallet_Page(null);
        ewallet.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_navigation_ewalletMouseClicked

    private void navigation_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_dashboardMouseClicked
        // TODO add your handling code here:
        EMP_Dashboard dashboard = new EMP_Dashboard(null);
        dashboard.setVisible(true);
        this.dispose();

    }//GEN-LAST:event_navigation_dashboardMouseClicked

    private void sendmoney_rbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendmoney_rbtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sendmoney_rbtnActionPerformed

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
            java.util.logging.Logger.getLogger(TransferToBank.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TransferToBank.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TransferToBank.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TransferToBank.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // Use the default constructor for testing
                new TransferToBank(
                        null,
                        "John Doe", // Example account name
                        100000.00, // Example balance
                        null // No parent page for testing
                ).setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accnumber_review;
    private javax.swing.JLabel accountnameTXT_LABEL;
    private javax.swing.JLabel accountnumberTXT_LABEL;
    private javax.swing.JTextField amount;
    private javax.swing.JLabel amountTXT_LABEL;
    private javax.swing.JLabel amount_review;
    private javax.swing.JPanel background;
    private javax.swing.JTextField bank_userid;
    private javax.swing.JLabel bankaccname_review;
    private javax.swing.JLabel banktransfer_title;
    private javax.swing.JTextField connect_accnumber;
    private javax.swing.JComboBox<String> country;
    private javax.swing.JLabel countryTXT_LABEL;
    private javax.swing.JLabel country_review;
    private javax.swing.JLabel credits;
    private javax.swing.JLabel currenrtbalance;
    private javax.swing.JTextField date;
    private javax.swing.JLabel dateTXT_LABEL;
    private javax.swing.JLabel date_review;
    private javax.swing.JButton execute_btn;
    private javax.swing.JLabel feesLabel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPanel leftpanel;
    private javax.swing.JLabel navigation_banktransfer;
    private javax.swing.JLabel navigation_dashboard;
    private javax.swing.JLabel navigation_ewallet;
    private javax.swing.JLabel notes;
    private javax.swing.JTextField recipientaccname;
    private javax.swing.JLabel recipientaccnameTXT_LABEL;
    private javax.swing.JLabel recipientaccname_review;
    private javax.swing.JTextField recipientaccnumber;
    private javax.swing.JLabel recipientaccnumberTXT_LABEL;
    private javax.swing.JLabel recipientaccnumber_review;
    private javax.swing.JLabel return_banktransferpage;
    private javax.swing.JButton sendmoney_rbtn;
    private javax.swing.JTextField title;
    private javax.swing.JLabel titleTXT_LABEL;
    private javax.swing.JLabel title_review;
    private javax.swing.JLabel total__review;
    private javax.swing.JLabel transactionreview_label;
    private javax.swing.JLabel transferdetails_label;
    // End of variables declaration//GEN-END:variables
}
