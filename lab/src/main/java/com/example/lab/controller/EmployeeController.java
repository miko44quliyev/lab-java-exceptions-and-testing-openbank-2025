package com.example.lab.controller;

import com.example.lab.model.Employee;
import com.example.lab.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctors")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping
    public List<Employee> getAllDoctors() {
        return employeeService.getAllDoctors();
    }

    @GetMapping("/id/{id}")
    public Employee getDoctorById(@PathVariable long id) {
        return employeeService.getDoctorById(id);
    }

    @GetMapping("/status/{status}")
    public List<Employee> getDoctorsByStatus(@PathVariable String status) {
        return employeeService.getDoctorsByStatus(status);
    }

    @GetMapping("/department/{department}")
    public List<Employee> getDoctorsByDepartment(@PathVariable String department) {
        return employeeService.getDoctorsByDepartment(department);
    }
    @PostMapping
    public ResponseEntity<Employee> create(@Valid @RequestBody Employee employee) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(employee));
    }

    @PutMapping("/{id}")
    public Employee fullUpdate(@PathVariable Long id, @Valid @RequestBody Employee employee) {
        return employeeService.fullUpdate(id,employee);
    }

    @PatchMapping("/{id}")
    public Employee partialUpdate(@PathVariable Long id, @RequestBody Employee employee) {
        return employeeService.partialUpdate(id, employee);
    }
    @PatchMapping("/{id}/status")
    public Employee updateDoctorStatus(@PathVariable Long id, @RequestParam String status) {
        return employeeService.updateStatus(id, status);
    }

    @PatchMapping("/{id}/department")
    public Employee updateDoctorDepartment(@PathVariable Long id, @RequestParam String department) {
        return employeeService.updateDepartment(id, department);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}