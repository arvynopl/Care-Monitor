// / File: src/main/java/com/caremonitor/view/HealthHistoryView.java
package com.caremonitor.view;

import com.caremonitor.controller.HealthDataController;
import com.caremonitor.controller.PatientController;
import com.caremonitor.model.HealthData;
import com.caremonitor.model.Patient;
import com.caremonitor.model.User;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.DateAxis;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import com.caremonitor.view.theme.UIStyles;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import com.caremonitor.util.DialogUtil;
import java.text.SimpleDateFormat;

public class HealthHistoryView {
    private JPanel mainPanel;
    private JTable healthTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> patientComboBox;
    private JSpinner fromDateSpinner;
    private JSpinner toDateSpinner;
    private JButton filterButton;
    private JButton downloadPdfButton;
    private ChartPanel chartPanel;
    
    private final Color LIGHT_BLUE = UIStyles.CHART_LIGHT_BLUE;
    private final Color DARK_BLUE = UIStyles.CHART_DARK_BLUE;
    private final Color RED = UIStyles.SOFT_RED;
    // private final Color GREEN = new Color(40, 167, 69);
    
    private PatientController patientController;
    private HealthDataController healthDataController;
    private User currentUser;
    private List<Patient> patients;

    public HealthHistoryView(User currentUser) {
        this.currentUser = currentUser;
        this.patientController = new PatientController();
        this.healthDataController = new HealthDataController();
        this.patients = new ArrayList<>();
        
        initializeComponents();
        loadPatients();
        setupEventListeners();
    }

    private void initializeComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Health History", SwingConstants.CENTER);
        titleLabel.setFont(UIStyles.ARIAL_BOLD_24);
        titleLabel.setForeground(DARK_BLUE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel filterPanel = createFilterPanel();
        headerPanel.add(filterPanel, BorderLayout.SOUTH);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setResizeWeight(0.5);

        chartPanel = createChartPanel();
        splitPane.setTopComponent(chartPanel);

        JPanel tablePanel = createTablePanel();
        splitPane.setBottomComponent(tablePanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Options"));

        filterPanel.add(new JLabel("Patient:"));
        patientComboBox = new JComboBox<>();
        patientComboBox.setPreferredSize(new Dimension(200, 25));
        filterPanel.add(patientComboBox);

        filterPanel.add(new JLabel("From:"));
        fromDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor fromDateEditor = new JSpinner.DateEditor(fromDateSpinner, "dd/MM/yyyy HH:mm");
        fromDateSpinner.setEditor(fromDateEditor);
        fromDateSpinner.setPreferredSize(new Dimension(150, 25));
        filterPanel.add(fromDateSpinner);

        filterPanel.add(new JLabel("To:"));
        toDateSpinner = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor toDateEditor = new JSpinner.DateEditor(toDateSpinner, "dd/MM/yyyy HH:mm");
        toDateSpinner.setEditor(toDateEditor);
        toDateSpinner.setPreferredSize(new Dimension(150, 25));
        filterPanel.add(toDateSpinner);

        filterButton = new JButton("Apply Filter");
        filterButton.setBackground(DARK_BLUE);
        filterButton.setForeground(Color.WHITE);
        filterButton.setFocusPainted(false);
        filterButton.setBorderPainted(false);
        filterButton.setOpaque(true);
        filterPanel.add(filterButton);

        downloadPdfButton = new JButton("Download PDF");
        downloadPdfButton.setBackground(RED);
        downloadPdfButton.setForeground(Color.WHITE);
        downloadPdfButton.setFocusPainted(false);
        downloadPdfButton.setBorderPainted(false);
        downloadPdfButton.setOpaque(true);
        filterPanel.add(downloadPdfButton);

        return filterPanel;
    }

    private ChartPanel createChartPanel() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        TimeSeries heartRateSeries = new TimeSeries("Heart Rate (Daily Avg)");
        TimeSeries temperatureSeries = new TimeSeries("Temperature (Daily Avg)");
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            Date javaDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Day day = new Day(javaDate);
            
            heartRateSeries.add(day, 70 + Math.random() * 20);
            temperatureSeries.add(day, 36.5 + Math.random());
        }
        
