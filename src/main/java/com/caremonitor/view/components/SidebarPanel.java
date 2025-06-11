// File: src/main/java/com/caremonitor/view/components/SidebarPanel.java
package com.caremonitor.view.components;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import com.caremonitor.view.theme.UIStyles;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarPanel extends JPanel {
    private JLabel healthDashboardLabel;
    private JLabel healthHistoryLabel;
    private JLabel criticalParametersLabel;
    private JLabel logoutLabel;
    private JLabel activeLabel;
    
    
    private String userRole; 
    
    public SidebarPanel() {
        this("CAREGIVER"); 
    }
    
    public SidebarPanel(String userRole) {
        this.userRole = userRole.toUpperCase();
        initializeComponents();
        setupLayout();
    }
    
    private void initializeComponents() {
        setBackground(UIStyles.DARK_BLUE);
        setMinimumSize(new Dimension(150, 0));
        setMaximumSize(new Dimension(300, Integer.MAX_VALUE));

        healthDashboardLabel = createMenuLabel("Health Dashboard", true);
        healthHistoryLabel = createMenuLabel("Health History", false);
        
        
        if ("CAREGIVER".equals(userRole)) {
            criticalParametersLabel = createMenuLabel("Critical Parameters", false);
        }
        
        logoutLabel = createMenuLabel("Logout", false);
        logoutLabel.setForeground(UIStyles.DANGER_RED);
        
        activeLabel = healthDashboardLabel;
    }
    
    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
       
        JLabel titleLabel = new JLabel("Care Monitor");
        titleLabel.setFont(UIStyles.ARIAL_BOLD_20);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        add(titleLabel);
        add(Box.createRigidArea(new Dimension(0, 20)));
        
        
        add(healthDashboardLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(healthHistoryLabel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        
       
        if ("CAREGIVER".equals(userRole)) {
            add(criticalParametersLabel);
            add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        add(Box.createVerticalGlue());
        add(Box.createRigidArea(new Dimension(0, 30)));
    }
    
    private JLabel createMenuLabel(String text, boolean isActive) {
        JLabel label = new JLabel("<html>" + text + "</html>");
        label.setFont(isActive ? UIStyles.ARIAL_BOLD_16 : UIStyles.ARIAL_PLAIN_16);
        label.setForeground(isActive ? UIStyles.LIGHT_BLUE : Color.WHITE);

        Border side = BorderFactory.createMatteBorder(0, 5, 0, 0,
                isActive ? UIStyles.LIGHT_BLUE : UIStyles.DARK_BLUE);
        Border padding = BorderFactory.createEmptyBorder(15, 25, 15, 30);
        label.setBorder(BorderFactory.createCompoundBorder(side, padding));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (label != activeLabel) {
                    label.setOpaque(true);
                    label.setBackground(UIStyles.HOVER_BLUE);
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                if (label != activeLabel) {
                    label.setOpaque(false);
                }
            }
        });
        
        return label;
    }
    
    public void setActiveItem(JLabel label) {

        if (activeLabel != null) {
            activeLabel.setForeground(Color.WHITE);
            activeLabel.setFont(UIStyles.ARIAL_PLAIN_16);
            activeLabel.setOpaque(false);
            Border inactiveSide = BorderFactory.createMatteBorder(0, 5, 0, 0, UIStyles.DARK_BLUE);
            Border padding = BorderFactory.createEmptyBorder(15, 25, 15, 30);
            activeLabel.setBorder(BorderFactory.createCompoundBorder(inactiveSide, padding));
        }


        activeLabel = label;
        if (activeLabel != null) {
            activeLabel.setForeground(UIStyles.LIGHT_BLUE);
            activeLabel.setFont(UIStyles.ARIAL_BOLD_16);
            activeLabel.setOpaque(true);
            activeLabel.setBackground(UIStyles.HOVER_BLUE);
            Border activeSide = BorderFactory.createMatteBorder(0, 5, 0, 0, UIStyles.LIGHT_BLUE);
            Border padding = BorderFactory.createEmptyBorder(15, 25, 15, 30);
            activeLabel.setBorder(BorderFactory.createCompoundBorder(activeSide, padding));
        }
    }
    
    public JLabel getHealthDashboardLabel() {
        return healthDashboardLabel;
    }
    
    public JLabel getHealthHistoryLabel() {
        return healthHistoryLabel;
    }
    
    public JLabel getCriticalParametersLabel() {
        return criticalParametersLabel;
    }
    
    public JLabel getLogoutLabel() {
        return logoutLabel;
    }
    
    public void addHealthDashboardListener(ActionListener listener) {
        healthDashboardLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.actionPerformed(null);
            }
        });
    }
    
    public void addHealthHistoryListener(ActionListener listener) {
        healthHistoryLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.actionPerformed(null);
            }
        });
    }
    
    public void addCriticalParametersListener(ActionListener listener) {
        if (criticalParametersLabel != null) {
            criticalParametersLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    listener.actionPerformed(null);
                }
            });
        }
    }
    
    public void addLogoutListener(ActionListener listener) {
        logoutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                listener.actionPerformed(null);
            }
        });
    }
}
