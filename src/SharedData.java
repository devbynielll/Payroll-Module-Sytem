
import java.io.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author danie
 */
public class SharedData {

    PreparedStatement pst1;
    PreparedStatement pst2;
    PreparedStatement pst3;
    PreparedStatement pst4;
    ResultSet rs;
    Connection conn;

    private static double totalIncome = 0.0; // Total income starts at 0
    private static double currentBalance = 0.0; // Available balance starts at 

    // Shared table model for Transaction_Database
    public static DefaultTableModel transactionTableModel = new DefaultTableModel(
            new Object[][]{}, // Initial empty data
            new String[]{"Date", "Reference ID", "Recipient Name", "Recipient Number", "Account Number", "Amount", "Status"}
    );

    private static DefaultTableModel notificationTableModel = new DefaultTableModel(
            new Object[]{"Date/Time", "Subject"}, 0
    );

    // Getters and setters for total income
    public static double getTotalIncome() {
        return totalIncome;
    }

    public static void setTotalIncome(double amount) {
        totalIncome = amount;
    }

    // Getters and setters for current balance
    public static double getCurrentBalance() {
        return currentBalance;
    }

    public static void addToTotalIncome(double amount) {
        totalIncome += amount; // Add to total income
    }

    public static void setCurrentBalance(double currentBalance) {
        SharedData.currentBalance = currentBalance;
    }

    public static synchronized void addToCurrentBalance(double amount) {
        currentBalance += amount;
    }

    public static void addTransaction(String date, String referenceId, String recipientName, String recipientNumber, String accountNumber, String amount, String status) {
        transactionTableModel.addRow(new Object[]{date, referenceId, recipientName, recipientNumber, accountNumber, amount, status});
    }

    public static void clearTransactions() {
        transactionTableModel.setRowCount(0); // Clears all rows
    }

    public static String formatCurrency(double amount) {
        return String.format("₱%,.2f", amount);
    }

    public static DefaultTableModel getNotificationTableModel() {
        return notificationTableModel;
    }

    public static void addNotification(String dateTime, String message) {
        // ✅ Prevent duplicate notifications by checking before adding
        if (!isNotificationStored(dateTime, message)) {
            notificationTableModel.addRow(new Object[]{dateTime, message});
        } else {
            System.out.println("Duplicate detected in SharedData! Skipping...");
        }
    }

    public static boolean isNotificationStored(String dateTime, String message) {
        DefaultTableModel model = notificationTableModel;

        for (int i = 0; i < model.getRowCount(); i++) {
            String storedDateTime = model.getValueAt(i, 0).toString();
            String storedMessage = model.getValueAt(i, 1).toString();
            if (storedDateTime.equals(dateTime) && storedMessage.equals(message)) {
                return true; // Notification already exists
            }
        }
        return false; // New notification
    }

    public static void updateTotalIncome(Connection conn, String userID, double updatedTotalIncome) {
        try {
            String sql = "UPDATE EmployeeIncome SET TotalIncome = ? WHERE UserID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, updatedTotalIncome);
            pst.setString(2, userID);
            pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void saveTotalIncome(Connection conn, String userID, double totalIncome) {
        try {
            String sql = "UPDATE SentSalarySummary SET TotalIncome = ? WHERE UserID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setDouble(1, totalIncome);
            pst.setString(2, userID);
            pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double loadTotalIncome(Connection conn, String userID) {
        double income = 0;
        try {
            String sql = "SELECT TotalIncome FROM SentSalarySummary WHERE UserID = ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, userID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                income = rs.getDouble("TotalIncome");
            }
            rs.close();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return income;
    }

    // Reset all shared data (called on application restart)
}
