// File: src/test/java/com/caremonitor/model/FamilyTest.java
package com.caremonitor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FamilyTest {

    private Family family;

    @BeforeEach
    void setUp() {
        family = new Family();
    }

    @Test
    void testFamilyConstructorWithParameters() {
        int id = 1;
        String name = "John Doe";
        int age = 45;
        String gender = "Male";
        String address = "123 Family St";
        String contact = "555-0123";
        String relationship = "Son";
        int patientId = 100;

        Family paramFamily = new Family(id, name, age, gender, address, contact, relationship, patientId);

        assertEquals(id, paramFamily.getId());
        assertEquals(name, paramFamily.getName());
        assertEquals(age, paramFamily.getAge());
        assertEquals(gender, paramFamily.getGender());
        assertEquals(address, paramFamily.getAddress());
        assertEquals(contact, paramFamily.getContact());
        assertEquals(relationship, paramFamily.getRelationship());
        assertEquals(patientId, paramFamily.getPatientId());
        assertEquals("FAMILY", paramFamily.getRole());
    }

    @Test
    void testSettersAndGetters() {
        int id = 2;
        String name = "Jane Smith";
        int age = 50;
        String gender = "Female";
        String address = "456 Care Ave";
        String contact = "555-0456";
        String relationship = "Daughter";
        int patientId = 200;

        family.setId(id);
        family.setName(name);
        family.setAge(age);
        family.setGender(gender);
        family.setAddress(address);
        family.setContact(contact);
        family.setRelationship(relationship);
        family.setPatientId(patientId);

        assertEquals(id, family.getId());
        assertEquals(name, family.getName());
        assertEquals(age, family.getAge());
        assertEquals(gender, family.getGender());
        assertEquals(address, family.getAddress());
        assertEquals(contact, family.getContact());
        assertEquals(relationship, family.getRelationship());
        assertEquals(patientId, family.getPatientId());
    }

    @Test
    void testPatientAssignment() {
        Patient patient = new Patient(1, "Elderly Patient", 80, "Male", "789 Senior St", "555-0789", "PAT001");
        
        family.setPatient(patient);
        
        assertEquals(patient, family.getPatient());
        assertEquals(patient.getId(), family.getPatientId());
    }

    @Test
    void testToString() {
        family.setId(1);
        family.setName("Mary Johnson");
        family.setAge(42);
        family.setGender("Female");
        family.setAddress("321 Family Lane");
        family.setContact("555-0321");
        family.setRelationship("Wife");
        family.setPatientId(150);

        String result = family.toString();

        // Test that the toString contains the expected information
        assertTrue(result.contains("Family{"));
        assertTrue(result.contains("id=1"));
        assertTrue(result.contains("name='Mary Johnson'"));
        assertTrue(result.contains("age=42"));
        assertTrue(result.contains("gender='Female'"));
        assertTrue(result.contains("address='321 Family Lane'"));
        assertTrue(result.contains("contact='555-0321'"));
        assertTrue(result.contains("relationship='Wife'"));
        assertTrue(result.contains("patientId=150"));
    }
}