package com.health.api.daoImpl;

import com.health.api.dao.PatientDAO;
import com.health.api.db.DatabaseConfig;
import com.health.api.model.Patient;
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

public class PatientDAOImpl implements PatientDAO {

    @Override
    public Patient getPatientById(int id) {
        Patient patient = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "SELECT * FROM Patient WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setName(rs.getString("name"));
                patient.setContactInformation(rs.getString("contactInformation"));
                patient.setAddress(rs.getString("address"));
                patient.setMedicalHistory(rs.getString("medicalHistory"));
                patient.setHealthStatus(rs.getString("healthStatus"));
            }
        } catch (SQLException e) {
            Logger.getLogger(PatientDAOImpl.class.getName()).log(Level.SEVERE, "An SQL exception occurred", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PatientDAOImpl.class.getName()).log(Level.SEVERE, "An error occurred while closing resources", ex);
            }
        }
        return patient;
    }

    @Override
    public List<Patient> getAllPatients() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Patient> patients = new ArrayList<>();

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return Collections.emptyList();
            }

            String query = "SELECT * FROM Patient";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contactInformation = resultSet.getString("contactInformation");
                String address = resultSet.getString("address");
                String medicalHistory = resultSet.getString("medicalHistory");
                String healthStatus = resultSet.getString("healthStatus");

                Patient patient = new Patient(id, name, contactInformation, address, medicalHistory, healthStatus);
                patients.add(patient);
            }

            return patients;
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while retrieving all patients", e);
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
                Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }

    @Override
    public Patient createPatient(Patient patient) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        PreparedStatement preparedStmt1 = null;
        ResultSet generatedKeys = null; // Define generatedKeys outside try-catch block
        ResultSet generatedKeys1 = null; // Define generatedKeys1 outside try-catch block

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "INSERT INTO Patient (name, contactInformation, address, medicalHistory, healthStatus) VALUES (?, ?, ?, ?, ?)";
            String query1 = "INSERT INTO Person (name, contactInformation, address, role, roleid) VALUES (?, ?, ?, ?, ?)";
            String query2 = "SELECT IFNULL(MAX(id), 0) AS latest_pid FROM Patient";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query2);
            int latestPid = 0;

            if (resultSet.next()) {
                latestPid = resultSet.getInt("latest_pid");
            } else {
                System.out.println("No records found in the table.");
            }

            // Increment latestPid and assign it to roleId
            String roleId = String.valueOf(++latestPid);

            // Execute query1 to insert data into Person table
            preparedStmt1 = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            preparedStmt1.setString(1, patient.getName());
            preparedStmt1.setString(2, patient.getContactInformation());
            preparedStmt1.setString(3, patient.getAddress());
            preparedStmt1.setString(4, "PATIENT");
            preparedStmt1.setString(5, roleId);

            int affectedRows1 = preparedStmt1.executeUpdate();
            if (affectedRows1 == 0) {
                throw new SQLException("Creating people failed, no rows affected.");
            }

            generatedKeys1 = preparedStmt1.getGeneratedKeys();
            if (generatedKeys1.next()) {
                patient.setId(generatedKeys1.getInt(1));
            } else {
                throw new SQLException("Creating people failed, no ID obtained.");
            }

            // Execute query to insert data into Patient table
            preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1, patient.getName());
            preparedStmt.setString(2, patient.getContactInformation());
            preparedStmt.setString(3, patient.getAddress());
            preparedStmt.setString(4, patient.getMedicalHistory());
            preparedStmt.setString(5, patient.getHealthStatus());

            int affectedRows = preparedStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating patient failed, no rows affected.");
            }

            generatedKeys = preparedStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                patient.setId(generatedKeys.getInt(1));
            } else {
                throw new SQLException("Creating patient failed, no ID obtained.");
            }

            connection.commit();
            return patient;
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while creating patient", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while rolling back transaction", ex);
                }
            }
            return null;
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (generatedKeys1 != null) {
                    generatedKeys1.close();
                }
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                if (preparedStmt1 != null) {
                    preparedStmt1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }

    @Override
    public Patient updatePatient(Patient patient) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        PreparedStatement preparedStmt1 = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PatientDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "UPDATE Patient SET name=?, contactInformation=?, address=?, medicalHistory=?, healthStatus=? WHERE id=?";
            String query1 = "UPDATE Person SET name=?, contactInformation=?, address=? WHERE roleid=? and role = ?";

            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, patient.getName());
            preparedStmt.setString(2, patient.getContactInformation());
            preparedStmt.setString(3, patient.getAddress());
            preparedStmt.setString(4, patient.getMedicalHistory());
            preparedStmt.setString(5, patient.getHealthStatus());
            preparedStmt.setInt(6, patient.getId());

            preparedStmt1 = connection.prepareStatement(query1);
            preparedStmt1.setString(1, patient.getName());
            preparedStmt1.setString(2, patient.getContactInformation());
            preparedStmt1.setString(3, patient.getAddress());
            preparedStmt1.setInt(4, patient.getId());
            preparedStmt1.setString(5, "PATIENT");

            int affectedRows = preparedStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating patient failed, no rows affected.");
            }

            int affectedRows1 = preparedStmt1.executeUpdate();
            if (affectedRows1 == 0) {
                throw new SQLException("Updating person failed, no rows affected.");
            }

            connection.commit();
            return patient;
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while updating patient", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while rolling back transaction", ex);
                }
            }
            return null;
        } finally {
            try {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing prepared statement", ex);
            }
        }
    }

    @Override
    public void deletePatient(int id) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        PreparedStatement preparedStmt1 = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
            }

            String query = "DELETE FROM Patient WHERE id=?";

            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.executeUpdate();

            String query1 = "DELETE FROM Person WHERE roleid=? and role = ?";
            preparedStmt1 = connection.prepareStatement(query1); // corrected the variable name here
            preparedStmt1.setInt(1, id);
            preparedStmt1.setString(2, "PATIENT");
            preparedStmt1.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while deleting patient", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while rolling back transaction", ex);
                }
            }
        } finally {
            try {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
                if (preparedStmt1 != null) { // added the closing for preparedStmt1
                    preparedStmt1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing prepared statement", ex);
            }

        }
    }

    @Override
    public List<Patient> getAllPatient() {
        List<Patient> patients = new ArrayList<>();
        try (Connection connection = DatabaseConfig.getInstance().getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Patient")) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contactInformation = resultSet.getString("contactInformation");
                String address = resultSet.getString("address");
                String medicalHistory = resultSet.getString("medicalHistory");
                String healthStatus = resultSet.getString("healthStatus");

                Patient patient = new Patient(id, name, contactInformation, address, medicalHistory, healthStatus);
                patients.add(patient);
            }

        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while retrieving all patients", e);
        }
        return patients;
    }
}
