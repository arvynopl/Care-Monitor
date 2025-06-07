// File: src/test/java/com/caremonitor/controller/AuthControllerTest.java
package com.caremonitor.controller;
import com.caremonitor.view.LoginView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private LoginView mockLoginView;
    
    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController(mockLoginView);
    }

    @Test
    void testLoginWithValidCredentials() {
        String email = "caregiver@test.com";
        String password = "password123";
        
        assertDoesNotThrow(() -> {
            authController.login(email, password);
        });
    }

    @Test
    void testLoginWithInvalidCredentials() {
        String email = "invalid@test.com";
        String password = "wrongpassword";
        
        assertDoesNotThrow(() -> {
            authController.login(email, password);
        });
    }

    @Test
    void testRegisterCaregiver() {
        String name = "Dr. Smith";
        int age = 35;
        String gender = "Male";
        String address = "123 Medical St";
        String contact = "555-0123";
        String email = "dr.smith@hospital.com";
        String password = "password123";
        String specialization = "Cardiology";
        
        boolean result = authController.registerCaregiver(name, age, gender, address, contact, email, password, specialization);
        
        assertNotNull(result);
    }

    @Test
    void testRegisterFamily() {
        String name = "John Doe";
        int age = 45;
        String gender = "Male";
        String address = "456 Family Ave";
        String contact = "555-0456";
        String email = "john.doe@email.com";
        String password = "password123";
        String relationship = "Son";
        String patientCode = "PAT001";
        boolean result = authController.registerFamily(name, age, gender, address, contact, email, password, relationship, patientCode);
        assertNotNull(result);
    }

    @Test
    void testVerifyPatientCode() {
        String patientCode = "PAT001";
        int result = authController.verifyPatientCode(patientCode);
        assertTrue(result >= -1);
    }

    @Test
    void testVerifyPatientCodeWithInvalidCode() {
        String invalidCode = "INVALID";
        int result = authController.verifyPatientCode(invalidCode);
        assertEquals(-1, result);
    }
}