package com.example.securex.utils;

import android.content.Context;

import com.example.securex.data.UserRepository;

public class ForgetPassword {

    public static String sendEmail(Context context) {

        String code = getAlphaNumericString();

        UserRepository userRepository = new UserRepository(context);
        String Email = userRepository.getEmail();
        String message = "Confirmation Code is: " + code;
        String subject = "Password Forget";

        try {
            JavaMailAPI javaMailAPI = new JavaMailAPI(context, Email, subject, message);
            javaMailAPI.execute();

        } catch (Exception e) {

        }
        return code;


    }

    static String getAlphaNumericString()
    {

        // chose a Character random from this String
        String AlphaNumericString = "0123456789";


        // create StringBuffer size of AlphaNumericString
        StringBuilder sb = new StringBuilder(4);

        for (int i = 0; i < 4; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            int index
                    = (int)(AlphaNumericString.length()
                    * Math.random());

            // add Character one by one in end of sb
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }
}
