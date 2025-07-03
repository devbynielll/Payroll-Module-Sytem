
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author danie
 */
public class EWallet_Send extends javax.swing.JFrame {

    private EWallet_Page eWalletPage;
    private EMP_Dashboard empDashboard;

    Connection conn;

    /**
     * Creates new form EWallet_Send
     */
    public EWallet_Send(EMP_Dashboard empDashboard, EWallet_Page eWalletPage) {

        this.conn = javaconnect.ConnectDb();

        this.empDashboard = empDashboard;
        this.eWalletPage = eWalletPage;
        initComponents();
        setLocationRelativeTo(null);
        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state

        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        navigation_ewallet.setEnabled(false);
        navigation_ewallet.setForeground(Color.GRAY);

        setTitle("Payroll Swift"); // ✅ Custom window title

        // Set default values for e-wallet number & account name
        ewalletnumber.setText("09193928187");
        ewallet_userid.setText("Juan Dela Cruz");

        // Make them uneditable
        ewalletnumber.setEditable(false);
        ewallet_userid.setEditable(false);

        // Auto-set current date
        setCurrentDate();

        // Sync balance from EWallet_Page
        updateCurrentBalanceDisplay();

        // Disable Send Money button initially
        sendmoney_btn.setEnabled(false);

        // Restrict recipient number & amount fields
        addInputRestrictions();

        setLatestUserId();
        ewallet_userid.setEnabled(false);
    }

    private void setLatestUserId() {
        String latestUserId = "";
        String sql = "SELECT UserID FROM Login ORDER BY LoginTime DESC LIMIT 1";

        try (PreparedStatement pst = conn.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                latestUserId = rs.getString("UserID");
                ewallet_userid.setText(latestUserId);

                // Update ewallet number based on this user ID
                updateEwalletNumberFromUserId(latestUserId);
            } else {
                ewallet_userid.setText("");
                ewalletnumber.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to fetch latest user ID.", "Error", JOptionPane.ERROR_MESSAGE);
            ewallet_userid.setText("");
            ewalletnumber.setText("");
        }
    }

