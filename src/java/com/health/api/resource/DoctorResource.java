package com.health.api.resource;

import com.health.api.dao.DoctorDAO;
import com.health.api.daoImpl.DoctorDAOImpl;
import com.health.api.model.Doctor;
import com.health.api.model.ResponseBean;
import java.io.Serializable;
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

@Path("/doctors")
public class DoctorResource {

    private DoctorDAO doctorDAO = new DoctorDAOImpl();
    ResponseBean responseBean = new ResponseBean();
    private Serializable doctors;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Doctor getDoctorById(@PathParam("id") int id) {
        Doctor doctor = new Doctor();
        try {
            doctor = doctorDAO.getDoctorById(id);
            if (doctor == null) {
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("Doctor not found");
                return doctor;
            }
            responseBean.setDoctorContent((List<Doctor>) doctor);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Doctor Retrieved Successfully");
            return doctor;
        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to retrieve doctor: " + e.getMessage());
            return doctor;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Doctor> getAllDoctor() {
        List<Doctor> allDoctors = new ArrayList<>();
        try {
            allDoctors = doctorDAO.getAllDoctors();
            responseBean.setDoctorContent(allDoctors);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Fetch All Doctor in Successfully");
            return allDoctors;
            //return doctors;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Fetch Fail..! " + e.getMessage());
            return allDoctors;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Doctor createDoctor(Doctor doctor) {
        Doctor createDoctor = new Doctor();
        try {
            if (doctor.getName() == null || doctor.getName().isEmpty()) {
                Logger.getLogger("First name is required");
            }

            if (doctorDAO == null) {
                Logger.getLogger("Doctor DAO is not properly initialized");
            }

            createDoctor = doctorDAO.createDoctor(doctor);
            responseBean.setDoctorContent((List<Doctor>) doctor);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Doctor Successfully Created");
            return createDoctor;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Doctor Unsuccessfully Created" + e.getMessage());
            return createDoctor;
        }

    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Doctor updateDoctor(@PathParam("id") int id, Doctor updateDoctor) {
        Doctor updated = new Doctor();
        try {
            Doctor existingDoctor = doctorDAO.getDoctorById(id);
            if (existingDoctor != null) {
                updateDoctor.setId(id);
                updated = doctorDAO.updateDoctor(updateDoctor);
                if (updated != null) {
                    responseBean.setDoctorContent((List<Doctor>) updated);
                    responseBean.setResponseCode("200");
                    responseBean.setResponseMsg("Doctor Updated Successfully");
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
                responseBean.setResponseMsg("Doctor not found");
                return updated;
            }
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Doctor Updated Unsuccessfully" + e.getMessage());
            return updated;
        }
    }

    @DELETE
    @Path("/{id}")
    public Doctor deleteDoctor(@PathParam("id") int id) {
        try {
            Doctor doctorToDelete = doctorDAO.getDoctorById(id);

            if (doctorToDelete != null) {
                doctorDAO.deleteDoctor(id);
                responseBean.setResponseCode("200");
                responseBean.setResponseMsg(" Doctor ID " + id +" Successfully Deleted");
                return doctorToDelete;
            } else {
                return null;
            }

        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to Delete Doctor" + e.getMessage());
            return null;
        }
    }
}
