package com.health.api.daoImpl;

import com.health.api.dao.AppointmentDAO;
import com.health.api.db.DatabaseConfig;
import com.health.api.model.Appointment;
import com.health.api.model.Doctor;
import com.health.api.model.Patient;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppointmentDAOImpl implements AppointmentDAO {

    @Override
    public Appointment getAppointmentById(int id) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "SELECT * FROM Appointment WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int appointmentId = rs.getInt("id");
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");
                int patientId = rs.getInt("patient_id");
                int doctorId = rs.getInt("doctor_id");

                Patient patient = getPatientById(patientId);
                Doctor doctor = getDoctorById(doctorId);

                Appointment appointment = new Appointment(date, time, patient, doctor);
                appointment.setId(appointmentId);
                return appointment;
            } else {
                return null; 
            }
        } catch (SQLException e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving appointment", e);
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

    @Override
    public List<Appointment> getAppointmentsByPatientId(int patientId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Appointment> appointments = new ArrayList<>();
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return appointments;
            }

            String query = "SELECT * FROM Appointment WHERE patient_id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, patientId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("id");
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");
                int doctorId = rs.getInt("doctor_id");

                Doctor doctor = getDoctorById(doctorId);

                Appointment appointment = new Appointment(date, time, null, doctor);
                appointment.setId(appointmentId);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving appointments", e);
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
        return appointments;
    }

    @Override
    public Appointment createAppointment(Appointment appointment) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet generatedKeys = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "INSERT INTO Appointment (date, time, patient_id, doctor_id) VALUES (?, ?, ?, ?)";
            preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setDate(1, appointment.getDate());
            preparedStmt.setTime(2, appointment.getTime());
            preparedStmt.setInt(3, appointment.getPatient().getId());
            preparedStmt.setInt(4, appointment.getDoctor().getId());

            int affectedRows = preparedStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating appointment failed, no rows affected.");
            }

            generatedKeys = preparedStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                appointment.setId((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating appointment failed, no ID obtained.");
            }

            connection.commit();
            return appointment;
        } catch (SQLException e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while creating appointment", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while rolling back transaction", ex);
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
                Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }

    @Override
    public Appointment updateAppointment(Appointment appointment) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "UPDATE Appointment SET date=?, time=?, patient_id=?, doctor_id=? WHERE id=?";
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setDate(1, appointment.getDate());
            preparedStmt.setTime(2, appointment.getTime());
            preparedStmt.setInt(3, appointment.getPatient().getId());
            preparedStmt.setInt(4, appointment.getDoctor().getId());
            preparedStmt.setInt(5, appointment.getId());

            int affectedRows = preparedStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating appointment failed, no rows affected.");
            }

            connection.commit();
            return appointment;
        } catch (SQLException e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while updating appointment", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while rolling back transaction", ex);
                }
            }
            return null;
        } finally {
            try {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing prepared statement", ex);
            }
        }
    }

    @Override
    public void deleteAppointment(int id) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return;
            }

            String query = "DELETE FROM Appointment WHERE id=?";
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, id);

            preparedStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while deleting appointment", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while rolling back transaction", ex);
                }
            }
        } finally {
            try {
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing prepared statement", ex);
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

    private Doctor getDoctorById(int doctorId) {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "SELECT * FROM Doctor WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, doctorId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String contactInformation = rs.getString("contactInformation");
                String address = rs.getString("address");
                String specialization = rs.getString("specialization");
                // Create and return Doctor object
                return new Doctor(id, name, contactInformation, address, specialization);
            } else {
                return null; // No doctor found with the given ID
            }
        } catch (SQLException e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving doctor", e);
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

    @Override
    public List<Appointment> getAppointment() {
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Appointment> appointments = new ArrayList<>();
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(AppointmentDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return appointments;
            }

            String query = "SELECT * FROM Appointment";
            stmt = connection.prepareStatement(query);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int appointmentId = rs.getInt("id");
                Date date = rs.getDate("date");
                Time time = rs.getTime("time");
                int patientId = rs.getInt("patient_id");
                int doctorId = rs.getInt("doctor_id");

                // Retrieve patient and doctor objects
                Patient patient = getPatientById(patientId);
                Doctor doctor = getDoctorById(doctorId);

                // Create Appointment object
                Appointment appointment = new Appointment(date, time, patient, doctor);
                appointment.setId(appointmentId);
                appointments.add(appointment);
            }
        } catch (SQLException e) {
            Logger.getLogger(AppointmentDAOImpl.class.getName()).log(Level.SEVERE, "An error occurred while retrieving appointments", e);
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
        return appointments;
    }

}