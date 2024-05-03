
package com.health.api.dao;

import com.health.api.model.Person;
import java.util.List;


public interface PersonDAO {
    public Person getPersonById(int id);

    public List<Person> getAllPeople();

    public Person createPerson(Person person);

    public Person updatePerson(Person person);

    public void deletePerson(int id);
}
