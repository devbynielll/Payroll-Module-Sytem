/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.swing.*;
import javax.swing.text.*;

public class Utils {
    public static void setDigitLimit(JTextField textField, int maxLength) {
        ((AbstractDocument) textField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                
                // Only allow digits
                String filtered = string.replaceAll("\\D+", "");
                
                // Calculate new length after insertion
                int newLength = fb.getDocument().getLength() + filtered.length();
                if (newLength <= maxLength) {
                    super.insertString(fb, offset, filtered, attr);
                } else {
                    // Insert only allowed number of digits to reach maxLength
                    int allowed = maxLength - fb.getDocument().getLength();
                    if (allowed > 0) {
                        super.insertString(fb, offset, filtered.substring(0, allowed), attr);
                    }
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;

                String filtered = text.replaceAll("\\D+", "");
                
                int currentLength = fb.getDocument().getLength();
                int newLength = currentLength - length + filtered.length();

                if (newLength <= maxLength) {
                    super.replace(fb, offset, length, filtered, attrs);
                } else {
                    int allowed = maxLength - (currentLength - length);
                    if (allowed > 0) {
                        super.replace(fb, offset, length, filtered.substring(0, allowed), attrs);
                    }
                }
            }
        });
    }
}
