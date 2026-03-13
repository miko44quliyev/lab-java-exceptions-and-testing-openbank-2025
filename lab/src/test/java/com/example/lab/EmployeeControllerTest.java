package com.example.lab;

import com.example.lab.controller.EmployeeController;
import com.example.lab.model.Employee;
import com.example.lab.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockitoBean
    EmployeeService employeeService;
    private final String  json = """
                {
                    "name": "Dr. Jane",
                    "department": "Cardiology",
                    "status": "ON"
                }
                """;
    @Test
    void getDoctorById_existingId_returnsDoctor() throws Exception {
        Employee doctor = new Employee(1L, "Dr. John", "Cardiology", "ON");
        when(employeeService.getDoctorById(1L)).thenReturn(doctor);

        mockMvc.perform(get("/api/doctors/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Dr. John"))
                .andExpect(jsonPath("$.department").value("Cardiology"))
                .andExpect(jsonPath("$.status").value("ON"));
    }

    @Test
    void getAllDoctors_returnsList() throws Exception {
        List<Employee> doctors = Arrays.asList(
                new Employee(1L, "Dr. John", "Cardiology", "ON"),
                new Employee(2L, "Dr. Smith", "Neurology", "OFF")
        );
        when(employeeService.getAllDoctors()).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Dr. John"))
                .andExpect(jsonPath("$[1].status").value("OFF"));
    }
    @Test
    void getDoctorsByStatus_returnsOk() throws Exception {
        List<Employee> doctors = List.of(new Employee(1L, "Dr. John", "Cardiology", "ON"));
        when(employeeService.getDoctorsByStatus("ON")).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors/status/ON")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("ON"));
    }

    @Test
    void getDoctorsByDepartment_returnsOk() throws Exception {
        List<Employee> doctors = List.of(new Employee(1L, "Dr. John", "Cardiology", "ON"));
        when(employeeService.getDoctorsByDepartment("Cardiology")).thenReturn(doctors);

        mockMvc.perform(get("/api/doctors/department/Cardiology")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].department").value("Cardiology"));
    }

    @Test
    void createDoctor_createsAndReturnsDoctor() throws Exception {
        Employee createdDoctor = new Employee(3L, "Dr. Jane", "Cardiology", "ON");

        when(employeeService.create(any(Employee.class))).thenReturn(createdDoctor);



        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("Dr. Jane"));
    }

    @Test
    void fullUpdateDoctor_updatesDoctor() throws Exception {
        Employee updatedDoctor = new Employee(1L, "Dr. John", "Cardiology", "OFF");
        when(employeeService.fullUpdate(eq(1L), any(Employee.class))).thenReturn(updatedDoctor);


        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OFF"));
    }

    @Test
    void partialUpdateDoctor_updatesDoctor() throws Exception {
        Employee updatedDoctor = new Employee(1L, "Dr. John", "Cardiology", "OFF");
        when(employeeService.partialUpdate(eq(1L), any(Employee.class))).thenReturn(updatedDoctor);

        mockMvc.perform(patch("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OFF"));
    }
    @Test
    void updateDoctorStatus_updatesStatus() throws Exception {
        Employee updatedDoctor = new Employee(1L, "Dr. John", "Cardiology", "OFF");
        when(employeeService.updateStatus(1L, "OFF")).thenReturn(updatedDoctor);

        mockMvc.perform(patch("/api/doctors/1/status")
                        .param("status", "OFF"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OFF"));
    }

    @Test
    void updateDoctorDepartment_updatesDepartment() throws Exception {
        Employee updatedDoctor = new Employee(1L, "Dr. John", "Neurology", "ON");
        when(employeeService.updateDepartment(1L, "Neurology")).thenReturn(updatedDoctor);

        mockMvc.perform(patch("/api/doctors/1/department")
                        .param("department", "Neurology"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.department").value("Neurology"));
    }

    @Test
    void deleteDoctor_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isNoContent());
    }

}

