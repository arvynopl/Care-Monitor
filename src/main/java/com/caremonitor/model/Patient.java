// / File: src/main/java/com/caremonitor/model/Patient.java
package com.caremonitor.model;

public class Patient extends User {
    private String emergencyContact;
    private String uniqueCode;
    private String patientCode;
    private int caregiverId;
    private boolean hasCaregiver;
    private Caregiver caregiver;

    public Patient() {
        super();
        this.role = "PATIENT";
    }

    public Patient(int id, String name, int age, String gender, String address, String contact, String patientCode) {
        super(id, name, age, gender, address, contact);
        this.patientCode = patientCode;
        this.uniqueCode = patientCode;
        this.emergencyContact = contact;
        this.hasCaregiver = false;
        this.role = "PATIENT";
    }

    public String getEmergencyContact() {
        return emergencyContact != null ? emergencyContact : contact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getUniqueCode() {
        return uniqueCode != null ? uniqueCode : patientCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
        this.patientCode = uniqueCode;
    }
    
    public String getPatientCode() {
        return patientCode != null ? patientCode : uniqueCode;
    }

    public void setPatientCode(String patientCode) {
        this.patientCode = patientCode;
        this.uniqueCode = patientCode;
    }

    public int getCaregiverId() {
        return caregiverId;
    }

    public void setCaregiverId(int caregiverId) {
        this.caregiverId = caregiverId;
        this.hasCaregiver = true;
    }

    public boolean hasCaregiver() {
        return hasCaregiver;
    }

    public void setHasCaregiver(boolean hasCaregiver) {
        this.hasCaregiver = hasCaregiver;
    }
    
    public Caregiver getCaregiver() {
        return caregiver;
    }
    
    public void setCaregiver(Caregiver caregiver) {
        this.caregiver = caregiver;
        if (caregiver != null) {
            this.caregiverId = caregiver.getId();
            this.hasCaregiver = true;
        } else {
            this.caregiverId = 0;
            this.hasCaregiver = false;
        }
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Patient patient = (Patient) obj;
        return id == patient.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}