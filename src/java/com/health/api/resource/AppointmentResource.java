package com.health.api.resource;

import com.health.api.dao.AppointmentDAO;
import com.health.api.dao.DoctorDAO;
import com.health.api.dao.PatientDAO;
import com.health.api.daoImpl.AppointmentDAOImpl;
import com.health.api.daoImpl.DoctorDAOImpl;
import com.health.api.daoImpl.PatientDAOImpl;
import com.health.api.model.Appointment;
import com.health.api.model.Doctor;
import com.health.api.model.Patient;
import com.health.api.model.ResponseBean;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/appointments")
public class AppointmentResource {

    private AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    private DoctorDAO doctorDAO = new DoctorDAOImpl();
    private PatientDAO patientDAO = new PatientDAOImpl();
    ResponseBean responseBean = new ResponseBean();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Appointment getAppointmentById(@PathParam("id") int id) {
        Appointment appointment = new Appointment();
        try {
            appointment = appointmentDAO.getAppointmentById(id);
            if (appointment == null) {
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("Appointment not found");
                return appointment;
            }
            responseBean.setContent(appointment);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Appointment Retrieved Successfully");
            return appointment;
        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to retrieve appointment: " + e.getMessage());
            return appointment;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Appointment> getAllAppointment() {
        List<Appointment> appointments = new ArrayList<>();
        try {
            appointments = appointmentDAO.getAppointment();
            responseBean.setContent(appointments);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Fetch All Appointment in Successfully");
            return appointments;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Fetch Fail..! " + e.getMessage());
            return appointments;
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Appointment createAppointment(Appointment appointment) {
        Appointment createdAppointment = new Appointment();
        try {
            if (appointment.getDoctor() == null || appointment.getDoctor().getId() <= 0 || appointment.getPatient() == null || appointment.getPatient().getId() <= 0) {
                ResponseBean responseBean = new ResponseBean();
                responseBean.setResponseCode("400");
                responseBean.setResponseMsg("Doctor and Patient with valid IDs are required");
                return createdAppointment;
            }

            Doctor doctorById = doctorDAO.getDoctorById(appointment.getDoctor().getId());
            Patient patientById = patientDAO.getPatientById(appointment.getPatient().getId());

            if (doctorById == null) {
                ResponseBean responseBean = new ResponseBean();
                responseBean.setResponseCode("400");
                responseBean.setResponseMsg("Doctor with the specified ID does not exist");
                return createdAppointment;
            } else if (patientById == null) {
                ResponseBean responseBean = new ResponseBean();
                responseBean.setResponseCode("400");
                responseBean.setResponseMsg("Patient with the specified ID does not exist");
                return createdAppointment;
            } else {
                createdAppointment = appointmentDAO.createAppointment(appointment);
                if (createdAppointment != null) {
                    ResponseBean responseBean = new ResponseBean();
                    responseBean.setContent(createdAppointment);
                    responseBean.setResponseCode("200");
                    responseBean.setResponseMsg("Appointment Successfully Created");
                    return createdAppointment;
                } else {
                    ResponseBean responseBean = new ResponseBean();
                    responseBean.setResponseCode("400");
                    responseBean.setResponseMsg("Failed to create appointment");
                    return createdAppointment;
                }
            }

        } catch (Exception e) {
            ResponseBean responseBean = new ResponseBean();
            responseBean.setResponseCode("500");
            responseBean.setResponseMsg("Internal Server Error: " + e.getMessage());
            return createdAppointment;
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Appointment updateAppointment(@PathParam("id") int id, Appointment updatedAppointment) {
        Appointment updated = new Appointment();
        try {
            Appointment existingAppointment = appointmentDAO.getAppointmentById(id);
            if (existingAppointment != null) {
                existingAppointment.setDate(updatedAppointment.getDate());
                existingAppointment.setTime(updatedAppointment.getTime());
                if (updatedAppointment.getPatient() != null && updatedAppointment.getPatient().getId() > 0) {
                    existingAppointment.setPatient(updatedAppointment.getPatient());
                }
                if (updatedAppointment.getDoctor() != null && updatedAppointment.getDoctor().getId() > 0) {
                    existingAppointment.setDoctor(updatedAppointment.getDoctor());
                }

                updated = appointmentDAO.updateAppointment(existingAppointment);
                responseBean.setContent(updated);
                responseBean.setResponseCode("200");
                responseBean.setResponseMsg("Appointment Successfully Updated");
                return updated;
            } else {
                responseBean.setContent(null);
                responseBean.setResponseCode("400");
                responseBean.setResponseMsg("Failed to update appointment");
                return updated;
            }
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("500");
            responseBean.setResponseMsg("Internal Server Error: " + e.getMessage());
            return updated;
        }
    }

    @DELETE
    @Path("/{id}")
    public Appointment deleteAppointment(@PathParam("id") int id) {
        Appointment appointmentToDelete = new Appointment();
        try {
            appointmentToDelete = appointmentDAO.getAppointmentById(id);

            if (appointmentToDelete != null) {
                appointmentDAO.deleteAppointment(id);
                responseBean.setResponseCode("200");
                responseBean.setResponseMsg("Appointment ID " + id + " Successfully Deleted");
                return appointmentToDelete;
            } else {
                return null;
            }
        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to delete appointment: " + e.getMessage());
        }
        return appointmentToDelete;        
    }
}
