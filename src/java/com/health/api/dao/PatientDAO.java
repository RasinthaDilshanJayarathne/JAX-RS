
package com.health.api.dao;

import com.health.api.model.Patient;
import java.util.List;


public interface PatientDAO {
    public Patient getPatientById(int id);

    public List<Patient> getAllPatients();

    public Patient createPatient(Patient patient);

    public Patient updatePatient(Patient patient);

    public void deletePatient(int id);

    public List<Patient> getAllPatient();
}
