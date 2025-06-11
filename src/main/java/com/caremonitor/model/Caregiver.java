//File: src/main/java/com/caremonitor/model/Caregiver.java
package com.caremonitor.model;

import java.util.ArrayList;
import java.util.List;

public class Caregiver extends User {
    private String specialization;
    private List<Patient> patients;
    private List<Patient> assignedPatients;
    
    public Caregiver() {
        super();
        this.patients = new ArrayList<>();
        this.assignedPatients = new ArrayList<>();
        this.role = "CAREGIVER";
    }
    
    public Caregiver(int id, String name, int age, String gender, String address, String contact, String specialization) {
        super(id, name, age, gender, address, contact);
        this.specialization = specialization;
        this.patients = new ArrayList<>();
        this.assignedPatients = new ArrayList<>();
        this.role = "CAREGIVER";
    }
    
    // Constructor for DatabaseManager compatibility
    public Caregiver(int id, String fullName, String email, String password, String specialization, String contact) {
        super(id, fullName, email, password, "CAREGIVER");
        this.specialization = specialization;
        this.contact = contact;
        this.patients = new ArrayList<>();
        this.assignedPatients = new ArrayList<>();
    }
    
    // Getters and Setters
    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    
    public List<Patient> getPatients() { return patients; }
    public void setPatients(List<Patient> patients) { this.patients = patients; }
    
    public List<Patient> getAssignedPatients() { return assignedPatients != null ? assignedPatients : patients; }
    public void setAssignedPatients(List<Patient> assignedPatients) { 
        this.assignedPatients = assignedPatients;
        this.patients = assignedPatients;
    }
    
    // Patient management methods
    public void addPatient(Patient patient) {
        if (!patients.contains(patient)) {
            patients.add(patient);
            if (patient != null) {
                patient.setCaregiver(this);
            }
        }
        if (assignedPatients != null && !assignedPatients.contains(patient)) {
            assignedPatients.add(patient);
        }
    }
    
    public void removePatient(Patient patient) {
        patients.remove(patient);
        if (assignedPatients != null) {
            assignedPatients.remove(patient);
        }
        if (patient != null && patient.getCaregiver() == this) {
            patient.setCaregiver(null);
        }
    }
    
    public int getPatientCount() {
        return patients != null ? patients.size() : 0;
    }
    
    @Override
    public String toString() {
        return "Caregiver{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", specialization='" + specialization + '\'' +
                ", patients=" + getPatientCount() +
                '}';
    }
}