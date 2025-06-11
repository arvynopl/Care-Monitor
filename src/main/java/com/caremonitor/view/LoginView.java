// / File: src/main/java/com/caremonitor/view/LoginView.java
package com.caremonitor.view;

import com.caremonitor.controller.AuthController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AuthController authController;
    
    private final Color DARK_BLUE = new Color(0, 32, 96);
    private final Color LIGHT_BLUE = new Color(59, 130, 246);
    private final Color FIELD_BORDER = new Color(209, 213, 219);

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
        
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        loginButton = new JButton("Log In");

        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FIELD_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(FIELD_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setBackground(DARK_BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(0, 45, 120));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(DARK_BLUE);
            }
        });
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(DARK_BLUE);
        
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(DARK_BLUE);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setBorder(new EmptyBorder(100, 50, 50, 50));
        
        JLabel logoLabel = new JLabel("Care Monitor");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 36));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Health Monitoring System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoPanel.add(Box.createVerticalGlue());
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        logoPanel.add(subtitleLabel);
        logoPanel.add(Box.createVerticalGlue());
        
        leftPanel.add(logoPanel, new GridBagConstraints());
        
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(new GridBagLayout());
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        
        JLabel titleLabel = new JLabel("Log In");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(new Color(31, 41, 55));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailLabel.setForeground(new Color(75, 85, 99));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        emailLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordLabel.setForeground(new Color(75, 85, 99));
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel dontHaveLabel = new JLabel("Don't have account? ");
        dontHaveLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dontHaveLabel.setForeground(new Color(107, 114, 128));
        
        JButton signUpLink = new JButton("Sign up");
        signUpLink.setFont(new Font("Arial", Font.PLAIN, 14));
        signUpLink.setForeground(LIGHT_BLUE);
        signUpLink.setBorderPainted(false);
        signUpLink.setContentAreaFilled(false);
        signUpLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signUpLink.setFocusPainted(false);
        signUpLink.addActionListener(e -> openRegisterView());
        
        registerPanel.add(dontHaveLabel);
        registerPanel.add(signUpLink);
        
        GridBagConstraints gbcForm = new GridBagConstraints();
        gbcForm.gridx = 0;
        gbcForm.fill = GridBagConstraints.HORIZONTAL;
        gbcForm.weightx = 1.0;

        gbcForm.insets = new Insets(0, 0, 20, 0);
        gbcForm.gridy = 0;
        formPanel.add(titleLabel, gbcForm);

        gbcForm.insets = new Insets(5, 0, 5, 0);
        gbcForm.gridy++;
        formPanel.add(emailLabel, gbcForm);
        gbcForm.gridy++;
        formPanel.add(emailField, gbcForm);

        gbcForm.gridy++;
        formPanel.add(passwordLabel, gbcForm);
        gbcForm.gridy++;
        formPanel.add(passwordField, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(15, 0, 5, 0);
        formPanel.add(loginButton, gbcForm);

        gbcForm.gridy++;
        gbcForm.insets = new Insets(10, 0, 0, 0);
        formPanel.add(registerPanel, gbcForm);
        
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.weightx = 1.0;
        rightGbc.weighty = 1.0;
        rightGbc.fill = GridBagConstraints.BOTH;
        rightPanel.add(formPanel, rightGbc);

        GridBagConstraints mainGbc = new GridBagConstraints();
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.weighty = 1.0;

        mainGbc.weightx = 0.4;
        mainGbc.gridx = 0;
        mainPanel.add(leftPanel, mainGbc);

        mainGbc.weightx = 0.6;
        mainGbc.gridx = 1;
        mainPanel.add(rightPanel, mainGbc);
        
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
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
