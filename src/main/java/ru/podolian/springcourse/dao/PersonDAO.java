package ru.podolian.springcourse.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import ru.podolian.springcourse.models.Person;

import java.util.List;

@Component
public class PersonDAO {
    private final JdbcTemplate jdbcTemplate;
    private final SpringResourceTemplateResolver springResourceTemplateResolver;

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate, SpringResourceTemplateResolver springResourceTemplateResolver) {
        this.jdbcTemplate = jdbcTemplate;
        this.springResourceTemplateResolver = springResourceTemplateResolver;
    }

    public List<Person> index() {
        return jdbcTemplate.query("SELECT * FROM Person", new BeanPropertyRowMapper<>(Person.class));
    }

    public Person show(int personId) {
        return jdbcTemplate.query("SELECT * FROM Person WHERE person_id=?", new Object[]{personId}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny().orElse(null);
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO Person (name, year) VALUES(?,?)", person.getName(),
                person.getYear());
    }

    public void update(int personId, Person updatedPerson) {
        jdbcTemplate.update("UPDATE Person SET name=?, year=? WHERE person_id=?", updatedPerson.getName(),
                updatedPerson.getYear(), personId);

    }

    public void delete(int personId) {
        jdbcTemplate.update("DELETE FROM Person WHERE person_id=?", personId);
    }
}
