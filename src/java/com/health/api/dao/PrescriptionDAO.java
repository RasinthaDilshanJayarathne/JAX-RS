
package com.health.api.dao;

import com.health.api.model.Prescription;
import java.util.List;


public interface PrescriptionDAO {
     public Prescription getPrescriptionById(int id);

    public List<Prescription> getPrescriptionsByPatientId(int patientId);

    public Prescription createPrescription(Prescription prescription);

    public Prescription updatePrescription(Prescription prescription);

    public void deletePrescription(int id);

    public List<Prescription> getAllPrescription();
}
