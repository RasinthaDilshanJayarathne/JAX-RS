
package com.health.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne // One Prescription belongs to One Patient
    private Patient patient;

    private String medication;
    private String dosage;
    private String instructions;
    private int duration; // Duration in days, weeks, etc.
    
    // Default constructor
    public Prescription() {
    }

    // Constructor with parameters
    public Prescription(Patient patient, String medication, String dosage, String instructions, int duration) {
        this.patient = patient;
        this.medication = medication;
        this.dosage = dosage;
        this.instructions = instructions;
        this.duration = duration;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for patient
    @JsonProperty("patient")
    public Patient getPatient() {
        return patient;
    }

    // Setter for patient
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    // Getter for medication
    public String getMedication() {
        return medication;
    }

    // Setter for medication
    public void setMedication(String medication) {
        this.medication = medication;
    }

    // Getter for dosage
    public String getDosage() {
        return dosage;
    }

    // Setter for dosage
    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    // Getter for instructions
    public String getInstructions() {
        return instructions;
    }

    // Setter for instructions
    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    // Getter for duration
    public int getDuration() {
        return duration;
    }

    // Setter for duration
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "id=" + id +
                ", patient=" + patient +
                ", medication='" + medication + '\'' +
                ", dosage='" + dosage + '\'' +
                ", instructions='" + instructions + '\'' +
                ", duration=" + duration +
                '}';
    }
}
