// / File: src/main/java/com/caremonitor/view/LoginView.java
package com.caremonitor.view;

import com.caremonitor.controller.AuthController;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.caremonitor.view.theme.UIStyles;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AuthController authController;
    

    public LoginView() {
        authController = new AuthController(this);
        initializeComponents();
        setupLayout();
        setupActions();
    }

    private void initializeComponents() {
        setTitle("Care Monitor - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1000, 600));
        
        emailField = new JTextField();
        passwordField = new JPasswordField();
        loginButton = new JButton("Log In");
        
        emailField.setPreferredSize(new Dimension(300, 45));
        emailField.setFont(UIStyles.ARIAL_PLAIN_14);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.FIELD_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        passwordField.setPreferredSize(new Dimension(300, 45));
        passwordField.setFont(UIStyles.ARIAL_PLAIN_14);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.FIELD_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setFont(UIStyles.ARIAL_BOLD_16);
        loginButton.setBackground(UIStyles.DARK_BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(UIStyles.DARKER_BLUE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(UIStyles.DARK_BLUE);
            }
        });
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(UIStyles.DARK_BLUE);
        leftPanel.setPreferredSize(new Dimension(500, 0));
        
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(UIStyles.DARK_BLUE);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(new EmptyBorder(100, 50, 50, 50));
        
        JLabel logoLabel = new JLabel("Care Monitor");
        logoLabel.setFont(UIStyles.ARIAL_BOLD_36);
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Health Monitoring System");
        subtitleLabel.setFont(UIStyles.ARIAL_PLAIN_18);
        subtitleLabel.setForeground(UIStyles.SUBTITLE_GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoPanel.add(Box.createVerticalGlue());
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        logoPanel.add(subtitleLabel);
        logoPanel.add(Box.createVerticalGlue());
        
        leftPanel.add(logoPanel);
        
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        formPanel.setMaximumSize(new Dimension(400, 500));
        
        JLabel titleLabel = new JLabel("Log In");
        titleLabel.setFont(UIStyles.ARIAL_BOLD_32);
        titleLabel.setForeground(UIStyles.TEXT_DARK);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(UIStyles.ARIAL_PLAIN_14);
        emailLabel.setForeground(UIStyles.TEXT_MEDIUM);
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailField.setMaximumSize(new Dimension(300, 45));
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UIStyles.ARIAL_PLAIN_14);
        passwordLabel.setForeground(UIStyles.TEXT_MEDIUM);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(300, 45));
        
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(300, 45));
        
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        registerPanel.setMaximumSize(new Dimension(300, 30));
        
        JLabel dontHaveLabel = new JLabel("Don't have account? ");
        dontHaveLabel.setFont(UIStyles.ARIAL_PLAIN_14);
        dontHaveLabel.setForeground(UIStyles.TEXT_LIGHT);
        
        JButton signUpLink = new JButton("Sign up");
        signUpLink.setFont(UIStyles.ARIAL_PLAIN_14);
        signUpLink.setForeground(UIStyles.LIGHT_BLUE);
        signUpLink.setBorderPainted(false);
        signUpLink.setContentAreaFilled(false);
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLink.setFocusPainted(false);
        signUpLink.addActionListener(e -> openRegisterView());
        
        registerPanel.add(dontHaveLabel);
        registerPanel.add(signUpLink);
        
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        formPanel.add(loginButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        formPanel.add(registerPanel);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        rightPanel.add(formPanel, gbc);
        
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }

    private void setupActions() {
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performLogin();
            }
        });
        
        emailField.addActionListener(e -> passwordField.requestFocus());
        passwordField.addActionListener(e -> performLogin());
    }
    
    private void performLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword());
        
        if (email.isEmpty() || password.isEmpty()) {
            showErrorMessage("Please enter both email and password");
            return;
        }
        
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                authController.login(email, password);
                return null;
            }
            
            @Override
            protected void done() {
                loginButton.setEnabled(true);
                loginButton.setText("Log In");
            }
        };
        worker.execute();
    }
    
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Login Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    private void openRegisterView() {
        RegisterView registerView = new RegisterView();
        registerView.setVisible(true);
        this.dispose();
    }
    
    public static void main(String[] args) {
        FlatLightLaf.setup();

        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
