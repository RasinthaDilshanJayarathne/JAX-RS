package com.health.api.resource;

import com.health.api.dao.PatientDAO;
import com.health.api.dao.PrescriptionDAO;
import com.health.api.daoImpl.PatientDAOImpl;
import com.health.api.daoImpl.PrescriptionDAOImpl;
import com.health.api.model.Patient;
import com.health.api.model.Prescription;
import com.health.api.model.ResponseBean;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/prescriptions")
public class PrescriptionResource {

    private PrescriptionDAO prescriptionDAO = new PrescriptionDAOImpl();
    private PatientDAO patientDAO = new PatientDAOImpl();
    ResponseBean responseBean = new ResponseBean();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Prescription getPrescriptionById(@PathParam("id") int id) {
        Prescription prescription = new Prescription();
        try {
            prescription = prescriptionDAO.getPrescriptionById(id);
            if (prescription == null) {
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("Prescription not found");
                return prescription;
            }
            responseBean.setContent(prescription);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Prescription Retrieved Successfully");
            return prescription;
        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to retrieve person: " + e.getMessage());
            return prescription;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Prescription createPrescription(@Valid Prescription prescription) {
        Prescription createdPrescription = new Prescription();
        try {
            if (prescription == null || prescription.getPatient() == null || prescription.getPatient().getId() <= 0) {
                responseBean.setResponseCode("400");
                responseBean.setResponseMsg("Patient with a valid ID is required");
                return createdPrescription;
            }
            if (prescription.getMedication() == null || prescription.getMedication().isEmpty()) {
                responseBean.setResponseCode("400");
                responseBean.setResponseMsg("Medication name is required");
                return createdPrescription;
            }

            Patient patientById = patientDAO.getPatientById(prescription.getPatient().getId());
            if (patientById == null) {
                responseBean.setResponseCode("400");
                responseBean.setResponseMsg("Patient with the specified ID does not exist");
                return createdPrescription;
            }

            createdPrescription = prescriptionDAO.createPrescription(prescription);
            if (createdPrescription != null) {
                responseBean.setContent(createdPrescription);
                responseBean.setResponseCode("200");
                responseBean.setResponseMsg("Prescription Successfully Created");
                return createdPrescription;
            } else {
                responseBean.setResponseCode("500");
                responseBean.setResponseMsg("Failed to create prescription");
                return createdPrescription;
            }
        } catch (Exception e) {
            Logger.getLogger(PrescriptionResource.class.getName()).log(Level.SEVERE, "Error occurred while creating prescription", e);
            responseBean.setResponseCode("500");
            responseBean.setResponseMsg("Prescription Unsuccessfully Created: " + e.getMessage());
            return createdPrescription;
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Prescription updatePrescription(@PathParam("id") int id, @Valid Prescription updatedPrescription) {
        Prescription updated = new Prescription();
        try {
            Prescription existingPrescription = prescriptionDAO.getPrescriptionById(id);

            if (existingPrescription != null) {
                updatedPrescription.setId(id);
                updated = prescriptionDAO.updatePrescription(updatedPrescription);
                if (updated != null) {
                    responseBean.setContent(updated);
                    responseBean.setResponseCode("200");
                    responseBean.setResponseMsg("Prescription Updated Successfully");
                    return updated;
                } else {
                    responseBean.setContent(null);
                    responseBean.setResponseCode("500");
                    responseBean.setResponseMsg("Internal Server Error");
                    return updated;
                }
            } else {
                responseBean.setContent(null);
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("Prescription not found");
                return updated;
            }
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Prescription Updated Unsuccessfully" + e.getMessage());
            return updated;
        }
    }

    @DELETE
    @Path("/{id}")
    public Prescription deletePrescription(@PathParam("id") int id) {
        
        
        try {
            Prescription prescriptionById = prescriptionDAO.getPrescriptionById(id);

            if (prescriptionById != null) {
                prescriptionDAO.deletePrescription(id);
                responseBean.setResponseCode("200");
                responseBean.setResponseMsg(id + " Prescription Deleted Successfully");
                return prescriptionById;
            } else {
                return null;
            }

        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to delete prescription: " + e.getMessage());
            return null;
        }
    }

    @GET
    @Path("/patient/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public List<Prescription> getPrescriptionsByPatientId(@PathParam("patientId") int patientId) {
        List<Prescription> prescriptions = new ArrayList<>();
        try {
            prescriptions = prescriptionDAO.getPrescriptionsByPatientId(patientId);
            responseBean.setContent(prescriptions);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Fetch Prescriptions By Patient in Successfully");
            return prescriptions;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Fetch Fail..! " + e.getMessage());
            return prescriptions;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Prescription> getAllPrescription() {
        List<Prescription> prescriptions = new ArrayList<>();
        try {
            prescriptions = prescriptionDAO.getAllPrescription();
            responseBean.setContent(prescriptions);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Fetch All Prescriptions in Successfully");
            return prescriptions;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Fetch Fail..! " + e.getMessage());
            return prescriptions;
        }
    }
}
