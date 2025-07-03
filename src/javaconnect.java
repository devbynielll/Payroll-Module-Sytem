/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author danie
 */

import java.sql.*; 
import javax.swing.JOptionPane;


public class javaconnect {
    
    static Connection connect = null;
    
    public static Connection ConnectDb() {
        
        try {
            System.out.println ("Classpath: " + System.getProperty ("java.class.path"));
            
            Class.forName("org.sqlite.JDBC");
            
            String dbUrl = "jdbc:sqlite:C:\\Users\\danie\\OneDrive\\Documents\\NetBeansProjects\\PayrollModule_2_Copy\\Payroll.db";
            
            connect = DriverManager.getConnection(dbUrl);
            
            return connect;
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog (null, e);
        }
        return null;
   
}
    public static void main (String [] args) {
        ConnectDb();
    }
}
