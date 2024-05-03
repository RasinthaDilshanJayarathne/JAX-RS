package com.health.api.resource;

import com.health.api.dao.MedicalRecordDAO;
import com.health.api.daoImpl.MedicalRecordDAOImpl;
import com.health.api.model.MedicalRecord;
import com.health.api.model.ResponseBean;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
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
import javax.ws.rs.core.Response;

@Path("/medicalRecords")
public class MedicalRecordResource {

    private MedicalRecordDAO medicalRecordDAO = new MedicalRecordDAOImpl();
    ResponseBean responseBean = new ResponseBean();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public MedicalRecord getMedicalRecordById(@PathParam("id") int id) {
        MedicalRecord medicalRecord = new MedicalRecord();
        try {
            medicalRecord = medicalRecordDAO.getMedicalRecordById(id);
            if (medicalRecord == null) {
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("Person not found");
                return medicalRecord;
            }
            responseBean.setContent(medicalRecord);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Medical Record Retrieved Successfully");
            return medicalRecord;
        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to retrieve medical record: " + e.getMessage());
            return medicalRecord;
        }
    }

    @GET
    @Path("/patient/{patientId}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<MedicalRecord> getMedicalRecordsByPatientId(@PathParam("patientId") int patientId) {
        List<MedicalRecord> medicalRecords = new ArrayList<>();
        try {
            medicalRecords = medicalRecordDAO.getMedicalRecordsByPatientId(patientId);
            if (medicalRecords != null && !medicalRecords.isEmpty()) {
                responseBean.setContent(medicalRecords);
                responseBean.setResponseCode("200");
                responseBean.setResponseMsg("Medical Records Successfully Fetched");
                return medicalRecords;
            } else {
                responseBean.setContent(null);
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("No medical records found for patient with ID: " + patientId);
                return medicalRecords;
            }
        } catch (Exception e) {
            Logger.getLogger(MedicalRecordResource.class.getName()).log(Level.SEVERE, "Error occurred while fetching medical records", e);
            responseBean.setContent(null);
            responseBean.setResponseCode("500");
            responseBean.setResponseMsg("Internal Server Error: " + e.getMessage());
            return medicalRecords;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        MedicalRecord createdRecord = new MedicalRecord();
        try {
            if (medicalRecord.getPatient().getName() == null || medicalRecord.getPatient().getName().isEmpty()) {
                Logger.getLogger("First name is required");
            }

            if (medicalRecordDAO == null) {
                Logger.getLogger("Medical Record DAO is not properly initialized");
            }

            createdRecord = medicalRecordDAO.createMedicalRecord(medicalRecord);
            responseBean.setContent(createdRecord);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Medical Record Successfully Created");
            return createdRecord;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Medical Record Unsuccessfully Created" + e.getMessage());
            return createdRecord;
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public MedicalRecord updateMedicalRecord(@PathParam("id") int id, MedicalRecord updateMedicalRecord) {
        MedicalRecord updated = new MedicalRecord();
        try {
            MedicalRecord medicalRecordById = medicalRecordDAO.getMedicalRecordById(id);

            if (medicalRecordById != null) {
                updateMedicalRecord.setId(id);
                updated = medicalRecordDAO.updateMedicalRecord(updateMedicalRecord);
                if (updated != null) {
                    responseBean.setContent(updated);
                    responseBean.setResponseCode("200");
                    responseBean.setResponseMsg("Medical Record Updated Successfully");
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
                responseBean.setResponseMsg("Medical Record not found");
                return updated;
            }
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Medical Record Updated Unsuccessfully" + e.getMessage());
            return updated;
        }
    }

    @DELETE
    @Path("/{id}")
    public MedicalRecord deleteMedicalRecord(@PathParam("id") int id) {

        try {
            MedicalRecord medicalRecordById = medicalRecordDAO.getMedicalRecordById(id);

            if (medicalRecordById != null) {
                medicalRecordDAO.deleteMedicalRecord(id);
                responseBean.setResponseCode("200");
                responseBean.setResponseMsg(id + "Medical Record Deleted Successfully");
                return medicalRecordById;
            } else {
                return null;
            }

        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to delete medical record: " + e.getMessage());
            return null;
        }
    }
}
