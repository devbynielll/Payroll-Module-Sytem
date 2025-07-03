/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author danie
 */


public class NewEmployeeStorage {
    private static List<EmployeeRecord> employeeList = new ArrayList<>();

    // ✅ Method to add a new employee
    public static void addEmployee(EmployeeRecord record) {
        employeeList.add(record);
    }

    // ✅ Method to retrieve stored employees
    public static List<EmployeeRecord> getEmployees() { 
        return employeeList; 
    }
}