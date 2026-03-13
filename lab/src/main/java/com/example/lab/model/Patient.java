package com.example.lab.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public class Patient {
    private Long id;

    @NotBlank(message = "Patient name cannot be empty")
    private String name;

    @NotNull(message = "Date of birth cannot be null")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Admitting doctor cannot be null")
    private Employee admittedBy;

    public Patient() {}

    public Patient(String name, LocalDate dateOfBirth, Employee admittedBy) {
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.admittedBy = admittedBy;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public Employee getAdmittedBy() { return admittedBy; }
    public void setAdmittedBy(Employee admittedBy) { this.admittedBy = admittedBy; }
}