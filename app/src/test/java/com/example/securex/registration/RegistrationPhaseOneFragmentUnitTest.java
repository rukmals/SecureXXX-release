package com.example.securex.registration;


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RegistrationPhaseOneFragmentUnitTest {

    private static final String TEST_EMAIL = "shashimal@gmail.com";
    private static final String TEST_USERNAME = "shashimal4";

    RegistrationPhaseOneFragment registrationPhaseOneFragment = new RegistrationPhaseOneFragment();




    @Test
    public void validateEmail(){
        boolean valid =registrationPhaseOneFragment.isValidEmail(TEST_EMAIL);
        assertEquals(valid,true);
    }
    @Test
    public void validateUsername(){
        boolean valid=registrationPhaseOneFragment.isValidUsername(TEST_USERNAME);
        assertEquals(valid,true);
    }

    @Test
    public void username_email_validation(){

        String result=registrationPhaseOneFragment.Validation(TEST_USERNAME,TEST_EMAIL);
        assertEquals(result,"valid");

    }

}