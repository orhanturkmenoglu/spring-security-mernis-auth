package com.example.spring_security_mernis_auth.mernis.service;


import com.example.spring_security_mernis_auth.mernis.client.RGLKPSPublicSoap;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class MernisService {

    private final RGLKPSPublicSoap mernisService;


    public MernisService() {
        this.mernisService = new RGLKPSPublicSoap();
    }

    public Boolean validateTCKN(Long identityNumber, String firstName, String lastName, int dateOfBirth) throws Exception {

        if (identityNumber == null || identityNumber.toString().length() != 11) {
            throw new IllegalArgumentException("Invalid TCKN. It must be 11 digits.");
        }

        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First Name cannot be empty.");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last Name cannot be empty.");
        }

        try {

            if (dateOfBirth < 1900 || dateOfBirth > Calendar.getInstance().get(Calendar.YEAR)) {
                throw new IllegalArgumentException("Invalid birth year.");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid birth year format.");
        }


        return mernisService.TCKimlikNoDogrula(identityNumber, firstName, lastName, dateOfBirth);
    }
}
