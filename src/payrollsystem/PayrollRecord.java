package payrollsystem;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author danie
 */
public class PayrollRecord {
    // ✅ Employee Details
    private String idNumber, fullName, jobPosition, employeeStatus, department;
    
    // ✅ Payroll Details
    private String payDate, payrollPeriod, payTime, payMethod, status;
    
    // ✅ Financial Details
    private String bankName, bankAccountName, bankAccountNumber;
    private String basicSalary, overtimePay, allowances, incentives, commissions;
    private String absences, lateUndertime, unpaidLeave, others;
    private String sss, philhealth, pagibig;
    private String grossPay, totalDeductions, netPay;

    // ✅ Constructor: Ensuring Proper Initialization
    public PayrollRecord(String payDate, String idNumber, String fullName, String jobPosition, String employeeStatus,
                         String bankName, String bankAccountName, String bankAccountNumber, String basicSalary, 
                         String overtimePay, String allowances, String incentives, String commissions, 
                         String absences, String lateUndertime, String unpaidLeave, String others, String sss, 
                         String philhealth, String pagibig, String grossPay, String totalDeductions, 
                         String netPay, String payrollPeriod, String payTime, String payMethod, 
                         String status, String department) { 
    
        this.payDate = (payDate != null) ? payDate : "N/A";
        this.idNumber = (idNumber != null) ? idNumber : "Unknown";
        this.fullName = (fullName != null) ? fullName : "No Name";
        this.jobPosition = (jobPosition != null) ? jobPosition : "Not Assigned";
        this.employeeStatus = (employeeStatus != null) ? employeeStatus : "Unknown";
        this.bankName = (bankName != null) ? bankName : "Philippine National Bank"; // ✅ Default Bank
        this.bankAccountName = (bankAccountName != null) ? bankAccountName : "Juan Dela Cruz"; // ✅ Default Account Name
        this.bankAccountNumber = (bankAccountNumber != null) ? bankAccountNumber : "1200 2300 3400 123"; // ✅ Default Account Number
        this.basicSalary = (basicSalary != null) ? basicSalary : "0.00";
        this.overtimePay = (overtimePay != null) ? overtimePay : "0.00";
        this.allowances = (allowances != null) ? allowances : "0.00";
        this.incentives = (incentives != null) ? incentives : "0.00";
        this.commissions = (commissions != null) ? commissions : "0.00";
        this.absences = (absences != null) ? absences : "0.00";
        this.lateUndertime = (lateUndertime != null) ? lateUndertime : "0.00";
        this.unpaidLeave = (unpaidLeave != null) ? unpaidLeave : "0.00";
        this.others = (others != null) ? others : "0.00";
        this.sss = (sss != null) ? sss : "0.00";
        this.philhealth = (philhealth != null) ? philhealth : "0.00";
        this.pagibig = (pagibig != null) ? pagibig : "0.00";
        this.grossPay = (grossPay != null) ? grossPay : "0.00";
        this.totalDeductions = (totalDeductions != null) ? totalDeductions : "0.00";
        this.netPay = (netPay != null) ? netPay : "0.00";
        this.payrollPeriod = (payrollPeriod != null) ? payrollPeriod : "N/A";
        this.payTime = (payTime != null) ? payTime : "00:00 AM";
        this.payMethod = (payMethod != null) ? payMethod : "Bank Transfer";
        this.status = (status != null) ? status : "Pending";
        this.department = (department != null) ? department : "Not Assigned";
    }

    // ✅ Getters for Retrieving Data
    public String getIdNumber() { return idNumber; }
    public String getFullName() { return fullName; }
    public String getJobPosition() { return jobPosition; }
    public String getEmployeeStatus() { return employeeStatus; }
    public String getBankName() { return bankName; }
    public String getBankAccountName() { return bankAccountName; } 
    public String getBankAccountNumber() { return bankAccountNumber; } 
    public String getBasicSalary() { return basicSalary; }
    public String getOvertimePay() { return overtimePay; }
    public String getAllowances() { return allowances; }
    public String getIncentives() { return incentives; }
    public String getCommissions() { return commissions; }
    public String getAbsences() { return absences; }
    public String getLateUndertime() { return lateUndertime; }
    public String getUnpaidLeave() { return unpaidLeave; }
    public String getOthers() { return others; }
    public String getSSS() { return sss; }
    public String getPhilHealth() { return philhealth; }
    public String getPagibig() { return pagibig; }
    public String getGrossPay() { return grossPay; }
    public String getTotalDeductions() { return totalDeductions; }
    public String getNetPay() { return netPay; }
    public String getPayrollPeriod() { return payrollPeriod; }
    public String getPayDate() { return payDate; }
    public String getPayTime() { return payTime; }
    public String getPayMethod() { return payMethod; }
    public String getStatus() { return status; }
    public String getDepartment() { return department; }

    // ✅ Setter Methods for Dynamic Updates
    public void setBankName(String bankName) { this.bankName = bankName; }
    public void setBankAccountName(String bankAccountName) { this.bankAccountName = bankAccountName; } 
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; } 
    public void setBasicSalary(String basicSalary) { this.basicSalary = basicSalary; }
    public void setOvertimePay(String overtimePay) { this.overtimePay = overtimePay; }
    public void setAllowances(String allowances) { this.allowances = allowances; }
    public void setIncentives(String incentives) { this.incentives = incentives; }
    public void setCommissions(String commissions) { this.commissions = commissions; }
    public void setAbsences(String absences) { this.absences = absences; }
    public void setLateUndertime(String lateUndertime) { this.lateUndertime = lateUndertime; }
    public void setUnpaidLeave(String unpaidLeave) { this.unpaidLeave = unpaidLeave; }
    public void setOthers(String others) { this.others = others; }
    public void setGrossPay(String grossPay) { this.grossPay = grossPay; }
    public void setTotalDeductions(String totalDeductions) { this.totalDeductions = totalDeductions; }
    public void setNetPay(String netPay) { this.netPay = netPay; }
    public void setDepartment(String department) { this.department = department; }
    public void setPayDate(String payDate) { this.payDate = payDate; }
}


