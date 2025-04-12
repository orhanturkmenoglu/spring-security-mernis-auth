package com.example.spring_security_mernis_auth.service;

import com.example.spring_security_mernis_auth.dto.LoginRequestDto;
import com.example.spring_security_mernis_auth.dto.LoginResponseDto;
import com.example.spring_security_mernis_auth.exception.InvalidTCKNException;
import com.example.spring_security_mernis_auth.exception.UserAlreadyExistsException;
import com.example.spring_security_mernis_auth.mernis.service.MernisService;
import com.example.spring_security_mernis_auth.model.User;
import com.example.spring_security_mernis_auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MernisService mernisService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(MernisService mernisService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.mernisService = mernisService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private User createUser(LoginRequestDto loginRequestDto) {

        String encodedPassword = passwordEncoder.encode(loginRequestDto.getPassword());

        User user = new User();
        user.setUsername(loginRequestDto.getUsername());
        user.setPassword(encodedPassword);
        user.setFirstName(loginRequestDto.getFirstName());
        user.setLastName(loginRequestDto.getLastName());
        user.setBirthYear(loginRequestDto.getBirthYear());
        user.setIdentityNumber(loginRequestDto.getIdentityNumber());
        user.setRole(loginRequestDto.getRole());

        return user;
    }

    public LoginResponseDto register(LoginRequestDto loginRequestDto) throws Exception {

        boolean isTcknValid;
        try {
            isTcknValid = mernisService.validateTCKN(
                    Long.parseLong(loginRequestDto.getIdentityNumber()),
                    loginRequestDto.getFirstName(),
                    loginRequestDto.getLastName(),
                    loginRequestDto.getBirthYear());
            log.info("T.C. Kimlik No dogrulandi.", loginRequestDto.getIdentityNumber());
        } catch (Exception e) {
            throw new InvalidTCKNException("T.C. Kimlik No doğrulaması sırasında bir hata oluştu.");
        }

        if (!isTcknValid) {
            throw new InvalidTCKNException("T.C. Kimlik No geçerli değil.");
        }

        if (userRepository.existsByUsername(loginRequestDto.getUsername())) {
            throw new UserAlreadyExistsException("Bu kullanıcı adı zaten mevcut.");
        }

        User user = createUser(loginRequestDto);

        userRepository.save(user);

        return new LoginResponseDto("Kullanıcı başarıyla kaydedildi.");
    }
}
