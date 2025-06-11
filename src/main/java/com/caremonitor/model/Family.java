// File: src/main/java/com/caremonitor/model/Family.java
package com.caremonitor.model;

public class Family extends User {
    private String relationship;
    private int patientId;
    private Patient patient;
    
    public Family() {
        super();
        this.role = "FAMILY";
    }
    
    public Family(int id, String name, int age, String gender, String address, String contact, String relationship, int patientId) {
        super(id, name, age, gender, address, contact);
        this.relationship = relationship;
        this.patientId = patientId;
        this.role = "FAMILY";
    }
    
    public Family(int id, String fullName, String email, String password, String contact, String relationship) {
        super(id, fullName, email, password, "FAMILY");
        this.contact = contact;
        this.relationship = relationship;
    }
    
    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
    
    public int getPatientId() { return patientId; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { 
        this.patient = patient;
        if (patient != null) {
            this.patientId = patient.getId();
        }
    }
    
    @Override
    public String toString() {
        return "Family{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", relationship='" + relationship + '\'' +
                ", patientId=" + patientId +
                '}';
    }
}