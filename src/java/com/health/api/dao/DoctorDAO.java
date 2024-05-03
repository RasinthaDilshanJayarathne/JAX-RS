
package com.health.api.dao;

import com.health.api.model.Doctor;
import java.util.List;


public interface DoctorDAO {
    public Doctor getDoctorById(int id);

    public List<Doctor> getAllDoctors();

    public Doctor createDoctor(Doctor doctor);

    public Doctor updateDoctor(Doctor doctor);

    public void deleteDoctor(int id);

}
