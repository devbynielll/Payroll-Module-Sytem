/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.JOptionPane;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 *
 * @author danie
 */

public class ForgotPassword extends javax.swing.JFrame {

    
    private String currentUserId; // Store the ID number passed from the previous page
    private String phoneNumber; // Store the phone number passed from the previous page
    private Timer timer; // Timer for the countdown
    private int timeRemaining = 60; // 60 seconds countdown
    private final String verificationCode = "123456"; // Hardcoded verification code

    /**
     * Constructor accepts user ID and phone number
     */
    public ForgotPassword(String idNumber, String phoneNumber) {
        this.currentUserId = idNumber; // Save the user ID
        this.phoneNumber = phoneNumber; // Save the phone number
        initComponents();
        initializeTimer();
        sendVerificationCode(); // Simulate sending the verification code
        setLocationRelativeTo(null); // Center the JFrame on the screen
        setResizable(false); // ✅ Prevents window resizing
        setExtendedState(JFrame.NORMAL); // ✅ Ensures window stays at default state
        
        ImageIcon logo = new ImageIcon("C:\\Users\\danie\\Downloads\\PS_FinalLogo.png"); // ✅ Update with correct file location
        setIconImage(logo.getImage());

        setTitle("Payroll Swift"); // ✅ Custom window title
    }

    // Simulate sending the verification code
    private void sendVerificationCode() {
        System.out.println("Verification code sent to phone (" + phoneNumber + "): " + verificationCode);
        // In a real-world scenario, integrate SMS or email API here
    }

