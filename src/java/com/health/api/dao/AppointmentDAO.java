
package com.health.api.dao;

import com.health.api.model.Appointment;
import java.util.List;


public interface AppointmentDAO {
    public Appointment getAppointmentById(int id);

    public List<Appointment> getAppointmentsByPatientId(int patientId);

    public Appointment createAppointment(Appointment appointment);

    public Appointment updateAppointment(Appointment appointment);

    public void deleteAppointment(int id);

    public List<Appointment> getAppointment();
}
