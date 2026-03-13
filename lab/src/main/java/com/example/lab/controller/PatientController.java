package com.example.lab.controller;

import com.example.lab.model.Employee;
import com.example.lab.model.Patient;
import com.example.lab.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public List<Patient> getAllPatients() {
        return patientService.getAllPatients();
    }
    @GetMapping("id/{id}")
    public Patient getPatientById(@PathVariable Long id) {
        return patientService.getPatientById(id);
    }
    @GetMapping("/dob")
    public List<Patient> getPatientsByDOBRange(
            @RequestParam("start") String start,
            @RequestParam("end") String end) {

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);
        return patientService.getPatientsByBirthRange(startDate, endDate);
    }

    @GetMapping("/department/{department}")
    public List<Patient> getPatientsByDoctorDepartment(@PathVariable String department) {
        return patientService.getPatientsByDoctorDepartment(department);
    }

    @GetMapping("/off-doctors")
    public List<Patient> getPatientsWithDoctorStatusOff() {
        return patientService.getPatientsWithDoctorStatusOFF();
    }

    @PostMapping
    public ResponseEntity<Patient> create(@Valid @RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.create(patient));
    }

    @PutMapping("/{id}")
    public Patient fullUpdate(@PathVariable Long id, @Valid @RequestBody Patient patient) {
        return patientService.fullUpdate(id,patient);
    }

    @PatchMapping("/{id}")
    public Patient partialUpdate(@PathVariable Long id, @RequestBody Patient patient) {
        return patientService.partialUpdate(id,patient);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        patientService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}