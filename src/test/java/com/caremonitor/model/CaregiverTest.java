//File: src/test/java/com/caremonitor/model/CaregiverTest.java
package com.caremonitor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CaregiverTest {

    private Caregiver caregiver;

    @BeforeEach
    void setUp() {
        caregiver = new Caregiver();
    }

    @Test
    void testCaregiverConstructorWithParameters() {
        int id = 1;
        String name = "Dr. Smith";
        int age = 35;
        String gender = "Male";
        String address = "123 Medical St";
        String contact = "555-0123";
        String specialization = "Cardiology";

        Caregiver paramCaregiver = new Caregiver(id, name, age, gender, address, contact, specialization);

        assertEquals(id, paramCaregiver.getId());
        assertEquals(name, paramCaregiver.getName());
        assertEquals(age, paramCaregiver.getAge());
        assertEquals(gender, paramCaregiver.getGender());
        assertEquals(address, paramCaregiver.getAddress());
        assertEquals(contact, paramCaregiver.getContact());
        assertEquals(specialization, paramCaregiver.getSpecialization());
        assertEquals("CAREGIVER", paramCaregiver.getRole());
        assertNotNull(paramCaregiver.getPatients());
        assertTrue(paramCaregiver.getPatients().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        int id = 1;
        String name = "Dr. Johnson";
        int age = 40;
        String gender = "Female";
        String address = "456 Hospital Ave";
        String contact = "555-0456";
        String specialization = "Neurology";

        caregiver.setId(id);
        caregiver.setName(name);
        caregiver.setAge(age);
        caregiver.setGender(gender);
        caregiver.setAddress(address);
        caregiver.setContact(contact);
        caregiver.setSpecialization(specialization);

        assertEquals(id, caregiver.getId());
        assertEquals(name, caregiver.getName());
        assertEquals(age, caregiver.getAge());
        assertEquals(gender, caregiver.getGender());
        assertEquals(address, caregiver.getAddress());
        assertEquals(contact, caregiver.getContact());
        assertEquals(specialization, caregiver.getSpecialization());
    }

    @Test
    void testPatientManagement() {
        Patient patient1 = new Patient(1, "John Doe", 65, "Male", "123 Main St", "555-1234", "PAT001");
        Patient patient2 = new Patient(2, "Jane Smith", 70, "Female", "456 Oak Ave", "555-5678", "PAT002");

        caregiver.addPatient(patient1);
        caregiver.addPatient(patient2);

        assertEquals(2, caregiver.getPatients().size());
        assertTrue(caregiver.getPatients().contains(patient1));
        assertTrue(caregiver.getPatients().contains(patient2));
        assertEquals(2, caregiver.getPatientCount());

        caregiver.removePatient(patient1);
        assertEquals(1, caregiver.getPatients().size());
        assertFalse(caregiver.getPatients().contains(patient1));
        assertTrue(caregiver.getPatients().contains(patient2));
        assertEquals(1, caregiver.getPatientCount());

        // Test adding duplicate patient
        caregiver.addPatient(patient2);
        assertEquals(1, caregiver.getPatients().size()); // Should not add duplicate
    }

    @Test
    void testToString() {
        caregiver.setId(1);
        caregiver.setName("Dr. Wilson");
        caregiver.setAge(45);
        caregiver.setGender("Male");
        caregiver.setAddress("789 Medical Center");
        caregiver.setContact("555-0789");
        caregiver.setSpecialization("Emergency Medicine");

        String result = caregiver.toString();

        // Test that the toString contains the expected information
        assertTrue(result.contains("Caregiver{"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name='Dr. Wilson'"));
        assertTrue(result.contains("age=45"));
        assertTrue(result.contains("gender='Male'"));
        assertTrue(result.contains("address='789 Medical Center'"));
        assertTrue(result.contains("contact='555-0789'"));
        assertTrue(result.contains("specialization='Emergency Medicine'"));
        assertTrue(result.contains("patients=0"));
    }
}