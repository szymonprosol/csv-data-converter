package com.kodilla.csvdataconverter;

import org.springframework.batch.item.ItemProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ProductProcessor implements ItemProcessor<PersonDate, PersonAge> {

    @Override
    public PersonAge process(PersonDate item) throws Exception {

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date dateOfBirth = sdf.parse(item.getDateOfBirth());
        Date nowDate = new Date();

        @SuppressWarnings("deprecation")
        int age = nowDate.getYear() - dateOfBirth.getYear();

        return new PersonAge(item.getFirstname(), item.getSurname(), age);
    }
}
