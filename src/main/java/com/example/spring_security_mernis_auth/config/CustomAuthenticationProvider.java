package com.example.spring_security_mernis_auth.config;

import com.example.spring_security_mernis_auth.mernis.service.MernisService;
import com.example.spring_security_mernis_auth.model.User;
import com.example.spring_security_mernis_auth.repository.UserRepository;
import com.example.spring_security_mernis_auth.service.CustomUserDetailsService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MernisService mernisService; // Mernis servisi
    private final UserRepository userRepository; // UserRepository
    private final CustomUserDetailsService userDetailsService;

    public CustomAuthenticationProvider(MernisService mernisService, UserRepository userRepository, CustomUserDetailsService userDetailsService) {
        this.mernisService = mernisService;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // Giriş yapan kullanıcının bilgilerini alıyoruz
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // UserRepository kullanarak kullanıcıyı veritabanından buluyoruz
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        // Mernis doğrulamasını yapıyoruz
        boolean isTcknValid = false;
        try {
            isTcknValid = Boolean.parseBoolean(mernisService.validateTCKN(Long.parseLong(user.getIdentityNumber()), user.getFirstName(),
                    user.getLastName(), user.getBirthYear()).toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!isTcknValid) {
            throw new BadCredentialsException("Invalid T.C. Kimlik No.");
        }

        // Şifreyi kontrol ediyoruz
        if (!user.getPassword().equals(password)) {
            throw new BadCredentialsException("Invalid password.");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());

    }


    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
