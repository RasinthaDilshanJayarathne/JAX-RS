package com.health.api.resource;

import com.health.api.dao.PatientDAO;
import com.health.api.daoImpl.PatientDAOImpl;
import com.health.api.model.Patient;
import com.health.api.model.ResponseBean;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/patients")
public class PatientResource {

    private PatientDAO patientDAO = new PatientDAOImpl();
    ResponseBean responseBean = new ResponseBean();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Patient getPatientById(@PathParam("id") int id) {
        Patient patient = new Patient();
        try {
            patient = patientDAO.getPatientById(id);
            if (patient == null) {
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("Patient not found");
                return patient;
            }
            responseBean.setContent(patient);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Patient Retrieved Successfully");
            return patient;
        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to retrieve patient: " + e.getMessage());
            return patient;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Patient> getPatient() {
        List<Patient> people = new ArrayList<>();
        try {
            people = patientDAO.getAllPatient();
            responseBean.setContent(people);
            responseBean.setContent(people);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Fetch All People in Successfully");
            return people;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Fetch Fail..! " + e.getMessage());
            return people;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Patient createPatient(Patient patient) {
        Patient createPatient = new Patient();
        try {
            if (patient.getName() == null || patient.getName().isEmpty()) {
                Logger.getLogger("First name is required");
            }

            if (patientDAO == null) {
                Logger.getLogger("Patient DAO is not properly initialized");
            }

            createPatient = patientDAO.createPatient(patient);
            responseBean.setContent(createPatient);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Patient Successfully Created");
            return createPatient;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Patient Unsuccessfully Created" + e.getMessage());
            return createPatient;
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Patient updatePatient(@PathParam("id") int id, Patient updatedPatient) {
        Patient updatePatient = new Patient();
        try {
            Patient existingPatient = patientDAO.getPatientById(id);
            if (existingPatient != null) {
                updatedPatient.setId(id);
                updatePatient = patientDAO.updatePatient(updatedPatient);
                if (updatePatient != null) {
                    responseBean.setContent(updatePatient);
                    responseBean.setResponseCode("200");
                    responseBean.setResponseMsg("Patient Updated Successfully");
                    return updatePatient;
                } else {
                    responseBean.setContent(null);
                    responseBean.setResponseCode("500");
                    responseBean.setResponseMsg("Internal Server Error");
                    return updatePatient;
                }
            } else {
                responseBean.setContent(null);
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("Patient not found");
                return updatePatient;
            }
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Patient Updated Unsuccessfully" + e.getMessage());
            return updatePatient;
        }
    }

    @DELETE
    @Path("/{id}")
    public Patient deletePatient(@PathParam("id") int id) {
        Patient patientToDelete = new Patient();
        try {
            patientToDelete = patientDAO.getPatientById(id);

            if (patientToDelete != null) {
                patientDAO.deletePatient(id);
                responseBean.setResponseCode("200");
                responseBean.setResponseMsg("Patient ID " + id + " Successfully Deleted");
                return patientToDelete;
            } else {
                return null;
            }
        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to delete patient: " + e.getMessage());
        }
        return patientToDelete;

    }
}
