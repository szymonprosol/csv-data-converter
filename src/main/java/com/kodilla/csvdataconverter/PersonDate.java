package com.kodilla.csvdataconverter;

import java.util.Date;

public class PersonDate {

    private String firstname;
    private String surname;
    private String dateOfBirth;

    public PersonDate() {
    }

    public PersonDate(String firstname, String surname, String dateOfBirth) {
        this.firstname = firstname;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
}
