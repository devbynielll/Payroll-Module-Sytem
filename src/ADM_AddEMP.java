
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

/**
 *
 * @author danie
 */
public class ADM_AddEMP extends javax.swing.JFrame {

    PreparedStatement pst1;
    PreparedStatement pst2;
    PreparedStatement pst3;
    PreparedStatement pst4;

    ResultSet rs;
    Connection conn;

    /**
     * Creates new form ADM_AddEMP
     */
    public ADM_AddEMP() {
        initComponents();

        this.conn = javaconnect.ConnectDb();  // Only once

        setLocationRelativeTo(null);
        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state

        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        setTitle("Payroll Swift"); // ✅ Custom window title

        disableMonthYearFields(); // ✅ Disable month & year fields

        restrictToLettersOnly(create_firstname);
        restrictToLettersOnly(create_lastname);
        restrictToLettersOnly(accountname);
        restrictToLettersOnly(create_managername);

        restrictToNumbersOnly(create_contactnumber, 11);
        restrictToNumbersOnly(accountnumber, 16);
        restrictToNumbersOnly(govidnumber, 15);

        create_dailyrate.setEditable(false);
        create_hourlyrate.setEditable(false);
        create_perminuterate.setEditable(false);
        create_basicsalary.setEditable(false);

        restrictToDateFormat(create_datehired);
        restrictToDateFormat(create_dateofbirth);

        LocalDate today = LocalDate.now();
        String fixedDateHired = String.format("%02d-%02d-%d", today.getMonthValue(), today.getDayOfMonth(), 2025);
        create_datehired.setText(fixedDateHired);

        create_confirm.setEnabled(false);
        navigation_createacc.setEnabled(false);

    }

    // ✅ Set current month & year, then disable fields
    private void disableMonthYearFields() {
        LocalDate today = LocalDate.now();
        month.setSelectedItem(today.getMonth().toString()); // ✅ Set current month
        year.setSelectedItem(String.valueOf(today.getYear())); // ✅ Set current year

        month.setEnabled(false); // ✅ Disable month field
        year.setEnabled(false);  // ✅ Disable year field
    }

