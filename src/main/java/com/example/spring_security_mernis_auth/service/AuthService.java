package com.example.spring_security_mernis_auth.service;

import com.example.spring_security_mernis_auth.dto.LoginRequestDto;
import com.example.spring_security_mernis_auth.dto.LoginResponseDto;
import com.example.spring_security_mernis_auth.dto.UserRequestDto;
import com.example.spring_security_mernis_auth.dto.UserResponseDto;
import com.example.spring_security_mernis_auth.exception.InvalidTCKNException;
import com.example.spring_security_mernis_auth.exception.UserAlreadyExistsException;
import com.example.spring_security_mernis_auth.mapper.UserMapper;
import com.example.spring_security_mernis_auth.mernis.service.MernisService;
import com.example.spring_security_mernis_auth.model.User;
import com.example.spring_security_mernis_auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final MernisService mernisService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;


    public AuthService(MernisService mernisService, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.mernisService = mernisService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    private User createUser(UserRequestDto userRequestDto) {
        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        User user = UserMapper.mapToUser(userRequestDto);
        user.setPassword(encodedPassword);

        return user;
    }

    public UserResponseDto register(UserRequestDto userRequestDto) throws Exception {

        boolean isTcknValid;
        try {
            isTcknValid = mernisService.validateTCKN(
                    userRequestDto.getIdentityNumber(),
                    userRequestDto.getFirstName().toUpperCase(new Locale("tr", "TR")),
                    userRequestDto.getLastName().toUpperCase(new Locale("tr", "TR")),
                    userRequestDto.getBirthYear());
        } catch (Exception e) {
            throw new InvalidTCKNException("T.C. Kimlik No doğrulaması sırasında bir hata oluştu.");
        }

        if (!isTcknValid) {
            throw new InvalidTCKNException("T.C. Kimlik No geçerli değil.");
        }

        if (userRepository.existsByUsername(userRequestDto.getUsername())) {
            throw new UserAlreadyExistsException("Bu kullanıcı adı zaten mevcut.");
        }

        User user = createUser(userRequestDto);

        User savedUser = userRepository.save(user);

        return UserMapper.mapToUserResponseDto(savedUser);
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        if (loginRequestDto.getUsername() == null || loginRequestDto.getPassword() == null) {
            return new LoginResponseDto("Kullanıcı adı veya sifre bos olamaz.");
        }

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

            if (!authenticate.isAuthenticated()) {
                return new LoginResponseDto("Kullanıcı adı veya sifre yanlis.");
            }

            SecurityContextHolder.getContext().setAuthentication(authenticate);

            return new LoginResponseDto("Kullanıcı girisi basarili.");
        } catch (Exception e) {
            return new LoginResponseDto("Kullanıcı adı veya sifre yanlis.");
        }
    }

    public String logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            HttpSession session = request.getSession();
            if (session != null) {
                session.invalidate();
            }
            SecurityContextHolder.clearContext();
            return "Cikis basarili.";
        } catch (Exception e) {
            log.error("Logout error: {}", e.getMessage());
            return "Cikis basarisiz.";
        }
    }

}
