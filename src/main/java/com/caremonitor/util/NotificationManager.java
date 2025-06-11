// / File: src/main/java/com/caremonitor/util/NotificationManager.java
package com.caremonitor.util;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationManager {
    private static NotificationManager instance;
    private Map<String, Long> lastNotificationTime;
    private static final long NOTIFICATION_COOLDOWN = 30000;
    private volatile long cooldownTimeMs = NOTIFICATION_COOLDOWN;
    
    private NotificationManager() {
        lastNotificationTime = new ConcurrentHashMap<>();
    }
    
    public static synchronized NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }
        return instance;
    }
    
    public void showNotification(String title, String message, int patientId) {
        showNotification(title, message, String.valueOf(patientId));
    }

    public void showNotification(String title, String message, String notificationKey) {
        long currentTime = System.currentTimeMillis();
        
        Long lastTime = lastNotificationTime.get(notificationKey);
        if (lastTime == null || (currentTime - lastTime) > cooldownTimeMs) {
            
            SwingUtilities.invokeLater(() -> {
                JDialog dialog = new JDialog((Frame) null, title, true);
                dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                dialog.setAlwaysOnTop(true);
                
                JPanel mainPanel = new JPanel(new BorderLayout());
                mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                mainPanel.setBackground(Color.WHITE);
                
                JLabel iconLabel = new JLabel();
                iconLabel.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
                iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
                
                JLabel messageLabel = new JLabel("<html><div style='width: 300px;'>" + message + "</div></html>");
                messageLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                
                JPanel contentPanel = new JPanel(new BorderLayout());
                contentPanel.setBackground(Color.WHITE);
                contentPanel.add(iconLabel, BorderLayout.WEST);
                contentPanel.add(messageLabel, BorderLayout.CENTER);
                
                JPanel buttonPanel = new JPanel(new FlowLayout());
                buttonPanel.setBackground(Color.WHITE);
                
                JButton okButton = new JButton("OK");
                okButton.setPreferredSize(new Dimension(80, 30));
                okButton.addActionListener(e -> dialog.dispose());
                
                buttonPanel.add(okButton);
                
                mainPanel.add(contentPanel, BorderLayout.CENTER);
                mainPanel.add(buttonPanel, BorderLayout.SOUTH);
                
                dialog.add(mainPanel);
                dialog.pack();
                dialog.setLocationRelativeTo(null);
                
                Toolkit.getDefaultToolkit().beep();
                
                dialog.setVisible(true);
            });
            
            lastNotificationTime.put(notificationKey, currentTime);
        }
    }
    
    public void resetNotification(int patientId) {
        resetNotification(String.valueOf(patientId));
    }
    
    public void resetNotification(String notificationKey) {
        lastNotificationTime.remove(notificationKey);
    }
    
    public void resetAllNotifications() {
        lastNotificationTime.clear();
    }
    
    public synchronized void setCooldownTime(long cooldownMs) {
        this.cooldownTimeMs = cooldownMs;
    }
}
