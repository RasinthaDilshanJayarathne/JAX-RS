
package com.health.api.dto;


public class DoctorDTO {
    private String specialization;
    private int id;
    private String name;
    private String contactInformation;
    private String address;

    public DoctorDTO() {
    }

    public DoctorDTO(String specialization, int id, String name, String contactInformation, String address) {
        this.specialization = specialization;
        this.id = id;
        this.name = name;
        this.contactInformation = contactInformation;
        this.address = address;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
    
    
}