    private void setCurrentDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        currentdate.setText(today.format(formatter));
        currentdate.setEditable(false); // Make the date field uneditable
    }

    private void updateCurrentBalanceDisplay() {
        double eWalletBalance = SharedData.getCurrentBalance();
        currentbalance.setText("E-Wallet Balance: ₱" + formatMoney(eWalletBalance));
    }

    private double calculateFee(double amount) {
        return amount * 0.02; // Fee is 2% of the transaction amount
    }

    private String formatMoney(double amount) {
        return String.format("%,.2f", amount);
    }

    private void addInputRestrictions() {
        // Restrict recipient number input (must start with '09' and be exactly 11 digits)
        recipientnumber.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c) || recipientnumber.getText().length() >= 11) {
                    e.consume();
                }
            }
        });

        // Restrict amount input to integers only
        amount.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!Character.isDigit(c)) {
                    e.consume();
                }
            }
        });
    }

    private void updateEwalletNumberFromUserId(String userId) {
        String sql = "SELECT Contact_Num FROM PersonalData WHERE UserID = ?";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setString(1, userId);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    String contactNum = rs.getString("Contact_Num");
                    ewalletnumber.setText(contactNum);
                } else {
                    ewalletnumber.setText(""); // Clear if no contact found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve contact number.", "Error", JOptionPane.ERROR_MESSAGE);
            ewalletnumber.setText("");
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
        navigation_dashboard = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        navigation_ewallet = new javax.swing.JLabel();
        navigation_banktransfer = new javax.swing.JLabel();
        credits = new javax.swing.JLabel();
        ewallet_title = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        returnto_banktransferpage = new javax.swing.JLabel();
        transactionreview_label = new javax.swing.JLabel();
        transferdetails_label = new javax.swing.JLabel();
        ewalletnumberTXT_LABEL = new javax.swing.JLabel();
        ewalletnumber = new javax.swing.JTextField();
        ewallet_userid = new javax.swing.JTextField();
        ewalletaccnameTXT_LABEL = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        recipirntaccname = new javax.swing.JTextField();
        recipientnumber = new javax.swing.JTextField();
        recipientaccnameTXT_LABEL = new javax.swing.JLabel();
        recipientTXT_LABEL = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        platform = new javax.swing.JComboBox<>();
        platformTXT_LABEL = new javax.swing.JLabel();
        referencenotesTXT_LABEL1 = new javax.swing.JLabel();
        referencenotes = new javax.swing.JTextField();
        amount = new javax.swing.JTextField();
        recipientaccnumberTXT_LABEL2 = new javax.swing.JLabel();
        recipientaccnumberTXT_LABEL3 = new javax.swing.JLabel();
        currentdate = new javax.swing.JTextField();
        ewalletnumber_review = new javax.swing.JLabel();
        userid_review = new javax.swing.JLabel();
        execute_btn = new javax.swing.JButton();
        recipientnumber_review = new javax.swing.JLabel();
        recipientname_review = new javax.swing.JLabel();
        recipientplatform_review = new javax.swing.JLabel();
        referenceornotes_review = new javax.swing.JLabel();
        amount_review = new javax.swing.JLabel();
        date_review = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        notes = new javax.swing.JLabel();
        fees_review = new javax.swing.JLabel();
        total_review = new javax.swing.JLabel();
        sendmoney_btn = new javax.swing.JButton();
        currentbalance = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        leftpanel.setBackground(new java.awt.Color(0, 0, 51));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AYROLL WIFT (2) (3).png"))); // NOI18N
        jLabel2.setText(" ");

        navigation_dashboard.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_dashboard.setForeground(new java.awt.Color(255, 255, 255));
        navigation_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image.png"))); // NOI18N
        navigation_dashboard.setText("  Dashboard");
        navigation_dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_dashboardMouseClicked(evt);
            }
        });

        jSeparator5.setForeground(new java.awt.Color(255, 255, 255));

        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));

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

        credits.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        credits.setForeground(new java.awt.Color(255, 255, 255));
        credits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        credits.setText("© 2025 Payroll Swift");

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
                .addGap(14, 14, 14))
        );

        ewallet_title.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        ewallet_title.setForeground(new java.awt.Color(0, 0, 0));
        ewallet_title.setText("SEND MONEY");

        returnto_banktransferpage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/replay (1).png"))); // NOI18N
        returnto_banktransferpage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                returnto_banktransferpageMouseClicked(evt);
            }
        });

        transactionreview_label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        transactionreview_label.setForeground(new java.awt.Color(0, 0, 0));
        transactionreview_label.setText("Transaction Review");

        transferdetails_label.setFont(new java.awt.Font("Segoe UI", 1, 20)); // NOI18N
        transferdetails_label.setForeground(new java.awt.Color(0, 0, 0));
        transferdetails_label.setText("Transfer Details");

        ewalletnumberTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        ewalletnumberTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        ewalletnumberTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        ewalletnumberTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ewalletnumberTXT_LABEL.setText("E-Wallet Number");

        ewalletnumber.setBackground(new java.awt.Color(255, 255, 255));
        ewalletnumber.setForeground(new java.awt.Color(0, 0, 0));

        ewallet_userid.setBackground(new java.awt.Color(255, 255, 255));
        ewallet_userid.setForeground(new java.awt.Color(0, 0, 0));

        ewalletaccnameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        ewalletaccnameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        ewalletaccnameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        ewalletaccnameTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        ewalletaccnameTXT_LABEL.setText("User ID");

        recipirntaccname.setBackground(new java.awt.Color(255, 255, 255));
        recipirntaccname.setForeground(new java.awt.Color(0, 0, 0));

        recipientnumber.setBackground(new java.awt.Color(255, 255, 255));
        recipientnumber.setForeground(new java.awt.Color(0, 0, 0));

        recipientaccnameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        recipientaccnameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientaccnameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        recipientaccnameTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        recipientaccnameTXT_LABEL.setText("Recipient Account Name");

        recipientTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        recipientTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        recipientTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        recipientTXT_LABEL.setText(" Recipient Number");

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
        platformTXT_LABEL.setText("Recipient Platform");

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

        recipientaccnumberTXT_LABEL3.setBackground(new java.awt.Color(0, 0, 0));
        recipientaccnumberTXT_LABEL3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientaccnumberTXT_LABEL3.setForeground(new java.awt.Color(0, 0, 0));
        recipientaccnumberTXT_LABEL3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        recipientaccnumberTXT_LABEL3.setText("Date");

        currentdate.setBackground(new java.awt.Color(255, 255, 255));
        currentdate.setForeground(new java.awt.Color(0, 0, 0));

        ewalletnumber_review.setBackground(new java.awt.Color(0, 0, 0));
        ewalletnumber_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        ewalletnumber_review.setForeground(new java.awt.Color(0, 0, 0));
        ewalletnumber_review.setText("E-Wallet Number:");

        userid_review.setBackground(new java.awt.Color(0, 0, 0));
        userid_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        userid_review.setForeground(new java.awt.Color(0, 0, 0));
        userid_review.setText("User ID:");

        execute_btn.setBackground(new java.awt.Color(0, 0, 51));
        execute_btn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        execute_btn.setForeground(new java.awt.Color(255, 255, 255));
        execute_btn.setText("Execute");
        execute_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                execute_btnMouseClicked(evt);
            }
        });

        recipientnumber_review.setBackground(new java.awt.Color(0, 0, 0));
        recipientnumber_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientnumber_review.setForeground(new java.awt.Color(0, 0, 0));
        recipientnumber_review.setText("Recipient Number:");

        recipientname_review.setBackground(new java.awt.Color(0, 0, 0));
        recipientname_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientname_review.setForeground(new java.awt.Color(0, 0, 0));
        recipientname_review.setText("Recipient Name:");

        recipientplatform_review.setBackground(new java.awt.Color(0, 0, 0));
        recipientplatform_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        recipientplatform_review.setForeground(new java.awt.Color(0, 0, 0));
        recipientplatform_review.setText("Recipient Platform:");

        referenceornotes_review.setBackground(new java.awt.Color(0, 0, 0));
        referenceornotes_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        referenceornotes_review.setForeground(new java.awt.Color(0, 0, 0));
        referenceornotes_review.setText("Reference/Notes:");

        amount_review.setBackground(new java.awt.Color(0, 0, 0));
        amount_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        amount_review.setForeground(new java.awt.Color(0, 0, 0));
        amount_review.setText("Amount:");

        date_review.setBackground(new java.awt.Color(0, 0, 0));
        date_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        date_review.setForeground(new java.awt.Color(0, 0, 0));
        date_review.setText("Date:");

        notes.setBackground(new java.awt.Color(0, 0, 0));
        notes.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        notes.setForeground(new java.awt.Color(0, 0, 0));
        notes.setText("Notes: Make sure the wallet number and platform are correct..");

        fees_review.setBackground(new java.awt.Color(0, 0, 0));
        fees_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        fees_review.setForeground(new java.awt.Color(0, 0, 0));
        fees_review.setText("Fees:");

        total_review.setBackground(new java.awt.Color(0, 0, 0));
        total_review.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        total_review.setForeground(new java.awt.Color(0, 0, 0));
        total_review.setText("Total:");

        sendmoney_btn.setBackground(new java.awt.Color(0, 0, 51));
        sendmoney_btn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        sendmoney_btn.setForeground(new java.awt.Color(255, 255, 255));
        sendmoney_btn.setText("Send Money");
        sendmoney_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendmoney_btnMouseClicked(evt);
            }
        });
        sendmoney_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendmoney_btnActionPerformed(evt);
            }
        });

        currentbalance.setBackground(new java.awt.Color(0, 0, 0));
        currentbalance.setFont(new java.awt.Font("Segoe UI Semibold", 0, 18)); // NOI18N
        currentbalance.setForeground(new java.awt.Color(0, 0, 0));
        currentbalance.setText("E-Wallet Balance:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(leftpanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(ewallet_title, javax.swing.GroupLayout.PREFERRED_SIZE, 254, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(returnto_banktransferpage)
                        .addGap(37, 37, 37))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 721, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(ewalletnumberTXT_LABEL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(ewalletaccnameTXT_LABEL, javax.swing.GroupLayout.DEFAULT_SIZE, 178, Short.MAX_VALUE))
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(ewallet_userid, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addComponent(ewalletnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(recipientaccnameTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(recipientTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(platformTXT_LABEL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(12, 12, 12)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(recipirntaccname, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(recipientnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(platform, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(notes)
                                                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(68, 68, 68)
                                                .addComponent(recipientaccnumberTXT_LABEL3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(recipientaccnumberTXT_LABEL2, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(referencenotesTXT_LABEL1, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(referencenotes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(amount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(execute_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(currentdate, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                                        .addComponent(sendmoney_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 255, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(54, 54, 54))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addComponent(transferdetails_label, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(transactionreview_label, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ewalletnumber_review, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(userid_review, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(recipientnumber_review, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(recipientname_review, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(recipientplatform_review, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(referenceornotes_review, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(amount_review, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(date_review, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 292, Short.MAX_VALUE)
                            .addComponent(fees_review, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                            .addComponent(total_review, javax.swing.GroupLayout.DEFAULT_SIZE, 303, Short.MAX_VALUE)
                            .addComponent(currentbalance, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(20, 20, 20))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ewallet_title, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(returnto_banktransferpage, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(transferdetails_label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(transactionreview_label, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ewalletnumberTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ewalletnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ewalletnumber_review, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ewallet_userid, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ewalletaccnameTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userid_review, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(recipientnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recipientTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(recipientnumber_review, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(11, 11, 11)
                        .addComponent(recipientname_review, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(recipirntaccname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recipientaccnameTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(platform, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(platformTXT_LABEL))
                        .addGap(18, 18, 18)
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(referencenotes, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(referencenotesTXT_LABEL1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(amount, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recipientaccnumberTXT_LABEL2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(currentdate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(recipientaccnumberTXT_LABEL3)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(recipientplatform_review, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(referenceornotes_review)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(amount_review, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(date_review)
                        .addGap(8, 8, 8)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(fees_review, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(total_review)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(execute_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sendmoney_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(notes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(currentbalance, javax.swing.GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
                .addGap(15, 15, 15))
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
        if (eWalletPage != null) {
            eWalletPage.setVisible(true); // ✅ Show the EWallet_Page again
            this.dispose(); // ✅ Close EWallet_Send
        } else {
            JOptionPane.showMessageDialog(this, "EWallet_Page reference is missing!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_returnto_banktransferpageMouseClicked

    private void platformActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_platformActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_platformActionPerformed

    private void execute_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_execute_btnMouseClicked
        try {
            double amountToSend = Double.parseDouble(amount.getText());

            // Validate inputs
            if (recipientnumber.getText().length() != 11 || !recipientnumber.getText().startsWith("09")) {
                JOptionPane.showMessageDialog(this, "Recipient number must start with '09' and be 11 digits!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (amountToSend <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Calculate fee & total
            double fee = calculateFee(amountToSend);
            double total = amountToSend + fee;

            // Auto-fill review fields
            ewalletnumber_review.setText("EWallet Number: " + ewalletnumber.getText());
            userid_review.setText("User ID: " + ewallet_userid.getText());
            recipientnumber_review.setText("Recipient Number: " + recipientnumber.getText());
            recipientname_review.setText("Recipient Name: " + recipirntaccname.getText());
            date_review.setText("Date: " + currentdate.getText());
            recipientplatform_review.setText("Recipient Platform: " + platform.getSelectedItem().toString()); // ✅ Fix: Populate with selected platform
            referenceornotes_review.setText("Reference/Notes: " + referencenotes.getText()); // ✅ Fix: Populate with user input
            amount_review.setText("Amount: ₱" + formatMoney(amountToSend));
            fees_review.setText("Fees: ₱" + formatMoney(fee));
            total_review.setText("Total: ₱" + formatMoney(total));

            // Enable the Send Money button
            sendmoney_btn.setEnabled(true);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_execute_btnMouseClicked

    private boolean deductFromEWalletCash(Connection conn, String userID, double amountToDeduct) {
        try {

            System.out.println("Attempting to deduct " + amountToDeduct + " from UserID: " + userID);

            String sql = "UPDATE EWallet_Cash SET CashedIn = CashedIn - ? WHERE UserID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, amountToDeduct);
            pst.setString(2, userID);

            int rowsAffected = pst.executeUpdate();
            pst.close();

            System.out.println("Rows affected: " + rowsAffected);

            if (rowsAffected == 0) {
                System.err.println("No rows updated - possibly invalid UserID.");
                return false;
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    private void sendmoney_btnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendmoney_btnMouseClicked
        try {
            double amountToSend = Double.parseDouble(amount.getText());
            double fee = calculateFee(amountToSend);
            double total = amountToSend + fee;

            String userID = ewallet_userid.getText().trim();  // e.g. "EMP143"

            if (userID.isEmpty()) {
                JOptionPane.showMessageDialog(this, "UserID not found!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to send ₱" + String.format("%,.2f", total) + "?\n"
                    + "This includes the amount plus any fees.",
                    "Confirm Send Money",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm != JOptionPane.YES_OPTION) {
                return; // User cancelled
            }

            if (SharedData.getCurrentBalance() >= total) {
                // Deduct from DB
                boolean success = deductFromEWalletCash(conn, userID, total);
                if (!success) {
                    JOptionPane.showMessageDialog(this, "Failed to update balance in database.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Update local balance and UI
                SharedData.setCurrentBalance(SharedData.getCurrentBalance() - total);
                eWalletPage.updateBalanceDisplay();
                updateCurrentBalanceDisplay();

                // Insert transaction into EwalletTable
                String insertSql = "INSERT INTO EwalletTable (UserID, Date, REF, RecipName, RecipNum, AccNum, Amount, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pst = conn.prepareStatement(insertSql)) {
                    pst.setString(1, userID);

                    // Current date & time
                    String currentDateTime = java.time.LocalDateTime.now().toString();
                    pst.setString(2, currentDateTime);

                    // Reference string
                    String ref = "REF" + System.currentTimeMillis();
                    pst.setString(3, ref);

                    pst.setString(4, recipirntaccname.getText().trim());
                    pst.setString(5, recipientnumber.getText().trim());
                    pst.setString(6, ewalletnumber.getText().trim());

                    pst.setDouble(7, total); // amount with fee included or just amountToSend if you prefer
                    pst.setString(8, "Success");

                    pst.executeUpdate();
                }

                recipientnumber.setText("");
                recipirntaccname.setText("");
                platform.setSelectedIndex(0);
                referencenotes.setText("");
                amount.setText("");

                ewalletnumber_review.setText("E-Wallet Number: ");
                userid_review.setText("User ID: ");
                recipientnumber_review.setText("Recipient Number: ");
                recipientname_review.setText("Recipient Name: ");
                recipientplatform_review.setText("Recipient Platform: ");
                referenceornotes_review.setText("Reference/Notes: ");
                amount_review.setText("Amount: ");
                date_review.setText("Date: ");
                fees_review.setText("Fees: ");
                total_review.setText("Total: ");

                eWalletPage.populateEwalletTransactionHistoryTable();

                JOptionPane.showMessageDialog(this, "Money sent successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "Insufficient funds!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid amount entered!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }//GEN-LAST:event_sendmoney_btnMouseClicked

    private void navigation_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_dashboardMouseClicked
        EMP_Dashboard dashboard = new EMP_Dashboard(null);
        dashboard.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_navigation_dashboardMouseClicked

    private void navigation_ewalletMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_ewalletMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_navigation_ewalletMouseClicked

    private void navigation_banktransferMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_banktransferMouseClicked
        // TODO add your handling code here:
        Bank_Transfer banktransfer = new Bank_Transfer(null);
        banktransfer.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_navigation_banktransferMouseClicked

    private void sendmoney_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendmoney_btnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sendmoney_btnActionPerformed

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
            java.util.logging.Logger.getLogger(EWallet_Send.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EWallet_Send.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EWallet_Send.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EWallet_Send.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                EMP_Dashboard empDashboard = new EMP_Dashboard(null); // Create EMP_Dashboard instance
                EWallet_Page eWalletPage = new EWallet_Page(empDashboard); // Create EWallet_Page instance
                new EWallet_Send(empDashboard, eWalletPage).setVisible(true); // Pass the references
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField amount;
    private javax.swing.JLabel amount_review;
    private javax.swing.JLabel credits;
    private javax.swing.JLabel currentbalance;
    private javax.swing.JTextField currentdate;
    private javax.swing.JLabel date_review;
    private javax.swing.JLabel ewallet_title;
    private javax.swing.JTextField ewallet_userid;
    private javax.swing.JLabel ewalletaccnameTXT_LABEL;
    private javax.swing.JTextField ewalletnumber;
    private javax.swing.JLabel ewalletnumberTXT_LABEL;
    private javax.swing.JLabel ewalletnumber_review;
    private javax.swing.JButton execute_btn;
    private javax.swing.JLabel fees_review;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
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
    private javax.swing.JComboBox<String> platform;
    private javax.swing.JLabel platformTXT_LABEL;
    private javax.swing.JLabel recipientTXT_LABEL;
    private javax.swing.JLabel recipientaccnameTXT_LABEL;
    private javax.swing.JLabel recipientaccnumberTXT_LABEL2;
    private javax.swing.JLabel recipientaccnumberTXT_LABEL3;
    private javax.swing.JLabel recipientname_review;
    private javax.swing.JTextField recipientnumber;
    private javax.swing.JLabel recipientnumber_review;
    private javax.swing.JLabel recipientplatform_review;
    private javax.swing.JTextField recipirntaccname;
    private javax.swing.JTextField referencenotes;
    private javax.swing.JLabel referencenotesTXT_LABEL1;
    private javax.swing.JLabel referenceornotes_review;
    private javax.swing.JLabel returnto_banktransferpage;
    private javax.swing.JButton sendmoney_btn;
    private javax.swing.JLabel total_review;
    private javax.swing.JLabel transactionreview_label;
    private javax.swing.JLabel transferdetails_label;
    private javax.swing.JLabel userid_review;
    // End of variables declaration//GEN-END:variables
}
