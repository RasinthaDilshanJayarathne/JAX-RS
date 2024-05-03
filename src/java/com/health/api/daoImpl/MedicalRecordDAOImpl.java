package com.health.api.daoImpl;

import com.health.api.dao.MedicalRecordDAO;
import com.health.api.db.DatabaseConfig;
import com.health.api.model.MedicalRecord;
import com.health.api.model.Patient;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MedicalRecordDAOImpl implements MedicalRecordDAO {

    @Override
    public MedicalRecord getMedicalRecordById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        MedicalRecord medicalRecord = null;

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(MedicalRecordDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "SELECT * FROM MedicalRecord WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Patient patient = new Patient();
                patient.setId(resultSet.getInt("patient_id"));

                String diagnosis = resultSet.getString("diagnosis");
                String treatmentPlan = resultSet.getString("treatmentPlan");
                List<String> allergies = Arrays.asList(resultSet.getString("allergies").split(","));

                medicalRecord = new MedicalRecord(patient, diagnosis, treatmentPlan, allergies);
                medicalRecord.setId(id);
            }
        } catch (SQLException e) {
            Logger.getLogger(MedicalRecordDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while retrieving medical record by ID", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MedicalRecordDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }

        return medicalRecord;
    }

    @Override
    public List<MedicalRecord> getMedicalRecordsByPatientId(int patientId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<MedicalRecord> medicalRecords = new ArrayList<>();

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(MedicalRecordDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return medicalRecords;
            }

            String query = "SELECT * FROM MedicalRecord WHERE patient_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, patientId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String diagnosis = resultSet.getString("diagnosis");
                String treatmentPlan = resultSet.getString("treatmentPlan");
                String allergiesStr = resultSet.getString("allergies");
                List<String> allergies = Arrays.asList(allergiesStr.split(","));

                Patient patient = getPatientById(patientId);

                MedicalRecord medicalRecord = new MedicalRecord(patient, diagnosis, treatmentPlan, allergies);
                medicalRecord.setId(id);
                medicalRecords.add(medicalRecord);
            }
        } catch (SQLException e) {
            Logger.getLogger(MedicalRecordDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while retrieving medical records by patient ID", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MedicalRecordDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }

        return medicalRecords;
    }

    @Override
    public MedicalRecord createMedicalRecord(MedicalRecord medicalRecord) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(MedicalRecordDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "INSERT INTO MedicalRecord (patient_id, diagnosis, treatmentplan, allergies) VALUES (?, ?, ?, ?)";
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, medicalRecord.getPatient().getId());
            statement.setString(2, medicalRecord.getDiagnosis());
            statement.setString(3, medicalRecord.getTreatmentPlan()); // Updated column name to 'treatmentplan'
            String allergies = String.join(",", medicalRecord.getAllergies());
            statement.setString(4, allergies);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating medical record failed, no rows affected.");
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                medicalRecord.setId(id);
            } else {
                throw new SQLException("Creating medical record failed, no ID obtained.");
            }
            connection.commit();
            return medicalRecord;
        } catch (SQLException e) {
            Logger.getLogger(MedicalRecordDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while creating medical record", e);
            return null;
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MedicalRecordDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }

    @Override
    public MedicalRecord updateMedicalRecord(MedicalRecord medicalRecord) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(MedicalRecordDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "UPDATE MedicalRecord SET diagnosis=?, treatment_plan=?, allergies=? WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setString(1, medicalRecord.getDiagnosis());
            statement.setString(2, medicalRecord.getTreatmentPlan());
            String allergies = String.join(",", medicalRecord.getAllergies());
            statement.setString(3, allergies);
            statement.setInt(4, medicalRecord.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating medical record failed, no rows affected.");
            }

            return medicalRecord;
        } catch (SQLException e) {
            Logger.getLogger(MedicalRecordDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while updating medical record", e);
            return null;
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MedicalRecordDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing statement", ex);
            }
        }
    }

    @Override
    public void deleteMedicalRecord(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(MedicalRecordDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return;
            }

            String query = "DELETE FROM MedicalRecord WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            Logger.getLogger(MedicalRecordDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while deleting medical record", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(MedicalRecordDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing statement", ex);
            }
        }
    }

    private Patient getPatientById(int patientId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "SELECT * FROM Patient WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, patientId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String contactInformation = rs.getString("contactInformation");
                String address = rs.getString("address");
                String medicalHistory = rs.getString("medicalHistory");
                String healthStatus = rs.getString("healthStatus");
                return new Patient(id, name, contactInformation, address, medicalHistory, healthStatus);
            } else {
                return null;
            }
        } catch (SQLException e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving patient", e);
            return null;
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "An SQL exception occurred", ex);
            }
        }
    }

}
