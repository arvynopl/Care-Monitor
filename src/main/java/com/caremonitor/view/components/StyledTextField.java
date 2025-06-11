package com.caremonitor.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A text field with consistent border, padding and font styling.
 */
public class StyledTextField extends JTextField {
    private static final Color FIELD_BORDER = new Color(209, 213, 219);

    public StyledTextField() {
        super();
        setFont(new Font("Arial", Font.PLAIN, 14));
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(FIELD_BORDER, 1),
                new EmptyBorder(10, 15, 10, 15)
        ));
    }
}
