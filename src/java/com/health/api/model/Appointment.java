
package com.health.api.model;

import java.sql.Date;
import java.sql.Time;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private Date date;
    private Time time;

    @ManyToOne // Many Appointments to One Patient
    private Patient patient;

    @ManyToOne // Many Appointments to One Doctor
    private Doctor doctor;
    
    // Default constructor
    public Appointment() {
    }

    // Constructor with parameters
    public Appointment(Date date, Time time, Patient patient, Doctor doctor) {
        this.date = date;
        this.time = time;
        this.patient = patient;
        this.doctor = doctor;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for date
    public Date getDate() {
        return date;
    }

    // Setter for date
    public void setDate(Date date) {
        this.date = date;
    }

    // Getter for time
    public Time getTime() {
        return time;
    }

    // Setter for time
    public void setTime(Time time) {
        this.time = time;
    }

    // Getter for patient
    public Patient getPatient() {
        return patient;
    }

    // Setter for patient
    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    // Getter for doctor
    public Doctor getDoctor() {
        return doctor;
    }

    // Setter for doctor
    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", patient=" + patient +
                ", doctor=" + doctor +
                '}';
    }
}
