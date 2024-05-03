package com.health.api.model;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne // One Medical Record belongs to One Patient
    private Patient patient;

    private String diagnosis;
    private String treatmentPlan;
    private List<String> allergies; // Assuming a list of allergies
    
    // Default constructor
    public MedicalRecord() {
    }

    // Constructor with parameters
    public MedicalRecord(Patient patient, String diagnosis, String treatmentPlan, List<String> allergies) {
        this.patient = patient;
        this.diagnosis = diagnosis;
        this.treatmentPlan = treatmentPlan;
        this.allergies = allergies;
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
    public Patient getPatient() {
        return patient;
    }

    // Setter for patient
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    // Getter for diagnosis
    public String getDiagnosis() {
        return diagnosis;
    }

    // Setter for diagnosis
    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    // Getter for treatmentPlan
    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    // Setter for treatmentPlan
    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
    }

    // Getter for allergies
    public List<String> getAllergies() {
        return allergies;
    }

    // Setter for allergies
    public void setAllergies(List<String> allergies) {
        this.allergies = allergies;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "id=" + id +
                ", patient=" + patient +
                ", diagnosis='" + diagnosis + '\'' +
                ", treatmentPlan='" + treatmentPlan + '\'' +
                ", allergies=" + allergies +
                '}';
    }

}
