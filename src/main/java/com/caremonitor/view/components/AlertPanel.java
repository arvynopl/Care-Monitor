// File: src/main/java/com/caremonitor/view/components/AlertPanel.java
package com.caremonitor.view.components;

import com.caremonitor.model.Patient;
import com.caremonitor.model.HealthData;

import javax.swing.*;
import java.awt.*;
import com.caremonitor.view.theme.UIStyles;
// import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AlertPanel extends JPanel {
    
    public enum AlertType {
        INFO, WARNING, ERROR
    }
    
    private List<AlertItem> alerts;
    
    public AlertPanel() {
        alerts = new ArrayList<>();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
    }
    
    public AlertPanel(Patient patient, HealthData data, String parameter) {
        this(); 
        addAlert(patient.getName() + ": " + parameter, AlertType.ERROR);
    }
    
    public void addAlert(String message, AlertType type) {
        AlertItem alertItem = new AlertItem(message, type);
        alerts.add(0, alertItem); 
        
        if (alerts.size() > 10) {
            alerts.remove(alerts.size() - 1);
        }
        
        refreshAlerts();
    }
    
    private void refreshAlerts() {
        removeAll();
        
        if (alerts.isEmpty()) {
            JLabel noAlertsLabel = new JLabel("No alerts");
            noAlertsLabel.setForeground(Color.GRAY);
            noAlertsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            add(noAlertsLabel);
        } else {
            for (AlertItem alert : alerts) {
                add(createAlertPanel(alert));
                add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        
        revalidate();
        repaint();
    }
    
    private JPanel createAlertPanel(AlertItem alert) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 3, 0, 0, getColorForType(alert.type)),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyles.LIGHT_GRAY_229, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
            )
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        String patientName = "Unknown";
        if (alert.message.contains(":")) {
            patientName = alert.message.substring(0, alert.message.indexOf(":"));
        }
        
        JLabel nameLabel = new JLabel(patientName);
        nameLabel.setFont(UIStyles.ARIAL_BOLD_14);
        
        JLabel iconLabel = new JLabel("●");
        iconLabel.setForeground(getColorForType(alert.type));
        
        headerPanel.add(nameLabel, BorderLayout.WEST);
        headerPanel.add(iconLabel, BorderLayout.EAST);
        
        String alertMessage = alert.message;
        if (alert.message.contains(":")) {
            alertMessage = alert.message.substring(alert.message.indexOf(":") + 1).trim();
        }
        
        JLabel messageLabel = new JLabel(alertMessage);
        messageLabel.setFont(UIStyles.ARIAL_PLAIN_12);
        
        JLabel timeLabel = new JLabel(getTimeAgo(alert.timestamp));
        timeLabel.setFont(UIStyles.ARIAL_ITALIC_11);
        timeLabel.setForeground(Color.GRAY);
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(messageLabel, BorderLayout.CENTER);
        panel.add(timeLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private Color getColorForType(AlertType type) {
        switch (type) {
            case INFO:
                return UIStyles.LIGHT_BLUE;
            case WARNING:
                return UIStyles.WARNING_ORANGE;
            case ERROR:
                return UIStyles.DANGER_RED;
            default:
                return Color.GRAY;
        }
    }
    
    private String getTimeAgo(Date timestamp) {
        long diffInMillis = new Date().getTime() - timestamp.getTime();
        long diffInSeconds = diffInMillis / 1000;
        long diffInMinutes = diffInSeconds / 60;
        long diffInHours = diffInMinutes / 60;
        long diffInDays = diffInHours / 24;
        
        if (diffInDays > 0) {
            return diffInDays + " day" + (diffInDays > 1 ? "s" : "") + " ago";
        } else if (diffInHours > 0) {
            return diffInHours + " hour" + (diffInHours > 1 ? "s" : "") + " ago";
        } else if (diffInMinutes > 0) {
            return diffInMinutes + " minute" + (diffInMinutes > 1 ? "s" : "") + " ago";
        } else {
            return "Just now";
        }
    }
    
    private class AlertItem {
        String message;
        AlertType type;
        Date timestamp;
        
        public AlertItem(String message, AlertType type) {
            this.message = message;
            this.type = type;
            this.timestamp = new Date();
        }
    }
}