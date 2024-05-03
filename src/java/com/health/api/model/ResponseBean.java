
package com.health.api.model;

import java.util.ArrayList;
import java.util.List;


public class ResponseBean {

    String responseCode;
    String responseMsg;
    Object content;
    List<Doctor> doctorContent = new ArrayList<>();
    List<Patient> patientContent = new ArrayList<>();
    List<Person> personContent = new ArrayList<>();

    public ResponseBean() {
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public List<Doctor> getDoctorContent() {
        return doctorContent;
    }

    public void setDoctorContent(List<Doctor> doctorContent) {
        this.doctorContent = doctorContent;
    }

    public List<Patient> getPatientContent() {
        return patientContent;
    }

    public void setPatientContent(List<Patient> patientContent) {
        this.patientContent = patientContent;
    }

    public List<Person> getPersonContent() {
        return personContent;
    }

    public void setPersonContent(List<Person> personContent) {
        this.personContent = personContent;
    }

    
    
}
