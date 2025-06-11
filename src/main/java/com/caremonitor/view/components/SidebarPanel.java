// File: src/main/java/com/caremonitor/view/components/SidebarPanel.java
package com.caremonitor.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SidebarPanel extends JPanel {
    private JLabel healthDashboardLabel;
    private JLabel healthHistoryLabel;
    private JLabel criticalParametersLabel;
    private JLabel logoutLabel;
    private JLabel activeLabel;
    
    private final Color DARK_BLUE = new Color(0, 32, 96);
    private final Color LIGHT_BLUE = new Color(59, 130, 246);
    private final Color HOVER_COLOR = new Color(30, 58, 138);
    
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
        setBackground(DARK_BLUE);
        
        
        healthDashboardLabel = createMenuLabel("Health Dashboard", true);
        healthHistoryLabel = createMenuLabel("Health History", false);
        
        
        if ("CAREGIVER".equals(userRole)) {
            criticalParametersLabel = createMenuLabel("Critical Parameters", false);
        }
        
        logoutLabel = createMenuLabel("Logout", false);
        logoutLabel.setForeground(new Color(239, 68, 68)); 
        
        activeLabel = healthDashboardLabel;
    }
    
    private void setupLayout() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
       
        JLabel titleLabel = new JLabel("Care Monitor");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        titleLabel.setMinimumSize(new Dimension(0, titleLabel.getPreferredSize().height));
        titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, titleLabel.getPreferredSize().height));
        
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
        add(logoutLabel);
        add(Box.createRigidArea(new Dimension(0, 30)));

        updatePreferredWidth(titleLabel);
    }
    
    private JLabel createMenuLabel(String text, boolean isActive) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(isActive ? LIGHT_BLUE : Color.WHITE);
        label.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        label.setCursor(new Cursor(Cursor.HAND_CURSOR));

        label.setMinimumSize(new Dimension(0, label.getPreferredSize().height));
        label.setMaximumSize(new Dimension(Integer.MAX_VALUE, label.getPreferredSize().height));
        
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (label != activeLabel) {
                    label.setOpaque(true);
                    label.setBackground(HOVER_COLOR);
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
            activeLabel.setOpaque(false);
        }
        
        
        activeLabel = label;
        if (activeLabel != null) {
            activeLabel.setForeground(LIGHT_BLUE);
            activeLabel.setOpaque(true);
            activeLabel.setBackground(HOVER_COLOR);
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

    private void updatePreferredWidth(JLabel titleLabel) {
        int maxWidth = titleLabel.getPreferredSize().width + 40;

        JLabel[] labels = { healthDashboardLabel, healthHistoryLabel, criticalParametersLabel, logoutLabel };
        for (JLabel lbl : labels) {
            if (lbl != null) {
                int w = lbl.getPreferredSize().width + 60;
                if (w > maxWidth) {
                    maxWidth = w;
                }
            }
        }

        setPreferredSize(new Dimension(maxWidth, 0));
    }
}
