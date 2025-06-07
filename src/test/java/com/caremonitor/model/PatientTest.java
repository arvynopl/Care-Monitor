// File: src/test/java/com/caremonitor/model/PatientTest.java
package com.caremonitor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient();
    }

    @Test
    void testPatientConstructorWithParameters() {
        int id = 1;
        String name = "John Doe";
        int age = 65;
        String gender = "Male";
        String address = "123 Main St";
        String contact = "555-1234";
        String patientCode = "PAT001";

        Patient paramPatient = new Patient(id, name, age, gender, address, contact, patientCode);

        assertEquals(id, paramPatient.getId());
        assertEquals(name, paramPatient.getName());
        assertEquals(age, paramPatient.getAge());
        assertEquals(gender, paramPatient.getGender());
        assertEquals(address, paramPatient.getAddress());
        assertEquals(contact, paramPatient.getEmergencyContact());
        assertEquals(patientCode, paramPatient.getPatientCode());
        assertEquals(patientCode, paramPatient.getUniqueCode());
        assertFalse(paramPatient.hasCaregiver());
        assertEquals("PATIENT", paramPatient.getRole());
    }

    @Test
    void testSettersAndGetters() {
        int id = 2;
        String name = "Jane Smith";
        int age = 70;
        String gender = "Female";
        String address = "456 Oak Ave";
        String emergencyContact = "555-5678";
        String uniqueCode = "PAT002";

        patient.setId(id);
        patient.setName(name);
        patient.setAge(age);
        patient.setGender(gender);
        patient.setAddress(address);
        patient.setEmergencyContact(emergencyContact);
        patient.setUniqueCode(uniqueCode);

        assertEquals(id, patient.getId());
        assertEquals(name, patient.getName());
        assertEquals(age, patient.getAge());
        assertEquals(gender, patient.getGender());
        assertEquals(address, patient.getAddress());
        assertEquals(emergencyContact, patient.getEmergencyContact());
        assertEquals(uniqueCode, patient.getUniqueCode());
        assertEquals(uniqueCode, patient.getPatientCode());
    }

    @Test
    void testCaregiverAssignment() {
        Caregiver caregiver = new Caregiver(1, "Dr. Smith", 35, "Male", "123 Medical St", "555-0123", "Cardiology");
        
        patient.setCaregiver(caregiver);
        
        assertEquals(caregiver, patient.getCaregiver());
        assertEquals(caregiver.getId(), patient.getCaregiverId());
        assertTrue(patient.hasCaregiver());
        
        // Test removing caregiver
        patient.setCaregiver(null);
        assertNull(patient.getCaregiver());
        assertEquals(0, patient.getCaregiverId());
        assertFalse(patient.hasCaregiver());
    }

    @Test
    void testToString() {
        patient.setId(1);
        patient.setName("Test Patient");
        
        String result = patient.toString();
        assertEquals("1 - Test Patient", result);
    }

    @Test
    void testEquals() {
        Patient patient1 = new Patient(1, "John Doe", 65, "Male", "123 Main St", "555-1234", "PAT001");
        Patient patient2 = new Patient(1, "Jane Smith", 70, "Female", "456 Oak Ave", "555-5678", "PAT002");
        Patient patient3 = new Patient(2, "Bob Johnson", 60, "Male", "789 Pine St", "555-9012", "PAT003");

        // Same ID should be equal
        assertEquals(patient1, patient2);
        
        // Different ID should not be equal
        assertNotEquals(patient1, patient3);
        
        // Same object should be equal
        assertEquals(patient1, patient1);
        
        // Null should not be equal
        assertNotEquals(patient1, null);
        
        // Different class should not be equal
        assertNotEquals(patient1, "not a patient");
    }

    @Test
    void testHashCode() {
        Patient patient1 = new Patient(1, "John Doe", 65, "Male", "123 Main St", "555-1234", "PAT001");
        Patient patient2 = new Patient(1, "Jane Smith", 70, "Female", "456 Oak Ave", "555-5678", "PAT002");
        Patient patient3 = new Patient(2, "Bob Johnson", 60, "Male", "789 Pine St", "555-9012", "PAT003");

        // Same ID should have same hash code
        assertEquals(patient1.hashCode(), patient2.hashCode());
        
        // Different ID should have different hash code
        assertNotEquals(patient1.hashCode(), patient3.hashCode());
    }
}