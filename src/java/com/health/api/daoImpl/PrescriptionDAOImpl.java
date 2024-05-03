
package com.health.api.daoImpl;

import com.health.api.dao.PrescriptionDAO;
import com.health.api.db.DatabaseConfig;
import com.health.api.model.Patient;
import com.health.api.model.Prescription;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrescriptionDAOImpl implements PrescriptionDAO {

    @Override
    public Prescription getPrescriptionById(int id) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "SELECT * FROM Prescription WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int prescriptionId = rs.getInt("id");
                int patientId = rs.getInt("patient_id");
                String medication = rs.getString("medication");
                String dosage = rs.getString("dosage");
                String instructions = rs.getString("instructions");
                int duration = rs.getInt("duration");

                // Retrieve patient object
                Patient patient = getPatientById(patientId);

                Prescription prescription = new Prescription(patient, medication, dosage, instructions, duration);
                prescription.setId(prescriptionId);
                return prescription;
            } else {
                return null; 
            }
        } catch (SQLException e) {
            Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving prescription", e);
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
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "An SQL exception occurred", ex);
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
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).severe("Failed to obtain database connection.");
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
            Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving patient", e);
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
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "An SQL exception occurred", ex);
            }
        }
    }

    @Override
    public List<Prescription> getPrescriptionsByPatientId(int patientId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Prescription> prescriptions = new ArrayList<>();
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return prescriptions;
            }

            String query = "SELECT * FROM Prescription WHERE patient_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, patientId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String medication = rs.getString("medication");
                String dosage = rs.getString("dosage");
                String instructions = rs.getString("instructions");
                int duration = rs.getInt("duration");

                Patient patient = getPatientById(patientId);

                Prescription prescription = new Prescription(patient, medication, dosage, instructions, duration);
                prescription.setId(id);
                prescriptions.add(prescription);
            }
        } catch (SQLException e) {
            Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving prescriptions", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "An SQL exception occurred", ex);
            }
        }
        return prescriptions;
    }

    @Override
    public Prescription createPrescription(Prescription prescription) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet generatedKeys = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "INSERT INTO Prescription (patient_id, medication, dosage, instructions, duration) VALUES (?, ?, ?, ?, ?)";
            preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setInt(1, prescription.getPatient().getId());
            preparedStmt.setString(2, prescription.getMedication());
            preparedStmt.setString(3, prescription.getDosage());
            preparedStmt.setString(4, prescription.getInstructions());
            preparedStmt.setInt(5, prescription.getDuration());

            int affectedRows = preparedStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating prescription failed, no rows affected.");
            }

            generatedKeys = preparedStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                prescription.setId((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating prescription failed, no ID obtained.");
            }

            connection.commit();
            return prescription;
        } catch (SQLException e) {
            Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while creating prescription", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while rolling back transaction", ex);
                }
            }
            return null;
        } finally {
            try {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }

    @Override
    public Prescription updatePrescription(Prescription prescription) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "UPDATE Prescription SET medication=?, dosage=?, instructions=?, duration=? WHERE id=?";
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, prescription.getMedication());
            preparedStmt.setString(2, prescription.getDosage());
            preparedStmt.setString(3, prescription.getInstructions());
            preparedStmt.setInt(4, prescription.getDuration());
            preparedStmt.setInt(5, prescription.getId());

            int affectedRows = preparedStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating prescription failed, no rows affected.");
            }

            connection.commit();
            return prescription;
        } catch (SQLException e) {
            Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while updating prescription", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while rolling back transaction", ex);
                }
            }
            return null;
        } finally {
            try {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing prepared statement", ex);
            }
        }
    }

    @Override
    public void deletePrescription(int id) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return;
            }

            String query = "DELETE FROM Prescription WHERE id=?";
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, id);

            preparedStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while deleting prescription", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while rolling back transaction", ex);
                }
            }
        } finally {
            try {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing prepared statement", ex);
            }
        }
    }

    @Override
    public List<Prescription> getAllPrescription() {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Prescription> prescriptions = new ArrayList<>();

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return Collections.emptyList(); // Return empty list or handle the error appropriately
            }

            String query = "SELECT * FROM Prescription";
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int patientId = resultSet.getInt("patient_id");
                String medication = resultSet.getString("medication");
                String dosage = resultSet.getString("dosage");
                String instructions = resultSet.getString("instructions");
                int duration = resultSet.getInt("duration");

                Patient patient = getPatientById(patientId);

                Prescription prescription = new Prescription(patient, medication, dosage, instructions, duration);
                prescription.setId(id);
                prescriptions.add(prescription);
            }

            return prescriptions;
        } catch (SQLException e) {
            Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while retrieving all prescriptions", e);
            return Collections.emptyList();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PrescriptionDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }

}