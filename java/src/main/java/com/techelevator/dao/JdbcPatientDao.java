package com.techelevator.dao;

import com.techelevator.model.Doctor;
import com.techelevator.model.Patient;
import org.springframework.beans.NullValueInNestedPathException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcPatientDao implements PatientDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcPatientDao(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Patient getPatientById(int patientId){
        Patient patient = null;
        String sql = "SELECT * " + "FROM patient " + "WHERE patient_id = ?" ;
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, patientId);

        if(results.next()){
            patient = mapRowToPatient(results);
        }
        return patient;
    }




    @Override
    public List<Patient> findAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patient;";
        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql);
            while(result.next()) {
                patients.add(mapRowToPatient(result));
            }
        } catch (NullValueInNestedPathException | EmptyResultDataAccessException e) {
            throw new RuntimeException("No patient found");
        }
        return patients;
    }

    @Override
    public int findPatientIdByPatientLastName(String patientLastName) {
        if (patientLastName == null) throw new IllegalArgumentException("Last name cannot be null");

        int patientId;
        try {
            patientId = jdbcTemplate.queryForObject("SELECT patient_id FROM patient WHERE last_name = ?", int.class, patientLastName);
        } catch (NullPointerException | EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("User " + patientLastName + " was not found.");
        }
        return patientId;
    }

    @Override
    public void create(Patient patient) {
        String sql = "INSERT INTO patient(user_id, first_name, last_name, address, city, states, zipcode, email_address, patient_number, birthdate)" +
                "VALUES ((SELECT MAX(user_id) FROM users), ?, ?, ?, ?, ?, ?, ?, ?, ?);" ;
        jdbcTemplate.update(sql, patient.getUserId(), patient.getFirstName(), patient.getLastName(), patient.getAddress(), patient.getCity(), patient.getStates(), patient.getZipcode(), patient.getEmailAddress(), patient.getPatientNumber(), patient.getBirthdate());

    }

    @Override
    public void updatePatient(int patientId, Patient patient) {
        String sql = "UPDATE patient SET patient_id=?, first_name=?, last_name=?, address=?, city=?, states=?, zipcode=?, email_address=?, patient_number=?, birthdate=? WHERE patient_id = ?;";
        jdbcTemplate.update(sql,patient, patientId);

    }

    @Override
    public void deletePatientById(int patientId) {
        String sql = "delete from patient where patient_id = ?";
        jdbcTemplate.update(sql, patientId);

    }



    private Patient mapRowToPatient(SqlRowSet results) {
        Patient patient = new Patient();
        patient.setPatientId(results.getInt("patient_id"));
        patient.setUserId(results.getInt("user_id"));
        patient.setFirstName(results.getString("first_name"));
        patient.setLastName(results.getString("last_name"));
        patient.setAddress(results.getString("address"));
        patient.setCity(results.getString("city"));
        patient.setStates(results.getString("states"));
        patient.setZipcode(results.getString("zipcode"));
        patient.setEmailAddress(results.getString("email_address"));
        patient.setPatientNumber(results.getString("patient_number"));
        patient.setBirthdate(results.getDate("birthdate"));

        return patient;

    }


}


