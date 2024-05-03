
package com.health.api.model;

import javax.persistence.Entity;


@Entity
public class Doctor extends Person{
    private String specialization;
    
    public Doctor() {
    }

    // Constructor with parameters
    public Doctor(int id, String name, String contactInformation, String address, String specialization) {
        super(id, name, contactInformation, address);
        this.specialization = specialization;
    }

    // Getter for specialization
    public String getSpecialization() {
        return specialization;
    }

    // Setter for specialization
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "specialization='" + specialization + '\'' +
                "} " + super.toString();
    }
}
