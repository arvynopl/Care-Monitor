// File: src/main/java/com/caremonitor/view/RegisterView.java
package com.caremonitor.view;

import com.caremonitor.controller.AuthController;
import com.caremonitor.controller.PatientController;
import com.caremonitor.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
// import java.awt.event.ItemEvent;
// import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterView extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JTextField contactField;
    private JComboBox<String> roleComboBox;
    private JButton continueButton;
    
    private JTextField specializationField;
    private JPanel caregiverPatientPanel;
    private List<JCheckBox> caregiverPatientCheckboxes;
    private Map<JCheckBox, JTextField> caregiverPatientCodes;
    
    private JTextField relationshipField;
    private JPanel familyPatientPanel;
    private List<JCheckBox> familyPatientCheckboxes;
    private Map<JCheckBox, JTextField> familyPatientCodes;
    
    private JButton registerButton;
    private JButton backButton;
    
    private AuthController authController;
    private PatientController patientController;
    private List<Patient> allPatients;
    
    private JPanel step1Panel;
    private JPanel step2Panel;
    private CardLayout cardLayout;
    private JPanel mainFormPanel;
    
    private final Color DARK_BLUE = new Color(0, 32, 96);
    private final Color LIGHT_BLUE = new Color(59, 130, 246);
    private final Dimension CODE_FIELD_SIZE = new Dimension(150, 25);
    
    public RegisterView() {
        authController = new AuthController(null);
        patientController = new PatientController();
        allPatients = patientController.getAllPatients();
        caregiverPatientCheckboxes = new ArrayList<>();
        familyPatientCheckboxes = new ArrayList<>();
        caregiverPatientCodes = new HashMap<>();
        familyPatientCodes = new HashMap<>();
        
        initializeComponents();
        setupLayout();
        setupActions();
    }

    private void initializeComponents() {
        setTitle("Care Monitor - Sign Up");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        nameField = new PlaceholderTextField();
        emailField = new PlaceholderTextField();
        passwordField = new JPasswordField();
        contactField = new PlaceholderTextField();
        roleComboBox = new JComboBox<>(new String[]{"-- Select Role --", "Caregiver", "Family"});
        continueButton = new JButton("Lanjut Pendaftaran");
        
        specializationField = new PlaceholderTextField();
        relationshipField = new PlaceholderTextField();
        registerButton = new JButton("Sign Up");
        backButton = new JButton("Back");
        
        Dimension fieldSize = new Dimension(350, 40);
        nameField.setPreferredSize(fieldSize);
        emailField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);
        contactField.setPreferredSize(fieldSize);
        roleComboBox.setPreferredSize(fieldSize);
        specializationField.setPreferredSize(fieldSize);
        relationshipField.setPreferredSize(fieldSize);
        
        continueButton.setPreferredSize(new Dimension(350, 45));
        continueButton.setBackground(DARK_BLUE);
        continueButton.setForeground(Color.WHITE);
        continueButton.setFont(new Font("Arial", Font.BOLD, 14));
        continueButton.setFocusPainted(false);
        continueButton.setBorderPainted(false);
        
        registerButton.setPreferredSize(new Dimension(350, 45));
        registerButton.setBackground(DARK_BLUE);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setBorderPainted(false);
        
        backButton.setPreferredSize(new Dimension(100, 35));
        backButton.setBackground(Color.LIGHT_GRAY);
        backButton.setForeground(Color.BLACK);
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setBackground(DARK_BLUE);
        leftPanel.setPreferredSize(new Dimension(500, 0));
        
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(DARK_BLUE);
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        
        JLabel logoLabel = new JLabel("Care Monitor");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Health Monitoring System");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.WHITE);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        logoPanel.add(logoLabel);
        logoPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        logoPanel.add(subtitleLabel);
        
        leftPanel.add(logoPanel);
        
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        
        cardLayout = new CardLayout();
        mainFormPanel = new JPanel(cardLayout);
        mainFormPanel.setBackground(Color.WHITE);
        
        createStep1Panel();
        createStep2Panel();
        
        mainFormPanel.add(step1Panel, "step1");
        mainFormPanel.add(step2Panel, "step2");
        
        rightPanel.add(mainFormPanel, BorderLayout.CENTER);
        
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private void createStep1Panel() {
        step1Panel = new JPanel(new GridBagLayout());
        step1Panel.setBackground(Color.WHITE);
        step1Panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel titleLabel = new JLabel("Sign Up");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(DARK_BLUE);
        gbc.gridx = 0; gbc.gridy = 0;
        step1Panel.add(titleLabel, gbc);
        
        JLabel nameLabel = new JLabel("Nama Lengkap");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 1; gbc.anchor = GridBagConstraints.WEST;
        step1Panel.add(nameLabel, gbc);
        
        gbc.gridy = 2; gbc.anchor = GridBagConstraints.CENTER;
        step1Panel.add(nameField, gbc);
        
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        step1Panel.add(emailLabel, gbc);
        
        gbc.gridy = 4; gbc.anchor = GridBagConstraints.CENTER;
        step1Panel.add(emailField, gbc);
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 5; gbc.anchor = GridBagConstraints.WEST;
        step1Panel.add(passwordLabel, gbc);
        
        gbc.gridy = 6; gbc.anchor = GridBagConstraints.CENTER;
        step1Panel.add(passwordField, gbc);
        
        JLabel contactLabel = new JLabel("Kontak");
        contactLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 7; gbc.anchor = GridBagConstraints.WEST;
        step1Panel.add(contactLabel, gbc);
        
        gbc.gridy = 8; gbc.anchor = GridBagConstraints.CENTER;
        step1Panel.add(contactField, gbc);
        
        JLabel roleLabel = new JLabel("Role");
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        gbc.gridy = 9; gbc.anchor = GridBagConstraints.WEST;
        step1Panel.add(roleLabel, gbc);
        
        gbc.gridy = 10; gbc.anchor = GridBagConstraints.CENTER;
        step1Panel.add(roleComboBox, gbc);
        
        gbc.gridy = 11; gbc.insets = new Insets(30, 0, 10, 0);
        step1Panel.add(continueButton, gbc);
        
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginPanel.setBackground(Color.WHITE);
        loginPanel.add(new JLabel("Already have account?"));
        JButton logInLink = new JButton("Log in");
        logInLink.setBorderPainted(false);
        logInLink.setContentAreaFilled(false);
        logInLink.setForeground(LIGHT_BLUE);
        logInLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logInLink.addActionListener(e -> openLoginView());
        loginPanel.add(logInLink);
        
        gbc.gridy = 12; gbc.insets = new Insets(10, 0, 0, 0);
        step1Panel.add(loginPanel, gbc);
    }
    
    private void createStep2Panel() {
        step2Panel = new JPanel(new BorderLayout());
        step2Panel.setBackground(Color.WHITE);
        step2Panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(backButton, BorderLayout.WEST);
        
        JLabel step2Title = new JLabel("Complete Registration", SwingConstants.CENTER);
        step2Title.setFont(new Font("Arial", Font.BOLD, 24));
        step2Title.setForeground(DARK_BLUE);
        headerPanel.add(step2Title, BorderLayout.CENTER);
        
        step2Panel.add(headerPanel, BorderLayout.NORTH);
        
    }
    
    private void setupCaregiverStep2() {
        if (step2Panel.getComponentCount() > 1) {
            step2Panel.remove(1);
        }
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        JPanel specPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        specPanel.setBackground(Color.WHITE);
        specPanel.add(new JLabel("Spesialisasi: "));
        specPanel.add(specializationField);
        contentPanel.add(specPanel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel patientsLabel = new JLabel("Pasien Ditangani:");
        patientsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contentPanel.add(patientsLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        caregiverPatientPanel = new JPanel();
        caregiverPatientPanel.setLayout(new BoxLayout(caregiverPatientPanel, BoxLayout.Y_AXIS));
        caregiverPatientPanel.setBackground(Color.WHITE);
        
        caregiverPatientCheckboxes.clear();
        caregiverPatientCodes.clear();
        
        for (Patient patient : allPatients) {
            JPanel patientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            patientPanel.setBackground(Color.WHITE);
            
            JCheckBox checkbox = new JCheckBox(patient.getId() + " - " + patient.getName());
            caregiverPatientCheckboxes.add(checkbox);
            
            PlaceholderTextField codeField = new PlaceholderTextField();
            codeField.setEnabled(false);
            codeField.setPlaceholderText("Kode Unik Pasien");
            codeField.setPreferredSize(CODE_FIELD_SIZE);
            caregiverPatientCodes.put(checkbox, codeField);
            
            patientPanel.add(checkbox);
            patientPanel.add(new JLabel("Kode: "));
            patientPanel.add(codeField);
            
            caregiverPatientPanel.add(patientPanel);
            
            checkbox.addActionListener(e -> codeField.setEnabled(checkbox.isSelected()));
        }
        
        JScrollPane scrollPane = new JScrollPane(caregiverPatientPanel);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Pilih Pasien"));
        contentPanel.add(scrollPane);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(registerButton);
        contentPanel.add(buttonPanel);
        
        step2Panel.add(contentPanel, BorderLayout.CENTER);
        step2Panel.revalidate();
        step2Panel.repaint();
    }
    
    private void setupFamilyStep2() {
        if (step2Panel.getComponentCount() > 1) {
            step2Panel.remove(1);
        }
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        
        JPanel relationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        relationPanel.setBackground(Color.WHITE);
        relationPanel.add(new JLabel("Hubungan dengan Pasien: "));
        relationPanel.add(relationshipField);
        contentPanel.add(relationPanel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        JLabel patientsLabel = new JLabel("Pasien Terkait:");
        patientsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        contentPanel.add(patientsLabel);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        familyPatientPanel = new JPanel();
        familyPatientPanel.setLayout(new BoxLayout(familyPatientPanel, BoxLayout.Y_AXIS));
        familyPatientPanel.setBackground(Color.WHITE);
        
        familyPatientCheckboxes.clear();
        familyPatientCodes.clear();
        
        for (Patient patient : allPatients) {
            JPanel patientPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            patientPanel.setBackground(Color.WHITE);
            
            JCheckBox checkbox = new JCheckBox(patient.getId() + " - " + patient.getName());
            familyPatientCheckboxes.add(checkbox);
            
            PlaceholderTextField codeField = new PlaceholderTextField();
            codeField.setEnabled(false);
            codeField.setPlaceholderText("Kode Unik Pasien");
            codeField.setPreferredSize(CODE_FIELD_SIZE);
            familyPatientCodes.put(checkbox, codeField);
            
            patientPanel.add(checkbox);
            patientPanel.add(new JLabel("Kode: "));
            patientPanel.add(codeField);
            
            familyPatientPanel.add(patientPanel);
            
            checkbox.addActionListener(e -> codeField.setEnabled(checkbox.isSelected()));
        }
        
        JScrollPane scrollPane = new JScrollPane(familyPatientPanel);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        scrollPane.setBorder(BorderFactory.createTitledBorder("Pilih Pasien"));
        contentPanel.add(scrollPane);
        
        contentPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(registerButton);
        contentPanel.add(buttonPanel);
        
        step2Panel.add(contentPanel, BorderLayout.CENTER);
        step2Panel.revalidate();
        step2Panel.repaint();
    }

    private void setupActions() {
        continueButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateStep1()) {
                    String selectedRole = (String) roleComboBox.getSelectedItem();
                    if ("Caregiver".equals(selectedRole)) {
                        setupCaregiverStep2();
                    } else if ("Family".equals(selectedRole)) {
                        setupFamilyStep2();
                    }
                    cardLayout.show(mainFormPanel, "step2");
                }
            }
        });
        
        backButton.addActionListener(e -> cardLayout.show(mainFormPanel, "step1"));
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateStep2()) {
                    performRegistration();
                }
            }
        });
    }
    
    private boolean validateStep1() {
        if (nameField.getText().trim().isEmpty() ||
            emailField.getText().trim().isEmpty() ||
            new String(passwordField.getPassword()).trim().isEmpty() ||
            contactField.getText().trim().isEmpty() ||
            roleComboBox.getSelectedIndex() == 0) {
            
            JOptionPane.showMessageDialog(this, 
                "Please fill in all required fields", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }
    
    private boolean validateStep2() {
        String selectedRole = (String) roleComboBox.getSelectedItem();
        
        if ("Caregiver".equals(selectedRole)) {
            if (specializationField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter specialization", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            boolean hasSelectedPatient = false;
            for (JCheckBox checkbox : caregiverPatientCheckboxes) {
                if (checkbox.isSelected()) {
                    hasSelectedPatient = true;
                    JTextField codeField = caregiverPatientCodes.get(checkbox);
                    if (codeField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, 
                            "Please enter patient code for selected patients", 
                            "Validation Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }
            
            if (!hasSelectedPatient) {
                JOptionPane.showMessageDialog(this, 
                    "Please select at least one patient", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
        } else if ("Family".equals(selectedRole)) {
            if (relationshipField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Please enter relationship", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            boolean hasSelectedPatient = false;
            for (JCheckBox checkbox : familyPatientCheckboxes) {
                if (checkbox.isSelected()) {
                    hasSelectedPatient = true;
                    JTextField codeField = familyPatientCodes.get(checkbox);
                    if (codeField.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(this, 
                            "Please enter patient code for selected patients", 
                            "Validation Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }
            
            if (!hasSelectedPatient) {
                JOptionPane.showMessageDialog(this, 
                    "Please select at least one patient", 
                    "Validation Error", 
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        
        return true;
    }
    
    private void performRegistration() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String contact = contactField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();
        
        boolean success = false;
        
        if ("Caregiver".equals(role)) {
            String specialization = specializationField.getText().trim();
            
            List<Integer> selectedPatientIds = new ArrayList<>();
            List<String> patientCodes = new ArrayList<>();
            
            for (JCheckBox checkbox : caregiverPatientCheckboxes) {
                if (checkbox.isSelected()) {
                    String checkboxText = checkbox.getText();
                    int patientId = Integer.parseInt(checkboxText.substring(0, checkboxText.indexOf(" - ")));
                    String code = caregiverPatientCodes.get(checkbox).getText().trim();
                    
                    if (authController.verifyPatientCode(code) != patientId) {
                        JOptionPane.showMessageDialog(this, 
                            "Invalid patient code for: " + checkboxText, 
                            "Registration Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    selectedPatientIds.add(patientId);
                    patientCodes.add(code);
                }
            }
            
            success = authController.registerCaregiverWithPatients(
                name, email, password, contact, specialization, selectedPatientIds, patientCodes);
            
        } else if ("Family".equals(role)) {
            String relationship = relationshipField.getText().trim();
            
            List<Integer> selectedPatientIds = new ArrayList<>();
            List<String> patientCodes = new ArrayList<>();
            
            for (JCheckBox checkbox : familyPatientCheckboxes) {
                if (checkbox.isSelected()) {
                    String checkboxText = checkbox.getText();
                    int patientId = Integer.parseInt(checkboxText.substring(0, checkboxText.indexOf(" - ")));
                    String code = familyPatientCodes.get(checkbox).getText().trim();
                    
                    if (authController.verifyPatientCode(code) != patientId) {
                        JOptionPane.showMessageDialog(this, 
                            "Invalid patient code for: " + checkboxText, 
                            "Registration Error", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    selectedPatientIds.add(patientId);
                    patientCodes.add(code);
                }
            }
            
            success = authController.registerFamilyWithPatients(
                name, email, password, contact, relationship, selectedPatientIds, patientCodes);
        }
        
        if (success) {
            JOptionPane.showMessageDialog(this, 
                "Registration successful! Please log in.", 
                "Registration Success", 
                JOptionPane.INFORMATION_MESSAGE);
            openLoginView();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Registration failed. Please try again.", 
                "Registration Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void openLoginView() {
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
        this.dispose();
    }

    private static class PlaceholderTextField extends JTextField {
        private String placeholder;
        
        public void setPlaceholderText(String placeholder) {
            this.placeholder = placeholder;
        }
        
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            
            if (placeholder != null && getText().isEmpty() && !hasFocus()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.GRAY);
                g2.setFont(getFont().deriveFont(Font.ITALIC));
                FontMetrics fm = g2.getFontMetrics();
                int x = getInsets().left;
                int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }
}