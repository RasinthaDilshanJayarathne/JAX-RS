
package com.health.api.model;

import javax.persistence.Entity;


@Entity
public class Patient extends Person{
    private String medicalHistory;
    private String healthStatus;
    
    // Default constructor
    public Patient() {
    }

    // Constructor with parameters
    public Patient(int id, String name, String contactInformation, String address, String medicalHistory, String healthStatus) {
        super(id, name, contactInformation, address);
        this.medicalHistory = medicalHistory;
        this.healthStatus = healthStatus;
    }

    // Getter for medicalHistory
    public String getMedicalHistory() {
        return medicalHistory;
    }

    // Setter for medicalHistory
    public void setMedicalHistory(String medicalHistory) {
        this.medicalHistory = medicalHistory;
    }

    // Getter for healthStatus
    public String getHealthStatus() {
        return healthStatus;
    }

    // Setter for healthStatus
    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "medicalHistory='" + medicalHistory + '\'' +
                ", healthStatus='" + healthStatus + '\'' +
                "} " + super.toString();
    }
}
