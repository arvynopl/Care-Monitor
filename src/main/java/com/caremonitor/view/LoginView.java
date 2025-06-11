// / File: src/main/java/com/caremonitor/view/LoginView.java
package com.caremonitor.view;

import com.caremonitor.controller.AuthController;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import com.caremonitor.view.components.StyledTextField;
import com.caremonitor.view.components.PrimaryButton;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import net.miginfocom.swing.MigLayout;
import com.caremonitor.view.theme.UIStyles;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginView extends JFrame {
    private StyledTextField emailField;
    private StyledTextField.StyledPasswordField passwordField;
    private PrimaryButton loginButton;
    private AuthController authController;

    private static final Dimension FIELD_SIZE = new Dimension(350, 40);
    private static final Dimension BUTTON_SIZE = new Dimension(350, 45);
    

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
        
        emailField = new StyledTextField();
        passwordField = new StyledTextField.StyledPasswordField();
        loginButton = new PrimaryButton("Log In");

        emailField.setPreferredSize(FIELD_SIZE);
        emailField.setMaximumSize(FIELD_SIZE);
        passwordField.setPreferredSize(FIELD_SIZE);
        passwordField.setMaximumSize(FIELD_SIZE);
        loginButton.setPreferredSize(BUTTON_SIZE);
        loginButton.setMaximumSize(BUTTON_SIZE);
        
        emailField.setFont(UIStyles.ARIAL_PLAIN_14);
        emailField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.FIELD_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));

        passwordField.setFont(UIStyles.ARIAL_PLAIN_14);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.FIELD_BORDER, 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
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

        JPanel leftPanel = new JPanel(new MigLayout("center, flowy", "[grow]", "push[]10[]push"));
        leftPanel.setBackground(UIStyles.DARK_BLUE);
        leftPanel.setPreferredSize(new Dimension(UIStyles.AUTH_LEFT_PANEL_WIDTH, 0));
        
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
        
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);
        
        JPanel formPanel = new JPanel(new MigLayout("wrap 1,fillx", "[grow,fill]", ""));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        formPanel.setMaximumSize(new Dimension(400, Integer.MAX_VALUE));
        
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
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UIStyles.ARIAL_PLAIN_14);
        passwordLabel.setForeground(UIStyles.TEXT_MEDIUM);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordLabel.setBorder(new EmptyBorder(0, 0, 8, 0));
        
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        registerPanel.setBackground(Color.WHITE);
        registerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
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
        
        formPanel.add(titleLabel, "align left");
        formPanel.add(emailLabel, "align left, gaptop 20");
        formPanel.add(emailField, "growx");
        formPanel.add(passwordLabel, "align left, gaptop 20");
        formPanel.add(passwordField, "growx");
        formPanel.add(loginButton, "gaptop 30, growx");
        formPanel.add(registerPanel, "gaptop 20, align left");
        
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
