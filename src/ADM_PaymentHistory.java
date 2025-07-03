
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author danie
 */
public class ADM_PaymentHistory extends javax.swing.JFrame {

    Connection conn;

    /**
     * Creates new form ADM_PaymentHistory
     */
    public ADM_PaymentHistory() {
        initComponents();

        this.conn = javaconnect.ConnectDb();  // Only once

        setLocationRelativeTo(null);

        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state

        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        setTitle("Payroll Swift"); // ✅ Custom window title

        loadUserIDs();
        loadAllPaymentHistory();

        viewalltransaction.setEnabled(false);
        navigation_paymenthistory.setEnabled(false);

    }

    private void loadAllPaymentHistory() {
        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        DefaultTableModel model = (DefaultTableModel) paymenthistory_table.getModel();
        model.setRowCount(0); // Clear previous data

        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:C:/Users/danie/OneDrive/Documents/NetBeansProjects/PayrollModule_2_Copy/Payroll.db";
            conn = DriverManager.getConnection(url);

            String query = "SELECT Date, UserID, FullName, JobPos, EmpStatus, GrossPay, TotalDeduc, NetPay "
                    + "FROM ADM_SalarySummary ORDER BY Date DESC";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                String date = rs.getString("Date");
                String userID = rs.getString("UserID");
                String fullName = rs.getString("FullName");
                String jobPos = rs.getString("JobPos");
                String empStatus = rs.getString("EmpStatus");
                double grossPay = rs.getDouble("GrossPay");
                double totalDeduc = rs.getDouble("TotalDeduc");
                double netPay = rs.getDouble("NetPay");

                model.addRow(new Object[]{
                    date,
                    userID,
                    fullName,
                    jobPos,
                    empStatus,
                    "₱" + String.format("%,.2f", grossPay),
                    "₱" + String.format("%,.2f", totalDeduc),
                    "₱" + String.format("%,.2f", netPay)
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading payment history: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }

    private void loadUserIDs() {
        try {
            Class.forName("org.sqlite.JDBC");  // Load SQLite driver
            String url = "jdbc:sqlite:C:/Users/danie/OneDrive/Documents/NetBeansProjects/PayrollModule_2_Copy/Payroll.db";
            Connection conn = DriverManager.getConnection(url);

            String sql = "SELECT UserID FROM ListEmpAccounts ORDER BY CAST(SUBSTR(UserID, 4) AS INTEGER) ASC";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            list_useridcombobox.removeAllItems(); // Clear old items

            while (rs.next()) {
                list_useridcombobox.addItem(rs.getString("UserID"));
            }

            rs.close();
            pst.close();
            conn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error loading UserIDs: " + e.getMessage());
        }
    }

    private void disableAllListFields() {
        list_firstname.setEditable(false);
        list_lastname.setEditable(false);
        list_department.setEnabled(false);
        list_jobposition.setEnabled(false);
        list_empstatus.setEnabled(false);
        list_datehired.setEditable(false);
        list_dailyrate.setEditable(false);
        list_hourlyrate.setEditable(false);
        list_perminuterate.setEditable(false);
        list_basicsalary.setEditable(false);

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
        left_panel = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        credits = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        navigation_paymenthistory = new javax.swing.JLabel();
        navigation_createaccount = new javax.swing.JLabel();
        navigation_viewaccount = new javax.swing.JLabel();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        useridTXT_LABEL = new javax.swing.JLabel();
        firstnameTXT_LABEL = new javax.swing.JLabel();
        lastnameTXT_LABEL = new javax.swing.JLabel();
        deptTXT_LABEL = new javax.swing.JLabel();
        jobpositionTXT_LABEL = new javax.swing.JLabel();
        empstatusTXT_LABEL = new javax.swing.JLabel();
        list_empstatus = new javax.swing.JComboBox<>();
        list_jobposition = new javax.swing.JComboBox<>();
        list_department = new javax.swing.JComboBox<>();
        list_lastname = new javax.swing.JTextField();
        list_firstname = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        list_datehired = new javax.swing.JTextField();
        sexTXT_LABEL6 = new javax.swing.JLabel();
        currenttimee = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        list_dailyrate = new javax.swing.JTextField();
        dailyrateTXT_LABEL = new javax.swing.JLabel();
        hourlyrateTXT_LABEL = new javax.swing.JLabel();
        list_hourlyrate = new javax.swing.JTextField();
        list_perminuterate = new javax.swing.JTextField();
        perminuterateTXT_LABEL = new javax.swing.JLabel();
        list_basicsalary = new javax.swing.JTextField();
        perminuterateTXT_LABEL1 = new javax.swing.JLabel();
        return_banktransferpage = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        list_useridcombobox = new javax.swing.JComboBox<>();
        list_generate = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        paymenthistory_table = new javax.swing.JTable();
        viewalltransaction = new javax.swing.JButton();
        generatepayslip = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(1043, 987));

        left_panel.setBackground(new java.awt.Color(0, 34, 71));
        left_panel.setForeground(new java.awt.Color(0, 0, 0));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AYROLL WIFT (2) (3).png"))); // NOI18N
        jLabel4.setText(" ");

        credits.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        credits.setForeground(new java.awt.Color(255, 255, 255));
        credits.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        credits.setText("© 2025 Payroll Swift");

        jSeparator5.setForeground(new java.awt.Color(255, 255, 255));

        navigation_paymenthistory.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_paymenthistory.setForeground(new java.awt.Color(255, 255, 255));
        navigation_paymenthistory.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image (5) (1).png"))); // NOI18N
        navigation_paymenthistory.setText("  Payment History");
        navigation_paymenthistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_paymenthistoryMouseClicked(evt);
            }
        });

        navigation_createaccount.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_createaccount.setForeground(new java.awt.Color(255, 255, 255));
        navigation_createaccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image (6).png"))); // NOI18N
        navigation_createaccount.setText("  Create Employee Account");
        navigation_createaccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_createaccountMouseClicked(evt);
            }
        });

        navigation_viewaccount.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_viewaccount.setForeground(new java.awt.Color(255, 255, 255));
        navigation_viewaccount.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image (7).png"))); // NOI18N
        navigation_viewaccount.setText("  View Employee Account");
        navigation_viewaccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_viewaccountMouseClicked(evt);
            }
        });

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout left_panelLayout = new javax.swing.GroupLayout(left_panel);
        left_panel.setLayout(left_panelLayout);
        left_panelLayout.setHorizontalGroup(
            left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, left_panelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(74, 74, 74))
            .addGroup(left_panelLayout.createSequentialGroup()
                .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(navigation_createaccount, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(left_panelLayout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addComponent(credits, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(left_panelLayout.createSequentialGroup()
                            .addGap(26, 26, 26)
                            .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(navigation_paymenthistory, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(navigation_viewaccount, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(left_panelLayout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(25, Short.MAX_VALUE))
            .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(left_panelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        left_panelLayout.setVerticalGroup(
            left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(left_panelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(navigation_paymenthistory)
                .addGap(27, 27, 27)
                .addComponent(navigation_createaccount)
                .addGap(26, 26, 26)
                .addComponent(navigation_viewaccount)
                .addGap(36, 36, 36)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 446, Short.MAX_VALUE)
                .addComponent(credits)
                .addGap(22, 22, 22))
            .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(left_panelLayout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 623, Short.MAX_VALUE)))
        );

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setText("Personal Data");

        useridTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        useridTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        useridTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        useridTXT_LABEL.setText("User ID");

        firstnameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        firstnameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        firstnameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        firstnameTXT_LABEL.setText("First Name");

        lastnameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        lastnameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        lastnameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        lastnameTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lastnameTXT_LABEL.setText("Last Name");

        deptTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        deptTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        deptTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        deptTXT_LABEL.setText("Department");

        jobpositionTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        jobpositionTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jobpositionTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        jobpositionTXT_LABEL.setText("Job Position");

        empstatusTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        empstatusTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        empstatusTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        empstatusTXT_LABEL.setText("Emp. Status");

        list_empstatus.setBackground(new java.awt.Color(255, 255, 255));
        list_empstatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        list_empstatus.setForeground(new java.awt.Color(0, 0, 51));
        list_empstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Full-Time", "Part-Time", "Probationary", "Contractual" }));
        list_empstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_empstatusActionPerformed(evt);
            }
        });

        list_jobposition.setBackground(new java.awt.Color(255, 255, 255));
        list_jobposition.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        list_jobposition.setForeground(new java.awt.Color(0, 0, 51));
        list_jobposition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Software Developer", "Front-End Developer", "Back-End Developer", "Full-Stack Developer", "Mobile App Developer", "UI/UX Designer", "System Administrator", "Network Administrator", "Cybersecurity Analyst", "Security Engineer", "Database Administrator", "Machine Learning Engineer", "AI Engineer", "Project Manager" }));
        list_jobposition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_jobpositionActionPerformed(evt);
            }
        });

        list_department.setBackground(new java.awt.Color(255, 255, 255));
        list_department.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        list_department.setForeground(new java.awt.Color(0, 0, 51));
        list_department.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Software Development", "UI/UX and Design", "IT Infrastructure", "Cybersecurity", "Data and Analytics", "AI and Machine Learning" }));
        list_department.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_departmentActionPerformed(evt);
            }
        });

        list_lastname.setBackground(new java.awt.Color(255, 255, 255));
        list_lastname.setForeground(new java.awt.Color(0, 0, 0));

        list_firstname.setBackground(new java.awt.Color(255, 255, 255));
        list_firstname.setForeground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 51));
        jLabel3.setText("Salary Records");

        list_datehired.setBackground(new java.awt.Color(255, 255, 255));
        list_datehired.setForeground(new java.awt.Color(0, 0, 0));

        sexTXT_LABEL6.setBackground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        sexTXT_LABEL6.setForeground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sexTXT_LABEL6.setText("Date Hired");

        currenttimee.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        currenttimee.setForeground(new java.awt.Color(0, 0, 0));
        currenttimee.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setText("Computed Rates");

        list_dailyrate.setBackground(new java.awt.Color(255, 255, 255));
        list_dailyrate.setForeground(new java.awt.Color(0, 0, 0));
        list_dailyrate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_dailyrateActionPerformed(evt);
            }
        });

        dailyrateTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        dailyrateTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        dailyrateTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        dailyrateTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dailyrateTXT_LABEL.setText("Daily Rate");

        hourlyrateTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        hourlyrateTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        hourlyrateTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        hourlyrateTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        hourlyrateTXT_LABEL.setText("Hourly Rate");

        list_hourlyrate.setBackground(new java.awt.Color(255, 255, 255));
        list_hourlyrate.setForeground(new java.awt.Color(0, 0, 0));

        list_perminuterate.setBackground(new java.awt.Color(255, 255, 255));
        list_perminuterate.setForeground(new java.awt.Color(0, 0, 0));
        list_perminuterate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_perminuterateActionPerformed(evt);
            }
        });

        perminuterateTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        perminuterateTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        perminuterateTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        perminuterateTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        perminuterateTXT_LABEL.setText("Per Minute Rate");

        list_basicsalary.setBackground(new java.awt.Color(255, 255, 255));
        list_basicsalary.setForeground(new java.awt.Color(0, 0, 0));

        perminuterateTXT_LABEL1.setBackground(new java.awt.Color(0, 0, 0));
        perminuterateTXT_LABEL1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        perminuterateTXT_LABEL1.setForeground(new java.awt.Color(0, 0, 0));
        perminuterateTXT_LABEL1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        perminuterateTXT_LABEL1.setText("Basic Salary");

        return_banktransferpage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/replay (1).png"))); // NOI18N
        return_banktransferpage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                return_banktransferpageMouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("PAYMENT HISTORY");

        list_useridcombobox.setBackground(new java.awt.Color(255, 255, 255));
        list_useridcombobox.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        list_useridcombobox.setForeground(new java.awt.Color(0, 0, 51));
        list_useridcombobox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_useridcomboboxActionPerformed(evt);
            }
        });

        list_generate.setBackground(new java.awt.Color(0, 0, 51));
        list_generate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        list_generate.setForeground(new java.awt.Color(255, 255, 255));
        list_generate.setText("Generate");
        list_generate.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                list_generateMouseClicked(evt);
            }
        });
        list_generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                list_generateActionPerformed(evt);
            }
        });

        paymenthistory_table.setBackground(new java.awt.Color(0, 34, 71));
        paymenthistory_table.setForeground(new java.awt.Color(255, 255, 255));
        paymenthistory_table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Payroll Period", "ID Number", "Full Name", "Job Position", "Employee Status", "Gross Pay", "Total Deductions", "Netpay"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        paymenthistory_table.setEnabled(false);
        paymenthistory_table.setGridColor(new java.awt.Color(0, 47, 100));
        paymenthistory_table.setRowHeight(50);
        paymenthistory_table.setSelectionBackground(new java.awt.Color(0, 34, 71));
        paymenthistory_table.setSelectionForeground(new java.awt.Color(0, 34, 71));
        paymenthistory_table.setShowGrid(false);
        paymenthistory_table.setShowHorizontalLines(true);
        jScrollPane1.setViewportView(paymenthistory_table);

        viewalltransaction.setBackground(new java.awt.Color(0, 0, 51));
        viewalltransaction.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        viewalltransaction.setForeground(new java.awt.Color(255, 255, 255));
        viewalltransaction.setText("View All");
        viewalltransaction.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewalltransactionMouseClicked(evt);
            }
        });

        generatepayslip.setBackground(new java.awt.Color(0, 0, 51));
        generatepayslip.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        generatepayslip.setForeground(new java.awt.Color(255, 255, 255));
        generatepayslip.setText("Generate Payslip");
        generatepayslip.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                generatepayslipMouseClicked(evt);
            }
        });
        generatepayslip.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generatepayslipActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(left_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(59, 59, 59)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(viewalltransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(generatepayslip)
                        .addGap(40, 40, 40))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(70, 70, 70)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(useridTXT_LABEL)
                                                    .addComponent(firstnameTXT_LABEL)
                                                    .addComponent(lastnameTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                    .addComponent(list_lastname, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(list_firstname, javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(list_useridcombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(list_generate))))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGap(27, 27, 27)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(deptTXT_LABEL)
                                                    .addComponent(jobpositionTXT_LABEL)
                                                    .addComponent(empstatusTXT_LABEL))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(list_department, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(list_jobposition, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(list_empstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(90, 90, 90)
                                        .addComponent(jLabel2)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(currenttimee, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(perminuterateTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(perminuterateTXT_LABEL1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(hourlyrateTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(dailyrateTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(list_dailyrate, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                                    .addComponent(list_hourlyrate)
                                                    .addComponent(list_perminuterate)
                                                    .addComponent(list_basicsalary)))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(16, 16, 16)
                                                .addComponent(sexTXT_LABEL6, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(list_datehired, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(101, 101, 101))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(return_banktransferpage, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jSeparator3, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(26, 26, 26))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(36, 36, 36)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 803, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(35, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(return_banktransferpage, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(15, 15, 15)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_dailyrate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dailyrateTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_hourlyrate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hourlyrateTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_perminuterate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(perminuterateTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_basicsalary, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(perminuterateTXT_LABEL1))
                        .addGap(46, 46, 46)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_datehired, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sexTXT_LABEL6)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_useridcombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(list_generate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(useridTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_firstname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstnameTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastnameTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_department, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deptTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_jobposition, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jobpositionTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(list_empstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(empstatusTXT_LABEL))))
                .addGap(0, 0, 0)
                .addComponent(currenttimee, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(viewalltransaction, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(generatepayslip, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(109, 109, 109))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(left_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1130, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1130, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 833, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 833, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void navigation_paymenthistoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_paymenthistoryMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_navigation_paymenthistoryMouseClicked

    private void navigation_createaccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_createaccountMouseClicked
        new ADM_AddEMP().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_navigation_createaccountMouseClicked

    private void navigation_viewaccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_viewaccountMouseClicked
        new ADM_ListEmp().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_navigation_viewaccountMouseClicked

    private void list_empstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_empstatusActionPerformed

    }//GEN-LAST:event_list_empstatusActionPerformed

    private void list_jobpositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_jobpositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_list_jobpositionActionPerformed

    private void list_departmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_departmentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_list_departmentActionPerformed

    private void list_dailyrateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_dailyrateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_list_dailyrateActionPerformed

    private void list_perminuterateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_perminuterateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_list_perminuterateActionPerformed

    private void return_banktransferpageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_return_banktransferpageMouseClicked
        this.dispose(); // ✅ Close ADM_AddEMP
        new ADM_PayrollModule().setVisible(true); // ✅ Open ADM_PayrollModule
        ADM_AddEMP addEmp = new ADM_AddEMP();
    }//GEN-LAST:event_return_banktransferpageMouseClicked

    private void list_useridcomboboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_useridcomboboxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_list_useridcomboboxActionPerformed

    private void list_generateMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_generateMouseClicked
        viewalltransaction.setEnabled(true);

        String selectedUserID = (String) list_useridcombobox.getSelectedItem();

        if (selectedUserID == null || selectedUserID.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a UserID.");
            return;
        }

        Connection conn = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        DefaultTableModel model = (DefaultTableModel) paymenthistory_table.getModel();
        model.setRowCount(0); // Clear existing rows

        try {
            // Load SQLite driver
            Class.forName("org.sqlite.JDBC");

            // Open connection once for both queries
            String url = "jdbc:sqlite:C:/Users/danie/OneDrive/Documents/NetBeansProjects/PayrollModule_2_Copy/Payroll.db";
            conn = DriverManager.getConnection(url);

            // ==============================
            // 1. Fetch employee account info
            // ==============================
            String empQuery = "SELECT * FROM ListEmpAccounts WHERE UserID = ?";
            pst = conn.prepareStatement(empQuery);
            pst.setString(1, selectedUserID);
            rs = pst.executeQuery();

            if (rs.next()) {
                list_firstname.setText(rs.getString("FirstName"));
                list_lastname.setText(rs.getString("LastName"));
                list_department.setSelectedItem(rs.getString("Dept"));
                list_jobposition.setSelectedItem(rs.getString("JobPos"));
                list_empstatus.setSelectedItem(rs.getString("EmpStatus"));
                list_datehired.setText(rs.getString("DateHired"));
                list_dailyrate.setText(rs.getString("DailyRate"));
                list_hourlyrate.setText(rs.getString("HourlyRate"));
                list_perminuterate.setText(rs.getString("PerMinuteRate"));
                list_basicsalary.setText(rs.getString("BasicSalary"));

                disableAllListFields();  // ✅ Disable fields after fill-up
            } else {
                JOptionPane.showMessageDialog(null, "No data found for selected UserID.");
            }

            rs.close();
            pst.close();

            // ==============================
            // 2. Load salary summary table
            // ==============================
            String summaryQuery = "SELECT Date, UserID, FullName, JobPos, EmpStatus, GrossPay, TotalDeduc, NetPay "
                    + "FROM ADM_SalarySummary WHERE UserID = ? ORDER BY Date DESC";
            pst = conn.prepareStatement(summaryQuery);
            pst.setString(1, selectedUserID);
            rs = pst.executeQuery();

            while (rs.next()) {
                String date = rs.getString("Date");
                String userID = rs.getString("UserID");
                String fullName = rs.getString("FullName");
                String jobPos = rs.getString("JobPos");
                String empStatus = rs.getString("EmpStatus");
                double grossPay = rs.getDouble("GrossPay");
                double totalDeduc = rs.getDouble("TotalDeduc");
                double netPay = rs.getDouble("NetPay");

                model.addRow(new Object[]{
                    date,
                    userID,
                    fullName,
                    jobPos,
                    empStatus,
                    "₱" + String.format("%,.2f", grossPay),
                    "₱" + String.format("%,.2f", totalDeduc),
                    "₱" + String.format("%,.2f", netPay)
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error generating data: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error closing resources: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_list_generateMouseClicked

    private void list_generateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_list_generateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_list_generateActionPerformed

    private void viewalltransactionMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewalltransactionMouseClicked
        list_firstname.setText("");
        list_lastname.setText("");
        list_department.setSelectedIndex(0);  // -1 means no selection
        list_jobposition.setSelectedIndex(0);
        list_empstatus.setSelectedIndex(0);
        list_datehired.setText("");
        list_dailyrate.setText("");
        list_hourlyrate.setText("");
        list_perminuterate.setText("");
        list_basicsalary.setText("");

        loadAllPaymentHistory();

    }//GEN-LAST:event_viewalltransactionMouseClicked

    private void generatepayslipActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generatepayslipActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_generatepayslipActionPerformed

    private void generatepayslipMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_generatepayslipMouseClicked
        new ADM_Payslip().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_generatepayslipMouseClicked

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
            java.util.logging.Logger.getLogger(ADM_PaymentHistory.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ADM_PaymentHistory.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ADM_PaymentHistory.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ADM_PaymentHistory.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ADM_PaymentHistory().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel credits;
    private javax.swing.JLabel currenttimee;
    private javax.swing.JLabel dailyrateTXT_LABEL;
    private javax.swing.JLabel deptTXT_LABEL;
    private javax.swing.JLabel empstatusTXT_LABEL;
    private javax.swing.JLabel firstnameTXT_LABEL;
    private javax.swing.JButton generatepayslip;
    private javax.swing.JLabel hourlyrateTXT_LABEL;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel jobpositionTXT_LABEL;
    private javax.swing.JLabel lastnameTXT_LABEL;
    private javax.swing.JPanel left_panel;
    private javax.swing.JTextField list_basicsalary;
    private javax.swing.JTextField list_dailyrate;
    private javax.swing.JTextField list_datehired;
    private javax.swing.JComboBox<String> list_department;
    private javax.swing.JComboBox<String> list_empstatus;
    private javax.swing.JTextField list_firstname;
    private javax.swing.JButton list_generate;
    private javax.swing.JTextField list_hourlyrate;
    private javax.swing.JComboBox<String> list_jobposition;
    private javax.swing.JTextField list_lastname;
    private javax.swing.JTextField list_perminuterate;
    private javax.swing.JComboBox<String> list_useridcombobox;
    private javax.swing.JLabel navigation_createaccount;
    private javax.swing.JLabel navigation_paymenthistory;
    private javax.swing.JLabel navigation_viewaccount;
    private javax.swing.JTable paymenthistory_table;
    private javax.swing.JLabel perminuterateTXT_LABEL;
    private javax.swing.JLabel perminuterateTXT_LABEL1;
    private javax.swing.JLabel return_banktransferpage;
    private javax.swing.JLabel sexTXT_LABEL6;
    private javax.swing.JLabel useridTXT_LABEL;
    private javax.swing.JButton viewalltransaction;
    // End of variables declaration//GEN-END:variables
}
