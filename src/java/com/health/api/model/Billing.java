package com.health.api.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne // One Billing belongs to One Appointment
    private Appointment appointment;

    private double amount;
    private String paymentStatus; // e.g., Paid, Unpaid
    
    // Default constructor
    public Billing() {
    }

    // Constructor with parameters
    public Billing(Appointment appointment, double amount, String paymentStatus) {
        this.appointment = appointment;
        this.amount = amount;
        this.paymentStatus = paymentStatus;
    }

    // Getter for id
    public int getId() {
        return id;
    }

    // Setter for id
    public void setId(int id) {
        this.id = id;
    }

    // Getter for appointment
    public Appointment getAppointment() {
        return appointment;
    }

    // Setter for appointment
    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    // Getter for amount
    public double getAmount() {
        return amount;
    }

    // Setter for amount
    public void setAmount(double amount) {
        this.amount = amount;
    }

    // Getter for paymentStatus
    public String getPaymentStatus() {
        return paymentStatus;
    }

    // Setter for paymentStatus
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    @Override
    public String toString() {
        return "Billing{" +
                "id=" + id +
                ", appointment=" + appointment +
                ", amount=" + amount +
                ", paymentStatus='" + paymentStatus + '\'' +
                '}';
    }
}
