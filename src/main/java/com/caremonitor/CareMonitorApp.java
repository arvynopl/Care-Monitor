//File: src/main/java/com/caremonitor/CareMonitorApp.java
package com.caremonitor;

import com.caremonitor.model.DatabaseManager;
import com.caremonitor.view.LoginView;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;

public class CareMonitorApp {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        
        FlatLightLaf.setup();

        DatabaseManager.getInstance();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            }
        });
    }
}