        dataset.addSeries(heartRateSeries);
        dataset.addSeries(temperatureSeries);
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Daily Average Health Parameters",
            "Date",
            "Value",
            dataset,
            true,
            true,
            false
        );

        XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();
        dateAxis.setDateFormatOverride(new SimpleDateFormat("dd/MM"));
        
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setAutoRangeIncludesZero(false);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, RED);
        renderer.setSeriesPaint(1, LIGHT_BLUE); 
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
        plot.setRenderer(renderer);

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 300));
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);
        
        return chartPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Health Data Records"));

        
        String[] columnNames = {"Date & Time", "Heart Rate (bpm)", "Temperature (°C)", "Blood Pressure", "Position"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        healthTable = new JTable(tableModel);
        healthTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        healthTable.setRowHeight(25);

        JTableHeader header = healthTable.getTableHeader();
        header.setFont(UIStyles.ARIAL_BOLD_12);
        header.setBackground(DARK_BLUE);
        header.setForeground(Color.WHITE);
        
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(DARK_BLUE);
                c.setForeground(Color.WHITE);
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(UIStyles.ARIAL_BOLD_12);
                return c;
            }
        };
        
        for (int i = 0; i < healthTable.getColumnCount(); i++) {
            healthTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(healthTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void setupEventListeners() {
        filterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applyFilter();
            }
        });

        downloadPdfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadPDF();
            }
        });

        patientComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (patientComboBox.getSelectedIndex() > 0) {
                    loadHealthDataForSelectedPatient();
                }
            }
        });
    }

    private void loadPatients() {
        try {
            if ("caregiver".equalsIgnoreCase(currentUser.getRole())) {
                patients = patientController.getPatientsByCaregiver(currentUser.getId());
            } else if ("family".equalsIgnoreCase(currentUser.getRole())) {
                patients = patientController.getPatientsByFamily(currentUser.getId());
            } else {
                patients = patientController.getAllPatients();
            }

            patientComboBox.removeAllItems();
            patientComboBox.addItem("-- Select Patient --");
            
            if (patients.isEmpty()) {
                patientComboBox.setEnabled(false);
                fromDateSpinner.setEnabled(false);
                toDateSpinner.setEnabled(false);
                filterButton.setEnabled(false);
                downloadPdfButton.setEnabled(false);
                
                JOptionPane.showMessageDialog(mainPanel, 
                    "No patients assigned to you", 
                    "No Patients", 
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                patientComboBox.setEnabled(true);
                fromDateSpinner.setEnabled(true);
                toDateSpinner.setEnabled(true);
                filterButton.setEnabled(true);
                downloadPdfButton.setEnabled(true);
                
                for (Patient patient : patients) {
                    patientComboBox.addItem(patient.getName() + " (ID: " + patient.getId() + ")");
                }
                
                if (patientComboBox.getItemCount() > 0) {
                    Patient firstPatient = patients.get(0);
                    loadHealthData(firstPatient.getId());
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, 
                "Error loading patients: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHealthDataForSelectedPatient() {
        int selectedIndex = patientComboBox.getSelectedIndex();
        if (selectedIndex > 0) {
            Patient selectedPatient = patients.get(selectedIndex - 1);
            loadHealthData(selectedPatient.getId());
        }
    }

    private void applyFilter() {
        int selectedIndex = patientComboBox.getSelectedIndex();
        if (selectedIndex <= 0) {
            DialogUtil.showMessage(mainPanel,
                "Please select a patient first.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        final Patient selectedPatient = patients.get(selectedIndex - 1);
        Date fromDate = (Date) fromDateSpinner.getValue();
        Date toDate = (Date) toDateSpinner.getValue();

        if (fromDate.after(toDate)) {
            JOptionPane.showMessageDialog(mainPanel, 
                "From date cannot be after To date.", 
                "Warning", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDateTime fromDateTime = fromDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toDateTime = toDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        loadHealthDataWithDateRange(selectedPatient.getId(), fromDateTime, toDateTime);
        updateChart(selectedPatient.getId(), fromDateTime, toDateTime);
    }

    private void loadHealthData(int patientId) {
        try {
            List<HealthData> healthDataList = healthDataController.getHealthDataByPatientId(patientId);
            updateTable(healthDataList);
            updateChartForPatient(patientId);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, 
                "Error loading health data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadHealthDataWithDateRange(int patientId, LocalDateTime fromDate, LocalDateTime toDate) {
        try {
            List<HealthData> healthDataList = healthDataController.getHealthDataByPatientIdAndDateRange(
                patientId, fromDate, toDate);
            updateTable(healthDataList);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(mainPanel, 
                "Error loading health data: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateTable(List<HealthData> healthDataList) {
        tableModel.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        
        for (HealthData data : healthDataList) {
            Object[] row = {
                data.getTimestamp().format(formatter),
                data.getHeartRate(),
                String.format("%.1f", data.getTemperature()),
                data.getBloodPressure(),
                data.getPosition()
            };
            tableModel.addRow(row);
        }
    }

    private void updateChartForPatient(int patientId) {
        try {
            List<HealthData> healthDataList = healthDataController.getHealthDataByPatientId(patientId);
            updateChartDataWithDailyAverage(healthDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateChart(int patientId, LocalDateTime fromDate, LocalDateTime toDate) {
        try {
            List<HealthData> healthDataList = healthDataController.getHealthDataByPatientIdAndDateRange(
                patientId, fromDate, toDate);
            updateChartDataWithDailyAverage(healthDataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateChartDataWithDailyAverage(List<HealthData> healthDataList) {
        TimeSeries heartRateSeries = new TimeSeries("Heart Rate (Daily Avg)");
        TimeSeries temperatureSeries = new TimeSeries("Temperature (Daily Avg)");
        
        if (healthDataList.isEmpty()) {
            LocalDate today = LocalDate.now();
            for (int i = 0; i < 7; i++) {
                LocalDate date = today.minusDays(i);
                Date javaDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Day day = new Day(javaDate);
                
                heartRateSeries.add(day, 70 + Math.random() * 20);
                temperatureSeries.add(day, 36.5 + Math.random());
            }
        } else {
            Map<LocalDate, List<HealthData>> dailyData = new HashMap<>();
            
            for (HealthData data : healthDataList) {
                LocalDate date = data.getTimestamp().toLocalDate();
                dailyData.computeIfAbsent(date, k -> new ArrayList<>()).add(data);
            }
            
            for (Map.Entry<LocalDate, List<HealthData>> entry : dailyData.entrySet()) {
                LocalDate date = entry.getKey();
                List<HealthData> dayData = entry.getValue();
                
                double avgHeartRate = dayData.stream()
                    .mapToDouble(HealthData::getHeartRate)
                    .average()
                    .orElse(0.0);
                
                double avgTemperature = dayData.stream()
                    .mapToDouble(HealthData::getTemperature)
                    .average()
                    .orElse(0.0);
                
                Date javaDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Day day = new Day(javaDate);
                
                heartRateSeries.add(day, avgHeartRate);
                temperatureSeries.add(day, avgTemperature);
            }
        }

        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(heartRateSeries);
        dataset.addSeries(temperatureSeries);

        JFreeChart chart = chartPanel.getChart();
        XYPlot plot = chart.getXYPlot();
        plot.setDataset(dataset);
        
        chart.setTitle("Daily Average Health Parameters");
        
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, RED);  
        renderer.setSeriesPaint(1, LIGHT_BLUE); 
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
    }

    private void downloadPDF() {
        int selectedIndex = patientComboBox.getSelectedIndex();
        if (selectedIndex <= 0) {
            DialogUtil.showMessage(mainPanel,
                "Please select a patient first.",
                "Warning",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        Patient selectedPatient = patients.get(selectedIndex - 1);

        LocalDateTime fromDateTime = ((Date) fromDateSpinner.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime toDateTime = ((Date) toDateSpinner.getValue()).toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime();

        List<HealthData> reportData = healthDataController.getHealthDataByPatientIdAndDateRange(
                selectedPatient.getId(), fromDateTime, toDateTime);

        if (reportData.isEmpty()) {
            DialogUtil.showMessage(mainPanel,
                "No data available for the selected period.",
                "No Data",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF Report");
        fileChooser.setSelectedFile(new java.io.File("health_report_" + selectedPatient.getName().replaceAll("\\s+", "_") + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(mainPanel);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            final String finalFilePath = filePath;

            downloadPdfButton.setEnabled(false);
            downloadPdfButton.setText("Generating...");

            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                private Exception error;

                @Override
                protected Void doInBackground() {
                    try {
                        healthDataController.generatePDFReport(finalFilePath, selectedPatient, fromDateTime, toDateTime);
                    } catch (Exception ex) {
                        error = ex;
                    }
                    return null;
                }

                @Override
                protected void done() {
                    downloadPdfButton.setEnabled(true);
                    downloadPdfButton.setText("Download PDF");
                    if (error == null) {
                        DialogUtil.showMessage(mainPanel,
                            "PDF report generated successfully!\nSaved to: " + finalFilePath,
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        DialogUtil.showMessage(mainPanel,
                            "Error generating PDF: " + error.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            };

            worker.execute();
        }
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

    public void refreshData() {
        loadPatients();
        tableModel.setRowCount(0);
        
        TimeSeries heartRateSeries = new TimeSeries("Heart Rate (Daily Avg)");
        TimeSeries temperatureSeries = new TimeSeries("Temperature (Daily Avg)");
        
        LocalDate today = LocalDate.now();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            Date javaDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Day day = new Day(javaDate);
            
            heartRateSeries.add(day, 70 + Math.random() * 20);
            temperatureSeries.add(day, 36.5 + Math.random());
        }
        
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(heartRateSeries);
        dataset.addSeries(temperatureSeries);
        
        JFreeChart chart = chartPanel.getChart();
        XYPlot plot = chart.getXYPlot();
        plot.setDataset(dataset);
        
        chart.setTitle("Daily Average Health Parameters");
        
        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, RED);   
        renderer.setSeriesPaint(1, LIGHT_BLUE); 
        renderer.setSeriesStroke(0, new BasicStroke(2.0f));
        renderer.setSeriesStroke(1, new BasicStroke(2.0f));
        renderer.setSeriesShapesVisible(0, true);
        renderer.setSeriesShapesVisible(1, true);
    }
}
