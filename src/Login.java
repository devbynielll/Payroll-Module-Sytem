/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

import javax.swing.JOptionPane;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author danie
 */
public class Login extends javax.swing.JFrame {

    Connection conn;
    PreparedStatement pst;
    ResultSet rs;

    public static HashMap<String, String> userPasswords = new HashMap<>();

    // Constructor
    public Login() {
        initComponents();

        this.conn = javaconnect.ConnectDb();

        setLocationRelativeTo(null); // Center the JFrame on the screen

        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state

        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        setTitle("Payroll Swift"); // ✅ Custom window title

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LeftBox = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPasswordField1 = new javax.swing.JPasswordField();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        showpassword = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        LeftBox.setBackground(new java.awt.Color(255, 255, 255));
        LeftBox.setMinimumSize(new java.awt.Dimension(400, 500));

        jLabel1.setBackground(new java.awt.Color(0, 153, 153));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 102, 102));
        jLabel1.setText("Login");

        jPasswordField1.setBackground(new java.awt.Color(255, 255, 255));
        jPasswordField1.setToolTipText("");
        jPasswordField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPasswordField1ActionPerformed(evt);
            }
        });

        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setToolTipText("");
        jTextField1.setActionCommand("<Not Set>");
        jTextField1.setName(""); // NOI18N
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/user (1).png"))); // NOI18N
        jLabel2.setText("User ID");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/lock (1).png"))); // NOI18N
        jLabel3.setText("Password");

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Login");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 13)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Forgot Password");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AYROLL WIFT (2) (1).png"))); // NOI18N
        jLabel6.setToolTipText("");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Smart & Secure Payroll Management");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(93, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addGap(120, 120, 120)
                .addComponent(jLabel5)
                .addGap(16, 16, 16))
        );

        showpassword.setFont(new java.awt.Font("Segoe UI Semibold", 1, 13)); // NOI18N
        showpassword.setForeground(new java.awt.Color(0, 0, 0));
        showpassword.setText("Show Password");
        showpassword.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                showpasswordMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout LeftBoxLayout = new javax.swing.GroupLayout(LeftBox);
        LeftBox.setLayout(LeftBoxLayout);
        LeftBoxLayout.setHorizontalGroup(
            LeftBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LeftBoxLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 116, Short.MAX_VALUE)
                .addGroup(LeftBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LeftBoxLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(131, 131, 131))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, LeftBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3)
                        .addComponent(jLabel2)
                        .addGroup(LeftBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPasswordField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(LeftBoxLayout.createSequentialGroup()
                                .addComponent(showpassword)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel4)))
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(114, 114, 114))
        );
        LeftBoxLayout.setVerticalGroup(
            LeftBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(LeftBoxLayout.createSequentialGroup()
                .addGap(90, 90, 90)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(LeftBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(showpassword)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(113, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LeftBox, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(LeftBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        this.dispose();
        new EnterID_ForgetPass().setVisible(true);
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String userId = jTextField1.getText().trim();
        String password = new String(jPasswordField1.getPassword()).trim();

        if (userId.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both User ID and Password", "Missing Fields", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            // Check Admin
            String sqlAdmin = "SELECT * FROM Admin WHERE UserID=? AND Password=?";
            pst = conn.prepareStatement(sqlAdmin);
            pst.setString(1, userId);
            pst.setString(2, password);
            rs = pst.executeQuery();

            if (rs.next()) {
                // Admin login success
                rs.close();
                pst.close();

                // Open Admin dashboard
                EMP_Dashboard empDashboard = new EMP_Dashboard(null); // Adjust constructor if needed
                ADM_PayrollModule admPayrollModule = new ADM_PayrollModule(empDashboard);
                admPayrollModule.setVisible(true);
                JOptionPane.showMessageDialog(this, "Welcome, Administrator!");
                this.dispose();
                return;
            }
            rs.close();
            pst.close();

            // Check Employee in EmpAcc or PersonalData
            String[] employeeTables = {"EmpAcc", "PersonalData"};
            String passwordColumn = ""; // will set per table
            boolean employeeFound = false;
            String loggedInUserID = null;

            for (String table : employeeTables) {
                passwordColumn = table.equals("PersonalData") ? "NewPass" : "Password";
                String sqlEmp = "SELECT * FROM " + table + " WHERE UserID=? AND " + passwordColumn + "=?";
                pst = conn.prepareStatement(sqlEmp);
                pst.setString(1, userId);
                pst.setString(2, password);
                rs = pst.executeQuery();

                if (rs.next()) {
                    loggedInUserID = rs.getString("UserID");
                    employeeFound = true;
                    break; // exit loop once found
                }

                rs.close();
                pst.close();
            }

            if (employeeFound) {
                // Insert login record BEFORE opening dashboard
                String insertLoginSQL = "INSERT INTO Login (UserID) VALUES (?)";
                try (PreparedStatement insertPst = conn.prepareStatement(insertLoginSQL)) {
                    insertPst.setString(1, loggedInUserID);
                    insertPst.executeUpdate();
                } catch (SQLException e) {
                    System.err.println("Failed to insert login record: " + e.getMessage());
                }

                String updateTimestampSQL = "UPDATE EmployeeIncome SET TimeStamp = CURRENT_TIMESTAMP WHERE UserID = ?";
                try (PreparedStatement updatePst = conn.prepareStatement(updateTimestampSQL)) {
                    updatePst.setString(1, loggedInUserID);
                    int rowsUpdated = updatePst.executeUpdate();
                    if (rowsUpdated == 0) {
                        // Optional: If no row exists, insert a new one with default income 0 and current timestamp
                        String insertIncomeSQL = "INSERT INTO EmployeeIncome (UserID, TotalIncome, TimeStamp) VALUES (?, 0.00, CURRENT_TIMESTAMP)";
                        try (PreparedStatement insertIncomePst = conn.prepareStatement(insertIncomeSQL)) {
                            insertIncomePst.setString(1, loggedInUserID);
                            insertIncomePst.executeUpdate();
                        }
                    }
                } catch (SQLException e) {
                    System.err.println("Failed to update EmployeeIncome timestamp: " + e.getMessage());
                }

                // Now open dashboard with correct userID and pass Bank_Transfer reference properly
                // Assuming EMP_Dashboard constructor requires Bank_Transfer and UserID or just UserID
                EMP_Dashboard empDashboard = new EMP_Dashboard(null); // <-- Replace 'null' with proper arg if needed
                empDashboard.setCurrentUserID(loggedInUserID);
                Bank_Transfer bankTransfer = new Bank_Transfer(empDashboard);
                empDashboard.setBankTransferPage(bankTransfer);
                empDashboard.setVisible(true);

                JOptionPane.showMessageDialog(this, "Welcome, Employee!");
                this.dispose();
                return;
            }

            // If no user found
            JOptionPane.showMessageDialog(this, "Invalid User ID or Password", "Login Failed", JOptionPane.ERROR_MESSAGE);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            try {
                if (rs != null && !rs.isClosed()) {
                    rs.close();
                }
                if (pst != null && !pst.isClosed()) {
                    pst.close();
                }
            } catch (SQLException e) {
                System.err.println("Failed to close resources: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed

    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jPasswordField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPasswordField1ActionPerformed

    }//GEN-LAST:event_jPasswordField1ActionPerformed


    private void showpasswordMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_showpasswordMouseClicked
        // TODO add your handling code here:
        if (showpassword.isSelected()) {
            jPasswordField1.setEchoChar((char) 0); // Show the password
        } else {
            jPasswordField1.setEchoChar('*'); // Hide the password
        }
    }//GEN-LAST:event_showpasswordMouseClicked

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel LeftBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JCheckBox showpassword;
    // End of variables declaration//GEN-END:variables
}
