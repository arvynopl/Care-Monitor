//File: src/main/java/com/caremonitor/view/RegisterView.java
package com.caremonitor.view;

import com.caremonitor.controller.AuthController;
import com.caremonitor.controller.PatientController;
import com.caremonitor.model.Patient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder; // Import LineBorder
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import com.caremonitor.view.theme.UIStyles;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterView extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JPasswordField passwordField; // Tetap JPasswordField
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

    private final Dimension FIELD_SIZE = new Dimension(350, 40); // Ukuran field yang konsisten
    private final Dimension BUTTON_SIZE = new Dimension(350, 45); // Ukuran tombol yang konsisten
    private final Dimension CODE_FIELD_SIZE = new Dimension(150, 30); // Ukuran field kode pasien

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
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Membuat window fullscreen

        nameField = new PlaceholderTextField("Masukkan nama lengkap Anda"); // Pass placeholder to constructor
        emailField = new PlaceholderTextField("contoh@email.com");
        // PERBAIKAN DI SINI: Gunakan PlaceholderPasswordField untuk passwordField
        passwordField = new PlaceholderPasswordField("Minimal 8 karakter");
        contactField = new PlaceholderTextField("Nomor telepon atau kontak lainnya");
        roleComboBox = new JComboBox<>(new String[]{"-- Pilih Peran --", "Caregiver", "Family"}); // Mengubah teks
        continueButton = new JButton("Lanjut Pendaftaran");

        specializationField = new PlaceholderTextField("Contoh: Dokter Umum, Perawat");
        relationshipField = new PlaceholderTextField("Contoh: Orang Tua, Pasangan, Anak");
        registerButton = new JButton("Daftar Sekarang"); // Mengubah teks
        backButton = new JButton("Kembali"); // Mengubah teks

        // Menerapkan ukuran konsisten
        nameField.setPreferredSize(FIELD_SIZE);
        emailField.setPreferredSize(FIELD_SIZE);
        passwordField.setPreferredSize(FIELD_SIZE);
        contactField.setPreferredSize(FIELD_SIZE);
        roleComboBox.setPreferredSize(FIELD_SIZE);
        specializationField.setPreferredSize(FIELD_SIZE);
        relationshipField.setPreferredSize(FIELD_SIZE);

        continueButton.setPreferredSize(BUTTON_SIZE);
        continueButton.setBackground(UIStyles.DARK_BLUE);
        continueButton.setFont(UIStyles.ARIAL_BOLD_14);
        continueButton.setFocusPainted(false);
        continueButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyles.DARK_BLUE, 1),
                new EmptyBorder(10, 20, 10, 20))); // Padding internal
        continueButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        registerButton.setPreferredSize(BUTTON_SIZE);
        registerButton.setBackground(UIStyles.DARK_BLUE);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(UIStyles.ARIAL_BOLD_14);
        registerButton.setFocusPainted(false);
        registerButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIStyles.DARK_BLUE, 1),
                new EmptyBorder(10, 20, 10, 20))); // Padding internal
        registerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.setPreferredSize(new Dimension(120, 40)); // Ukuran sedikit lebih besar
        backButton.setBackground(UIStyles.LIGHT_GRAY_220); // Warna abu-abu yang lebih terang
        backButton.setForeground(Color.BLACK);
        backButton.setFont(UIStyles.ARIAL_PLAIN_14);
        backButton.setFocusPainted(false);
        backButton.setBorder(BorderFactory.createLineBorder(UIStyles.BORDER_GRAY, 1)); // Border tipis
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE); // Background panel utama

        // Left Panel (Logo & Title)
        JPanel leftPanel = new JPanel(new GridBagLayout()); // Menggunakan GridBagLayout untuk pemusatan
        leftPanel.setBackground(UIStyles.DARK_BLUE);
        leftPanel.setPreferredSize(new Dimension(500, 0)); // Lebar tetap 500

        JPanel logoContentPanel = new JPanel(); // Panel untuk menampung logo dan subtitle
        logoContentPanel.setBackground(UIStyles.DARK_BLUE);
        logoContentPanel.setLayout(new BoxLayout(logoContentPanel, BoxLayout.Y_AXIS));
        logoContentPanel.setBorder(new EmptyBorder(50, 50, 50, 50)); // Padding internal

        JLabel logoLabel = new JLabel("Care Monitor");
        logoLabel.setFont(UIStyles.ARIAL_BOLD_36); // Ukuran font lebih besar
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Health Monitoring System");
        subtitleLabel.setFont(UIStyles.ARIAL_PLAIN_18); // Ukuran font lebih besar
        subtitleLabel.setForeground(UIStyles.SUBTITLE_GRAY); // Warna sedikit abu-abu untuk subtitle
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoContentPanel.add(Box.createVerticalGlue()); // Push content to center vertically
        logoContentPanel.add(logoLabel);
        logoContentPanel.add(Box.createRigidArea(new Dimension(0, 15))); // Jarak lebih besar
        logoContentPanel.add(subtitleLabel);
        logoContentPanel.add(Box.createVerticalGlue()); // Push content to center vertically

        leftPanel.add(logoContentPanel); // Tambahkan logoContentPanel ke leftPanel yang ber-GridBagLayout

        // Right Panel (Forms)
        JPanel rightPanel = new JPanel(new BorderLayout()); // Gunakan BorderLayout untuk mainFormPanel
        rightPanel.setBackground(Color.WHITE);

        cardLayout = new CardLayout();
        mainFormPanel = new JPanel(cardLayout);
        mainFormPanel.setBackground(Color.WHITE);

        createStep1Panel();
        createStep2Panel(); // createStep2Panel hanya membuat header, konten akan ditambahkan di setupCaregiver/FamilyStep2

        mainFormPanel.add(step1Panel, "step1");
        mainFormPanel.add(step2Panel, "step2");

        rightPanel.add(mainFormPanel, BorderLayout.CENTER); // mainFormPanel akan mengambil seluruh ruang di rightPanel

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        setContentPane(mainPanel);
    }

    private void createStep1Panel() {
        step1Panel = new JPanel(new GridBagLayout()); // Tetap GridBagLayout untuk centering
        step1Panel.setBackground(Color.WHITE);
        step1Panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50)); // Padding di sekitar form

        JPanel formContentPanel = new JPanel(); // Panel baru untuk menampung semua input form
        formContentPanel.setLayout(new BoxLayout(formContentPanel, BoxLayout.Y_AXIS));
        formContentPanel.setBackground(Color.WHITE);
        formContentPanel.setMaximumSize(new Dimension(FIELD_SIZE.width + 100, Integer.MAX_VALUE)); // Lebar maksimum untuk form

        JLabel titleLabel = new JLabel("Daftar Akun Baru"); // Mengubah teks
        titleLabel.setFont(UIStyles.ARIAL_BOLD_32); // Ukuran font lebih besar
        titleLabel.setForeground(UIStyles.DARK_BLUE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align left

        formContentPanel.add(titleLabel);
        formContentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Jarak dari judul

        // Helper method for adding labeled fields
        addFormField(formContentPanel, "Nama Lengkap", nameField);
        addFormField(formContentPanel, "Email", emailField);
        addFormField(formContentPanel, "Password", passwordField); // PasswordField sekarang juga Placeholder
        addFormField(formContentPanel, "Kontak", contactField);

        // Role ComboBox
        JLabel roleLabel = new JLabel("Pilih Peran Anda"); // Mengubah teks
        roleLabel.setFont(UIStyles.ARIAL_PLAIN_14);
        roleLabel.setForeground(UIStyles.TEXT_MEDIUM);
        roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        formContentPanel.add(roleLabel);
        formContentPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        roleComboBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        roleComboBox.setMaximumSize(FIELD_SIZE); // Menggunakan FIELD_SIZE
        formContentPanel.add(roleComboBox);
        formContentPanel.add(Box.createRigidArea(new Dimension(0, 30))); // Jarak setelah role

        // Continue Button
        continueButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        continueButton.setMaximumSize(BUTTON_SIZE); // Menggunakan BUTTON_SIZE
        formContentPanel.add(continueButton);
        formContentPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Login Link Panel
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // Align left
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // Penting untuk BoxLayout Y_AXIS
        loginPanel.setMaximumSize(new Dimension(FIELD_SIZE.width, 30)); // Batasi lebar
        JLabel alreadyHaveLabel = new JLabel("Sudah punya akun? "); // Mengubah teks
        alreadyHaveLabel.setFont(UIStyles.ARIAL_PLAIN_14);
        alreadyHaveLabel.setForeground(UIStyles.TEXT_LIGHT);
        loginPanel.add(alreadyHaveLabel);

        JButton logInLink = new JButton("Masuk"); // Mengubah teks
        logInLink.setBorderPainted(false);
        logInLink.setContentAreaFilled(false);
        logInLink.setForeground(UIStyles.LIGHT_BLUE);
        logInLink.setFont(UIStyles.ARIAL_PLAIN_14);
        logInLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logInLink.setFocusPainted(false);
        logInLink.addActionListener(e -> openLoginView());
        loginPanel.add(logInLink);

        formContentPanel.add(loginPanel);

        // Add formContentPanel to step1Panel, centered
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        step1Panel.add(formContentPanel, gbc);
    }

    // Helper method to add a label and a field with consistent styling
    private void addFormField(JPanel parentPanel, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(UIStyles.ARIAL_PLAIN_14);
        label.setForeground(UIStyles.TEXT_MEDIUM);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        parentPanel.add(label);
        parentPanel.add(Box.createRigidArea(new Dimension(0, 5))); // Small space between label and field
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(FIELD_SIZE); // Menggunakan FIELD_SIZE
        parentPanel.add(field);
        parentPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Space after field
    }


    private void createStep2Panel() {
        step2Panel = new JPanel(new BorderLayout());
        step2Panel.setBackground(Color.WHITE);
        step2Panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.add(backButton, BorderLayout.WEST);

        JLabel step2Title = new JLabel("Lengkapi Pendaftaran", SwingConstants.CENTER); // Mengubah teks
        step2Title.setFont(UIStyles.ARIAL_BOLD_28); // Ukuran font lebih besar
        step2Title.setForeground(UIStyles.DARK_BLUE);
        headerPanel.add(step2Title, BorderLayout.CENTER);

        step2Panel.add(headerPanel, BorderLayout.NORTH);
    }

    private void setupCaregiverStep2() {
        if (step2Panel.getComponentCount() > 1) {
            step2Panel.remove(1); // Remove the old content panel (BorderLayout.CENTER)
        }

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        GridBagConstraints gbcContent = new GridBagConstraints();
        gbcContent.insets = new Insets(5, 0, 5, 0);
        gbcContent.anchor = GridBagConstraints.WEST;
        gbcContent.fill = GridBagConstraints.HORIZONTAL;

        // Specialization Field
        JPanel specPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        specPanel.setBackground(Color.WHITE);
        JLabel specLabel = new JLabel("Spesialisasi:");
        specLabel.setFont(UIStyles.ARIAL_PLAIN_14);
        specLabel.setForeground(UIStyles.TEXT_MEDIUM);
        specPanel.add(specLabel);
        specPanel.add(Box.createRigidArea(new Dimension(5,0))); // Add small space
        specializationField.setPreferredSize(FIELD_SIZE); // Ensure consistent size
        specPanel.add(specializationField);
        gbcContent.gridx = 0;
        gbcContent.gridy = 0;
        contentPanel.add(specPanel, gbcContent);

        // Patients Label
        JLabel patientsLabel = new JLabel("Pasien yang Ditangani:"); // Mengubah teks
        patientsLabel.setFont(UIStyles.ARIAL_BOLD_16);
        gbcContent.gridy = 1;
        gbcContent.insets = new Insets(20, 0, 10, 0);
        contentPanel.add(patientsLabel, gbcContent);

        // Patient Checkboxes Panel
        caregiverPatientPanel = new JPanel();
        caregiverPatientPanel.setLayout(new BoxLayout(caregiverPatientPanel, BoxLayout.Y_AXIS));
        caregiverPatientPanel.setBackground(Color.WHITE);
        caregiverPatientPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        caregiverPatientCheckboxes.clear();
        caregiverPatientCodes.clear();

        List<Patient> unassignedPatients = patientController.getUnassignedPatients();

        if (unassignedPatients.isEmpty()) {
            caregiverPatientPanel.add(new JLabel("Tidak ada pasien yang belum ditangani.")); // Mengubah teks
        } else {
            for (Patient patient : unassignedPatients) {
                JPanel patientRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                patientRowPanel.setBackground(Color.WHITE);

                JCheckBox checkbox = new JCheckBox(patient.getId() + " - " + patient.getName());
                checkbox.setFont(UIStyles.ARIAL_PLAIN_14);
                caregiverPatientCheckboxes.add(checkbox);

                PlaceholderTextField codeField = new PlaceholderTextField(); // Ini sudah benar
                codeField.setEnabled(false);
                codeField.setPlaceholderText("Kode Unik Pasien");
                codeField.setPreferredSize(CODE_FIELD_SIZE);
                caregiverPatientCodes.put(checkbox, codeField);

                patientRowPanel.add(checkbox);
                JLabel codeLabel = new JLabel("Kode:");
                codeLabel.setFont(UIStyles.ARIAL_PLAIN_14);
                patientRowPanel.add(codeLabel);
                patientRowPanel.add(codeField);

                caregiverPatientPanel.add(patientRowPanel);

                checkbox.addActionListener(e -> codeField.setEnabled(checkbox.isSelected()));
            }
        }

        JScrollPane scrollPane = new JScrollPane(caregiverPatientPanel);
        scrollPane.setPreferredSize(new Dimension(FIELD_SIZE.width + 100, 250)); // Lebar scrollPane sesuai form
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER_GRAY),
                "Pilih Pasien",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIStyles.ARIAL_BOLD_14,
                UIStyles.TEXT_MEDIUM)); // Border dengan judul lebih modern
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        gbcContent.gridy = 2;
        gbcContent.weighty = 1.0;
        gbcContent.fill = GridBagConstraints.BOTH;
        contentPanel.add(scrollPane, gbcContent);

        // Register Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(registerButton);
        gbcContent.gridy = 3;
        gbcContent.weighty = 0;
        gbcContent.insets = new Insets(30, 0, 0, 0);
        gbcContent.fill = GridBagConstraints.NONE;
        gbcContent.anchor = GridBagConstraints.CENTER;
        contentPanel.add(buttonPanel, gbcContent);

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.add(contentPanel);
        step2Panel.add(wrapperPanel, BorderLayout.CENTER);

        step2Panel.revalidate();
        step2Panel.repaint();
    }

    private void setupFamilyStep2() {
        if (step2Panel.getComponentCount() > 1) {
            step2Panel.remove(1); // Remove the old content panel (BorderLayout.CENTER)
        }

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(new EmptyBorder(30, 0, 0, 0));

        GridBagConstraints gbcContent = new GridBagConstraints();
        gbcContent.insets = new Insets(5, 0, 5, 0);
        gbcContent.anchor = GridBagConstraints.WEST;
        gbcContent.fill = GridBagConstraints.HORIZONTAL;

        // Relationship Field
        JPanel relationPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        relationPanel.setBackground(Color.WHITE);
        JLabel relationLabel = new JLabel("Hubungan dengan Pasien:"); // Mengubah teks
        relationLabel.setFont(UIStyles.ARIAL_PLAIN_14);
        relationLabel.setForeground(UIStyles.TEXT_MEDIUM);
        relationPanel.add(relationLabel);
        relationPanel.add(Box.createRigidArea(new Dimension(5,0))); // Add small space
        relationshipField.setPreferredSize(FIELD_SIZE); // Ensure consistent size
        relationPanel.add(relationshipField);
        gbcContent.gridx = 0;
        gbcContent.gridy = 0;
        contentPanel.add(relationPanel, gbcContent);

        // Patients Label
        JLabel patientsLabel = new JLabel("Pasien Terkait:");
        patientsLabel.setFont(UIStyles.ARIAL_BOLD_16);
        gbcContent.gridy = 1;
        gbcContent.insets = new Insets(20, 0, 10, 0);
        contentPanel.add(patientsLabel, gbcContent);

        // Patient Checkboxes Panel
        familyPatientPanel = new JPanel();
        familyPatientPanel.setLayout(new BoxLayout(familyPatientPanel, BoxLayout.Y_AXIS));
        familyPatientPanel.setBackground(Color.WHITE);
        familyPatientPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        familyPatientCheckboxes.clear();
        familyPatientCodes.clear();

        if (allPatients.isEmpty()) {
            familyPatientPanel.add(new JLabel("Tidak ada pasien tersedia.")); // Mengubah teks
        } else {
            for (Patient patient : allPatients) {
                JPanel patientRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
                patientRowPanel.setBackground(Color.WHITE);

                JCheckBox checkbox = new JCheckBox(patient.getId() + " - " + patient.getName());
                checkbox.setFont(UIStyles.ARIAL_PLAIN_14);
                familyPatientCheckboxes.add(checkbox);

                PlaceholderTextField codeField = new PlaceholderTextField(); // Ini sudah benar
                codeField.setEnabled(false);
                codeField.setPlaceholderText("Kode Unik Pasien");
                codeField.setPreferredSize(CODE_FIELD_SIZE);
                familyPatientCodes.put(checkbox, codeField);

                patientRowPanel.add(checkbox);
                JLabel codeLabel = new JLabel("Kode:");
                codeLabel.setFont(UIStyles.ARIAL_PLAIN_14);
                patientRowPanel.add(codeLabel);
                patientRowPanel.add(codeField);

                familyPatientPanel.add(patientRowPanel);

                checkbox.addActionListener(e -> codeField.setEnabled(checkbox.isSelected()));
            }
        }

        JScrollPane scrollPane = new JScrollPane(familyPatientPanel);
        scrollPane.setPreferredSize(new Dimension(FIELD_SIZE.width + 100, 250)); // Lebar scrollPane sesuai form
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(UIStyles.BORDER_GRAY),
                "Pilih Pasien",
                TitledBorder.LEFT, TitledBorder.TOP,
                UIStyles.ARIAL_BOLD_14,
                UIStyles.TEXT_MEDIUM)); // Border dengan judul lebih modern
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        gbcContent.gridy = 2;
        gbcContent.weighty = 1.0;
        gbcContent.fill = GridBagConstraints.BOTH;
        contentPanel.add(scrollPane, gbcContent);

        // Register Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(registerButton);
        gbcContent.gridy = 3;
        gbcContent.weighty = 0;
        gbcContent.insets = new Insets(30, 0, 0, 0);
        gbcContent.fill = GridBagConstraints.NONE;
        gbcContent.anchor = GridBagConstraints.CENTER;
        contentPanel.add(buttonPanel, gbcContent);

        JPanel wrapperPanel = new JPanel(new GridBagLayout());
        wrapperPanel.setBackground(Color.WHITE);
        wrapperPanel.add(contentPanel);
        step2Panel.add(wrapperPanel, BorderLayout.CENTER);

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
                "Mohon lengkapi semua kolom yang wajib diisi.", // Mengubah teks
                "Kesalahan Validasi", // Mengubah teks
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
                    "Mohon masukkan spesialisasi.", // Mengubah teks
                    "Kesalahan Validasi", // Mengubah teks
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
                            "Mohon masukkan kode pasien untuk pasien yang dipilih.", // Mengubah teks
                            "Kesalahan Validasi", // Mengubah teks
                            JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }

            if (!hasSelectedPatient) {
                JOptionPane.showMessageDialog(this,
                    "Mohon pilih setidaknya satu pasien.", // Mengubah teks
                    "Kesalahan Validasi", // Mengubah teks
                    JOptionPane.ERROR_MESSAGE);
                return false;
            }

        } else if ("Family".equals(selectedRole)) {
            if (relationshipField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Mohon masukkan hubungan Anda dengan pasien.", // Mengubah teks
                    "Kesalahan Validasi", // Mengubah teks
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
                            "Mohon masukkan kode pasien untuk pasien yang dipilih.", // Mengubah teks
                            "Kesalahan Validasi", // Mengubah teks
                            JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                }
            }

            if (!hasSelectedPatient) {
                JOptionPane.showMessageDialog(this,
                    "Mohon pilih setidaknya satu pasien.", // Mengubah teks
                    "Kesalahan Validasi", // Mengubah teks
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
                            "Kode pasien tidak valid untuk: " + checkboxText, // Mengubah teks
                            "Kesalahan Pendaftaran", // Mengubah teks
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
                            "Kode pasien tidak valid untuk: " + checkboxText, // Mengubah teks
                            "Kesalahan Pendaftaran", // Mengubah teks
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
                "Pendaftaran berhasil! Silakan masuk.", // Mengubah teks
                "Pendaftaran Berhasil", // Mengubah teks
                JOptionPane.INFORMATION_MESSAGE);
            openLoginView();
        } else {
            JOptionPane.showMessageDialog(this,
                "Pendaftaran gagal. Mohon coba lagi.", // Mengubah teks
                "Kesalahan Pendaftaran", // Mengubah teks
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openLoginView() {
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
        this.dispose();
    }

    // Class untuk JTextField dengan placeholder
    private static class PlaceholderTextField extends JTextField {
        private String placeholder;

        public PlaceholderTextField() {
            this(null); // Call constructor with null placeholder
        }

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(UIStyles.SUBTITLE_GRAY, 1), // Border abu-abu tipis
                    new EmptyBorder(10, 10, 10, 10))); // Padding internal
            setFont(UIStyles.ARIAL_PLAIN_14);
            setOpaque(false); // Make it non-opaque so background can be drawn
        }

        public void setPlaceholderText(String placeholder) {
            this.placeholder = placeholder;
            repaint(); // Repaint to show placeholder immediately if text is empty
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

    // KELAS BARU UNTUK JPasswordField dengan placeholder
    private static class PlaceholderPasswordField extends JPasswordField {
        private String placeholder;

        public PlaceholderPasswordField() {
            this(null);
        }

        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(UIStyles.SUBTITLE_GRAY, 1), // Border abu-abu tipis
                    new EmptyBorder(10, 10, 10, 10))); // Padding internal
            setFont(UIStyles.ARIAL_PLAIN_14);
            setEchoChar('•'); // Karakter standar untuk password
            setOpaque(false); // Make it non-opaque so background can be drawn
        }

        public void setPlaceholderText(String placeholder) {
            this.placeholder = placeholder;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Only draw placeholder if document is empty AND component is not focused
            if (placeholder != null && getPassword().length == 0 && !hasFocus()) {
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