/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author danie
 */
public class EmployeeRecord {
    private String userID;
    private String fullName;
    private String bankName;
    private String accountNumber;
    private String department;
    private String jobPosition;
    private String employmentStatus;

    public EmployeeRecord(String userID, String fullName, String bankName, String accountNumber,
                          String department, String jobPosition, String employmentStatus) {
        this.userID = userID;
        this.fullName = fullName;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.department = department;
        this.jobPosition = jobPosition;
        this.employmentStatus = employmentStatus;
    }

    // ✅ Getters to retrieve employee details
    public String getUserID() { return userID; }
    public String getFullName() { return fullName; }
    public String getBankName() { return bankName; }
    public String getAccountNumber() { return accountNumber; }
    public String getDepartment() { return department; }
    public String getJobPosition() { return jobPosition; }
    public String getEmploymentStatus() { return employmentStatus; }
}