    private void restrictToDateFormat(JTextField field) {
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();

                // ✅ Allow only digits and hyphens (-)
                if (!Character.isDigit(c) && c != '-') {
                    evt.consume(); // ✅ Block invalid input
                }

                // ✅ Ensure format is mm-dd-yyyy (10 characters max)
                if (field.getText().length() >= 10) {
                    evt.consume(); // ✅ Limit input length
                }
            }
        });
    }

    private boolean isValidDateOfBirth(String dob) {
        // ✅ Ensure format is mm-dd-yyyy
        if (!dob.matches("\\d{2}-\\d{2}-\\d{4}")) {
            return false; // ❌ Invalid format
        }

        // ✅ Extract the year from DOB
        String[] parts = dob.split("-");
        int year = Integer.parseInt(parts[2]);

        // ✅ Ensure year is between 1965 and 2007
        return year >= 1965 && year <= 2007;
    }

    // ✅ Ensure User ID starts with EMP followed by 3 digits
    private boolean isValidUserID(String userID) {
        return userID.matches("EMP\\d{3}");
    }

    // ✅ Ensure Date Format is mm-dd-yyyy
    private boolean isValidDate(String date) {
        return date.matches("\\d{2}-\\d{2}-\\d{4}"); // ✅ Ensures format like 03-02-2004
    }

    // ✅ Ensure Contact Number starts with 09 and is exactly 11 digits
    private boolean isValidContactNumber(String contact) {
        return contact.matches("09\\d{9}");
    }

    // ✅ Ensure Account Name does not contain numbers
    private boolean isValidName(String name) {
        return name.matches("^[a-zA-Z ]+$");
    }

    // ✅ Ensure Gov ID Number only contains numbers
    private boolean isValidGovID(String govID) {
        return govID.matches("\\d+");
    }

    // ✅ Ensure Salary Fields Cannot Be Edited
    private void disableSalaryFields() {
        create_dailyrate.setEditable(false);
        create_hourlyrate.setEditable(false);
        create_perminuterate.setEditable(false);
        create_basicsalary.setEditable(false);
    }

    private void restrictToNumbersOnly(JTextField field, int maxLength) {
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c) || field.getText().length() >= maxLength) { // ✅ Allow only digits
                    evt.consume(); // ✅ Block the input if it's not a number or exceeds limit
                }
            }
        });

    }

    private void restrictToLettersOnly(JTextField field) {
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isLetter(c) && !Character.isWhitespace(c)) { // ✅ Allow only letters and spaces
                    evt.consume(); // ✅ Block the input
                }
            }
        });
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
        jSeparator6 = new javax.swing.JSeparator();
        navigation_banktransfer = new javax.swing.JLabel();
        navigation_createacc = new javax.swing.JLabel();
        navigation_dashboard = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        useridTXT_LABEL = new javax.swing.JLabel();
        firstnameTXT_LABEL = new javax.swing.JLabel();
        lastnameTXT_LABEL = new javax.swing.JLabel();
        sexTXT_LABEL = new javax.swing.JLabel();
        deptTXT_LABEL = new javax.swing.JLabel();
        jobpositionTXT_LABEL = new javax.swing.JLabel();
        empstatusTXT_LABEL = new javax.swing.JLabel();
        create_empstatus = new javax.swing.JComboBox<>();
        create_jobposition = new javax.swing.JComboBox<>();
        create_department = new javax.swing.JComboBox<>();
        create_sex = new javax.swing.JComboBox<>();
        create_lastname = new javax.swing.JTextField();
        create_firstname = new javax.swing.JTextField();
        create_userid = new javax.swing.JTextField();
        bankDetail_LABEL = new javax.swing.JLabel();
        month = new javax.swing.JComboBox<>();
        monthTXT_LABEL = new javax.swing.JLabel();
        year = new javax.swing.JComboBox<>();
        yearTXT_LABEL = new javax.swing.JLabel();
        banknameTXT_LABEL = new javax.swing.JLabel();
        bankname = new javax.swing.JComboBox<>();
        accountnumber = new javax.swing.JTextField();
        accountnumberTXT_LABEL = new javax.swing.JLabel();
        accountnameTXT_LABEL = new javax.swing.JLabel();
        accountname = new javax.swing.JTextField();
        governmentID = new javax.swing.JComboBox<>();
        govIDTXT_LABEL = new javax.swing.JLabel();
        govidnumberTXT_LABEL = new javax.swing.JLabel();
        govidnumber = new javax.swing.JTextField();
        create_dateofbirth = new javax.swing.JTextField();
        create_address = new javax.swing.JTextField();
        create_contactnumber = new javax.swing.JTextField();
        create_emailaddress = new javax.swing.JTextField();
        sexTXT_LABEL1 = new javax.swing.JLabel();
        sexTXT_LABEL2 = new javax.swing.JLabel();
        sexTXT_LABEL3 = new javax.swing.JLabel();
        sexTXT_LABEL4 = new javax.swing.JLabel();
        sexTXT_LABEL5 = new javax.swing.JLabel();
        create_civilstatus = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        create_datehired = new javax.swing.JTextField();
        sexTXT_LABEL6 = new javax.swing.JLabel();
        create_managername = new javax.swing.JTextField();
        sexTXT_LABEL7 = new javax.swing.JLabel();
        currenttimee = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        create_dailyrate = new javax.swing.JTextField();
        dailyrateTXT_LABEL = new javax.swing.JLabel();
        hourlyrateTXT_LABEL = new javax.swing.JLabel();
        create_hourlyrate = new javax.swing.JTextField();
        create_perminuterate = new javax.swing.JTextField();
        perminuterateTXT_LABEL = new javax.swing.JLabel();
        create_basicsalary = new javax.swing.JTextField();
        perminuterateTXT_LABEL1 = new javax.swing.JLabel();
        create_confirm = new javax.swing.JButton();
        create_clear = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        create_execute = new javax.swing.JButton();
        return_banktransferpage = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        create_password = new javax.swing.JTextField();
        govidnumberTXT_LABEL1 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        notes = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

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

        jSeparator6.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator6.setForeground(new java.awt.Color(255, 255, 255));

        navigation_banktransfer.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_banktransfer.setForeground(new java.awt.Color(255, 255, 255));
        navigation_banktransfer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image (7).png"))); // NOI18N
        navigation_banktransfer.setText("  View Employee Account");
        navigation_banktransfer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_banktransferMouseClicked(evt);
            }
        });

        navigation_createacc.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_createacc.setForeground(new java.awt.Color(255, 255, 255));
        navigation_createacc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image (6).png"))); // NOI18N
        navigation_createacc.setText("  Create Employee Account");
        navigation_createacc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_createaccMouseClicked(evt);
            }
        });

        navigation_dashboard.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        navigation_dashboard.setForeground(new java.awt.Color(255, 255, 255));
        navigation_dashboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image (5) (1).png"))); // NOI18N
        navigation_dashboard.setText("  Payment History");
        navigation_dashboard.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                navigation_dashboardMouseClicked(evt);
            }
        });

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
                    .addComponent(navigation_createacc, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(left_panelLayout.createSequentialGroup()
                            .addGap(35, 35, 35)
                            .addComponent(credits, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(left_panelLayout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(left_panelLayout.createSequentialGroup()
                                    .addGap(9, 9, 9)
                                    .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(navigation_dashboard, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(navigation_banktransfer, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 214, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        left_panelLayout.setVerticalGroup(
            left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(left_panelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(navigation_dashboard)
                .addGap(27, 27, 27)
                .addComponent(navigation_createacc)
                .addGap(26, 26, 26)
                .addComponent(navigation_banktransfer)
                .addGap(36, 36, 36)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 433, Short.MAX_VALUE)
                .addComponent(credits)
                .addGap(22, 22, 22))
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

        sexTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        sexTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL.setText("Sex");

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

        create_empstatus.setBackground(new java.awt.Color(255, 255, 255));
        create_empstatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        create_empstatus.setForeground(new java.awt.Color(0, 0, 51));
        create_empstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Full-Time", "Part-Time", "Probationary", "Contractual" }));
        create_empstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_empstatusActionPerformed(evt);
            }
        });

        create_jobposition.setBackground(new java.awt.Color(255, 255, 255));
        create_jobposition.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        create_jobposition.setForeground(new java.awt.Color(0, 0, 51));
        create_jobposition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Software Developer", "Front-End Developer", "Back-End Developer", "Full-Stack Developer", "Mobile App Developer", "UI/UX Designer", "System Administrator", "Network Administrator", "Cybersecurity Analyst", "Security Engineer", "Database Administrator", "Machine Learning Engineer", "AI Engineer", "Project Manager" }));
        create_jobposition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_jobpositionActionPerformed(evt);
            }
        });

        create_department.setBackground(new java.awt.Color(255, 255, 255));
        create_department.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        create_department.setForeground(new java.awt.Color(0, 0, 51));
        create_department.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Software Development", "UI/UX and Design", "IT Infrastructure", "Cybersecurity", "Data and Analytics", "AI and Machine Learning" }));
        create_department.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_departmentActionPerformed(evt);
            }
        });

        create_sex.setBackground(new java.awt.Color(255, 255, 255));
        create_sex.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        create_sex.setForeground(new java.awt.Color(0, 0, 51));
        create_sex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        create_sex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_sexActionPerformed(evt);
            }
        });

        create_lastname.setBackground(new java.awt.Color(255, 255, 255));
        create_lastname.setForeground(new java.awt.Color(0, 0, 0));

        create_firstname.setBackground(new java.awt.Color(255, 255, 255));
        create_firstname.setForeground(new java.awt.Color(0, 0, 0));

        create_userid.setBackground(new java.awt.Color(255, 255, 255));
        create_userid.setForeground(new java.awt.Color(0, 0, 0));

        bankDetail_LABEL.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        bankDetail_LABEL.setForeground(new java.awt.Color(0, 0, 51));
        bankDetail_LABEL.setText("Bank Detail");

        month.setBackground(new java.awt.Color(255, 255, 255));
        month.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        month.setForeground(new java.awt.Color(0, 0, 51));
        month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "May", "June", "July", "August", "September", "October", "November", "December" }));
        month.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthActionPerformed(evt);
            }
        });

        monthTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        monthTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        monthTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        monthTXT_LABEL.setText("Month");

        year.setBackground(new java.awt.Color(255, 255, 255));
        year.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        year.setForeground(new java.awt.Color(0, 0, 51));
        year.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2026", "2027", "2028", "2029", "2030" }));
        year.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearActionPerformed(evt);
            }
        });

        yearTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        yearTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        yearTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        yearTXT_LABEL.setText("Year");

        banknameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        banknameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        banknameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        banknameTXT_LABEL.setText("Bank Name");

        bankname.setBackground(new java.awt.Color(255, 255, 255));
        bankname.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bankname.setForeground(new java.awt.Color(0, 0, 51));
        bankname.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BDO Unibank", "Metrobank", "Bank of the Philippine Islands (BPI)", "Philippine National Bank (PNB)", "Land Bank of the Philippines (LandBank)", "Security Bank", "China Banking Corporation (China Bank)", "UnionBank of the Philippines", "EastWest Bank", "Rizal Commercial Banking Corporation (RCBC)", "Development Bank of the Philippines (DBP)", "UCPB (United Coconut Planters Bank)", "Philippine Savings Bank (PSBank)", "Maybank Philippines", "Standard Chartered Bank Philippines" }));
        bankname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                banknameActionPerformed(evt);
            }
        });

        accountnumber.setBackground(new java.awt.Color(255, 255, 255));
        accountnumber.setForeground(new java.awt.Color(0, 0, 0));

        accountnumberTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        accountnumberTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        accountnumberTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        accountnumberTXT_LABEL.setText("Account Number");

        accountnameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        accountnameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        accountnameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        accountnameTXT_LABEL.setText("Account Name");

        accountname.setBackground(new java.awt.Color(255, 255, 255));
        accountname.setForeground(new java.awt.Color(0, 0, 0));

        governmentID.setBackground(new java.awt.Color(255, 255, 255));
        governmentID.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        governmentID.setForeground(new java.awt.Color(0, 0, 51));
        governmentID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TIN", "SSS", "PhilHealth", "Pag-IBIG" }));
        governmentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                governmentIDActionPerformed(evt);
            }
        });

        govIDTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        govIDTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        govIDTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        govIDTXT_LABEL.setText("Government ID");

        govidnumberTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        govidnumberTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        govidnumberTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        govidnumberTXT_LABEL.setText("Gov. ID Number");

        govidnumber.setBackground(new java.awt.Color(255, 255, 255));
        govidnumber.setForeground(new java.awt.Color(0, 0, 0));

        create_dateofbirth.setBackground(new java.awt.Color(255, 255, 255));
        create_dateofbirth.setForeground(new java.awt.Color(0, 0, 0));
        create_dateofbirth.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_dateofbirthActionPerformed(evt);
            }
        });

        create_address.setBackground(new java.awt.Color(255, 255, 255));
        create_address.setForeground(new java.awt.Color(0, 0, 0));

        create_contactnumber.setBackground(new java.awt.Color(255, 255, 255));
        create_contactnumber.setForeground(new java.awt.Color(0, 0, 0));

        create_emailaddress.setBackground(new java.awt.Color(255, 255, 255));
        create_emailaddress.setForeground(new java.awt.Color(0, 0, 0));

        sexTXT_LABEL1.setBackground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        sexTXT_LABEL1.setForeground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sexTXT_LABEL1.setText("Date of Birth");

        sexTXT_LABEL2.setBackground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        sexTXT_LABEL2.setForeground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL2.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sexTXT_LABEL2.setText("Civil Status");

        sexTXT_LABEL3.setBackground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        sexTXT_LABEL3.setForeground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL3.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sexTXT_LABEL3.setText("Address");

        sexTXT_LABEL4.setBackground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        sexTXT_LABEL4.setForeground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL4.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sexTXT_LABEL4.setText("Contact Number");

        sexTXT_LABEL5.setBackground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        sexTXT_LABEL5.setForeground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL5.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sexTXT_LABEL5.setText("Email Address");

        create_civilstatus.setBackground(new java.awt.Color(255, 255, 255));
        create_civilstatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        create_civilstatus.setForeground(new java.awt.Color(0, 0, 51));
        create_civilstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Single", "Married", "Widowed", "Separated", "Annulled" }));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 51));
        jLabel3.setText("Employment Information");

        create_datehired.setBackground(new java.awt.Color(255, 255, 255));
        create_datehired.setForeground(new java.awt.Color(0, 0, 0));

        sexTXT_LABEL6.setBackground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL6.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        sexTXT_LABEL6.setForeground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sexTXT_LABEL6.setText("Date Hired");

        create_managername.setBackground(new java.awt.Color(255, 255, 255));
        create_managername.setForeground(new java.awt.Color(0, 0, 0));

        sexTXT_LABEL7.setBackground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL7.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        sexTXT_LABEL7.setForeground(new java.awt.Color(0, 0, 0));
        sexTXT_LABEL7.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        sexTXT_LABEL7.setText("Manager Name");

        currenttimee.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        currenttimee.setForeground(new java.awt.Color(0, 0, 0));
        currenttimee.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setText("Computed Rates");

        create_dailyrate.setBackground(new java.awt.Color(255, 255, 255));
        create_dailyrate.setForeground(new java.awt.Color(0, 0, 0));
        create_dailyrate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_dailyrateActionPerformed(evt);
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

        create_hourlyrate.setBackground(new java.awt.Color(255, 255, 255));
        create_hourlyrate.setForeground(new java.awt.Color(0, 0, 0));

        create_perminuterate.setBackground(new java.awt.Color(255, 255, 255));
        create_perminuterate.setForeground(new java.awt.Color(0, 0, 0));
        create_perminuterate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_perminuterateActionPerformed(evt);
            }
        });

        perminuterateTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        perminuterateTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        perminuterateTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        perminuterateTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        perminuterateTXT_LABEL.setText("Per Minute Rate");

        create_basicsalary.setBackground(new java.awt.Color(255, 255, 255));
        create_basicsalary.setForeground(new java.awt.Color(0, 0, 0));

        perminuterateTXT_LABEL1.setBackground(new java.awt.Color(0, 0, 0));
        perminuterateTXT_LABEL1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        perminuterateTXT_LABEL1.setForeground(new java.awt.Color(0, 0, 0));
        perminuterateTXT_LABEL1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        perminuterateTXT_LABEL1.setText("Basic Salary");

        create_confirm.setBackground(new java.awt.Color(0, 0, 51));
        create_confirm.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        create_confirm.setForeground(new java.awt.Color(255, 255, 255));
        create_confirm.setText("Confirm");
        create_confirm.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_confirmMouseClicked(evt);
            }
        });
        create_confirm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_confirmActionPerformed(evt);
            }
        });

        create_clear.setBackground(new java.awt.Color(51, 0, 0));
        create_clear.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        create_clear.setForeground(new java.awt.Color(255, 255, 255));
        create_clear.setText("Clear");
        create_clear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_clearMouseClicked(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(0, 0, 51));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        jLabel6.setText("Create Account");

        create_execute.setBackground(new java.awt.Color(0, 0, 51));
        create_execute.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        create_execute.setForeground(new java.awt.Color(255, 255, 255));
        create_execute.setText("Execute");
        create_execute.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                create_executeMouseClicked(evt);
            }
        });
        create_execute.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_executeActionPerformed(evt);
            }
        });

        return_banktransferpage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/replay (1).png"))); // NOI18N
        return_banktransferpage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                return_banktransferpageMouseClicked(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(0, 0, 51));
        jLabel7.setText("CREATE EMPLOYEE ACCOUNT");

        create_password.setBackground(new java.awt.Color(255, 255, 255));
        create_password.setForeground(new java.awt.Color(0, 0, 0));
        create_password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                create_passwordActionPerformed(evt);
            }
        });

        govidnumberTXT_LABEL1.setBackground(new java.awt.Color(0, 0, 0));
        govidnumberTXT_LABEL1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        govidnumberTXT_LABEL1.setForeground(new java.awt.Color(0, 0, 0));
        govidnumberTXT_LABEL1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        govidnumberTXT_LABEL1.setText("New Account Password");

        notes.setBackground(new java.awt.Color(0, 0, 0));
        notes.setFont(new java.awt.Font("Segoe UI", 2, 12)); // NOI18N
        notes.setForeground(new java.awt.Color(0, 0, 0));
        notes.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        notes.setText("Note: Please review the employee’s information and role before confirming.");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(304, 304, 304)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(deptTXT_LABEL)
                                    .addComponent(jobpositionTXT_LABEL)
                                    .addComponent(empstatusTXT_LABEL))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(create_department, 0, 1, Short.MAX_VALUE)
                                    .addComponent(create_jobposition, 0, 1, Short.MAX_VALUE)
                                    .addComponent(create_empstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(3, 3, 3)
                                .addComponent(jLabel3))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(285, 285, 285)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(sexTXT_LABEL6, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sexTXT_LABEL7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(create_execute, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(create_datehired, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(create_managername, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(jLabel5))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(86, 86, 86)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(perminuterateTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(perminuterateTXT_LABEL1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(hourlyrateTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(dailyrateTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(create_dailyrate, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                                    .addComponent(create_hourlyrate)
                                    .addComponent(create_perminuterate)
                                    .addComponent(create_basicsalary))))
                        .addGap(6, 6, 6))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(create_confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(create_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(49, 49, 49))))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(left_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(return_banktransferpage, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(32, 32, 32))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(jLabel2))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(useridTXT_LABEL)
                                            .addComponent(firstnameTXT_LABEL)
                                            .addComponent(sexTXT_LABEL)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(lastnameTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(sexTXT_LABEL2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(sexTXT_LABEL4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(sexTXT_LABEL5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(sexTXT_LABEL1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(sexTXT_LABEL3, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(create_dateofbirth)
                                            .addComponent(create_lastname)
                                            .addComponent(create_firstname)
                                            .addComponent(create_userid)
                                            .addComponent(create_address)
                                            .addComponent(create_contactnumber, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(create_emailaddress, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(create_civilstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(create_sex, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGap(36, 36, 36)
                                                    .addComponent(bankDetail_LABEL))
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                    .addGap(36, 36, 36)
                                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(banknameTXT_LABEL)
                                                                        .addComponent(yearTXT_LABEL)))
                                                                .addComponent(accountnumberTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(monthTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING)
                                                                .addComponent(govidnumberTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING))
                                                            .addComponent(govIDTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(accountnameTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING))
                                                        .addComponent(govidnumberTXT_LABEL1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(create_password, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                            .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(accountname)
                                                            .addComponent(accountnumber)
                                                            .addComponent(bankname, 0, 1, Short.MAX_VALUE)
                                                            .addComponent(month, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                            .addComponent(govidnumber, javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(governmentID, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                            .addGap(17, 17, 17))
                                        .addComponent(currenttimee, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 369, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(21, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 735, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(notes, javax.swing.GroupLayout.PREFERRED_SIZE, 402, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(return_banktransferpage, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(bankDetail_LABEL)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(monthTXT_LABEL)
                            .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(yearTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(bankname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(banknameTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(accountnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(accountnumberTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(accountname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(accountnameTXT_LABEL)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_userid, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(useridTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_firstname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(firstnameTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lastnameTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_sex, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sexTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_dateofbirth, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sexTXT_LABEL1))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(governmentID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(govIDTXT_LABEL)
                        .addComponent(sexTXT_LABEL2))
                    .addComponent(create_civilstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(govidnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(govidnumberTXT_LABEL)
                    .addComponent(create_address, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sexTXT_LABEL3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(create_contactnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sexTXT_LABEL4))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(currenttimee, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(create_emailaddress, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sexTXT_LABEL5))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(create_password, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(govidnumberTXT_LABEL1)))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_department, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(deptTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_jobposition, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jobpositionTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_empstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(empstatusTXT_LABEL)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_dailyrate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(dailyrateTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_hourlyrate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hourlyrateTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_perminuterate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(perminuterateTXT_LABEL))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_datehired, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sexTXT_LABEL6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(create_managername, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(sexTXT_LABEL7)))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(create_basicsalary, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(perminuterateTXT_LABEL1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(create_confirm, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(create_clear, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(create_execute, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(notes)
                .addGap(191, 191, 191))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(left_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 821, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void create_empstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_empstatusActionPerformed


    }//GEN-LAST:event_create_empstatusActionPerformed

    private void create_jobpositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_jobpositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_create_jobpositionActionPerformed

    private void create_departmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_departmentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_create_departmentActionPerformed

    private void create_sexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_sexActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_create_sexActionPerformed

    private void monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_monthActionPerformed

    private void yearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_yearActionPerformed

    private void banknameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_banknameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_banknameActionPerformed

    private void governmentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_governmentIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_governmentIDActionPerformed

    private void create_dateofbirthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_dateofbirthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_create_dateofbirthActionPerformed

    private void create_dailyrateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_dailyrateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_create_dailyrateActionPerformed

    private void create_perminuterateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_perminuterateActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_create_perminuterateActionPerformed

    private void return_banktransferpageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_return_banktransferpageMouseClicked
        this.dispose(); // ✅ Close ADM_AddEMP
        new ADM_PayrollModule().setVisible(true); // ✅ Open ADM_PayrollModule
        ADM_AddEMP addEmp = new ADM_AddEMP();
    }//GEN-LAST:event_return_banktransferpageMouseClicked

    private void create_executeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_executeMouseClicked
        // ✅ Get Selected Job Position & Employment Status

        String position = create_jobposition.getSelectedItem().toString();
        String employmentStatus = create_empstatus.getSelectedItem().toString();
        String userID = create_userid.getText().trim();

        try {
            String checkUserIDQuery = "SELECT COUNT(*) FROM PersonalData WHERE UserID = ?";
            PreparedStatement pst = conn.prepareStatement(checkUserIDQuery);
            pst.setString(1, userID);
            ResultSet rs = pst.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "User ID '" + userID + "' is already registered in the system. Please enter a unique User ID.", "Duplicate User ID", JOptionPane.ERROR_MESSAGE);
                rs.close();
                pst.close();
                return;
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error checking User ID: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String dob = create_dateofbirth.getText().trim();
        String contact = create_contactnumber.getText().trim();
        String email = create_emailaddress.getText().trim();
        String accountNumber = accountnumber.getText().trim();
        String accountName = accountname.getText().trim();
        String firstName = create_firstname.getText().trim();
        String lastName = create_lastname.getText().trim();
        String govID = govidnumber.getText().trim();
        String dateHired = create_datehired.getText().trim();
        String managerName = create_managername.getText().trim();
        String password = create_password.getText().trim();  // <== Added

        // ✅ Strictly enforce validation checks before proceeding
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password field cannot be empty!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidUserID(userID)) {
            JOptionPane.showMessageDialog(this, "Invalid User ID format! Must start with EMP followed by 3 numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidDateOfBirth(dob)) {
            JOptionPane.showMessageDialog(this, "Invalid Date of Birth! Must be between 1965 and 2007.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidContactNumber(contact)) {
            JOptionPane.showMessageDialog(this, "Invalid Contact Number! Must start with 09 and be 11 digits.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!isValidName(accountName) || !isValidName(firstName) || !isValidName(lastName) || !isValidName(managerName)) {
            JOptionPane.showMessageDialog(this, "Names cannot contain numbers!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidGovID(govID)) {
            JOptionPane.showMessageDialog(this, "Invalid Gov ID Number! Must only contain numbers.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!isValidDate(dateHired) || !dateHired.endsWith("2025")) {
            JOptionPane.showMessageDialog(this, "Invalid Date Hired! Must be in mm-dd-yyyy format and set to 2025.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Ensure all fields have values before proceeding
        if (position.isEmpty() || employmentStatus.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Job Position and Employment Status!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ Process Salary Values Only If All Inputs Are Valid
        double dailyRate = 0, hourlyRate = 0, perMinuteRate = 0;

        // ✅ Assign salary values based on Job Position
        switch (position) {
            case "Software Developer":
                dailyRate = 1776;
                hourlyRate = 222;
                perMinuteRate = 3.70;
                break;
            case "Front-End Developer":
                dailyRate = 2081;
                hourlyRate = 260;
                perMinuteRate = 4.33;
                break;
            case "Back-End Developer":
                dailyRate = 2163;
                hourlyRate = 270;
                perMinuteRate = 4.50;
                break;
            case "Full-Stack Developer":
                dailyRate = 2184;
                hourlyRate = 273;
                perMinuteRate = 4.55;
                break;
            case "Mobile App Developer":
                dailyRate = 3728;
                hourlyRate = 466;
                perMinuteRate = 7.77;
                break;
            case "UI/UX Designer":
                dailyRate = 1600;
                hourlyRate = 200;
                perMinuteRate = 3.33;
                break;
            case "System Administrator":
                dailyRate = 1636;
                hourlyRate = 205;
                perMinuteRate = 3.42;
                break;
            case "Network Administrator":
                dailyRate = 1596;
                hourlyRate = 200;
                perMinuteRate = 3.33;
                break;
            case "Cybersecurity Analyst":
                dailyRate = 3952;
                hourlyRate = 494;
                perMinuteRate = 8.23;
                break;
            case "Security Engineer":
                dailyRate = 3880;
                hourlyRate = 485;
                perMinuteRate = 8.08;
                break;
            case "Database Administrator":
                dailyRate = 3888;
                hourlyRate = 486;
                perMinuteRate = 8.10;
                break;
            case "Machine Learning Engineer":
                dailyRate = 4152;
                hourlyRate = 519;
                perMinuteRate = 8.65;
                break;
            case "AI Engineer":
                dailyRate = 3936;
                hourlyRate = 492;
                perMinuteRate = 8.20;
                break;
            case "Project Manager":
                dailyRate = 1688;
                hourlyRate = 211;
                perMinuteRate = 3.52;
                break;
        }

        // ✅ Adjust salary based on Employment Status
        switch (employmentStatus) {
            case "Part-Time":
                dailyRate *= 0.5;
                hourlyRate *= 0.5;
                perMinuteRate *= 0.5;
                break;
            case "Probationary":
                dailyRate *= 0.75;
                hourlyRate *= 0.75;
                perMinuteRate *= 0.75;
                break;
            case "Contractual":
                dailyRate *= 0.85;
                hourlyRate *= 0.85;
                perMinuteRate *= 0.85;
                break;
        }

        // ✅ Calculate Basic Salary Assuming 22 Workdays Per Month
        double basicSalary = dailyRate * 22;

        // ✅ Apply Values to the Fields
        create_dailyrate.setText(String.format("%.2f", dailyRate));
        create_hourlyrate.setText(String.format("%.2f", hourlyRate));
        create_perminuterate.setText(String.format("%.2f", perMinuteRate));
        create_basicsalary.setText(String.format("%.2f", basicSalary));

        create_confirm.setEnabled(true);

        JOptionPane.showMessageDialog(this, "Salary updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_create_executeMouseClicked

    private void create_clearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_clearMouseClicked
        create_userid.setText("");
        create_dateofbirth.setText("");
        create_contactnumber.setText("");
        create_emailaddress.setText("");
        accountnumber.setText("");
        accountname.setText("");
        create_firstname.setText("");
        create_lastname.setText("");
        govidnumber.setText("");
        create_datehired.setText("");
        create_managername.setText("");
        create_password.setText("");
        create_address.setText("");

        create_civilstatus.setSelectedIndex(0);
        bankname.setSelectedIndex(0);
        governmentID.setSelectedIndex(0);

        create_dailyrate.setText("");
        create_hourlyrate.setText("");
        create_perminuterate.setText("");
        create_basicsalary.setText("");

        create_department.setSelectedIndex(0);
        create_jobposition.setSelectedIndex(0);
        create_empstatus.setSelectedIndex(0);
        create_sex.setSelectedIndex(0);

        // ✅ Prevent validation errors when clearing fields
        JOptionPane.showMessageDialog(this, "Fields cleared successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_create_clearMouseClicked

    private void create_confirmMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_create_confirmMouseClicked

        int confirm = JOptionPane.showConfirmDialog(null,
                "Are you sure you want to create this employee account?",
                "Confirm Create Account",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            // User chose NO or closed the dialog, so just return and do nothing
            return;
        }

        try {
            conn.setAutoCommit(false); // Start transaction

            // 1️⃣ Insert into PersonalData
            String sqlpd = "INSERT INTO PersonalData(UserID, FirstName, LastName, Sex, DOB, CivilStatus, Address, Contact_Num, EmailAdd, NewPass) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pst1 = conn.prepareStatement(sqlpd);
            pst1.setString(1, create_userid.getText());
            pst1.setString(2, create_firstname.getText());
            pst1.setString(3, create_lastname.getText());
            pst1.setString(4, (String) create_sex.getSelectedItem());
            pst1.setString(5, create_dateofbirth.getText());
            pst1.setString(6, (String) create_civilstatus.getSelectedItem());
            pst1.setString(7, create_address.getText());
            pst1.setString(8, create_contactnumber.getText());
            pst1.setString(9, create_emailaddress.getText());
            pst1.setString(10, create_password.getText());
            pst1.executeUpdate();

            // 2️⃣ Insert into BankDetails
            String sqlbd = "INSERT INTO BankDetails(Month, Year, BankName, AccountNum, AccountName, GovernmentID, GovIDNum, UserID) values (?, ?, ?, ?, ?, ?, ?, ?)";
            pst2 = conn.prepareStatement(sqlbd);
            pst2.setString(1, (String) month.getSelectedItem());
            pst2.setString(2, (String) year.getSelectedItem());
            pst2.setString(3, (String) bankname.getSelectedItem());
            pst2.setString(4, accountnumber.getText());
            pst2.setString(5, accountname.getText());
            pst2.setString(6, (String) governmentID.getSelectedItem());
            pst2.setString(7, govidnumber.getText());
            pst2.setString(8, create_userid.getText());
            pst2.executeUpdate();

            // 3️⃣ Insert into EmploymentInformation
            String sqlei = "INSERT INTO EmploymentInformation(Department, JobPos, EmpStat, DateHired, ManagerName, UserID) values (?, ?, ?, ?, ?, ?)";
            pst3 = conn.prepareStatement(sqlei);
            pst3.setString(1, (String) create_department.getSelectedItem());
            pst3.setString(2, (String) create_jobposition.getSelectedItem());
            pst3.setString(3, (String) create_empstatus.getSelectedItem());
            pst3.setString(4, create_datehired.getText());
            pst3.setString(5, create_managername.getText());
            pst3.setString(6, create_userid.getText());
            pst3.executeUpdate();

            // 4️⃣ Insert into ComputedRates
            String sqlcr = "INSERT INTO ComputedRates(DailyRate, HourlyRate, PerMinRate, BasicSalary, UserID) values (?, ?, ?, ?, ?)";
            pst4 = conn.prepareStatement(sqlcr);
            pst4.setString(1, create_dailyrate.getText());
            pst4.setString(2, create_hourlyrate.getText());
            pst4.setString(3, create_perminuterate.getText());
            pst4.setString(4, create_basicsalary.getText());
            pst4.setString(5, create_userid.getText());
            pst4.executeUpdate();

            // 5️⃣ Insert into EmployeeIncome (initialize TotalIncome to 0.00)
            String sqlEI = "INSERT OR IGNORE INTO EmployeeIncome(UserID, TotalIncome) VALUES (?, ?)";
            PreparedStatement pstEI = conn.prepareStatement(sqlEI);
            pstEI.setString(1, create_userid.getText());
            pstEI.setDouble(2, 0.00);
            pstEI.executeUpdate();
            pstEI.close();

            // 6️⃣ Insert into ListEmpAccounts (consolidated)
            String query = "INSERT INTO ListEmpAccounts (UserID, FirstName, LastName, Sex, DOB, CivilStatus, Address, ContactNum, Email, Month, Year, BankName, AccNum, AccName, GovID, GovIDNum, Password, Dept, JobPos, EmpStatus, DateHired, ManagerName, DailyRate, HourlyRate, PerMinuteRate, BasicSalary) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pst = conn.prepareStatement(query);
            pst.setString(1, create_userid.getText());
            pst.setString(2, create_firstname.getText());
            pst.setString(3, create_lastname.getText());
            pst.setString(4, create_sex.getSelectedItem().toString());
            pst.setString(5, create_dateofbirth.getText());
            pst.setString(6, create_civilstatus.getSelectedItem().toString());
            pst.setString(7, create_address.getText());
            pst.setString(8, create_contactnumber.getText());
            pst.setString(9, create_emailaddress.getText());
            pst.setString(10, month.getSelectedItem().toString());
            pst.setString(11, year.getSelectedItem().toString());
            pst.setString(12, bankname.getSelectedItem().toString());
            pst.setString(13, accountnumber.getText());
            pst.setString(14, accountname.getText());
            pst.setString(15, governmentID.getSelectedItem().toString());
            pst.setString(16, govidnumber.getText());
            pst.setString(17, create_password.getText());
            pst.setString(18, create_department.getSelectedItem().toString());
            pst.setString(19, create_jobposition.getSelectedItem().toString());
            pst.setString(20, create_empstatus.getSelectedItem().toString());
            pst.setString(21, create_datehired.getText());
            pst.setString(22, create_managername.getText());
            pst.setDouble(23, Double.parseDouble(create_dailyrate.getText()));
            pst.setDouble(24, Double.parseDouble(create_hourlyrate.getText()));
            pst.setDouble(25, Double.parseDouble(create_perminuterate.getText()));
            pst.setDouble(26, Double.parseDouble(create_basicsalary.getText()));
            pst.executeUpdate();
            pst.close();

            // ✅ Commit everything
            conn.commit();
            JOptionPane.showMessageDialog(null, "All data successfully saved.");

            // 🔃 Clear all fields
            create_userid.setText("");
            create_dateofbirth.setText("");
            create_contactnumber.setText("");
            create_emailaddress.setText("");
            accountnumber.setText("");
            accountname.setText("");
            create_firstname.setText("");
            create_lastname.setText("");
            govidnumber.setText("");
            create_datehired.setText("");
            create_managername.setText("");
            create_password.setText("");
            create_address.setText("");
            create_civilstatus.setSelectedIndex(0);
            bankname.setSelectedIndex(0);
            governmentID.setSelectedIndex(0);
            create_dailyrate.setText("");
            create_hourlyrate.setText("");
            create_perminuterate.setText("");
            create_basicsalary.setText("");
            create_department.setSelectedIndex(0);
            create_jobposition.setSelectedIndex(0);
            create_empstatus.setSelectedIndex(0);
            create_sex.setSelectedIndex(0);
            create_confirm.setEnabled(false);

        } catch (Exception e) {
            try {
                conn.rollback(); // ❌ Rollback if error
            } catch (Exception rollbackEx) {
                JOptionPane.showMessageDialog(null, "Rollback failed: " + rollbackEx.getMessage());
            }
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (pst1 != null) {
                    pst1.close();
                }
                if (pst2 != null) {
                    pst2.close();
                }
                if (pst3 != null) {
                    pst3.close();
                }
                if (pst4 != null) {
                    pst4.close();
                }
                conn.setAutoCommit(true);
                conn.close(); // ✅ Close only after everything is done
            } catch (Exception closeEx) {
                JOptionPane.showMessageDialog(null, "Closing connection error: " + closeEx.getMessage());
            }
        }
    }//GEN-LAST:event_create_confirmMouseClicked

    private void create_confirmActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_confirmActionPerformed


    }//GEN-LAST:event_create_confirmActionPerformed

    private void create_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_create_passwordActionPerformed

    private void create_executeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_create_executeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_create_executeActionPerformed

    private void navigation_banktransferMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_banktransferMouseClicked
        new ADM_ListEmp().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_navigation_banktransferMouseClicked

    private void navigation_createaccMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_createaccMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_navigation_createaccMouseClicked

    private void navigation_dashboardMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_navigation_dashboardMouseClicked
        // TODO add your handling code here:
        new ADM_PaymentHistory().setVisible(true);
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
            java.util.logging.Logger.getLogger(ADM_AddEMP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ADM_AddEMP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ADM_AddEMP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ADM_AddEMP.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ADM_AddEMP().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField accountname;
    private javax.swing.JLabel accountnameTXT_LABEL;
    private javax.swing.JTextField accountnumber;
    private javax.swing.JLabel accountnumberTXT_LABEL;
    private javax.swing.JLabel bankDetail_LABEL;
    private javax.swing.JComboBox<String> bankname;
    private javax.swing.JLabel banknameTXT_LABEL;
    private javax.swing.JTextField create_address;
    private javax.swing.JTextField create_basicsalary;
    private javax.swing.JComboBox<String> create_civilstatus;
    private javax.swing.JButton create_clear;
    private javax.swing.JButton create_confirm;
    private javax.swing.JTextField create_contactnumber;
    private javax.swing.JTextField create_dailyrate;
    private javax.swing.JTextField create_datehired;
    private javax.swing.JTextField create_dateofbirth;
    private javax.swing.JComboBox<String> create_department;
    private javax.swing.JTextField create_emailaddress;
    private javax.swing.JComboBox<String> create_empstatus;
    private javax.swing.JButton create_execute;
    private javax.swing.JTextField create_firstname;
    private javax.swing.JTextField create_hourlyrate;
    private javax.swing.JComboBox<String> create_jobposition;
    private javax.swing.JTextField create_lastname;
    private javax.swing.JTextField create_managername;
    private javax.swing.JTextField create_password;
    private javax.swing.JTextField create_perminuterate;
    private javax.swing.JComboBox<String> create_sex;
    private javax.swing.JTextField create_userid;
    private javax.swing.JLabel credits;
    private javax.swing.JLabel currenttimee;
    private javax.swing.JLabel dailyrateTXT_LABEL;
    private javax.swing.JLabel deptTXT_LABEL;
    private javax.swing.JLabel empstatusTXT_LABEL;
    private javax.swing.JLabel firstnameTXT_LABEL;
    private javax.swing.JLabel govIDTXT_LABEL;
    private javax.swing.JComboBox<String> governmentID;
    private javax.swing.JTextField govidnumber;
    private javax.swing.JLabel govidnumberTXT_LABEL;
    private javax.swing.JLabel govidnumberTXT_LABEL1;
    private javax.swing.JLabel hourlyrateTXT_LABEL;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel jobpositionTXT_LABEL;
    private javax.swing.JLabel lastnameTXT_LABEL;
    private javax.swing.JPanel left_panel;
    private javax.swing.JComboBox<String> month;
    private javax.swing.JLabel monthTXT_LABEL;
    private javax.swing.JLabel navigation_banktransfer;
    private javax.swing.JLabel navigation_createacc;
    private javax.swing.JLabel navigation_dashboard;
    private javax.swing.JLabel notes;
    private javax.swing.JLabel perminuterateTXT_LABEL;
    private javax.swing.JLabel perminuterateTXT_LABEL1;
    private javax.swing.JLabel return_banktransferpage;
    private javax.swing.JLabel sexTXT_LABEL;
    private javax.swing.JLabel sexTXT_LABEL1;
    private javax.swing.JLabel sexTXT_LABEL2;
    private javax.swing.JLabel sexTXT_LABEL3;
    private javax.swing.JLabel sexTXT_LABEL4;
    private javax.swing.JLabel sexTXT_LABEL5;
    private javax.swing.JLabel sexTXT_LABEL6;
    private javax.swing.JLabel sexTXT_LABEL7;
    private javax.swing.JLabel useridTXT_LABEL;
    private javax.swing.JComboBox<String> year;
    private javax.swing.JLabel yearTXT_LABEL;
    // End of variables declaration//GEN-END:variables
}
