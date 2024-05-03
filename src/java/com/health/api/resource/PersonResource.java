package com.health.api.resource;

import com.health.api.dao.PersonDAO;
import com.health.api.daoImpl.PersonDAOImpl;
import com.health.api.model.Person;
import com.health.api.model.ResponseBean;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/people")
public class PersonResource {

    PersonDAO personDAO = new PersonDAOImpl();
    ResponseBean responseBean = new ResponseBean();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPersonById(@PathParam("id") int id) {
        Person person = new Person();
        try {
            person = personDAO.getPersonById(id);
            if (person == null) {
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("Person not found");
                return person;
            }

            responseBean.setContent(person);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Person Retrieved Successfully");
            return person;
        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to retrieve person: " + e.getMessage());
            return person;
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Person> getPeople() {
        List<Person> people = new ArrayList<>();
        try {
            people = personDAO.getAllPeople();
            responseBean.setContent(people);
            responseBean.setContent(people);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Fetch All Patient in Successfully");
            return people;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Fetch Fail..! " + e.getMessage());
            return people;
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person createPerson(@Valid Person person) {
        Person createPerson = new Person();
        try {
            if (person.getName() == null || person.getName().isEmpty()) {
                Logger.getLogger("First name is required");
            }

            if (personDAO == null) {
                Logger.getLogger("Person DAO is not properly initialized");
            }

            createPerson = personDAO.createPerson(person);
            responseBean.setContent(createPerson);
            responseBean.setResponseCode("200");
            responseBean.setResponseMsg("Person Successfully Created");
            return createPerson;
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Person Unsuccessfully Created" + e.getMessage());
            return createPerson;
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Person updatePerson(@PathParam("id") int id, @Valid Person updatedPerson) {
        Person updatePerson = new Person();
        try {
            Person existingPerson = personDAO.getPersonById(id);

            if (existingPerson != null) {
                updatedPerson.setId(id);
                if (updatedPerson.getRoleid() == null && updatedPerson.getRole() == null) {
                    updatePerson = personDAO.updatePerson(updatedPerson);
                    if (updatePerson != null) {
                        responseBean.setContent(updatePerson);
                        responseBean.setResponseCode("200");
                        responseBean.setResponseMsg("Person Updated Successfully");
                        return updatePerson;
                    } else {
                        responseBean.setContent(null);
                        responseBean.setResponseCode("500");
                        responseBean.setResponseMsg("Internal Server Error");
                        return updatePerson;
                    }
                } else {
                    responseBean.setContent(null);
                    responseBean.setResponseCode("404");
                    responseBean.setResponseMsg("Since a role has been assigned, it should be updated from the relevant role");
                    return updatePerson;
                }
            } else {
                responseBean.setContent(null);
                responseBean.setResponseCode("404");
                responseBean.setResponseMsg("Person not found");
                return updatePerson;
            }
        } catch (Exception e) {
            responseBean.setContent(null);
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Person Updated Unsuccessfully" + e.getMessage());
        }
        return updatePerson;
    }

    @DELETE
    @Path("/{id}")
    public Person deletePerson(@PathParam("id") int id) {
        Person personToDelete = new Person();
        try {
            personToDelete = personDAO.getPersonById(id);

            if (personToDelete != null) {
                personDAO.deletePerson(id);
                responseBean.setResponseCode("200");
                responseBean.setResponseMsg(" Person ID " + id + " Successfully Deleted");
                return personToDelete;
            } else {
                return null;
            }
        } catch (Exception e) {
            responseBean.setResponseCode("400");
            responseBean.setResponseMsg("Failed to delete person: " + e.getMessage());
        }
        return personToDelete;
    }

}
