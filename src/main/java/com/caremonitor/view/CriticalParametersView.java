// / File: src/main/java/com/caremonitor/view/CriticalParametersView.java
package com.caremonitor.view;

import com.caremonitor.controller.CriticalParameterController;
import com.caremonitor.controller.PatientController;
import com.caremonitor.model.CriticalParameter;
import com.caremonitor.model.Patient;
import com.caremonitor.model.User;

import javax.swing.*;
import javax.swing.JTextField;
import java.awt.*;
import com.caremonitor.view.theme.UIStyles;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class CriticalParametersView {
    private JPanel mainPanel;
    private JComboBox<Patient> patientComboBox;
    
    private JTextField minHeartRateField;
    private JTextField maxHeartRateField;
    private JTextField minBloodPressureField;
    private JTextField maxBloodPressureField;
    private JTextField minTemperatureField;
    private JTextField maxTemperatureField;
    
    private JButton saveButton;
    
    private User currentUser;
    private PatientController patientController;
    private CriticalParameterController criticalParameterController;
    
    
    public CriticalParametersView(User user) {
        this.currentUser = user;
        this.patientController = new PatientController();
        this.criticalParameterController = new CriticalParameterController();
        
        initializeComponents();
        setupLayout();
        loadPatients();
    }
    
    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        JLabel titleLabel = new JLabel("Critical Parameters");
        titleLabel.setFont(UIStyles.ARIAL_BOLD_24);
        titleLabel.setForeground(UIStyles.DARK_BLUE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JLabel patientLabel = new JLabel("Patient:");
        patientLabel.setFont(UIStyles.ARIAL_BOLD_16);
        patientComboBox = new JComboBox<>();
        patientComboBox.setPreferredSize(new Dimension(300, 35));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        formPanel.add(patientLabel, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        formPanel.add(patientComboBox, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        
        gbc.gridx = 0;
        JLabel parameterLabel = new JLabel("Parameter");
        parameterLabel.setFont(UIStyles.ARIAL_BOLD_16);
        formPanel.add(parameterLabel, gbc);
        
        gbc.gridx = 1;
        JLabel minLabel = new JLabel("Minimum");
        minLabel.setFont(UIStyles.ARIAL_BOLD_16);
        minLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(minLabel, gbc);
        
        gbc.gridx = 2;
        JLabel maxLabel = new JLabel("Maximum");
        maxLabel.setFont(UIStyles.ARIAL_BOLD_16);
        maxLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(maxLabel, gbc);
        
        gbc.gridx = 3;
        JLabel unitLabel = new JLabel("Unit");
        unitLabel.setFont(UIStyles.ARIAL_BOLD_16);
        unitLabel.setHorizontalAlignment(SwingConstants.CENTER);
        formPanel.add(unitLabel, gbc);
        
        gbc.gridy = 2;
        gbc.gridx = 0;
        JLabel heartRateLabel = new JLabel("Heart Rate:");
        heartRateLabel.setFont(UIStyles.ARIAL_BOLD_16);
        formPanel.add(heartRateLabel, gbc);
        
        gbc.gridx = 1;
        minHeartRateField = new JTextField(10);
        minHeartRateField.setPreferredSize(new Dimension(120, 35));
        formPanel.add(minHeartRateField, gbc);
        
        gbc.gridx = 2;
        maxHeartRateField = new JTextField(10);
        maxHeartRateField.setPreferredSize(new Dimension(120, 35));
        formPanel.add(maxHeartRateField, gbc);
        
        gbc.gridx = 3;
        JLabel bpmLabel = new JLabel("bpm");
        bpmLabel.setFont(UIStyles.ARIAL_BOLD_16);
        formPanel.add(bpmLabel, gbc);
        
        gbc.gridy = 3;
        gbc.gridx = 0;
        JLabel bloodPressureLabel = new JLabel("Blood Pressure:");
        bloodPressureLabel.setFont(UIStyles.ARIAL_BOLD_16);
        formPanel.add(bloodPressureLabel, gbc);
        
        gbc.gridx = 1;
        minBloodPressureField = new JTextField(10);
        minBloodPressureField.setPreferredSize(new Dimension(120, 35));
        formPanel.add(minBloodPressureField, gbc);
        
        gbc.gridx = 2;
        maxBloodPressureField = new JTextField(10);
        maxBloodPressureField.setPreferredSize(new Dimension(120, 35));
        formPanel.add(maxBloodPressureField, gbc);
        
        gbc.gridx = 3;
        JLabel mmHgLabel = new JLabel("mmHg");
        mmHgLabel.setFont(UIStyles.ARIAL_BOLD_16);
        formPanel.add(mmHgLabel, gbc);
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        JLabel temperatureLabel = new JLabel("Temperature:");
        temperatureLabel.setFont(UIStyles.ARIAL_BOLD_16);
        formPanel.add(temperatureLabel, gbc);
        
        gbc.gridx = 1;
        minTemperatureField = new JTextField(10);
        minTemperatureField.setPreferredSize(new Dimension(120, 35));
        formPanel.add(minTemperatureField, gbc);
        
        gbc.gridx = 2;
        maxTemperatureField = new JTextField(10);
        maxTemperatureField.setPreferredSize(new Dimension(120, 35));
        formPanel.add(maxTemperatureField, gbc);
        
        gbc.gridx = 3;
        JLabel celsiusLabel = new JLabel("°C");
        celsiusLabel.setFont(UIStyles.ARIAL_BOLD_16);
        formPanel.add(celsiusLabel, gbc);
        
        saveButton = new JButton("Save");
        saveButton.setBackground(UIStyles.DARK_BLUE);
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setPreferredSize(new Dimension(140, 45));
        saveButton.setFont(UIStyles.ARIAL_BOLD_16);
        
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        formPanel.add(saveButton, gbc);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        
        patientComboBox.addActionListener(e -> {
            if (patientComboBox.getSelectedItem() != null) {
                Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
                loadCriticalParameters(selectedPatient.getId());
            }
        });
        
        saveButton.addActionListener(e -> saveCriticalParameters());
    }
    
    private void setupLayout() {
        mainPanel.setPreferredSize(new Dimension(700, 500));
    }
    
    private void loadPatients() {
        patientComboBox.removeAllItems();
        
        List<Patient> patients = new ArrayList<>();
        
        String userRole = currentUser.getRole().toUpperCase();
        
        if (userRole.equals("CAREGIVER")) {
            patients = patientController.getPatientsByCaregiver(currentUser.getId());
        } else if (userRole.equals("FAMILY")) {
            Patient patient = patientController.getPatientByFamily(currentUser.getId());
            if (patient != null) {
                patients.add(patient);
            }
        } else {
            patients = patientController.getAllPatients();
        }
        
        if (patients.isEmpty()) {
            patientComboBox.setEnabled(false);
            disableAllFields();
            setPlaceholderText("No patients assigned");
        } else {
            patientComboBox.setEnabled(true);
            enableAllFields();
            
            for (Patient patient : patients) {
                patientComboBox.addItem(patient);
            }
            
            if (patientComboBox.getItemCount() > 0) {
                Patient firstPatient = (Patient) patientComboBox.getItemAt(0);
                loadCriticalParameters(firstPatient.getId());
            }
        }
    }
    
    private void disableAllFields() {
        minHeartRateField.setEnabled(false);
        maxHeartRateField.setEnabled(false);
        minBloodPressureField.setEnabled(false);
        maxBloodPressureField.setEnabled(false);
        minTemperatureField.setEnabled(false);
        maxTemperatureField.setEnabled(false);
        saveButton.setEnabled(false);
    }
    
    private void enableAllFields() {
        minHeartRateField.setEnabled(true);
        maxHeartRateField.setEnabled(true);
        minBloodPressureField.setEnabled(true);
        maxBloodPressureField.setEnabled(true);
        minTemperatureField.setEnabled(true);
        maxTemperatureField.setEnabled(true);
        saveButton.setEnabled(true);
    }
    
    private void setPlaceholderText(String text) {
        minHeartRateField.setText(text);
        maxHeartRateField.setText(text);
        minBloodPressureField.setText(text);
        maxBloodPressureField.setText(text);
        minTemperatureField.setText(text);
        maxTemperatureField.setText(text);
    }
    
    private void loadCriticalParameters(int patientId) {
        CriticalParameter criticalParameter = criticalParameterController.getCriticalParametersByPatientId(patientId);
        
        if (criticalParameter != null) {
            minHeartRateField.setText(String.valueOf(criticalParameter.getMinHeartRate()));
            maxHeartRateField.setText(String.valueOf(criticalParameter.getMaxHeartRate()));
            minBloodPressureField.setText(criticalParameter.getMinBloodPressure());
            maxBloodPressureField.setText(criticalParameter.getMaxBloodPressure());
            minTemperatureField.setText(String.valueOf(criticalParameter.getMinTemperature()));
            maxTemperatureField.setText(String.valueOf(criticalParameter.getMaxTemperature()));
        } else {
            minHeartRateField.setText("60");
            maxHeartRateField.setText("100");
            minBloodPressureField.setText("90/60");
            maxBloodPressureField.setText("120/80");
            minTemperatureField.setText("36.1");
            maxTemperatureField.setText("37.2");
        }
    }
    
    private void saveCriticalParameters() {
        if (patientComboBox.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(mainPanel, 
                "Please select a patient", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (minHeartRateField.getText().trim().isEmpty() || 
            maxHeartRateField.getText().trim().isEmpty() ||
            minBloodPressureField.getText().trim().isEmpty() || 
            maxBloodPressureField.getText().trim().isEmpty() ||
            minTemperatureField.getText().trim().isEmpty() ||
            maxTemperatureField.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(mainPanel, 
                "Please fill in all fields", 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            double minHeartRate = Double.parseDouble(minHeartRateField.getText().trim());
            double maxHeartRate = Double.parseDouble(maxHeartRateField.getText().trim());
            String minBloodPressure = minBloodPressureField.getText().trim();
            String maxBloodPressure = maxBloodPressureField.getText().trim();
            double minTemperature = Double.parseDouble(minTemperatureField.getText().trim());
            double maxTemperature = Double.parseDouble(maxTemperatureField.getText().trim());
            
            if (!minBloodPressure.contains("/") || !maxBloodPressure.contains("/")) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Blood pressure should be in format 'systolic/diastolic' (e.g., 120/80)",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] minBPParts = minBloodPressure.split("/");
            String[] maxBPParts = maxBloodPressure.split("/");

            if (minBPParts.length != 2 || maxBPParts.length != 2) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Blood pressure should be in format 'systolic/diastolic' (e.g., 120/80)",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            int minSystolic = Integer.parseInt(minBPParts[0].trim());
            int minDiastolic = Integer.parseInt(minBPParts[1].trim());
            int maxSystolic = Integer.parseInt(maxBPParts[0].trim());
            int maxDiastolic = Integer.parseInt(maxBPParts[1].trim());

            if (minHeartRate < 30 || minHeartRate > 200 ||
                maxHeartRate < 30 || maxHeartRate > 200) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Heart rate must be between 30 and 200 bpm",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (minTemperature < 32.0 || minTemperature > 42.0 ||
                maxTemperature < 32.0 || maxTemperature > 42.0) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Temperature must be between 32.0 and 42.0 °C",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (minSystolic < 70 || minSystolic > 200 ||
                maxSystolic < 70 || maxSystolic > 200 ||
                minDiastolic < 40 || minDiastolic > 130 ||
                maxDiastolic < 40 || maxDiastolic > 130) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Blood pressure must be within 70-200/40-130 mmHg",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (minHeartRate >= maxHeartRate) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Minimum heart rate must be less than maximum heart rate",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (minTemperature >= maxTemperature) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Minimum temperature must be less than maximum temperature",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (minSystolic >= maxSystolic || minDiastolic >= maxDiastolic) {
                JOptionPane.showMessageDialog(mainPanel,
                    "Minimum blood pressure must be less than maximum blood pressure",
                    "Validation Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            Patient selectedPatient = (Patient) patientComboBox.getSelectedItem();
            
            CriticalParameter criticalParameter = new CriticalParameter(selectedPatient.getId());
            criticalParameter.setMinHeartRate(minHeartRate);
            criticalParameter.setMaxHeartRate(maxHeartRate);
            criticalParameter.setMinBloodPressure(minBloodPressure);
            criticalParameter.setMaxBloodPressure(maxBloodPressure);
            criticalParameter.setMinTemperature(minTemperature);
            criticalParameter.setMaxTemperature(maxTemperature);
            
            boolean success = criticalParameterController.updateCriticalParameters(criticalParameter);
            
            if (success) {
                JOptionPane.showMessageDialog(mainPanel, 
                    "Critical parameters saved successfully for " + selectedPatient.getName() + 
                    "\nThese values will now be used as thresholds for alerts and notifications.", 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(mainPanel, 
                    "Failed to save critical parameters. Please check the console for error details.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(mainPanel,
                "Please enter valid numeric values for heart rate, temperature, and blood pressure",
                "Validation Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public JPanel getMainPanel() {
        return mainPanel;
    }
    
    public void refreshData() {
        loadPatients();
    }
}