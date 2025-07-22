package ru.podolian.springcourse.dao;

import org.jspecify.annotations.Nullable;
import org.springframework.jdbc.core.RowMapper;
import ru.podolian.springcourse.models.Person;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonMapper implements RowMapper<Person> {

    @Override
    public @Nullable Person mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Person person = new Person();

        person.setId(resultSet.getInt("id"));
        person.setName(resultSet.getString("name"));
        person.setEmail(resultSet.getString("email"));
        person.setAge(resultSet.getInt("age"));

        return person;
    }
}
