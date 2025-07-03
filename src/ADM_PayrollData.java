/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author danie
 */
public class ADM_PayrollData {
        public static DefaultTableModel payrollTableModel = new DefaultTableModel(
        new Object[][]{}, // Initial empty data
        new String[]{"Date", "ID Number", "Full Name", "Job Position", "Employee Status", "Account Number", "Gross Pay", "Total Deductions", "Net Pay"}
    );
        
            private ADM_PayrollData() {
        // Private constructor to prevent instantiation
    }
}
