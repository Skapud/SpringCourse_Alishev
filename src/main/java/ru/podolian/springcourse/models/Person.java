package ru.podolian.springcourse.models;

import jakarta.validation.constraints.*;

public class Person {
    private int personId;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 5, max = 100, message = "Name should be between 5 and 100 characters")
    private String name;

    @Min(value = 1901, message = "Year of birth should be greater than 1900")
    @Max(value = 2019, message = "Year of birth should be lower than 2020")
    private int year;

    public Person(){}

    public Person(int id, String name, int year) {
        this.personId = id;
        this.name = name;
        this.year = year;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
