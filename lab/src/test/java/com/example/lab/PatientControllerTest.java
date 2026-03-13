package com.example.lab;
import com.example.lab.controller.PatientController;
import com.example.lab.model.Employee;
import com.example.lab.model.Patient;
import com.example.lab.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PatientService patientService;

    private final String patientJson = """
        {
            "name": "Alice",
            "dateOfBirth": "1990-01-01",
            "admittedBy": {
                "id": 1,
                "name": "Dr. John",
                "department": "Cardiology",
                "status": "ON"
            }
        }
        """;

    @Test
    void getAllPatients_returnsList() throws Exception {
        Employee doctor = new Employee(1L, "Dr. John", "Cardiology", "ON");
        Patient patient = new Patient("Alice", LocalDate.of(1990, 1, 1), doctor);
        patient.setId(1L);

        when(patientService.getAllPatients()).thenReturn(List.of(patient));

        mockMvc.perform(get("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].admittedBy.name").value("Dr. John"));
    }

    @Test
    void getPatientById_existingId_returnsPatient() throws Exception {
        Employee doctor = new Employee(1L, "Dr. John", "Cardiology", "ON");
        Patient patient = new Patient("Alice", LocalDate.of(1990, 1, 1), doctor);
        patient.setId(1L);

        when(patientService.getPatientById(1L)).thenReturn(patient);

        mockMvc.perform(get("/api/patients/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.admittedBy.name").value("Dr. John"));
    }

    @Test
    void createPatient_createsAndReturnsPatient() throws Exception {
        Employee doctor = new Employee(1L, "Dr. John", "Cardiology", "ON");
        Patient patientCreated = new Patient("Alice", LocalDate.of(1990, 1, 1), doctor);
        patientCreated.setId(1L);

        when(patientService.create(any(Patient.class))).thenReturn(patientCreated);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.admittedBy.name").value("Dr. John"));
    }

    @Test
    void fullUpdatePatient_updatesPatient() throws Exception {
        Employee doctor = new Employee(1L, "Dr. John", "Cardiology", "ON");
        Patient updatedPatient = new Patient("Alice Updated", LocalDate.of(1990, 1, 1), doctor);
        updatedPatient.setId(1L);

        when(patientService.fullUpdate(eq(1L), any(Patient.class))).thenReturn(updatedPatient);

        mockMvc.perform(put("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson.replace("Alice", "Alice Updated")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Updated"));
    }

    @Test
    void partialUpdatePatient_updatesPatient() throws Exception {
        Employee doctor = new Employee(1L, "Dr. John", "Cardiology", "ON");
        Patient updatedPatient = new Patient("Alice Partial", LocalDate.of(1990, 1, 1), doctor);
        updatedPatient.setId(1L);

        when(patientService.partialUpdate(eq(1L), any(Patient.class))).thenReturn(updatedPatient);

        mockMvc.perform(patch("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson.replace("Alice", "Alice Partial")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice Partial"));
    }

    @Test
    void deletePatient_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/patients/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void getPatientById_nonExistent_returns404() throws Exception {
        when(patientService.getPatientById(999L))
                .thenThrow(new RuntimeException("Patient not found"));

        mockMvc.perform(get("/api/patients/id/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPatient_invalidData_returns400() throws Exception {
        String invalidJson = """
            {
                "name": "",
                "dateOfBirth": "2025-01-01"
            }
            """;

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void fullUpdatePatient_nonExistent_returns404() throws Exception {
        when(patientService.fullUpdate(eq(99L), any(Patient.class))).thenThrow(new RuntimeException("Patient not found"));

        mockMvc.perform(put("/api/patients/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patientJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void partialUpdatePatient_changeAdmittedBy_updatesDoctor() throws Exception {
        Employee newDoctor = new Employee(2L, "Dr. Smith", "Neurology", "ON");
        Patient updatedPatient = new Patient("Alice", LocalDate.of(1990, 1, 1), newDoctor);
        updatedPatient.setId(1L);

        when(patientService.partialUpdate(eq(1L), any(Patient.class))).thenReturn(updatedPatient);

        String patchJson = """
            {
                "admittedBy": {
                    "id": 2,
                    "name": "Dr. Smith",
                    "department": "Neurology",
                    "status": "ON"
                }
            }
            """;

        mockMvc.perform(patch("/api/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.admittedBy.name").value("Dr. Smith"))
                .andExpect(jsonPath("$.admittedBy.department").value("Neurology"));
    }


}