    private void initializeTimer() {
        // Create a new Timer that triggers every 1 second
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeRemaining > 0) {
                    timeRemaining--;
                    timerLabel.setText(timeRemaining + "s");
                } else {
                    timer.stop(); // Stop the timer when it reaches 0
                    resendButton.setEnabled(true); // Enable the Resend button
                    timerLabel.setText("You can now resend the code.");
                }
            }
        });

        // Start the timer initially
        timer.start();
    }
           
    public ForgotPassword() {
        initComponents();
        initializeTimer();
        setLocationRelativeTo(null); // Centers the JFrame on the screen
    }
    
    // This class restricts input to numeric values and limits the input length
    public class NumericDocumentFilter extends PlainDocument {
        private final int maxLength;

        // Constructor to set the maximum allowed length
        public NumericDocumentFilter(int maxLength) {
            this.maxLength = maxLength;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) {
                return;
            }

            // Check if the input is numeric
            if (str.matches("\\d+")) { // Matches only digits
                // Check if the current text + new input exceeds the max length
                if ((getLength() + str.length()) <= maxLength) {
                    super.insertString(offset, str, attr);
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        RightBox = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        ForgotPassTxt = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        timerLabel = new javax.swing.JLabel();
        resendButton = new javax.swing.JLabel();
        Return = new javax.swing.JLabel();

        RightBox.setBackground(new java.awt.Color(0, 102, 102));
        RightBox.setForeground(new java.awt.Color(255, 255, 255));
        RightBox.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        RightBox.setPreferredSize(new java.awt.Dimension(400, 500));

        javax.swing.GroupLayout RightBoxLayout = new javax.swing.GroupLayout(RightBox);
        RightBox.setLayout(RightBoxLayout);
        RightBoxLayout.setHorizontalGroup(
            RightBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 361, Short.MAX_VALUE)
        );
        RightBoxLayout.setVerticalGroup(
            RightBoxLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setForeground(new java.awt.Color(255, 255, 255));

        jPanel2.setBackground(new java.awt.Color(0, 102, 102));
        jPanel2.setMinimumSize(new java.awt.Dimension(400, 500));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/AYROLL WIFT (2) (1).png"))); // NOI18N
        jLabel6.setToolTipText("");

        jLabel5.setFont(new java.awt.Font("Segoe UI Semibold", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Smart & Secure Payroll Management");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(101, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel6))
                .addGap(83, 83, 83))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addContainerGap())
        );

        ForgotPassTxt.setBackground(new java.awt.Color(0, 153, 153));
        ForgotPassTxt.setFont(new java.awt.Font("Segoe UI", 1, 27)); // NOI18N
        ForgotPassTxt.setForeground(new java.awt.Color(0, 102, 102));
        ForgotPassTxt.setText("Forgot your Password?");

        jLabel1.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("We're sending a 6-number verification code to your phone.");

        jLabel3.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Enter it below to reset your password.");

        jTextField1.setDocument(new NumericDocumentFilter(6)); // Set max length to 6 digits
        jTextField1.setBackground(new java.awt.Color(255, 255, 255));
        jTextField1.setForeground(new java.awt.Color(0, 0, 0));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jTextField1.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent evt) {
                char c = evt.getKeyChar();
                // Allow only digits and control characters like backspace
                if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE && c != KeyEvent.VK_DELETE) {
                    evt.consume(); // Ignore the event (block the input)
                }
            }
        });

        jLabel4.setBackground(new java.awt.Color(0, 0, 0));
        jLabel4.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("Verification Code");

        jButton1.setBackground(new java.awt.Color(0, 102, 102));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        timerLabel.setBackground(new java.awt.Color(0, 0, 0));
        timerLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        timerLabel.setForeground(new java.awt.Color(0, 0, 0));
        timerLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timerLabel.setText("1:00");

        resendButton.setBackground(new java.awt.Color(0, 0, 0));
        resendButton.setForeground(new java.awt.Color(0, 0, 0));
        resendButton.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        resendButton.setText("Resend");
        resendButton.setForeground(new java.awt.Color(0, 102, 255)); // Set a link-like color
        resendButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR)); // Set hand cursor
        resendButton.setEnabled(false);

        resendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (resendButton.isEnabled()) { // Check if enabled
                    timeRemaining = 60; // Reset the timer
                    resendButton.setEnabled(false); // Disable the resend button
                    timer.start(); // Restart the timer
                }
            }
        });
        resendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                resendButtonMouseClicked(evt);
            }
        });

        Return.setFont(new java.awt.Font("Segoe UI Semibold", 0, 14)); // NOI18N
        Return.setForeground(new java.awt.Color(0, 102, 102));
        Return.setText("Return to Login");
        Return.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        Return.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReturnMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(118, 118, 118)
                        .addComponent(ForgotPassTxt))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel4)
                                .addComponent(timerLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(resendButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(214, 214, 214)
                        .addComponent(Return))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(140, 140, 140)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 383, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 82, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(ForgotPassTxt)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(1, 1, 1)
                .addComponent(jLabel3)
                .addGap(27, 27, 27)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(timerLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resendButton)
                .addGap(26, 26, 26)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(Return)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     String enteredCode = jTextField1.getText().trim();

    // Check if the input is empty
    if (enteredCode.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter the verification code.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check if the code matches the hardcoded value
    if (enteredCode.equals("123456")) {
        JOptionPane.showMessageDialog(this, "Verification successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        this.dispose();
        new ResetingPassword(currentUserId).setVisible(true); // Pass the user ID to ResetingPassword
    } else {
        JOptionPane.showMessageDialog(this, "Invalid verification code. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    }//GEN-LAST:event_jButton1ActionPerformed

    
    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void ReturnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReturnMouseClicked
        this.dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_ReturnMouseClicked

    private void resendButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_resendButtonMouseClicked
        // TODO add your handling code here:
            if (resendButton.isEnabled()) { // Check if enabled
            timeRemaining = 60; // Reset the timer
            resendButton.setEnabled(false); // Disable the resend button
            timer.start(); // Restart the timer
            sendVerificationCode(); // Resend verification code
        }
    }//GEN-LAST:event_resendButtonMouseClicked

    
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
            java.util.logging.Logger.getLogger(ForgotPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ForgotPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ForgotPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ForgotPassword.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ForgotPassword().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ForgotPassTxt;
    private javax.swing.JLabel Return;
    private javax.swing.JPanel RightBox;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JLabel resendButton;
    private javax.swing.JLabel timerLabel;
    // End of variables declaration//GEN-END:variables
}
