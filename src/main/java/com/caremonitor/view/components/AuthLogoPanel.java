// File: src/main/java/com/caremonitor/view/components/AuthLogoPanel.java
package com.caremonitor.view.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.caremonitor.view.theme.UIStyles;

/**
 * Small panel showing the application logo and subtitle used
 * on authentication screens.
 */
public class AuthLogoPanel extends JPanel {
    public AuthLogoPanel() {
        setBackground(UIStyles.DARK_BLUE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(new EmptyBorder(50, 50, 50, 50));

        JLabel logoLabel = new JLabel("Care Monitor");
        logoLabel.setFont(UIStyles.ARIAL_BOLD_36);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Health Monitoring System");
        subtitleLabel.setFont(UIStyles.ARIAL_PLAIN_18);
        subtitleLabel.setForeground(UIStyles.SUBTITLE_GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalGlue());
        add(logoLabel);
        add(Box.createRigidArea(new Dimension(0, 15)));
        add(subtitleLabel);
        add(Box.createVerticalGlue());
    }
}
