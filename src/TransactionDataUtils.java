/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.io.*;
import javax.swing.table.DefaultTableModel;

public class TransactionDataUtils {

    private static final String TRANSACTION_FILE = "transactions.csv"; // File to store transactions

    // Save transaction data to a CSV file
    public static void saveTransactions(DefaultTableModel model) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TRANSACTION_FILE))) {
            // Write column headers
            for (int i = 0; i < model.getColumnCount(); i++) {
                writer.print(model.getColumnName(i));
                if (i < model.getColumnCount() - 1) {
                    writer.print(",");
                }
            }
            writer.println();

            // Write rows
            for (int i = 0; i < model.getRowCount(); i++) {
                for (int j = 0; j < model.getColumnCount(); j++) {
                    writer.print(model.getValueAt(i, j));
                    if (j < model.getColumnCount() - 1) {
                        writer.print(",");
                    }
                }
                writer.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load transaction data from a CSV file
    public static void loadTransactions(DefaultTableModel model) {
        File file = new File(TRANSACTION_FILE);
        if (!file.exists()) {
            return; // If the file doesn't exist, there's nothing to load
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;

            // Clear existing data
            model.setRowCount(0);

            while ((line = reader.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header row
                    continue;
                }

                // Split the line into columns and add to the model
                String[] rowData = line.split(",");
                model.addRow(rowData);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}