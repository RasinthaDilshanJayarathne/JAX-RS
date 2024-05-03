package com.health.api.daoImpl;

import com.health.api.dao.DoctorDAO;
import com.health.api.db.DatabaseConfig;
import com.health.api.model.Doctor;
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

public class DoctorDAOImpl implements DoctorDAO {

    @Override
    public Doctor getDoctorById(int id) {
        Doctor doctor = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "SELECT * FROM Doctor WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setName(rs.getString("name"));
                doctor.setContactInformation(rs.getString("contactInformation"));
                doctor.setAddress(rs.getString("address"));
                doctor.setSpecialization(rs.getString("specialization"));
            }
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "An error occurred while closing resources", e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "An SQL exception occurred", ex);
            }
        }
        return doctor;
    }

    @Override
    public List<Doctor> getAllDoctors() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Doctor> doctors = new ArrayList<>();

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return Collections.emptyList();
            }

            String query = "SELECT * FROM Doctor";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contactInformation = resultSet.getString("contactInformation");
                String address = resultSet.getString("address");
                String specialization = resultSet.getString("specialization");

                Doctor doctor = new Doctor(id, name, contactInformation, address, specialization);
                doctors.add(doctor);
            }

            return doctors;
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while retrieving all doctors", e);
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
    public Doctor createDoctor(Doctor doctor) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        PreparedStatement preparedStmt1 = null;
        ResultSet generatedKeys = null; // Define generatedKeys outside try-catch block
        ResultSet generatedKeys1 = null; // Define generatedKeys1 outside try-catch block

        String roleId = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }
            String query = "INSERT INTO Doctor (name, contactInformation, address, specialization) VALUES (?, ?, ?, ?)";
            String query1 = "INSERT INTO Person (name, contactInformation, address, role, roleid) VALUES (?, ?, ?, ?, ?)";
            String query2 = "SELECT IFNULL(MAX(id), 0) AS latest_pid FROM Doctor";

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query2);

            if (resultSet.next()) {
                int latestPid = resultSet.getInt("latest_pid");

                // Increment latestPid and assign it to roleId
                roleId = String.valueOf(++latestPid);

            } else {
                System.out.println("No records found in the table.");
            }

            preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1, doctor.getName());
            preparedStmt.setString(2, doctor.getContactInformation());
            preparedStmt.setString(3, doctor.getAddress());
            preparedStmt.setString(4, doctor.getSpecialization());

            preparedStmt1 = connection.prepareStatement(query1, Statement.RETURN_GENERATED_KEYS);
            preparedStmt1.setString(1, doctor.getName());
            preparedStmt1.setString(2, doctor.getContactInformation());
            preparedStmt1.setString(3, doctor.getAddress());
            preparedStmt1.setString(4, "DOCTOR");
            preparedStmt1.setString(5, roleId);

            int affectedRows = preparedStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating doctor failed, no rows affected.");
            }

            int affectedRows1 = preparedStmt1.executeUpdate();
            if (affectedRows1 == 0) {
                throw new SQLException("Creating people failed, no rows affected.");
            }

            generatedKeys = preparedStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                doctor.setId((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating doctor failed, no ID obtained.");
            }

            generatedKeys1 = preparedStmt1.getGeneratedKeys();
            if (generatedKeys1.next()) {
                doctor.setId((int) generatedKeys1.getLong(1));
            } else {
                throw new SQLException("Creating people failed, no ID obtained.");
            }

            connection.commit();
            return doctor;

        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while creating doctor", e);
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
    public Doctor updateDoctor(Doctor doctor) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        PreparedStatement preparedStmt1 = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "UPDATE Doctor SET name=?, contactInformation=?, address=?, specialization=? WHERE id=?";
            String query1 = "UPDATE Person SET name=?, contactInformation=?, address=? WHERE roleid=? and role = ?";

            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, doctor.getName());
            preparedStmt.setString(2, doctor.getContactInformation());
            preparedStmt.setString(3, doctor.getAddress());
            preparedStmt.setString(4, doctor.getSpecialization());
            preparedStmt.setInt(5, doctor.getId());

            preparedStmt1 = connection.prepareStatement(query1);
            preparedStmt1.setString(1, doctor.getName());
            preparedStmt1.setString(2, doctor.getContactInformation());
            preparedStmt1.setString(3, doctor.getAddress());
            preparedStmt1.setInt(4, doctor.getId());
            preparedStmt1.setString(5, "DOCTOR");

            int affectedRows = preparedStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating doctor failed, no rows affected.");
            }

            int affectedRows1 = preparedStmt1.executeUpdate();
            if (affectedRows1 == 0) {
                throw new SQLException("Updating person failed, no rows affected.");
            }

            connection.commit();
            return doctor;
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while updating doctor", e);
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
                if (preparedStmt1 != null) { // Close prepared statement for Person table
                    preparedStmt1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }

    @Override
    public void deleteDoctor(int id) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        PreparedStatement preparedStmt1 = null;

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return;
            }

            String query = "DELETE FROM Doctor WHERE id=?";
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, id);
            preparedStmt.executeUpdate();

            String query1 = "DELETE FROM Person WHERE roleid=? and role = ?";
            preparedStmt1 = connection.prepareStatement(query1); // Use query1 here
            preparedStmt1.setInt(1, id);
            preparedStmt1.setString(2, "DOCTOR");
            preparedStmt1.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while deleting doctor", e);
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
                if (preparedStmt1 != null) {
                    preparedStmt1.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }

}
