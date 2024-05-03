
package com.health.api.dao;

import com.health.api.model.MedicalRecord;
import java.util.List;

public interface MedicalRecordDAO {
    public MedicalRecord getMedicalRecordById(int id);

    public List<MedicalRecord> getMedicalRecordsByPatientId(int patientId);

    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord);

    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord);

    public void deleteMedicalRecord(int id);
}
