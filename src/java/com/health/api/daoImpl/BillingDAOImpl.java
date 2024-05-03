package com.health.api.daoImpl;

import com.health.api.dao.BillingDAO;
import com.health.api.db.DatabaseConfig;
import com.health.api.model.Appointment;
import com.health.api.model.Billing;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BillingDAOImpl implements BillingDAO {

    @Override
    public Billing getBillingById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Billing billing = null;

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(BillingDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null; 
            }

            String query = "SELECT * FROM Billing WHERE id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Appointment appointment = new Appointment(); 
                appointment.setId(resultSet.getInt("appointment_id"));

                double amount = resultSet.getDouble("amount");
                String paymentStatus = resultSet.getString("paymentStatus");

                billing = new Billing(appointment, amount, paymentStatus);
                billing.setId(id);
            }
        } catch (SQLException e) {
            Logger.getLogger(BillingDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while retrieving billing by ID", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BillingDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }

        return billing;
    }

    @Override
    public List<Billing> getBillingByAppointmentId(int appointmentId) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Billing> billings = new ArrayList<>();

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(BillingDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return billings;
            }

            String query = "SELECT * FROM Billing WHERE appointment_id = ?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, appointmentId);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                double amount = resultSet.getDouble("amount");
                String paymentStatus = resultSet.getString("payment_status");

                Appointment appointment = new Appointment();
                appointment.setId(resultSet.getInt("appointment_id"));

                Billing billing = new Billing(appointment, amount, paymentStatus);
                billing.setId(id);
                billings.add(billing);
            }
        } catch (SQLException e) {
            Logger.getLogger(BillingDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while retrieving billing by appointment ID", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BillingDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }

        return billings;
    }

    @Override
    public Billing createBilling(Billing billing) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        Billing createdBilling = null;

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(BillingDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "INSERT INTO Billing (appointment_id, amount, paymentStatus) VALUES (?, ?, ?)";
            statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, billing.getAppointment().getId());
            statement.setDouble(2, billing.getAmount());
            statement.setString(3, billing.getPaymentStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                Logger.getLogger(BillingDAOImpl.class.getName()).severe("Creating billing failed, no rows affected.");
                return null;
            }

            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);
                createdBilling = new Billing(billing.getAppointment(), billing.getAmount(), billing.getPaymentStatus());
                createdBilling.setId(id);
            } else {
                Logger.getLogger(BillingDAOImpl.class.getName()).severe("Creating billing failed, no ID obtained.");
            }
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(BillingDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while creating billing", e);
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BillingDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }

        return createdBilling;
    }

    @Override
    public Billing updateBilling(Billing billing) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Billing updatedBilling = null;

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(BillingDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "UPDATE Billing SET appointment_id=?, amount=?, payment_status=? WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, billing.getAppointment().getId());
            statement.setDouble(2, billing.getAmount());
            statement.setString(3, billing.getPaymentStatus());
            statement.setInt(4, billing.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                Logger.getLogger(BillingDAOImpl.class.getName()).severe("Updating billing failed, no rows affected.");
                return null;
            }

            updatedBilling = billing;
        } catch (SQLException e) {
            Logger.getLogger(BillingDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while updating billing", e);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BillingDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }

        return updatedBilling;
    }

    @Override
    public void deleteBilling(int id) {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(BillingDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return; 
            }

            String query = "DELETE FROM Billing WHERE id=?";
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);

            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                Logger.getLogger(BillingDAOImpl.class.getName()).severe("Deleting billing failed, no rows affected.");
            } else {
                Logger.getLogger(BillingDAOImpl.class.getName()).info("Billing with ID " + id + " deleted successfully.");
            }
        } catch (SQLException e) {
            Logger.getLogger(BillingDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while deleting billing", e);
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(BillingDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }

}