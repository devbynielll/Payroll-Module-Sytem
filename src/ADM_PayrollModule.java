/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import payrollsystem.PayrollData;
import payrollsystem.PayrollRecord;
import javax.swing.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import defaultpackage.PaySlip;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.text.PlainDocument;

// Replace `your_package` with the actual package name
/**
 *
 * @author danie
 */
public class ADM_PayrollModule extends javax.swing.JFrame {

    PreparedStatement pst1;
    PreparedStatement pst2;
    PreparedStatement pst3;
    PreparedStatement pst4;
    ResultSet rs;
    Connection conn;

    private javax.swing.JTable notifications_table;

    private javax.swing.JTable newemployee_database; // ✅ Declare it at the class level

    private EMP_Dashboard empDashboard; // Reference to EMP_Dashboard

    private static List<String> payrollPeriods = new ArrayList<>(); // ✅ Stores payroll periods dynamically

    private javax.swing.JComboBox<String> payperiod_combobox;

    public ADM_PayrollModule(EMP_Dashboard empDashboard) {
        this.empDashboard = empDashboard; // Store the EMP_Dashboard reference
        initComponents();

        this.conn = javaconnect.ConnectDb();

        pack();
        setLocationRelativeTo(null);
        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state

        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        setTitle("Payroll Swift"); // ✅ Custom window title

        startDateTimeUpdater();
        populateSalarySummaryTable();

        restrictToIntegers(allowances);
        restrictToIntegers(incentives);
        restrictToIntegers(commissions);
        restrictToIntegers(absences);
        restrictToIntegers(lateundertime);
        restrictToIntegers(unpaidleave);
        restrictToIntegers(others);

        payperiod_combobox = new javax.swing.JComboBox<>();

        // Disable and set non-editable fields
        basicsalary.setEditable(false);
        overtimepay.setEditable(false);
        apply_newEarningDeductions.setEnabled(false);
        reset_newEarningDeductions.setEnabled(false);
        clearAll.setEnabled(false);
        sendSalary_button.setEnabled(false);

        workhours.setEnabled(false);

        workdays.setEditable(false);
        overtimehours.setEditable(false);
        lateorundertime.setEditable(false);

        ((PlainDocument) workdays.getDocument()).setDocumentFilter(new IntegerOnlyFilter());
        ((PlainDocument) overtimehours.getDocument()).setDocumentFilter(new IntegerOnlyFilter());
        ((PlainDocument) lateorundertime.getDocument()).setDocumentFilter(new IntegerOnlyFilter());

        sss.setEditable(false);
        pagibig.setEditable(false);
        philhealth.setEditable(false);
        lateundertime.setEditable(false);

        for (String period : payrollPeriods) {
            payperiod_combobox.addItem(period);

        }

        // (Other fields initialization logic remains unchanged)
        basicsalary.setEditable(false);
        overtimepay.setEditable(false);

        totaldeductions.setEditable(false);
        grosspay.setEditable(false);
        netpay.setEditable(false);
        dailyrate.setEditable(false);
        hourlyrate.setEditable(false);
        perminuterate.setEditable(false);
        grosspay.setEditable(false);
        totaldeductions.setEditable(false);
        dailyrate.setEditable(false);
        hourlyrate.setEditable(false);
        perminuterate.setEditable(false);
        netpay.setEditable(false);
        firstname.setEditable(false);
        lastname.setEditable(false);
        govidnumber.setEditable(false);
        accountnumber.setEditable(false);
        accountname.setEditable(false);

        sendSalary_button.setEnabled(false);

        netpay.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                toggleSendSalaryButton();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                toggleSendSalaryButton();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                toggleSendSalaryButton();
            }
        });
    }

    private void toggleSendSalaryButton() {
        sendSalary_button.setEnabled(!netpay.getText().trim().isEmpty());
    }

    public ADM_PayrollModule() {
        this(null); // Delegate to the parameterized constructor with a null EMP_Dashboard

    }

    private void restrictToIntegers(JTextField field) {
        field.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                if (!Character.isDigit(c)) { // ✅ Allow only digits (0-9)
                    evt.consume(); // ✅ Prevent input of non-numeric characters
                }
            }
        });
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

    private void populateSalarySummaryTable() {
        PreparedStatement pst = null;
        ResultSet rs = null;

        DefaultTableModel model = (DefaultTableModel) Payroll_Database.getModel(); // Replace with your JTable variable
        model.setRowCount(0); // Clear existing rows

        try {
            String query = "SELECT Date, UserID, FullName, JobPos, EmpStatus, AccNum, GrossPay, TotalDeduc, NetPay "
                    + "FROM ADM_SalarySummary ORDER BY Date DESC";

            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            while (rs.next()) {
                String date = rs.getString("Date");
                String userID = rs.getString("UserID");
                String fullName = rs.getString("FullName");
                String jobPos = rs.getString("JobPos");
                String empStatus = rs.getString("EmpStatus");
                String accNum = rs.getString("AccNum");
                double grossPay = rs.getDouble("GrossPay");
                double totalDeduc = rs.getDouble("TotalDeduc");
                double netPay = rs.getDouble("NetPay");

                model.addRow(new Object[]{
                    date,
                    userID,
                    fullName,
                    jobPos,
                    empStatus,
                    accNum,
                    "₱" + String.format("%,.2f", grossPay),
                    "₱" + String.format("%,.2f", totalDeduc),
                    "₱" + String.format("%,.2f", netPay)
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading salary summary: " + e.getMessage());
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        personalData_LABEL = new javax.swing.JPanel();
        left_panel = new javax.swing.JPanel();
        jSeparator2 = new javax.swing.JSeparator();
        name_leftpanel = new javax.swing.JLabel();
        id_leftpanel = new javax.swing.JLabel();
        birthday_leftpanel = new javax.swing.JLabel();
        contact_leftpanel = new javax.swing.JLabel();
        email_leftpanel = new javax.swing.JLabel();
        jobposition_leftpanel = new javax.swing.JLabel();
        department_leftpanel = new javax.swing.JLabel();
        datehired_leftpanel = new javax.swing.JLabel();
        empstatus_leftpanel = new javax.swing.JLabel();
        payschedule_leftpanel = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        logout_btn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        createemployeeaccount = new javax.swing.JButton();
        viewlistofemployees = new javax.swing.JButton();
        paymenthistory = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Payroll_Database = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        idnumber = new javax.swing.JTextField();
        lastname = new javax.swing.JTextField();
        firstname = new javax.swing.JTextField();
        sex = new javax.swing.JComboBox<>();
        useridTXT_LABEL = new javax.swing.JLabel();
        firstnameTXT_LABEL = new javax.swing.JLabel();
        lastnameTXT_LABEL = new javax.swing.JLabel();
        sexTXT_LABEL = new javax.swing.JLabel();
        deptTXT_LABEL = new javax.swing.JLabel();
        jobpositionTXT_LABEL = new javax.swing.JLabel();
        empstatusTXT_LABEL = new javax.swing.JLabel();
        monthTXT_LABEL = new javax.swing.JLabel();
        empstatus = new javax.swing.JComboBox<>();
        department = new javax.swing.JComboBox<>();
        jobposition = new javax.swing.JComboBox<>();
        bankname = new javax.swing.JComboBox<>();
        month = new javax.swing.JComboBox<>();
        year = new javax.swing.JComboBox<>();
        accountname = new javax.swing.JTextField();
        accountnumber = new javax.swing.JTextField();
        incentives = new javax.swing.JTextField();
        yearTXT_LABEL = new javax.swing.JLabel();
        accountnumberTXT_LABEL = new javax.swing.JLabel();
        banknameTXT_LABEL = new javax.swing.JLabel();
        accountnameTXT_LABEL = new javax.swing.JLabel();
        govidnumberTXT_LABEL = new javax.swing.JLabel();
        bankDetail_LABEL = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        userid_generate = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        govidnumber = new javax.swing.JTextField();
        basicsalary = new javax.swing.JTextField();
        overtimepay = new javax.swing.JTextField();
        allowances = new javax.swing.JTextField();
        commissions = new javax.swing.JTextField();
        basicsalaryTXT_LABEL = new javax.swing.JLabel();
        allowancesTXT_LABEL = new javax.swing.JLabel();
        overtimeTXT_LABEL = new javax.swing.JLabel();
        incentivesTXT_LABEL = new javax.swing.JLabel();
        commissionsTXT_LABEL = new javax.swing.JLabel();
        deductionsLABEL = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        dailyrate = new javax.swing.JTextField();
        dailyrateTXT_LABEL = new javax.swing.JLabel();
        hourlyrate = new javax.swing.JTextField();
        perminuterate = new javax.swing.JTextField();
        hourlyrateTXT_LABEL = new javax.swing.JLabel();
        perminuterateTXT_LABEL = new javax.swing.JLabel();
        attendance = new javax.swing.JLabel();
        workdays = new javax.swing.JTextField();
        workdaysTXT_LABEL = new javax.swing.JLabel();
        workhours = new javax.swing.JTextField();
        overtimehours = new javax.swing.JTextField();
        lateorundertime = new javax.swing.JTextField();
        workhoursTXT_LABEL = new javax.swing.JLabel();
        overtimehoursTXT_LABEL = new javax.swing.JLabel();
        lateundertimeTXT_LABEL = new javax.swing.JLabel();
        grosspayTXT_LABEL = new javax.swing.JLabel();
        grosspay = new javax.swing.JTextField();
        absences = new javax.swing.JTextField();
        unpaidleave = new javax.swing.JTextField();
        others = new javax.swing.JTextField();
        absencesTXT_LABEL = new javax.swing.JLabel();
        unpaudleaveTXT_LABEL = new javax.swing.JLabel();
        othersTXT_LABEL = new javax.swing.JLabel();
        totaldeductions = new javax.swing.JTextField();
        totaldeductionsTXT_LABEL = new javax.swing.JLabel();
        netpay = new javax.swing.JTextField();
        netpayTXT_LABEL = new javax.swing.JLabel();
        governmentID = new javax.swing.JComboBox<>();
        govIDTXT_LABEL = new javax.swing.JLabel();
        sendSalary_button = new javax.swing.JButton();
        apply_newEarningDeductions = new javax.swing.JButton();
        reset_newEarningDeductions = new javax.swing.JButton();
        clearAll = new javax.swing.JButton();
        pagibig = new javax.swing.JTextField();
        sssTXT_LABEL = new javax.swing.JLabel();
        philhealth = new javax.swing.JTextField();
        sss = new javax.swing.JTextField();
        philhealthTXT_LABEL = new javax.swing.JLabel();
        pagibigTXT_LABEL = new javax.swing.JLabel();
        lateundertime = new javax.swing.JTextField();
        latesundertimeTXT_LABEL = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        currentdate = new javax.swing.JLabel();
        currenttime = new javax.swing.JLabel();

        personalData_LABEL.setBackground(new java.awt.Color(255, 255, 255));

        left_panel.setBackground(new java.awt.Color(18, 18, 52));
        left_panel.setForeground(new java.awt.Color(0, 0, 0));

        jSeparator2.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        name_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        name_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        name_leftpanel.setText("Name:");

        id_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        id_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        id_leftpanel.setText("Employee ID:");

        birthday_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        birthday_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        birthday_leftpanel.setText("Birthday:");

        contact_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        contact_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        contact_leftpanel.setText("Contact:");

        email_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        email_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        email_leftpanel.setText("Email:");

        jobposition_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        jobposition_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        jobposition_leftpanel.setText("Job Position:");

        department_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        department_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        department_leftpanel.setText("Dept: ");

        datehired_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        datehired_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        datehired_leftpanel.setText("Date Hired:");

        empstatus_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        empstatus_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        empstatus_leftpanel.setText("Emp. Status:");

        payschedule_leftpanel.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        payschedule_leftpanel.setForeground(new java.awt.Color(255, 255, 255));
        payschedule_leftpanel.setText("Pay Schedule:");

        jSeparator4.setBackground(new java.awt.Color(0, 0, 0));
        jSeparator4.setForeground(new java.awt.Color(255, 255, 255));

        logout_btn.setBackground(new java.awt.Color(18, 18, 52));
        logout_btn.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        logout_btn.setForeground(new java.awt.Color(255, 255, 255));
        logout_btn.setText("Logout");
        logout_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logout_btnActionPerformed(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AYROLL WIFT (2) (3).png"))); // NOI18N
        jLabel4.setText(" ");

        createemployeeaccount.setBackground(new java.awt.Color(0, 34, 71));
        createemployeeaccount.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        createemployeeaccount.setForeground(new java.awt.Color(255, 255, 255));
        createemployeeaccount.setText("Create Employee Account");
        createemployeeaccount.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                createemployeeaccountMouseClicked(evt);
            }
        });
        createemployeeaccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                createemployeeaccountActionPerformed(evt);
            }
        });

        viewlistofemployees.setBackground(new java.awt.Color(0, 34, 71));
        viewlistofemployees.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        viewlistofemployees.setForeground(new java.awt.Color(255, 255, 255));
        viewlistofemployees.setText("View List of Employees");
        viewlistofemployees.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                viewlistofemployeesMouseClicked(evt);
            }
        });

        paymenthistory.setBackground(new java.awt.Color(0, 34, 71));
        paymenthistory.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        paymenthistory.setForeground(new java.awt.Color(255, 255, 255));
        paymenthistory.setText("Payment History");
        paymenthistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                paymenthistoryMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout left_panelLayout = new javax.swing.GroupLayout(left_panel);
        left_panel.setLayout(left_panelLayout);
        left_panelLayout.setHorizontalGroup(
            left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, left_panelLayout.createSequentialGroup()
                .addGap(0, 17, Short.MAX_VALUE)
                .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(left_panelLayout.createSequentialGroup()
                        .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(name_leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(id_leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(email_leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(contact_leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(birthday_leftpanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(left_panelLayout.createSequentialGroup()
                                .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jobposition_leftpanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(datehired_leftpanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(empstatus_leftpanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(department_leftpanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(payschedule_leftpanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, left_panelLayout.createSequentialGroup()
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23))))
            .addGroup(left_panelLayout.createSequentialGroup()
                .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(left_panelLayout.createSequentialGroup()
                        .addGap(74, 74, 74)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(logout_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(left_panelLayout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(createemployeeaccount, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, left_panelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(viewlistofemployees, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(paymenthistory, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        left_panelLayout.setVerticalGroup(
            left_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(left_panelLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(paymenthistory, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(viewlistofemployees, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(createemployeeaccount, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(logout_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        Payroll_Database.setBackground(new java.awt.Color(51, 51, 51));
        Payroll_Database.setForeground(new java.awt.Color(255, 255, 255));
        Payroll_Database.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Date", "ID Number", "Full Name", "Job Position", "Employee Status", "Account Number", "Gross Pay", "Total Deductions", "Net Pay"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        Payroll_Database.setRowHeight(30);
        Payroll_Database.setShowGrid(true);
        Payroll_Database.setSurrendersFocusOnKeystroke(true);
        Payroll_Database.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(Payroll_Database);
        if (Payroll_Database.getColumnModel().getColumnCount() > 0) {
            Payroll_Database.getColumnModel().getColumn(0).setResizable(false);
            Payroll_Database.getColumnModel().getColumn(1).setResizable(false);
            Payroll_Database.getColumnModel().getColumn(2).setResizable(false);
            Payroll_Database.getColumnModel().getColumn(3).setResizable(false);
            Payroll_Database.getColumnModel().getColumn(4).setResizable(false);
            Payroll_Database.getColumnModel().getColumn(5).setResizable(false);
            Payroll_Database.getColumnModel().getColumn(6).setResizable(false);
            Payroll_Database.getColumnModel().getColumn(7).setResizable(false);
            Payroll_Database.getColumnModel().getColumn(8).setResizable(false);
        }

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 51));
        jLabel2.setText("Personal Data");

        idnumber.setBackground(new java.awt.Color(255, 255, 255));
        idnumber.setForeground(new java.awt.Color(0, 0, 0));

        lastname.setBackground(new java.awt.Color(255, 255, 255));
        lastname.setForeground(new java.awt.Color(0, 0, 0));

        firstname.setBackground(new java.awt.Color(255, 255, 255));
        firstname.setForeground(new java.awt.Color(0, 0, 0));

        sex.setBackground(new java.awt.Color(255, 255, 255));
        sex.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        sex.setForeground(new java.awt.Color(0, 0, 51));
        sex.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Male", "Female" }));
        sex.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sexActionPerformed(evt);
            }
        });

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

        monthTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        monthTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        monthTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        monthTXT_LABEL.setText("Month");

        empstatus.setBackground(new java.awt.Color(255, 255, 255));
        empstatus.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        empstatus.setForeground(new java.awt.Color(0, 0, 51));
        empstatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Full-Time", "Part-Time", "Probationary", "Contractual" }));
        empstatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                empstatusActionPerformed(evt);
            }
        });

        department.setBackground(new java.awt.Color(255, 255, 255));
        department.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        department.setForeground(new java.awt.Color(0, 0, 51));
        department.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Software Development", "UI/UX and Design", "IT Infrastructure", "Cybersecurity", "Data and Analytics", "AI and Machine Learning" }));
        department.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                departmentActionPerformed(evt);
            }
        });

        jobposition.setBackground(new java.awt.Color(255, 255, 255));
        jobposition.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jobposition.setForeground(new java.awt.Color(0, 0, 51));
        jobposition.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Software Developer", "Front-End Developer", "Back-End Developer", "Full-Stack Developer", "Mobile App Developer", "UI/UX Designer", "System Administrator", "Network Administrator", "Cybersecurity Analyst", "Security Engineer", "Database Administrator", "Machine Learning Engineer", "AI Engineer", "Project Manager" }));
        jobposition.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jobpositionActionPerformed(evt);
            }
        });

        bankname.setBackground(new java.awt.Color(255, 255, 255));
        bankname.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        bankname.setForeground(new java.awt.Color(0, 0, 51));
        bankname.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BDO Unibank", "Metrobank", "Bank of the Philippine Islands (BPI)", "Philippine National Bank (PNB)", "Land Bank of the Philippines (LandBank)", "Security Bank", "China Banking Corporation (China Bank)", "UnionBank of the Philippines", "EastWest Bank", "Rizal Commercial Banking Corporation (RCBC)", "Development Bank of the Philippines (DBP)", "UCPB (United Coconut Planters Bank)", "Philippine Savings Bank (PSBank)", "Maybank Philippines", "Standard Chartered Bank Philippines" }));
        bankname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                banknameActionPerformed(evt);
            }
        });

        month.setBackground(new java.awt.Color(255, 255, 255));
        month.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        month.setForeground(new java.awt.Color(0, 0, 51));
        month.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December" }));
        month.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                monthActionPerformed(evt);
            }
        });

        year.setBackground(new java.awt.Color(255, 255, 255));
        year.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        year.setForeground(new java.awt.Color(0, 0, 51));
        year.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2026", "2027", "2028", "2029", "2030" }));
        year.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearActionPerformed(evt);
            }
        });

        accountname.setBackground(new java.awt.Color(255, 255, 255));
        accountname.setForeground(new java.awt.Color(0, 0, 0));

        accountnumber.setBackground(new java.awt.Color(255, 255, 255));
        accountnumber.setForeground(new java.awt.Color(0, 0, 0));

        incentives.setBackground(new java.awt.Color(255, 255, 255));
        incentives.setForeground(new java.awt.Color(0, 0, 0));

        yearTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        yearTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        yearTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        yearTXT_LABEL.setText("Year");

        accountnumberTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        accountnumberTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        accountnumberTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        accountnumberTXT_LABEL.setText("Account Number");

        banknameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        banknameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        banknameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        banknameTXT_LABEL.setText("Bank Name");

        accountnameTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        accountnameTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        accountnameTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        accountnameTXT_LABEL.setText("Account Name");

        govidnumberTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        govidnumberTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        govidnumberTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        govidnumberTXT_LABEL.setText("Gov. ID Number");

        bankDetail_LABEL.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        bankDetail_LABEL.setForeground(new java.awt.Color(0, 0, 51));
        bankDetail_LABEL.setText("Bank Detail");

        userid_generate.setBackground(new java.awt.Color(28, 28, 54));
        userid_generate.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        userid_generate.setForeground(new java.awt.Color(255, 255, 255));
        userid_generate.setText("Generate");
        userid_generate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userid_generateActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 51));
        jLabel1.setText("Monthly Entries");

        govidnumber.setBackground(new java.awt.Color(255, 255, 255));
        govidnumber.setForeground(new java.awt.Color(0, 0, 0));

        basicsalary.setBackground(new java.awt.Color(255, 255, 255));
        basicsalary.setForeground(new java.awt.Color(0, 0, 0));

        overtimepay.setBackground(new java.awt.Color(255, 255, 255));
        overtimepay.setForeground(new java.awt.Color(0, 0, 0));

        allowances.setBackground(new java.awt.Color(255, 255, 255));
        allowances.setForeground(new java.awt.Color(0, 0, 0));

        commissions.setBackground(new java.awt.Color(255, 255, 255));
        commissions.setForeground(new java.awt.Color(0, 0, 0));

        basicsalaryTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        basicsalaryTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        basicsalaryTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        basicsalaryTXT_LABEL.setText("Basic Salary");

        allowancesTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        allowancesTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        allowancesTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        allowancesTXT_LABEL.setText("Allowances");

        overtimeTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        overtimeTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        overtimeTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        overtimeTXT_LABEL.setText("Overtime Pay");

        incentivesTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        incentivesTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        incentivesTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        incentivesTXT_LABEL.setText("Incentives");

        commissionsTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        commissionsTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        commissionsTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        commissionsTXT_LABEL.setText("Commissions");

        deductionsLABEL.setBackground(new java.awt.Color(102, 0, 0));
        deductionsLABEL.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        deductionsLABEL.setForeground(new java.awt.Color(102, 0, 0));
        deductionsLABEL.setText("Deductions");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(0, 0, 51));
        jLabel5.setText("Computed Rates");

        dailyrate.setBackground(new java.awt.Color(255, 255, 255));
        dailyrate.setForeground(new java.awt.Color(0, 0, 0));

        dailyrateTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        dailyrateTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        dailyrateTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        dailyrateTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        dailyrateTXT_LABEL.setText("Daily Rate");

        hourlyrate.setBackground(new java.awt.Color(255, 255, 255));
        hourlyrate.setForeground(new java.awt.Color(0, 0, 0));

        perminuterate.setBackground(new java.awt.Color(255, 255, 255));
        perminuterate.setForeground(new java.awt.Color(0, 0, 0));

        hourlyrateTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        hourlyrateTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        hourlyrateTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        hourlyrateTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        hourlyrateTXT_LABEL.setText("Hourly Rate");

        perminuterateTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        perminuterateTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        perminuterateTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        perminuterateTXT_LABEL.setText("Per Minute Rate");

        attendance.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        attendance.setForeground(new java.awt.Color(0, 0, 51));
        attendance.setText("Attendance");

        workdays.setBackground(new java.awt.Color(255, 255, 255));
        workdays.setForeground(new java.awt.Color(0, 0, 0));

        workdaysTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        workdaysTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        workdaysTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        workdaysTXT_LABEL.setText("Work Days");

        workhours.setBackground(new java.awt.Color(255, 255, 255));
        workhours.setForeground(new java.awt.Color(0, 0, 0));

        overtimehours.setBackground(new java.awt.Color(255, 255, 255));
        overtimehours.setForeground(new java.awt.Color(0, 0, 0));

        lateorundertime.setBackground(new java.awt.Color(255, 255, 255));
        lateorundertime.setForeground(new java.awt.Color(0, 0, 0));

        workhoursTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        workhoursTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        workhoursTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        workhoursTXT_LABEL.setText("Work Hours");

        overtimehoursTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        overtimehoursTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        overtimehoursTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        overtimehoursTXT_LABEL.setText("Overtime Hours");

        lateundertimeTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        lateundertimeTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        lateundertimeTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        lateundertimeTXT_LABEL.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lateundertimeTXT_LABEL.setText("Late/Undertime Minutes");

        grosspayTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        grosspayTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        grosspayTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        grosspayTXT_LABEL.setText("Gross Pay");

        grosspay.setBackground(new java.awt.Color(255, 255, 255));
        grosspay.setForeground(new java.awt.Color(0, 0, 0));

        absences.setBackground(new java.awt.Color(255, 255, 255));
        absences.setForeground(new java.awt.Color(0, 0, 0));

        unpaidleave.setBackground(new java.awt.Color(255, 255, 255));
        unpaidleave.setForeground(new java.awt.Color(0, 0, 0));

        others.setBackground(new java.awt.Color(255, 255, 255));
        others.setForeground(new java.awt.Color(0, 0, 0));

        absencesTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        absencesTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        absencesTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        absencesTXT_LABEL.setText("Absences");

        unpaudleaveTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        unpaudleaveTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        unpaudleaveTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        unpaudleaveTXT_LABEL.setText("Unpaid Leave");

        othersTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        othersTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        othersTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        othersTXT_LABEL.setText("Others");

        totaldeductions.setBackground(new java.awt.Color(255, 255, 255));
        totaldeductions.setForeground(new java.awt.Color(0, 0, 0));
        totaldeductions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totaldeductionsActionPerformed(evt);
            }
        });

        totaldeductionsTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        totaldeductionsTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        totaldeductionsTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        totaldeductionsTXT_LABEL.setText("Total Deductions");

        netpay.setBackground(new java.awt.Color(255, 255, 255));
        netpay.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        netpay.setForeground(new java.awt.Color(0, 0, 0));

        netpayTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        netpayTXT_LABEL.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        netpayTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        netpayTXT_LABEL.setText("Net Pay");

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

        sendSalary_button.setBackground(new java.awt.Color(0, 0, 51));
        sendSalary_button.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        sendSalary_button.setForeground(new java.awt.Color(255, 255, 255));
        sendSalary_button.setText("Send Salary");
        sendSalary_button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sendSalary_buttonMouseClicked(evt);
            }
        });
        sendSalary_button.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendSalary_buttonActionPerformed(evt);
            }
        });

        apply_newEarningDeductions.setBackground(new java.awt.Color(0, 0, 51));
        apply_newEarningDeductions.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        apply_newEarningDeductions.setForeground(new java.awt.Color(255, 255, 255));
        apply_newEarningDeductions.setText("Apply");
        apply_newEarningDeductions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                apply_newEarningDeductionsMouseClicked(evt);
            }
        });

        reset_newEarningDeductions.setBackground(new java.awt.Color(51, 0, 0));
        reset_newEarningDeductions.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        reset_newEarningDeductions.setForeground(new java.awt.Color(255, 255, 255));
        reset_newEarningDeductions.setText("Reset");
        reset_newEarningDeductions.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                reset_newEarningDeductionsMouseClicked(evt);
            }
        });
        reset_newEarningDeductions.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reset_newEarningDeductionsActionPerformed(evt);
            }
        });

        clearAll.setBackground(new java.awt.Color(51, 0, 0));
        clearAll.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        clearAll.setForeground(new java.awt.Color(255, 255, 255));
        clearAll.setText("Clear All");
        clearAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                clearAllMouseClicked(evt);
            }
        });

        pagibig.setBackground(new java.awt.Color(255, 255, 255));

        sssTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        sssTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        sssTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        sssTXT_LABEL.setText("SSS");

        philhealth.setBackground(new java.awt.Color(255, 255, 255));

        sss.setBackground(new java.awt.Color(255, 255, 255));

        philhealthTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        philhealthTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        philhealthTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        philhealthTXT_LABEL.setText("PhilHealth");

        pagibigTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        pagibigTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        pagibigTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        pagibigTXT_LABEL.setText("Pag-IBIG");

        lateundertime.setBackground(new java.awt.Color(255, 255, 255));

        latesundertimeTXT_LABEL.setBackground(new java.awt.Color(0, 0, 0));
        latesundertimeTXT_LABEL.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        latesundertimeTXT_LABEL.setForeground(new java.awt.Color(0, 0, 0));
        latesundertimeTXT_LABEL.setText("Lates/Undertime");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(102, 0, 0));
        jLabel6.setText("Gov. Contributions");

        currentdate.setFont(new java.awt.Font("Segoe UI Semibold", 1, 14)); // NOI18N
        currentdate.setForeground(new java.awt.Color(0, 0, 0));
        currentdate.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        currentdate.setText("APRIL 12, 2025");

        currenttime.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        currenttime.setForeground(new java.awt.Color(0, 0, 0));
        currenttime.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        currenttime.setText("00:00:00");

        javax.swing.GroupLayout personalData_LABELLayout = new javax.swing.GroupLayout(personalData_LABEL);
        personalData_LABEL.setLayout(personalData_LABELLayout);
        personalData_LABELLayout.setHorizontalGroup(
            personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(personalData_LABELLayout.createSequentialGroup()
                .addComponent(left_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createSequentialGroup()
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(basicsalaryTXT_LABEL)
                                    .addComponent(allowancesTXT_LABEL)
                                    .addComponent(overtimeTXT_LABEL)
                                    .addComponent(incentivesTXT_LABEL)
                                    .addComponent(commissionsTXT_LABEL)
                                    .addComponent(grosspayTXT_LABEL))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(overtimepay)
                                    .addComponent(allowances)
                                    .addComponent(incentives)
                                    .addComponent(commissions)
                                    .addComponent(basicsalary)
                                    .addComponent(grosspay, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                                .addGap(18, 18, 18)
                                                .addComponent(apply_newEarningDeductions, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(reset_newEarningDeductions, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createSequentialGroup()
                                                .addGap(47, 47, 47)
                                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(absencesTXT_LABEL)
                                                    .addComponent(unpaudleaveTXT_LABEL)
                                                    .addComponent(othersTXT_LABEL)
                                                    .addComponent(latesundertimeTXT_LABEL))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(unpaidleave)
                                                        .addComponent(others)
                                                        .addComponent(absences, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(lateundertime, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(38, 38, 38)
                                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(sssTXT_LABEL)
                                                    .addComponent(philhealthTXT_LABEL)
                                                    .addComponent(pagibigTXT_LABEL))))
                                        .addGap(18, 18, 18)
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(sss, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(philhealth, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(pagibig, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(clearAll, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(netpayTXT_LABEL)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(netpay, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                        .addGap(226, 226, 226)
                                        .addComponent(totaldeductionsTXT_LABEL)
                                        .addGap(18, 18, 18)
                                        .addComponent(totaldeductions, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                                        .addComponent(sendSalary_button, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(23, 23, 23))
                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(useridTXT_LABEL)
                                            .addComponent(firstnameTXT_LABEL)
                                            .addComponent(lastnameTXT_LABEL)
                                            .addComponent(sexTXT_LABEL)
                                            .addComponent(deptTXT_LABEL)
                                            .addComponent(jobpositionTXT_LABEL)
                                            .addComponent(empstatusTXT_LABEL))
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                                .addGap(215, 215, 215)
                                                .addComponent(userid_generate))
                                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(sex, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(department, 0, 1, Short.MAX_VALUE)
                                                    .addComponent(lastname)
                                                    .addComponent(firstname)
                                                    .addComponent(idnumber)
                                                    .addComponent(jobposition, 0, 1, Short.MAX_VALUE)
                                                    .addComponent(empstatus, 0, 200, Short.MAX_VALUE)))))
                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(jLabel2)))
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                        .addGap(36, 36, 36)
                                        .addComponent(bankDetail_LABEL))
                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(personalData_LABELLayout.createSequentialGroup()
                                                    .addGap(36, 36, 36)
                                                    .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(banknameTXT_LABEL)
                                                        .addComponent(yearTXT_LABEL)))
                                                .addComponent(accountnumberTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(monthTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(govidnumberTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(govIDTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING)
                                                    .addComponent(accountnameTXT_LABEL, javax.swing.GroupLayout.Alignment.TRAILING))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(accountname)
                                            .addComponent(accountnumber)
                                            .addComponent(bankname, 0, 1, Short.MAX_VALUE)
                                            .addComponent(month, 0, 201, Short.MAX_VALUE)
                                            .addComponent(govidnumber, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(governmentID, javax.swing.GroupLayout.Alignment.TRAILING, 0, 1, Short.MAX_VALUE))
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                                .addGap(67, 67, 67)
                                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                            .addComponent(workdaysTXT_LABEL)
                                                            .addComponent(workhoursTXT_LABEL)
                                                            .addComponent(overtimehoursTXT_LABEL)
                                                            .addComponent(lateundertimeTXT_LABEL))
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                            .addComponent(workhours, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 89, Short.MAX_VALUE)
                                                            .addComponent(overtimehours, javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(lateorundertime, javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(workdays)))
                                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                                        .addGap(68, 68, 68)
                                                        .addComponent(attendance)))
                                                .addGap(31, 31, 31))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(currentdate, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(currenttime, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(23, 23, 23))))))
                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                        .addGap(821, 821, 821)
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                                .addComponent(perminuterateTXT_LABEL)
                                                .addGap(18, 18, 18)
                                                .addComponent(perminuterate, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createSequentialGroup()
                                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(hourlyrateTXT_LABEL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(dailyrateTXT_LABEL, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGap(18, 18, 18)
                                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(hourlyrate)
                                                    .addComponent(dailyrate, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(163, 163, 163)
                                        .addComponent(deductionsLABEL)
                                        .addGap(152, 152, 152)
                                        .addComponent(jLabel6)
                                        .addGap(135, 135, 135)
                                        .addComponent(jLabel5)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1007, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(20, 20, 20))
        );
        personalData_LABELLayout.setVerticalGroup(
            personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(left_panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(personalData_LABELLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(bankDetail_LABEL)
                    .addComponent(attendance))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(useridTXT_LABEL)
                    .addComponent(monthTXT_LABEL)
                    .addComponent(month, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userid_generate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(workdays, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(workdaysTXT_LABEL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(firstname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(firstnameTXT_LABEL)
                    .addComponent(year, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(yearTXT_LABEL)
                    .addComponent(workhours, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(workhoursTXT_LABEL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lastname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lastnameTXT_LABEL)
                    .addComponent(bankname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(banknameTXT_LABEL)
                    .addComponent(overtimehours, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(overtimehoursTXT_LABEL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sex, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sexTXT_LABEL)
                    .addComponent(accountnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accountnumberTXT_LABEL)
                    .addComponent(lateorundertime, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lateundertimeTXT_LABEL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(department, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(deptTXT_LABEL)
                    .addComponent(accountname, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(accountnameTXT_LABEL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jobposition, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jobpositionTXT_LABEL)
                    .addComponent(governmentID, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(govIDTXT_LABEL))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(empstatus, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(empstatusTXT_LABEL)
                            .addComponent(govidnumber, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(govidnumberTXT_LABEL))
                        .addGap(29, 29, 29))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createSequentialGroup()
                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(currenttime, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(currentdate, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(deductionsLABEL)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addGap(18, 18, 18)
                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(basicsalary, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(basicsalaryTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(overtimepay, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(overtimeTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(allowances, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(allowancesTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(incentives, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(incentivesTXT_LABEL))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(commissions, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(commissionsTXT_LABEL)))
                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(absences, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(absencesTXT_LABEL)
                                    .addComponent(sssTXT_LABEL))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(philhealth, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(philhealthTXT_LABEL)
                                    .addComponent(lateundertime, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(latesundertimeTXT_LABEL))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(unpaidleave, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(unpaudleaveTXT_LABEL)
                                    .addComponent(pagibigTXT_LABEL)
                                    .addComponent(pagibig, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(others, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(othersTXT_LABEL))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sss, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(personalData_LABELLayout.createSequentialGroup()
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(dailyrate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(dailyrateTXT_LABEL))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(hourlyrate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(hourlyrateTXT_LABEL))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(perminuterate, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(perminuterateTXT_LABEL))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                                .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createSequentialGroup()
                                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(netpay, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(netpayTXT_LABEL))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(sendSalary_button, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(grosspay, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(grosspayTXT_LABEL)
                                        .addComponent(totaldeductions, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(totaldeductionsTXT_LABEL)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(personalData_LABELLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(reset_newEarningDeductions, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
                                .addComponent(clearAll, javax.swing.GroupLayout.DEFAULT_SIZE, 32, Short.MAX_VALUE))
                            .addGroup(personalData_LABELLayout.createSequentialGroup()
                                .addComponent(apply_newEarningDeductions, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 1, Short.MAX_VALUE)))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(personalData_LABEL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(personalData_LABEL, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void sexActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sexActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sexActionPerformed

    private void empstatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_empstatusActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_empstatusActionPerformed

    private void departmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_departmentActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_departmentActionPerformed

    private void jobpositionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jobpositionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jobpositionActionPerformed

    private void banknameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_banknameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_banknameActionPerformed

    private void monthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_monthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_monthActionPerformed

    private void yearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yearActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_yearActionPerformed

    private void userid_generateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userid_generateActionPerformed

        workdays.setEditable(true);
        overtimehours.setEditable(true);
        lateorundertime.setEditable(true);

        ResultSet rs = null;
        PreparedStatement pst1 = null;
        PreparedStatement pst2 = null;

        apply_newEarningDeductions.setEnabled(true);
        reset_newEarningDeductions.setEnabled(true);

        clearAll.setEnabled(true);

        try {
            String userID = idnumber.getText();

            // Query PersonalData by UserID
            String sqlpd = "SELECT * FROM PersonalData WHERE UserID=?";
            pst1 = conn.prepareStatement(sqlpd);
            pst1.setString(1, userID);
            rs = pst1.executeQuery();

            if (rs.next()) {
                // Set personal data fields
                idnumber.setText(rs.getString("UserID"));
                firstname.setText(rs.getString("FirstName"));
                lastname.setText(rs.getString("LastName"));
                sex.setSelectedItem(rs.getString("Sex"));

                String fullName = "Name: " + firstname.getText() + " " + lastname.getText();
                name_leftpanel.setText(fullName);

                id_leftpanel.setText("Employee ID: " + rs.getString("UserID"));
                birthday_leftpanel.setText("Birth Date: " + rs.getString("DOB"));
                contact_leftpanel.setText("Contact No.: " + rs.getString("Contact_Num"));
                email_leftpanel.setText("Email: " + rs.getString("EmailAdd"));

                firstname.setEditable(false);
                lastname.setEditable(false);
                sex.setEnabled(false);

            } else {
                JOptionPane.showMessageDialog(null, "UserID not found in database.");

                // Clear form or reset fields here if needed
                firstname.setText("");
                lastname.setText("");
                sex.setSelectedIndex(-1);
                department.setSelectedIndex(-1);
                jobposition.setSelectedIndex(-1);
                empstatus.setSelectedIndex(-1);
                // Clear other fields as needed

                return; // Exit the method early
            }
            rs.close();
            pst1.close();

            // Query EmploymentInformation by UserID
            String sqlei = "SELECT * FROM EmploymentInformation WHERE UserID = ?";
            pst2 = conn.prepareStatement(sqlei);
            pst2.setString(1, userID);
            rs = pst2.executeQuery();

            if (rs.next()) {
                department.setSelectedItem(rs.getString("Department"));
                jobposition.setSelectedItem(rs.getString("JobPos"));
                empstatus.setSelectedItem(rs.getString("EmpStat"));

                jobposition_leftpanel.setText("Job Position: " + rs.getString("JobPos"));
                department_leftpanel.setText("Dept: " + rs.getString("Department"));
                datehired_leftpanel.setText("Date Hired: " + rs.getString("DateHired"));
                empstatus_leftpanel.setText("Employment Status: " + rs.getString("EmpStat"));
                payschedule_leftpanel.setText("Pay Schedule: Monthly");

                department.setEnabled(false);
                jobposition.setEnabled(false);
                empstatus.setEnabled(false);

            } else {
                department.setSelectedIndex(-1);
                jobposition.setSelectedIndex(-1);
                empstatus.setSelectedIndex(-1);
            }
            rs.close();
            pst2.close();

            // 3. Query BankDetails by UserID
            String sqlbd = "SELECT * FROM BankDetails WHERE UserID = ?";
            pst3 = conn.prepareStatement(sqlbd);
            pst3.setString(1, userID);
            rs = pst3.executeQuery();

            if (rs.next()) {
                month.setSelectedItem(rs.getString("Month"));
                year.setSelectedItem(rs.getString("Year"));
                bankname.setSelectedItem(rs.getString("BankName"));
                accountnumber.setText(rs.getString("AccountNum"));
                accountname.setText(rs.getString("AccountName"));
                governmentID.setSelectedItem(rs.getString("GovernmentID"));
                govidnumber.setText(rs.getString("GovIDNum"));

                month.setEnabled(false);
                year.setEnabled(false);
                bankname.setEnabled(false);
                accountnumber.setEditable(false);
                accountname.setEditable(false);
                governmentID.setEnabled(false);
                govidnumber.setEditable(false);

            } else {
                month.setSelectedIndex(-1);
                year.setSelectedIndex(-1);
                bankname.setSelectedIndex(-1);
                accountnumber.setText("");
                accountname.setText("");
                governmentID.setSelectedIndex(-1);
                govidnumber.setText("");
            }
            rs.close();
            pst3.close();

            String sqlcr = "SELECT * FROM ComputedRates WHERE UserID = ?";
            pst4 = conn.prepareStatement(sqlcr); // ✅ Correct usage
            pst4.setString(1, userID);
            rs = pst4.executeQuery();

            if (rs.next()) {
                // Set personal data fields
                dailyrate.setText(rs.getString("DailyRate"));
                hourlyrate.setText(rs.getString("HourlyRate"));
                perminuterate.setText(rs.getString("PerMinRate"));

                dailyrate.setEditable(false);
                hourlyrate.setEditable(false);
                perminuterate.setEditable(false);

            } else {
                JOptionPane.showMessageDialog(null, "UserID not found in database.");
            }
            rs.close();
            pst4.close();

            // 4. Query Automated_Attendance by UserID
            String sqlatt = "SELECT * FROM Automated_Attendance WHERE UserID = ?";
            PreparedStatement pst5 = conn.prepareStatement(sqlatt);
            pst5.setString(1, userID);
            rs = pst5.executeQuery();

            if (rs.next()) {
                workdays.setText(rs.getString("WorkDays"));
                overtimehours.setText(rs.getString("OvertimeHours"));
                lateorundertime.setText(rs.getString("LateUndetimeMin"));

            } else {
                workdays.setText("");
                overtimehours.setText("");
                lateorundertime.setText("");
            }
            rs.close();
            pst5.close();

            // Optional: You can continue querying other tables like ComputedRates here...
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst1 != null) {
                    pst1.close();
                }
                if (pst2 != null) {
                    pst2.close();
                }
                if (pst3 != null) {
                    pst3.close();
                }
            } catch (Exception ex) {
                // Optional logging
            }
        }

    }//GEN-LAST:event_userid_generateActionPerformed


    private void governmentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_governmentIDActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_governmentIDActionPerformed

    private void apply_newEarningDeductionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_apply_newEarningDeductionsMouseClicked

        applyEarningsAndDeductions();

    }

// ✅ Apply Manual Earnings & Deductions
    private void applyEarningsAndDeductions() {
        double dailyRate = 0, hourlyRate = 0, perMinuteRate = 0;

        String position = jobposition.getSelectedItem().toString();
        String employmentStatus = empstatus.getSelectedItem().toString();

        sss.setEditable(false);
        philhealth.setEditable(false);
        pagibig.setEditable(false);

        lateundertime.setEditable(false);

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

        // ✅ Multiply Workdays with Daily Rate to get Basic Salary
        int workDays = 0;

        if (!this.workdays.getText()
                .isEmpty()) {
            try {
                workDays = Integer.parseInt(this.workdays.getText());
            } catch (NumberFormatException e) {
                workDays = 0;
            }
        }

        double basicSalary = dailyRate * workDays;

        basicsalary.setText(formatMoney(basicSalary)); // Update field

        double sssContribution = 0;
        double philHealth = 0;
        double pagIbig = 0;

        if (basicSalary
                > 0) {
            sssContribution = computeSSS(basicSalary);

            double[] philHealthContributions = computePhilHealth(basicSalary);
            philHealth = philHealthContributions[0];  // employee share

            pagIbig = computePagibig(basicSalary);

            sss.setText(formatMoney(sssContribution));
            philhealth.setText(formatMoney(philHealth));
            pagibig.setText(formatMoney(pagIbig));
        } else {
            sss.setText(formatMoney(0));
            philhealth.setText(formatMoney(0));
            pagibig.setText(formatMoney(0));
        }

        // ✅ Automatically compute Daily Hours (8 hours per workday)
        int totalHours = workDays * 8;

        workhours.setText(String.valueOf(totalHours));

        // ✅ Parse Other Earnings
        double allowancesValue = parseMoneySafe(allowances.getText());
        double incentivesValue = parseMoneySafe(incentives.getText());
        double commissionsValue = parseMoneySafe(commissions.getText());
        double overtimePay = parseMoneySafe(overtimepay.getText());

        // ✅ Deductions
        double absencesOrLates = absences.getText().isEmpty() ? 0 : parseMoneySafe(absences.getText());
        double unpaidLeaveValue = unpaidleave.getText().isEmpty() ? 0 : parseMoneySafe(unpaidleave.getText());
        double otherDeductions = others.getText().isEmpty() ? 0 : parseMoneySafe(others.getText());
        double lateUndertime = lateundertime.getText().isEmpty() ? 0 : parseMoneySafe(lateundertime.getText());

        double overtimeHours = 0;

        if (!overtimehours.getText()
                .isEmpty()) {
            try {
                overtimeHours = Double.parseDouble(overtimehours.getText());
            } catch (NumberFormatException e) {
                overtimeHours = 0;
            }
        }
        overtimePay = overtimeHours * hourlyRate * 1.25; // 125% of regular hourly rate

        overtimepay.setText(formatMoney(overtimePay));

// ✅ Compute Late/Undertime Deduction from Minutes
        int lateOrUndertimeMinutes = 0;

        if (!lateorundertime.getText()
                .isEmpty()) {
            try {
                lateOrUndertimeMinutes = Integer.parseInt(lateorundertime.getText());
            } catch (NumberFormatException e) {
                lateOrUndertimeMinutes = 0;
            }
        }
        lateUndertime = lateOrUndertimeMinutes * perMinuteRate;

        lateundertime.setText(formatMoney(lateUndertime));

        double totalDeductions = sssContribution + philHealth + pagIbig + absencesOrLates + unpaidLeaveValue + otherDeductions + lateUndertime;

        // ✅ Compute Gross and Net Pay
        double updatedGrossPay = basicSalary + overtimePay + allowancesValue + incentivesValue + commissionsValue;
        double updatedNetPay = updatedGrossPay - totalDeductions;

        // ✅ Update UI Fields
        grosspay.setText(formatMoney(updatedGrossPay));
        totaldeductions.setText(formatMoney(totalDeductions));
        netpay.setText(formatMoney(updatedNetPay));

        sendSalary_button.setEnabled(
                true);

    }

    public static double[] computePhilHealth(double salary) {
        double minSalary = 10000.01;
        double maxSalary = 100000.00;
        double premiumRate = 0.05; // 5%

        // Apply salary caps
        double applicableSalary = Math.min(Math.max(salary, minSalary), maxSalary);

        // Calculate total premium contribution
        double totalPremium = applicableSalary * premiumRate;

        // Split equally between employee and employer
        double employeeShare = totalPremium / 2;
        double employerShare = totalPremium / 2;

        return new double[]{employeeShare, employerShare};
    }

    public double computePagibig(double salary) {
        double salaryCap = 10000.0;
        double applicableSalary = Math.min(salary, salaryCap);

        double employeeRate = (salary <= 1500) ? 0.01 : 0.02;
        return applicableSalary * employeeRate;
    }

    private double computeSSS(double salary) {
        // Clamp salary to MSC limits
        double minMSC = 5000;
        double maxMSC = 35000;

        // If salary is below minimum, use minimum MSC
        if (salary < minMSC) {
            salary = minMSC;
        }

        // If salary is above maximum, cap it
        if (salary > maxMSC) {
            salary = maxMSC;
        }

        // Round down to the nearest 500 for valid MSC
        double msc = Math.floor(salary / 500) * 500;

        // SSS total contribution is 15% of MSC
        double totalContribution = msc * 0.15;

        // Employee share is 5%
        double employeeShare = totalContribution * (5.0 / 15.0);

        return employeeShare;
    }

    // ✅ Helper Method: Safely Parse Money Fields (Prevents Empty Input Errors)
    private double parseMoneySafe(String text) {
        try {
            return Double.parseDouble(text.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0; // Default to zero if input is invalid
        }
    }

    // ✅ Helper Method: Safely Convert Strings to Doubles (Prevents Errors)
    private double parseDoubleSafe(String text) {
        try {
            return Double.parseDouble(text.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0; // Return 0 if the input is empty or invalid
        }

    }

// Helper to format money (with commas)
    private String formatMoney(double amount) {
        return String.format("%,.2f", amount);
    }

// Helper to parse money (removes commas)
    private double parseMoney(String amount) {
        try {
            return Double.parseDouble(amount.replace(",", ""));
        } catch (NumberFormatException e) {
            return 0.0;
        }

    }//GEN-LAST:event_apply_newEarningDeductionsMouseClicked
    private void updateTotalDeductions() {
        // Parse individual deduction fields
        double absencesOrLates = absences.getText().isEmpty() ? 0 : parseMoney(absences.getText());
        double unpaidLeave = unpaidleave.getText().isEmpty() ? 0 : parseMoney(unpaidleave.getText());
        double otherss = others.getText().isEmpty() ? 0 : parseMoney(others.getText());

        // ✅ Calculate total deductions (IGNORE blank fields)
        double totalDeductions = absencesOrLates + unpaidLeave + otherss;

        // ✅ Update UI
        totaldeductions.setText(formatMoney(totalDeductions));
    }


    private void reset_newEarningDeductionsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_reset_newEarningDeductionsMouseClicked
        resetEarningsAndDeductions();
    }

    private void resetEarningsAndDeductions() {
        allowances.setText("");
        incentives.setText("");
        commissions.setText("");

        // ✅ Reset deduction fields
        absences.setText("");
        unpaidleave.setText("");
        others.setText("");

        // ✅ Restore default gross pay (Basic Salary + Overtime)
        double basicSalary = parseMoneySafe(basicsalary.getText());
        double overtimePay = parseMoneySafe(overtimepay.getText());

        double defaultGrossPay = basicSalary + overtimePay;

        // ✅ Restore default total deductions (Only Gov. Contributions)
        double absencesOrLates = parseMoneySafe(absences.getText());
        double lateUndertime = parseMoneySafe(lateundertime.getText()); // ✅ include this!
        double philHealth = parseMoneySafe(philhealth.getText());
        double pagIbig = parseMoneySafe(pagibig.getText());
        double sssContribution = parseMoneySafe(sss.getText());

        double defaultTotalDeductions = absencesOrLates + lateUndertime + philHealth + pagIbig + sssContribution;

        // ✅ Restore Net Pay
        double defaultNetPay = defaultGrossPay - defaultTotalDeductions;

        // ✅ Update UI Fields
        grosspay.setText(formatMoney(defaultGrossPay));
        totaldeductions.setText(formatMoney(defaultTotalDeductions));
        netpay.setText(formatMoney(defaultNetPay));

    }//GEN-LAST:event_reset_newEarningDeductionsMouseClicked


    private void clearAllMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_clearAllMouseClicked

        // TODO add your handling code here:
        int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to clear all fields and reset the form?", "Clear All Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirmation == JOptionPane.YES_OPTION) {

            workdays.setEditable(false);
            overtimehours.setEditable(false);
            lateorundertime.setEditable(false);

            // Clear all fields
            idnumber.setText("");
            firstname.setText("");
            lastname.setText("");
            sex.setSelectedIndex(0);
            department.setSelectedIndex(0);
            jobposition.setSelectedIndex(0);
            empstatus.setSelectedIndex(0);
            month.setSelectedIndex(0);
            year.setSelectedIndex(0);
            bankname.setSelectedIndex(0);
            accountnumber.setText("");
            accountname.setText("");
            governmentID.setSelectedIndex(0);
            govidnumber.setText("");
            basicsalary.setText("");
            overtimepay.setText("");
            allowances.setText("");
            incentives.setText("");
            commissions.setText("");
            dailyrate.setText("");
            hourlyrate.setText("");
            perminuterate.setText("");
            absences.setText("");
            unpaidleave.setText("");
            others.setText("");
            totaldeductions.setText("");
            grosspay.setText("");
            netpay.setText("");
            workdays.setText("");
            workhours.setText("");
            overtimehours.setText("");
            lateorundertime.setText("");
            lateundertime.setText("");

            sss.setText("");
            philhealth.setText("");
            pagibig.setText("");

            name_leftpanel.setText("Name:");
            id_leftpanel.setText("Employee ID:");
            birthday_leftpanel.setText("Birth Date:");
            contact_leftpanel.setText("Contact:");
            email_leftpanel.setText("Email:");
            jobposition_leftpanel.setText("Job Position:");
            department_leftpanel.setText("Dept:");
            datehired_leftpanel.setText("Date Hired:");
            empstatus_leftpanel.setText("Employment Status:");
            payschedule_leftpanel.setText("Pay Schedule:");

            // Reset fields to default states
            firstname.setEditable(true);
            lastname.setEditable(true);
            sex.setEnabled(true);
            department.setEnabled(true);
            jobposition.setEnabled(true);
            empstatus.setEnabled(true);
            month.setEnabled(true);
            year.setEnabled(true);
            bankname.setEnabled(true);
            accountnumber.setEditable(true);
            accountname.setEditable(true);
            governmentID.setEnabled(true);
            govidnumber.setEditable(true);
            basicsalary.setEditable(false);
            overtimepay.setEditable(false);
            totaldeductions.setEditable(false);
            grosspay.setEditable(false);
            netpay.setEditable(false);
            dailyrate.setEditable(false);
            hourlyrate.setEditable(false);
            perminuterate.setEditable(false);

            userid_generate.setEnabled(true);

            apply_newEarningDeductions.setEnabled(false);
            reset_newEarningDeductions.setEnabled(false);
            clearAll.setEnabled(false);
            sendSalary_button.setEnabled(false);

        }
    }//GEN-LAST:event_clearAllMouseClicked


    private void sendSalary_buttonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sendSalary_buttonMouseClicked

    }

    public void addNotification(String dateTime, String message) {
        DefaultTableModel model = (DefaultTableModel) notifications_table.getModel();
        model.addRow(new Object[]{dateTime, message});

        // ✅ Fix: Ensure notifications persist across pages
        SharedData.addNotification(dateTime, message);
    }

// Helper method to clear all fields and reset to default
    private void clearAllFields() {
        idnumber.setText("");
        firstname.setText("");
        lastname.setText("");
        sex.setSelectedIndex(0);
        department.setSelectedIndex(0);
        jobposition.setSelectedIndex(0);
        empstatus.setSelectedIndex(0);
        month.setSelectedIndex(0);
        year.setSelectedIndex(0);
        bankname.setSelectedIndex(0);
        accountnumber.setText("");
        accountname.setText("");
        governmentID.setSelectedIndex(0);
        govidnumber.setText("");
        basicsalary.setText("");
        overtimepay.setText("");
        allowances.setText("");
        incentives.setText("");
        commissions.setText("");
        dailyrate.setText("");
        hourlyrate.setText("");
        perminuterate.setText("");
        absences.setText("");
        unpaidleave.setText("");
        others.setText("");
        totaldeductions.setText("");
        grosspay.setText("");
        netpay.setText("");
        workdays.setText("");
        workhours.setText("");
        overtimehours.setText("");
        lateorundertime.setText("");
        lateundertime.setText("");

        // Reset fields to default states
        firstname.setEditable(true);
        lastname.setEditable(true);
        sex.setEnabled(true);
        department.setEnabled(true);
        jobposition.setEnabled(true);
        empstatus.setEnabled(true);
        month.setEnabled(true);
        year.setEnabled(true);
        bankname.setEnabled(true);
        accountnumber.setEditable(true);
        accountname.setEditable(true);
        governmentID.setEnabled(true);
        govidnumber.setEditable(true);
        basicsalary.setEditable(false);
        overtimepay.setEditable(false);
        totaldeductions.setEditable(false);

        workdays.setText("");
        workhours.setText("");
        overtimehours.setText("");
        lateorundertime.setText("");

        apply_newEarningDeductions.setEnabled(false);
        reset_newEarningDeductions.setEnabled(false);
        clearAll.setEnabled(false);
        sendSalary_button.setEnabled(false);

        grosspay.setEditable(false);
        netpay.setEditable(false);
        dailyrate.setEditable(false);
        hourlyrate.setEditable(false);
        perminuterate.setEditable(false);
        overtimehours.setEditable(true);
        workdays.setEditable(true);
        workhours.setEditable(true);
        lateorundertime.setEditable(true);

        id_leftpanel.setText("Employee ID:");
        name_leftpanel.setText("Name:");
        birthday_leftpanel.setText("Birthday:");
        contact_leftpanel.setText("Contact:");
        email_leftpanel.setText("Email:");
        jobposition_leftpanel.setText("Job Position:");
        department_leftpanel.setText("Dept:");
        datehired_leftpanel.setText("Date Hired:");
        payschedule_leftpanel.setText("Pay Schedule:");
        empstatus_leftpanel.setText("Emp. Status");

        // Disable the Edit button

    }//GEN-LAST:event_sendSalary_buttonMouseClicked

    private void logout_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logout_btnActionPerformed
        int response = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to log out?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        // Check the user's response
        if (response == JOptionPane.YES_OPTION) {
            this.dispose(); // Close the current window
            new Login().setVisible(true); // Open the login window
        }
        // If NO_OPTION is selected, the dialog will simply close and no further action will be taken
    }//GEN-LAST:event_logout_btnActionPerformed

    private void totaldeductionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totaldeductionsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totaldeductionsActionPerformed

    private void reset_newEarningDeductionsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reset_newEarningDeductionsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_reset_newEarningDeductionsActionPerformed

    private void createemployeeaccountMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_createemployeeaccountMouseClicked
        // TODO add your handling code here:
        this.dispose(); // ✅ Close ADM_PayrollModule
        new ADM_AddEMP().setVisible(true); // ✅ Open ADM_AddEMP
    }//GEN-LAST:event_createemployeeaccountMouseClicked

    private void sendSalary_buttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendSalary_buttonActionPerformed
        // TODO add your handling code here:
        if (!sendSalary_button.isEnabled()) {
            return; // Do nothing if the button is disabled
        }

        int confirmation = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to send the salary?",
                "Send Salary Confirmation",
                JOptionPane.YES_NO_OPTION);

        if (confirmation == JOptionPane.YES_OPTION) {

            // Calculate payroll period
            java.time.LocalDate currentDate = java.time.LocalDate.now();
            java.time.LocalDate startDate = currentDate.minusDays(30);

// Format payroll period as "yyyy-MM-dd to yyyy-MM-dd"
            String payrollPeriodFormatted = startDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                    + " - " + currentDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd"));

// Prepare insert SQL for PayrollHistory
            String insertPayrollHistorySql = "INSERT INTO PayrollHistory (UserID, PayrollPeriod, Status) VALUES (?, ?, ?)";

            try (PreparedStatement pstHistory = conn.prepareStatement(insertPayrollHistorySql)) {
                pstHistory.setString(1, idnumber.getText());             // UserID
                pstHistory.setString(2, payrollPeriodFormatted);          // PayrollPeriod
                pstHistory.setString(3, "Success");                        // Status

                int rows = pstHistory.executeUpdate();
                if (rows > 0) {
                    System.out.println("PayrollHistory record inserted successfully.");
                } else {
                    System.err.println("PayrollHistory insert failed.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error inserting PayrollHistory: " + e.getMessage());
            }

            String updateIncomeSql = "INSERT INTO EmployeeIncome (UserID, TotalIncome) VALUES (?, ?) "
                    + "ON CONFLICT(UserID) DO UPDATE SET TotalIncome = TotalIncome + EXCLUDED.TotalIncome";

            PreparedStatement pstIncome = null;
            try {
                pstIncome = conn.prepareStatement(updateIncomeSql);

                // Parse the value from text field (remove commas)
                double netPayAmount = Double.parseDouble(netpay.getText().replace(",", ""));

                // Set parameters
                pstIncome.setString(1, idnumber.getText());
                pstIncome.setDouble(2, netPayAmount); // ✅ Store as number, no comma formatting

                pstIncome.executeUpdate();
                System.out.println("EmployeeIncome updated successfully.");

                // Optional: Show a message with formatted income
                DecimalFormat df = new DecimalFormat("#,##0.00");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error updating EmployeeIncome: " + ex.getMessage());
            } finally {
                try {
                    if (pstIncome != null) {
                        pstIncome.close();
                    }
                } catch (SQLException e) {
                    System.err.println("Failed to close pstIncome: " + e.getMessage());
                }
            }
        }

        if (confirmation == JOptionPane.YES_OPTION) {

            LocalDate currentDate = LocalDate.now();
            LocalDate startDate = currentDate.minusDays(30);

            // Formatters
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // payPeriod (date range only) for PeriodCombobox
            String payPeriod = startDate.format(dateFormatter) + " to " + currentDate.format(dateFormatter);

            // payPeriodWithTime for PayPeriod (date range with current time at the end)
            LocalDateTime currentDateTime = LocalDateTime.now();
            String payPeriodWithTime = startDate.format(dateFormatter) + " to " + currentDateTime.format(dateTimeFormatter);

            // PaymentDate as yyyy-MM-dd string
            String paymentDate = currentDate.format(dateFormatter);

            String insertPaySlipSql = "INSERT INTO PaySlip ("
                    + "PeriodCombobox, Name, UserID, JobPos, Dept, PayPeriod, WorkDays, BasicSalary, "
                    + "OvertimePay, Allowances, Incentives, Commissions, Absences, LatesUndertime, "
                    + "UnpaidLeave, Others, SSS, Philhealth, PagIBIG, TotalEarnings, TotalDeduc, "
                    + "PaymentDate, BankName, BankAccName, BankNum, NetPay, PayMethod) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstPaySlip = conn.prepareStatement(insertPaySlipSql)) {

                pstPaySlip.setString(1, payPeriodWithTime); // PeriodCombobox (date only)
                pstPaySlip.setString(2, firstname.getText().trim() + " " + lastname.getText().trim()); // Name
                pstPaySlip.setString(3, idnumber.getText().trim()); // UserID
                pstPaySlip.setString(4, jobposition.getSelectedItem().toString().trim()); // JobPos
                pstPaySlip.setString(5, department.getSelectedItem().toString().trim()); // Dept
                pstPaySlip.setString(6, payPeriod); // PayPeriod (with time)

                pstPaySlip.setInt(7, parseOrDefaultInt(workdays.getText())); // WorkDays
                pstPaySlip.setDouble(8, parseOrDefaultDouble(basicsalary.getText())); // BasicSalary
                pstPaySlip.setDouble(9, parseOrDefaultDouble(overtimepay.getText())); // OvertimePay
                pstPaySlip.setDouble(10, parseOrDefaultDouble(allowances.getText())); // Allowances
                pstPaySlip.setDouble(11, parseOrDefaultDouble(incentives.getText())); // Incentives
                pstPaySlip.setDouble(12, parseOrDefaultDouble(commissions.getText())); // Commissions
                pstPaySlip.setDouble(13, parseOrDefaultDouble(absences.getText())); // Absences
                pstPaySlip.setDouble(14, parseOrDefaultDouble(lateundertime.getText())); // LatesUndertime
                pstPaySlip.setDouble(15, parseOrDefaultDouble(unpaidleave.getText())); // UnpaidLeave
                pstPaySlip.setDouble(16, parseOrDefaultDouble(others.getText())); // Others

                pstPaySlip.setDouble(17, parseOrDefaultDouble(sss.getText())); // SSS
                pstPaySlip.setDouble(18, parseOrDefaultDouble(philhealth.getText())); // Philhealth
                pstPaySlip.setDouble(19, parseOrDefaultDouble(pagibig.getText())); // PagIBIG

                pstPaySlip.setDouble(20, parseOrDefaultDouble(grosspay.getText())); // TotalEarnings
                pstPaySlip.setDouble(21, parseOrDefaultDouble(totaldeductions.getText())); // TotalDeduc

                pstPaySlip.setString(22, paymentDate); // PaymentDate

                pstPaySlip.setString(23, bankname.getSelectedItem().toString().trim()); // BankName
                pstPaySlip.setString(24, accountname.getText().trim()); // BankAccName
                pstPaySlip.setString(25, accountnumber.getText().trim()); // BankNum

                pstPaySlip.setDouble(26, parseOrDefaultDouble(netpay.getText())); // NetPay
                pstPaySlip.setString(27, "Bank Transfer"); // PayMethod

                int rowsInserted = pstPaySlip.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("PaySlip inserted successfully.");
                } else {
                    System.err.println("PaySlip insert failed.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Error inserting into PaySlip: " + ex.getMessage());
            }

            // Insert into ADM_SalarySummary
            String insertSummarySQL = "INSERT INTO ADM_SalarySummary (Date, UserID, FullName, JobPos, EmpStatus, AccNum, GrossPay, TotalDeduc, NetPay) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstSummary = conn.prepareStatement(insertSummarySQL)) {
                String nowDateTime = java.time.LocalDateTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                String fullName = firstname.getText().trim() + " " + lastname.getText().trim();

                pstSummary.setString(1, nowDateTime); // Date
                pstSummary.setString(2, idnumber.getText().trim()); // UserID
                pstSummary.setString(3, fullName); // FullName
                pstSummary.setString(4, jobposition.getSelectedItem().toString().trim()); // JobPos
                pstSummary.setString(5, empstatus.getSelectedItem().toString().trim()); // EmpStatus
                pstSummary.setString(6, accountnumber.getText().trim()); // AccNum

                double gross = Double.parseDouble(grosspay.getText().replace(",", ""));
                double totalDeduction = Double.parseDouble(totaldeductions.getText().replace(",", ""));
                double net = Double.parseDouble(netpay.getText().replace(",", ""));

                pstSummary.setDouble(7, gross); // GrossPay
                pstSummary.setDouble(8, totalDeduction); // TotalDeduc
                pstSummary.setDouble(9, net); // NetPay

                int rows = pstSummary.executeUpdate();
                if (rows > 0) {
                    System.out.println("ADM_SalarySummary record inserted successfully.");
                } else {
                    System.out.println("ADM_SalarySummary insert failed.");
                }
            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error inserting ADM_SalarySummary record: " + e.getMessage());
            }

            String currentDateTimee = java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm:ss"));

            String sqlInsert = "INSERT INTO SentSalary (UserID, BasicSalary, OvertimePay, Allowances, Incentives, Commissions, "
                    + "Absences, LatesorUndertime, UnpaidLeave, Others, SSS, PhilHealth, \"Pag-IBIG\", TotalDeduc, GrossPay, "
                    + "PaymentDate, NetPay, WorkDays, TimeStamp) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, datetime('now', 'localtime'))";

            PreparedStatement pstInsert = null;

            try {
                pstInsert = conn.prepareStatement(sqlInsert);

                pstInsert.setString(1, idnumber.getText());
                pstInsert.setString(2, basicsalary.getText());
                pstInsert.setString(3, overtimepay.getText());

                String allowancesValue = allowances.getText().trim();
                if (allowancesValue.isEmpty()) {
                    allowancesValue = "0.00";
                }

                String incentivesValue = incentives.getText().trim();
                if (incentivesValue.isEmpty()) {
                    incentivesValue = "0.00";
                }

                String absencesValue = absences.getText().trim();
                if (absencesValue.isEmpty()) {
                    absencesValue = "0.00";
                }

                String commissionsValue = commissions.getText().trim();
                if (commissionsValue.isEmpty()) {
                    commissionsValue = "0.00";
                }

                String unpaidleaveValue = unpaidleave.getText().trim();
                if (unpaidleaveValue.isEmpty()) {
                    unpaidleaveValue = "0.00";
                }

                String othersValue = others.getText().trim();
                if (othersValue.isEmpty()) {
                    othersValue = "0.00";
                }

                pstInsert.setDouble(4, Double.parseDouble(allowancesValue));
                pstInsert.setDouble(5, Double.parseDouble(incentivesValue));
                pstInsert.setDouble(6, Double.parseDouble(commissionsValue));
                pstInsert.setDouble(7, Double.parseDouble(absencesValue));
                pstInsert.setDouble(8, lateundertime.getText().trim().isEmpty() ? 0.00 : Double.parseDouble(lateundertime.getText().trim()));
                pstInsert.setDouble(9, Double.parseDouble(unpaidleaveValue));
                pstInsert.setDouble(10, Double.parseDouble(othersValue));

                pstInsert.setString(11, sss.getText());
                pstInsert.setString(12, philhealth.getText());
                pstInsert.setString(13, pagibig.getText());

                double deductions = Double.parseDouble(totaldeductions.getText().replace(",", ""));
                pstInsert.setString(14, String.format("%.2f", deductions));

                double gp = Double.parseDouble(grosspay.getText().replace(",", ""));
                pstInsert.setString(15, String.format("%.2f", gp));

                pstInsert.setString(16, currentDateTimee);

                double np = Double.parseDouble(netpay.getText().replace(",", ""));
                pstInsert.setString(17, String.format("%.2f", np));

                pstInsert.setString(18, workdays.getText());

                int rowsAffected = pstInsert.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Salary record inserted successfully.");
                } else {
                    System.out.println("Insert failed, no rows affected.");
                }

                System.out.println("GrossPay input: " + grosspay.getText());
                System.out.println("Total Deductions input: " + totaldeductions.getText());
                System.out.println("NetPay input: " + netpay.getText());

                populateSalarySummaryTable();

            } catch (SQLException | NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error inserting salary record: " + e.getMessage());

            } finally {
                try {
                    if (pstInsert != null) {
                        pstInsert.close();
                    }

                } catch (SQLException e) {
                    System.err.println("Failed to close resources: " + e.getMessage());
                }

                java.time.LocalDate payrollStartDate = java.time.LocalDate.now().minusDays(30);
                String payrollPeriod = String.format("%s - %s",
                        payrollStartDate.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                        currentDate
                );

                // ✅ Get the last date in the payroll period for proper payment date
                String paymentTime = java.time.LocalTime.now()
                        .format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));

                // ✅ Clear Fields After Successful Processing
                clearAllFields();

                JOptionPane.showMessageDialog(this, "Salary has been sent successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                userid_generate.setEnabled(true);

                // ✅ Add Payroll Period to Combo Box & List
                if (!payrollPeriods.contains(payrollPeriod)) { // Prevent duplicates
                    payrollPeriods.add(payrollPeriod); // ✅ Store period in the list
                    payperiod_combobox.addItem(payrollPeriod); // ✅ Add to combo box
                }
            }
        }

    }//GEN-LAST:event_sendSalary_buttonActionPerformed

    private void viewlistofemployeesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_viewlistofemployeesMouseClicked
        new ADM_ListEmp().setVisible(true); // ✅ Open ADM_AddEMP
        this.dispose(); // ✅ Close ADM_PayrollModule

    }//GEN-LAST:event_viewlistofemployeesMouseClicked

    private void createemployeeaccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_createemployeeaccountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_createemployeeaccountActionPerformed

    private void paymenthistoryMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_paymenthistoryMouseClicked
        new ADM_PaymentHistory().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_paymenthistoryMouseClicked
// Helper methods to safely parse numbers

    private int parseOrDefaultInt(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(text.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseOrDefaultDouble(String text) {
        if (text == null || text.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(text.replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(() -> new ADM_PayrollModule().setVisible(true));

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTable Payroll_Database;
    private javax.swing.JTextField absences;
    private javax.swing.JLabel absencesTXT_LABEL;
    private javax.swing.JTextField accountname;
    private javax.swing.JLabel accountnameTXT_LABEL;
    private javax.swing.JTextField accountnumber;
    private javax.swing.JLabel accountnumberTXT_LABEL;
    private javax.swing.JTextField allowances;
    private javax.swing.JLabel allowancesTXT_LABEL;
    private javax.swing.JButton apply_newEarningDeductions;
    private javax.swing.JLabel attendance;
    private javax.swing.JLabel bankDetail_LABEL;
    private javax.swing.JComboBox<String> bankname;
    private javax.swing.JLabel banknameTXT_LABEL;
    private javax.swing.JTextField basicsalary;
    private javax.swing.JLabel basicsalaryTXT_LABEL;
    private javax.swing.JLabel birthday_leftpanel;
    private javax.swing.JButton clearAll;
    private javax.swing.JTextField commissions;
    private javax.swing.JLabel commissionsTXT_LABEL;
    private javax.swing.JLabel contact_leftpanel;
    private javax.swing.JButton createemployeeaccount;
    private javax.swing.JLabel currentdate;
    private javax.swing.JLabel currenttime;
    private javax.swing.JTextField dailyrate;
    private javax.swing.JLabel dailyrateTXT_LABEL;
    private javax.swing.JLabel datehired_leftpanel;
    private javax.swing.JLabel deductionsLABEL;
    private javax.swing.JComboBox<String> department;
    private javax.swing.JLabel department_leftpanel;
    private javax.swing.JLabel deptTXT_LABEL;
    private javax.swing.JLabel email_leftpanel;
    private javax.swing.JComboBox<String> empstatus;
    private javax.swing.JLabel empstatusTXT_LABEL;
    private javax.swing.JLabel empstatus_leftpanel;
    private javax.swing.JTextField firstname;
    private javax.swing.JLabel firstnameTXT_LABEL;
    private javax.swing.JLabel govIDTXT_LABEL;
    private javax.swing.JComboBox<String> governmentID;
    private javax.swing.JTextField govidnumber;
    private javax.swing.JLabel govidnumberTXT_LABEL;
    private javax.swing.JTextField grosspay;
    private javax.swing.JLabel grosspayTXT_LABEL;
    private javax.swing.JTextField hourlyrate;
    private javax.swing.JLabel hourlyrateTXT_LABEL;
    private javax.swing.JLabel id_leftpanel;
    private javax.swing.JTextField idnumber;
    private javax.swing.JTextField incentives;
    private javax.swing.JLabel incentivesTXT_LABEL;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JComboBox<String> jobposition;
    private javax.swing.JLabel jobpositionTXT_LABEL;
    private javax.swing.JLabel jobposition_leftpanel;
    private javax.swing.JTextField lastname;
    private javax.swing.JLabel lastnameTXT_LABEL;
    private javax.swing.JTextField lateorundertime;
    private javax.swing.JLabel latesundertimeTXT_LABEL;
    private javax.swing.JTextField lateundertime;
    private javax.swing.JLabel lateundertimeTXT_LABEL;
    private javax.swing.JPanel left_panel;
    private javax.swing.JButton logout_btn;
    private javax.swing.JComboBox<String> month;
    private javax.swing.JLabel monthTXT_LABEL;
    private javax.swing.JLabel name_leftpanel;
    private javax.swing.JTextField netpay;
    private javax.swing.JLabel netpayTXT_LABEL;
    private javax.swing.JTextField others;
    private javax.swing.JLabel othersTXT_LABEL;
    private javax.swing.JLabel overtimeTXT_LABEL;
    private javax.swing.JTextField overtimehours;
    private javax.swing.JLabel overtimehoursTXT_LABEL;
    private javax.swing.JTextField overtimepay;
    private javax.swing.JTextField pagibig;
    private javax.swing.JLabel pagibigTXT_LABEL;
    private javax.swing.JButton paymenthistory;
    private javax.swing.JLabel payschedule_leftpanel;
    private javax.swing.JTextField perminuterate;
    private javax.swing.JLabel perminuterateTXT_LABEL;
    private javax.swing.JPanel personalData_LABEL;
    private javax.swing.JTextField philhealth;
    private javax.swing.JLabel philhealthTXT_LABEL;
    private javax.swing.JButton reset_newEarningDeductions;
    private javax.swing.JButton sendSalary_button;
    private javax.swing.JComboBox<String> sex;
    private javax.swing.JLabel sexTXT_LABEL;
    private javax.swing.JTextField sss;
    private javax.swing.JLabel sssTXT_LABEL;
    private javax.swing.JTextField totaldeductions;
    private javax.swing.JLabel totaldeductionsTXT_LABEL;
    private javax.swing.JTextField unpaidleave;
    private javax.swing.JLabel unpaudleaveTXT_LABEL;
    private javax.swing.JLabel useridTXT_LABEL;
    private javax.swing.JButton userid_generate;
    private javax.swing.JButton viewlistofemployees;
    private javax.swing.JTextField workdays;
    private javax.swing.JLabel workdaysTXT_LABEL;
    private javax.swing.JTextField workhours;
    private javax.swing.JLabel workhoursTXT_LABEL;
    private javax.swing.JComboBox<String> year;
    private javax.swing.JLabel yearTXT_LABEL;
    // End of variables declaration//GEN-END:variables
}
