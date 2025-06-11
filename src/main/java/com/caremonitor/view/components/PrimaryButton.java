package com.caremonitor.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.caremonitor.view.theme.UIStyles;

/**
 * Reusable primary action button with consistent styling.
 */
public class PrimaryButton extends JButton {
    public PrimaryButton(String text) {
        super(text);
        initialize();
    }

    public PrimaryButton() {
        this("");
    }

    private void initialize() {
        setBackground(UIStyles.DARK_BLUE);
        setForeground(Color.WHITE);
        setFont(UIStyles.ARIAL_BOLD_14);
        setFocusPainted(false);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyles.DARK_BLUE, 1),
                new EmptyBorder(10, 20, 10, 20)));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setOpaque(true);

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(UIStyles.DARKER_BLUE);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(UIStyles.DARK_BLUE);
            }
        });
    }
}
