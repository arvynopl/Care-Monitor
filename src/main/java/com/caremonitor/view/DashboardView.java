// / File: src/main/java/com/caremonitor/view/DashboardView.java
package com.caremonitor.view;

import com.caremonitor.controller.HealthDataController;
import com.caremonitor.controller.PatientController;
import com.caremonitor.model.HealthData;
import com.caremonitor.model.Patient;
import com.caremonitor.model.User;
import com.caremonitor.simulation.IoTDeviceSimulator;
import com.caremonitor.util.NotificationManager;
import com.caremonitor.view.components.PatientCard;
import com.caremonitor.view.components.AlertPanel;

import javax.swing.*;
// import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardView {
    private JPanel mainPanel;
    private JPanel patientsPanel;
    private JPanel alertsPanel;
    private JScrollPane patientsScrollPane;
    private JScrollPane alertsScrollPane;

    private PatientController patientController;
    private HealthDataController healthDataController;
    private User currentUser;
    private List<IoTDeviceSimulator> simulators;
    private NotificationManager notificationManager;

    private final Color DARK_BLUE = new Color(0, 32, 96);

    public DashboardView(User currentUser) {
        this.currentUser = currentUser;
        this.patientController = new PatientController();
        this.healthDataController = new HealthDataController();
        this.simulators = new ArrayList<>();
        this.notificationManager = NotificationManager.getInstance();

        initializeComponents();
        loadPatients();
        startSimulators();
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel titleLabel = new JLabel("Health Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(DARK_BLUE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JPanel patientsSection = new JPanel(new BorderLayout());
        patientsSection.setBackground(Color.WHITE);

        JPanel patientHeaderPanel = new JPanel(new BorderLayout());
        patientHeaderPanel.setBackground(Color.WHITE);

        JLabel patientsLabel = new JLabel("Patients");
        patientsLabel.setFont(new Font("Arial", Font.BOLD, 18));

        patientHeaderPanel.add(patientsLabel, BorderLayout.WEST);

        patientsPanel = new JPanel();
        patientsPanel.setLayout(new BoxLayout(patientsPanel, BoxLayout.Y_AXIS));
        patientsPanel.setBackground(Color.WHITE);

        patientsScrollPane = new JScrollPane(patientsPanel);
        patientsScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        patientsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        patientsSection.add(patientHeaderPanel, BorderLayout.NORTH);
        patientsSection.add(patientsScrollPane, BorderLayout.CENTER);

        JPanel alertsSection = new JPanel(new BorderLayout());
        alertsSection.setBackground(Color.WHITE);

        JLabel alertsLabel = new JLabel("Recent Alerts");
        alertsLabel.setFont(new Font("Arial", Font.BOLD, 18));

        alertsPanel = new JPanel();
        alertsPanel.setLayout(new BoxLayout(alertsPanel, BoxLayout.Y_AXIS));
        alertsPanel.setBackground(Color.WHITE);

        alertsScrollPane = new JScrollPane(alertsPanel);
        alertsScrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        alertsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        alertsSection.add(alertsLabel, BorderLayout.NORTH);
        alertsSection.add(alertsScrollPane, BorderLayout.CENTER);

        contentPanel.add(patientsSection, BorderLayout.CENTER);
        contentPanel.add(alertsSection, BorderLayout.EAST);

        patientsSection.setPreferredSize(new Dimension(0, 0));
        alertsSection.setPreferredSize(new Dimension(300, 0));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }

    private void loadPatients() {
        patientsPanel.removeAll();

        try {
            List<Patient> patients = new ArrayList<>();
            if ("caregiver".equalsIgnoreCase(currentUser.getRole())) {
                patients = patientController.getPatientsByCaregiver(currentUser.getId());
            } else if ("family".equalsIgnoreCase(currentUser.getRole())) {
                patients = patientController.getPatientsByFamily(currentUser.getId());
                System.out.println("Family user ID: " + currentUser.getId() + ", Found patients: " + patients.size());
            } else {
                patients = patientController.getAllPatients();
            }

            if (patients.isEmpty()) {
                JLabel noPatientLabel = new JLabel("No patients assigned");
                noPatientLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                noPatientLabel.setFont(new Font("Arial", Font.ITALIC, 14));
                noPatientLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
                patientsPanel.add(noPatientLabel);
            } else {
                for (Patient patient : patients) {
                    HealthData latestData = healthDataController.getLatestHealthData(patient.getId());

                    PatientCard patientCard = new PatientCard(patient, latestData);
                    patientsPanel.add(patientCard);
                    patientsPanel.add(Box.createRigidArea(new Dimension(0, 10)));

                    startSimulatorForPatient(patient);
                }
            }

            loadAlerts();

            patientsPanel.revalidate();
            patientsPanel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel,
                "Error loading patients: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadAlerts() {
    }

    private void startSimulatorForPatient(Patient patient) {
        List<Patient> patientList = new ArrayList<>();
        patientList.add(patient);

        IoTDeviceSimulator simulator = new IoTDeviceSimulator(patientList);
        simulator.addDataListener(new IoTDeviceSimulator.DataListener() {
            @Override
            public void onDataReceived(int patientId, HealthData data) {
                healthDataController.saveHealthData(data);

                SwingUtilities.invokeLater(() -> {
                    updatePatientCard(patient.getId(), data);
                    checkForAlerts(patient, data);
                });
            }
        });

        simulator.start();
        simulators.add(simulator);
    }

    private void updatePatientCard(int patientId, HealthData data) {
        for (Component component : patientsPanel.getComponents()) {
            if (component instanceof PatientCard) {
                PatientCard card = (PatientCard) component;
                if (card.getPatient().getId() == patientId) {
                    card.updateHealthData(data);
                    break;
                }
            }
        }
    }

    private void checkForAlerts(Patient patient, HealthData data) {
        List<String> abnormalParams = healthDataController.getAbnormalParameters(data);

        if (abnormalParams != null && !abnormalParams.isEmpty()) {
            for (String parameter : abnormalParams) {
                addAlert(patient, data, parameter);

                String message;
                if ("caregiver".equalsIgnoreCase(currentUser.getRole())) {
                    message = "Kondisi Abnormal terdeteksi pada Pasien " + patient.getName() + ". Silakan datangi Pasien";
                } else if ("family".equalsIgnoreCase(currentUser.getRole())) {
                    message = "Kondisi Abnormal terdeteksi pada Pasien " + patient.getName() + ". Silakan hubungi Caregiver Keluarga Anda";
                } else {
                    message = "Kondisi Abnormal terdeteksi pada Pasien " + patient.getName() + ". Silakan periksa kondisi pasien";
                }
                
                String notificationKey = currentUser.getRole() + "_" + currentUser.getId() + "_" + patient.getId();
                notificationManager.showNotification("Peringatan Kondisi Kritis", message, notificationKey);
                
                break;
            }
        }
    }

    private void addAlert(Patient patient, HealthData data, String parameter) {
        AlertPanel alertPanel = new AlertPanel(patient, data, parameter);
        alertsPanel.add(alertPanel, 0);
        alertsPanel.add(Box.createRigidArea(new Dimension(0, 10)), 1);

        while (alertsPanel.getComponentCount() > 20) {
            alertsPanel.remove(alertsPanel.getComponentCount() - 1);
        }

        alertsPanel.revalidate();
        alertsPanel.repaint();
    }

    private void startSimulators() {
    }

    public void stopAutoRefresh() {
        stopSimulators();
    }

    private void stopSimulators() {
        for (IoTDeviceSimulator simulator : simulators) {
            simulator.stop();
        }
        simulators.clear();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void refreshData() {
        loadPatients();
    }
}
