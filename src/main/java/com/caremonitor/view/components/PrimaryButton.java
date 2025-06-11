package com.caremonitor.view.components;

import javax.swing.*;
import java.awt.*;

/**
 * A button styled for primary actions.
 */
public class PrimaryButton extends JButton {
    private static final Color DARK_BLUE = new Color(0, 32, 96);
    private static final Color HOVER_BLUE = new Color(0, 45, 120);

    public PrimaryButton() {
        this("");
    }

    public PrimaryButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 16));
        setBackground(DARK_BLUE);
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                setBackground(HOVER_BLUE);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                setBackground(DARK_BLUE);
            }
        });
    }
}
