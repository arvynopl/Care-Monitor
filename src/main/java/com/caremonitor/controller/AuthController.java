package com.caremonitor.controller;

import com.caremonitor.model.DatabaseManager;
import com.caremonitor.model.User;
import com.caremonitor.model.Patient;
import com.caremonitor.model.Caregiver;
import com.caremonitor.model.Family;
import com.caremonitor.view.LoginView;
import com.caremonitor.view.DashboardView;
import com.caremonitor.view.components.SidebarPanel;
import com.caremonitor.view.HealthHistoryView;
import com.caremonitor.view.CriticalParametersView;
import com.caremonitor.view.theme.UIStyles;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AuthController {
    private LoginView loginView;
    private DatabaseManager dbManager;
    
    public AuthController(LoginView loginView) {
        this.loginView = loginView;
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public void login(String email, String password) {
        try {
            User user = authenticateUser(email, password);
            if (user != null) {
                System.out.println("Login successful for user: " + user.getName());
                
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (loginView != null) {
                            loginView.setVisible(false);
                            loginView.dispose();
                        }
                        
                        createDashboardFrame(user);
                        
                        System.out.println("Dashboard opened successfully");
                    } catch (Exception e) {
                        System.err.println("Error opening dashboard: " + e.getMessage());
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, 
                            "Error opening dashboard: " + e.getMessage(), 
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                });
            } else {
                SwingUtilities.invokeLater(() -> {
                    if (loginView != null) {
                        JOptionPane.showMessageDialog(loginView, 
                            "Invalid email or password", 
                            "Login Failed", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> {
                if (loginView != null) {
                    JOptionPane.showMessageDialog(loginView, 
                        "Login error: " + e.getMessage(), 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
        }
    }
    
    private void createDashboardFrame(User user) {
        JFrame dashboardFrame = new JFrame("Care Monitor - Dashboard (" + user.getName() + ")");
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        dashboardFrame.setMinimumSize(new Dimension(1000, 600));
        JPanel mainPanel = new JPanel(new BorderLayout());


        SidebarPanel sidebarPanel = new SidebarPanel(user.getRole());
        CardLayout contentCardLayout = new CardLayout();
        JPanel contentPanel = new JPanel(contentCardLayout);
        DashboardView dashboardView = new DashboardView(user);
        HealthHistoryView healthHistoryView = new HealthHistoryView(user);
        
        CriticalParametersView criticalParametersView = null;
        if ("CAREGIVER".equalsIgnoreCase(user.getRole())) {
            criticalParametersView = new CriticalParametersView(user);
            contentPanel.add(criticalParametersView.getMainPanel(), "parameters");
        }

        contentPanel.add(dashboardView.getMainPanel(), "dashboard");
        contentPanel.add(healthHistoryView.getMainPanel(), "history");

        // Ensure the dashboard is shown initially to match the active sidebar
        contentCardLayout.show(contentPanel, "dashboard");
        
        setupNavigation(sidebarPanel, contentCardLayout, contentPanel,
                       dashboardView, healthHistoryView, criticalParametersView,
                       dashboardFrame, user);
        sidebarPanel.addLogoutListener(e -> logout(dashboardFrame, dashboardView));
        
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        dashboardFrame.setContentPane(mainPanel);
        
        sidebarPanel.setActiveItem(sidebarPanel.getHealthDashboardLabel());
        
        dashboardFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (dashboardView != null) {
                    dashboardView.stopAutoRefresh();
                }
                System.exit(0);
            }
        });
        
        dashboardFrame.setVisible(true);
        dashboardFrame.toFront();
        dashboardFrame.requestFocus();
    }
    
    private void setupNavigation(SidebarPanel sidebarPanel, CardLayout contentCardLayout, 
                               JPanel contentPanel, DashboardView dashboardView, 
                               HealthHistoryView healthHistoryView, 
                               CriticalParametersView criticalParametersView,
                               JFrame dashboardFrame, User user) {
        
        sidebarPanel.addHealthDashboardListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentCardLayout.show(contentPanel, "dashboard");
                sidebarPanel.setActiveItem(sidebarPanel.getHealthDashboardLabel());
                updateFrameTitle(dashboardFrame, "Dashboard", user);
            }
        });
        
        sidebarPanel.addHealthHistoryListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                healthHistoryView.refreshData();
                contentCardLayout.show(contentPanel, "history");
                sidebarPanel.setActiveItem(sidebarPanel.getHealthHistoryLabel());
                updateFrameTitle(dashboardFrame, "Health History", user);
            }
        });
        
        if ("CAREGIVER".equalsIgnoreCase(user.getRole()) && criticalParametersView != null) {
            sidebarPanel.addCriticalParametersListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    criticalParametersView.refreshData();
                    contentCardLayout.show(contentPanel, "parameters");
                    sidebarPanel.setActiveItem(sidebarPanel.getCriticalParametersLabel());
                    updateFrameTitle(dashboardFrame, "Critical Parameters", user);
                }
            });
        }
        
    }
    
    private void updateFrameTitle(JFrame frame, String currentView, User user) {
        String roleDisplay = user.getRole().substring(0, 1).toUpperCase() + 
                           user.getRole().substring(1).toLowerCase();
        frame.setTitle("Care Monitor - " + currentView + " (" + roleDisplay + ": " + user.getName() + ")");
    }
    
    private void logout(JFrame dashboardFrame, DashboardView dashboardView) {
        int option = JOptionPane.showConfirmDialog(
            dashboardFrame,
            "Are you sure you want to logout?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            if (dashboardView != null) {
                dashboardView.stopAutoRefresh();
            }
            
            dashboardFrame.dispose();
            
            SwingUtilities.invokeLater(() -> {
                LoginView newLoginView = new LoginView();
                newLoginView.setVisible(true);
            });
        }
    }
    
    private User authenticateUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    int userId = rs.getInt("id");
                    String fullName = rs.getString("full_name");
                    
                    switch (role.toUpperCase()) {
                        case "CAREGIVER":
                            String specialization = rs.getString("specialization");
                            String contact = rs.getString("contact");
                            return new Caregiver(userId, fullName, email, password, specialization, contact);
                        case "FAMILY":
                            String contactFamily = rs.getString("contact");
                            String relationship = rs.getString("relationship");
                            return new Family(userId, fullName, email, password, contactFamily, relationship);
                        default:
                            System.err.println("Unknown user role: " + role);
                            return null;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during authentication: " + e.getMessage());
            e.printStackTrace();
        }
        
        String patientSql = "SELECT * FROM patients WHERE unique_code = ? AND name = ?";
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(patientSql)) {
            
            pstmt.setString(1, email); 
            pstmt.setString(2, password);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int userId = rs.getInt("id");
                    String name = rs.getString("name");
                    int age = rs.getInt("age");
                    String gender = rs.getString("gender");
                    String address = rs.getString("address");
                    String contact = rs.getString("emergency_contact");
                    String patientCode = rs.getString("unique_code");
                    
                    return new Patient(userId, name, age, gender, address, contact, patientCode);
                }
            }
        } catch (SQLException e) {
            System.err.println("Database error during patient authentication: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean registerCaregiverWithPatients(String name, String email, String password, 
                                                String contact, String specialization, 
                                                List<Integer> patientIds, List<String> patientCodes) {
        try (Connection conn = dbManager.getConnection()) {
            conn.setAutoCommit(false);
            
            String userSql = "INSERT INTO users (full_name, email, password, role, specialization, contact) VALUES (?, ?, ?, 'CAREGIVER', ?, ?)";
            PreparedStatement userPstmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userPstmt.setString(1, name);
            userPstmt.setString(2, email);
            userPstmt.setString(3, password);
            userPstmt.setString(4, specialization);
            userPstmt.setString(5, contact);
            
            int userResult = userPstmt.executeUpdate();
            if (userResult == 0) {
                conn.rollback();
                return false;
            }
            
            ResultSet generatedKeys = userPstmt.getGeneratedKeys();
            int userId = 0;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return false;
            }
            
            for (int i = 0; i < patientIds.size(); i++) {
                int patientId = patientIds.get(i);
                String patientCode = patientCodes.get(i);
                
                String verifySql = "SELECT id FROM patients WHERE id = ? AND unique_code = ?";
                PreparedStatement verifyPstmt = conn.prepareStatement(verifySql);
                verifyPstmt.setInt(1, patientId);
                verifyPstmt.setString(2, patientCode);
                
                ResultSet verifyRs = verifyPstmt.executeQuery();
                if (!verifyRs.next()) {
                    conn.rollback();
                    return false;
                }
                
                String assignSql = "UPDATE patients SET caregiver_id = ? WHERE id = ?";
                PreparedStatement assignPstmt = conn.prepareStatement(assignSql);
                assignPstmt.setInt(1, userId);
                assignPstmt.setInt(2, patientId);
                assignPstmt.executeUpdate();
            }
            
            String caregiverSql = "INSERT INTO caregivers (user_id, specialization, patient_ids) VALUES (?, ?, ?)";
            PreparedStatement caregiverPstmt = conn.prepareStatement(caregiverSql);
            caregiverPstmt.setInt(1, userId);
            caregiverPstmt.setString(2, specialization);
            
            Integer[] patientArray = patientIds.toArray(new Integer[0]);
            java.sql.Array sqlArray = conn.createArrayOf("INTEGER", patientArray);
            caregiverPstmt.setArray(3, sqlArray);
            caregiverPstmt.executeUpdate();
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error registering caregiver: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean registerFamilyWithPatients(String name, String email, String password, 
                                             String contact, String relationship, 
                                             List<Integer> patientIds, List<String> patientCodes) {
        try (Connection conn = dbManager.getConnection()) {
            conn.setAutoCommit(false);
            
            String userSql = "INSERT INTO users (full_name, email, password, role, contact, relationship) VALUES (?, ?, ?, 'FAMILY', ?, ?)";
            PreparedStatement userPstmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);
            userPstmt.setString(1, name);
            userPstmt.setString(2, email);
            userPstmt.setString(3, password);
            userPstmt.setString(4, contact);
            userPstmt.setString(5, relationship);
            
            int userResult = userPstmt.executeUpdate();
            if (userResult == 0) {
                conn.rollback();
                return false;
            }
            
            ResultSet generatedKeys = userPstmt.getGeneratedKeys();
            int userId = 0;
            if (generatedKeys.next()) {
                userId = generatedKeys.getInt(1);
            } else {
                conn.rollback();
                return false;
            }
            
            for (int i = 0; i < patientIds.size(); i++) {
                int patientId = patientIds.get(i);
                String patientCode = patientCodes.get(i);
                
                String verifySql = "SELECT id FROM patients WHERE id = ? AND unique_code = ?";
                PreparedStatement verifyPstmt = conn.prepareStatement(verifySql);
                verifyPstmt.setInt(1, patientId);
                verifyPstmt.setString(2, patientCode);
                
                ResultSet verifyRs = verifyPstmt.executeQuery();
                if (!verifyRs.next()) {
                    conn.rollback();
                    return false;
                }
                
                String familyPatientSql = "INSERT INTO family_patient (family_id, patient_id, full_name, email, contact, relationship) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement familyPatientPstmt = conn.prepareStatement(familyPatientSql);
                familyPatientPstmt.setInt(1, userId);
                familyPatientPstmt.setInt(2, patientId);
                familyPatientPstmt.setString(3, name);
                familyPatientPstmt.setString(4, email);
                familyPatientPstmt.setString(5, contact);
                familyPatientPstmt.setString(6, relationship);
                familyPatientPstmt.executeUpdate();
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error registering family: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean registerCaregiver(String name, int age, String gender, String address, 
                                   String contact, String email, String password, String specialization) {
        String sql = "INSERT INTO users (full_name, email, password, role, specialization, contact) VALUES (?, ?, ?, 'CAREGIVER', ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, specialization);
            pstmt.setString(5, contact);
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error registering caregiver: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean registerFamily(String name, int age, String gender, String address, 
                                String contact, String email, String password, String relationship, String patientCode) {
        int patientId = verifyPatientCode(patientCode);
        if (patientId == -1) {
            return false;
        }
        
        String sql = "INSERT INTO users (full_name, email, password, role, contact, relationship) VALUES (?, ?, ?, 'FAMILY', ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, contact);
            pstmt.setString(5, relationship);
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error registering family member: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public int verifyPatientCode(String patientCode) {
        String sql = "SELECT id FROM patients WHERE unique_code = ?";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, patientCode);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error verifying patient code: " + e.getMessage());
            e.printStackTrace();
        }
        
        return -1;
    }
}
