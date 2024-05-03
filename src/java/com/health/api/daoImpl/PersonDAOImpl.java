package com.health.api.daoImpl;

import com.health.api.dao.PersonDAO;
import com.health.api.db.DatabaseConfig;
import com.health.api.model.Person;
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

public class PersonDAOImpl implements PersonDAO {

    @Override
    public Person getPersonById(int id) {
        Person person = null;
        Connection connection = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "SELECT * FROM Person WHERE id = ?";
            stmt = connection.prepareStatement(query);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person();
                person.setId(rs.getInt("id"));
                person.setName(rs.getString("name"));
                person.setContactInformation(rs.getString("contactInformation"));
                person.setAddress(rs.getString("address"));
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
        return person;
    }

    @Override
    public List<Person> getAllPeople() {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        List<Person> people = new ArrayList<>();

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return Collections.emptyList();
            }

            String query = "SELECT * FROM Person";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String contactInformation = resultSet.getString("contactInformation");
                String address = resultSet.getString("address");

                Person person = new Person(id, name, contactInformation, address);
                people.add(person);
            }

            return people;
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while retrieving all people", e);
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
    public Person createPerson(Person person) {

        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }
            String query = "INSERT INTO Person (name, contactInformation, address) VALUES (?, ?, ?)";
            preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStmt.setString(1, person.getName());
            preparedStmt.setString(2, person.getContactInformation());
            preparedStmt.setString(3, person.getAddress());

            int affectedRows = preparedStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating person failed, no rows affected.");
            }

            ResultSet generatedKeys = preparedStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                person.setId((int) generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating person failed, no ID obtained.");
            }

            connection.commit();
            return person;
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while creating person", e);
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
                Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }

    @Override
    public Person updatePerson(Person person) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return null;
            }

            String query = "UPDATE Person SET name=?, contactInformation=?, address=? WHERE id=?";
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setString(1, person.getName());
            preparedStmt.setString(2, person.getContactInformation());
            preparedStmt.setString(3, person.getAddress());
            preparedStmt.setInt(4, person.getId());

            int affectedRows = preparedStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating person failed, no rows affected.");

            }
            connection.commit();
            return person;
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while updating person", e);
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
    public void deletePerson(int id) {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        ResultSet resultSet = null;
        String roleValue = null;
        int ridValue = 0;

        try {
            connection = DatabaseConfig.getInstance().getConnection();
            if (connection == null) {
                Logger.getLogger(PersonDAOImpl.class.getName()).severe("Failed to obtain database connection.");
                return;
            }

            String query = "SELECT role, roleid FROM Person WHERE id = ?";
            preparedStmt = connection.prepareStatement(query);
            preparedStmt.setInt(1, id);
            resultSet = preparedStmt.executeQuery();

            if (resultSet.next()) {
                roleValue = resultSet.getString("role");
                ridValue = resultSet.getInt("roleid");
            }

            String queryDelete = null;
            if ("DOCTOR".equals(roleValue)) {
                queryDelete = "DELETE FROM DOCTOR WHERE id=?";
            } else if ("PATIENT".equals(roleValue)) {
                queryDelete = "DELETE FROM PATIENT WHERE id=?";
            }

            if (queryDelete != null) {
                preparedStmt = connection.prepareStatement(queryDelete);
                preparedStmt.setInt(1, ridValue);
                preparedStmt.executeUpdate();
            }

            String deletePersonQuery = "DELETE FROM Person WHERE id=?";
            preparedStmt = connection.prepareStatement(deletePersonQuery);
            preparedStmt.setInt(1, id);
            preparedStmt.executeUpdate();

            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while deleting person", e);
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while rolling back transaction", ex);
                }
            }
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStmt != null) {
                    preparedStmt.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(PersonDAOImpl.class.getName()).log(Level.SEVERE, "Error occurred while closing resources", ex);
            }
        }
    }
}