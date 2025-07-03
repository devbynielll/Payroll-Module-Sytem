package payrollsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import payrollsystem.PayrollRecord;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class PayrollData {
    // ✅ Declare the payrollRecords list to store payroll history
    private static ArrayList<PayrollRecord> payrollRecords = new ArrayList<>();

    // ✅ Shared table model for payroll history
    public static DefaultTableModel payrollTableModel = new DefaultTableModel(
        new Object[][] {},
        new String[] {"Payroll Period", "Pay Date", "Gross Pay", "Deductions", "Net Pay", "Pay Method", "Status"}
    );

    // ✅ Store payroll records in the list
    public static void addPayrollRecord(PayrollRecord record) {
        payrollRecords.add(record);
    }

    // ✅ Retrieve the latest payroll record safely
    public static PayrollRecord getLatestPayrollRecord() {
        return payrollRecords.isEmpty() ? null : payrollRecords.get(payrollRecords.size() - 1);
    }
    
    public static PayrollRecord getPayrollRecordByPeriod(String payrollPeriod) {
    for (PayrollRecord record : payrollRecords) { // Assuming payrollRecords is a list of records
        if (record.getPayrollPeriod().equals(payrollPeriod)) {
            return record;
        }
    }
    return null; // No matching record found
}

}
