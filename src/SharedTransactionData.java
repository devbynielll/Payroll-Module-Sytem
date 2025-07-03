/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.table.DefaultTableModel;

public class SharedTransactionData {
    // Shared transaction data model
    public static DefaultTableModel transactionTableModel = new DefaultTableModel(
        new Object[][]{}, // Initial empty data
        new String[]{"Date", "Reference ID", "Recipient Name", "Recipient Number", "Account Number", "Amount", "Status"}
    );
}