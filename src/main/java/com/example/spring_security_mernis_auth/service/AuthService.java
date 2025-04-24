package com.example.spring_security_mernis_auth.service;

import com.example.spring_security_mernis_auth.dto.*;
import com.example.spring_security_mernis_auth.exception.InvalidTCKNException;
import com.example.spring_security_mernis_auth.exception.UserAlreadyExistsException;
import com.example.spring_security_mernis_auth.mapper.UserMapper;
import com.example.spring_security_mernis_auth.mernis.service.MernisService;
import com.example.spring_security_mernis_auth.model.Authority;
import com.example.spring_security_mernis_auth.model.User;
import com.example.spring_security_mernis_auth.repository.AuthorityRepository;
import com.example.spring_security_mernis_auth.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    private final MernisService mernisService;

    private final UserRepository userRepository;

    private final AuthorityRepository authorityRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtil jwtTokenUtil;

    private final JwtTokenCacheService jwtTokenCacheService;


    public AuthService(MernisService mernisService, UserRepository userRepository, AuthorityRepository authorityRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtTokenCacheService jwtTokenCacheService) {
        this.mernisService = mernisService;
        this.userRepository = userRepository;
        this.authorityRepository = authorityRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.jwtTokenCacheService = jwtTokenCacheService;
    }

    @Transactional
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


        String encodedPassword = passwordEncoder.encode(userRequestDto.getPassword());
        User user = UserMapper.mapToUser(userRequestDto);
        user.setPassword(encodedPassword);

        Set<Authority> authorities = new HashSet<>();

        for (Authority authority : userRequestDto.getAuthorities()) {
            authority.setUser(user);
            authorities.add(authority);
        }

        user.setAuthorities(authorities);

        User savedUser = userRepository.save(user);

        authorityRepository.saveAll(authorities);

        UserResponseDto userResponseDto = UserMapper.mapToUserResponseDto(savedUser);
        userResponseDto.setAuthorities(savedUser.getAuthorities());

        return userResponseDto;
    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        if (loginRequestDto.getUsername() == null || loginRequestDto.getPassword() == null) {
            throw new NullPointerException("Kullanıcı adı veya sifre bos olamaz.");
        }

        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword()));

            if (!authenticate.isAuthenticated()) {
                throw new BadCredentialsException("Kullanıcı adı veya sifre yanlis.");
            }

            SecurityContextHolder.getContext().setAuthentication(authenticate);

            User user = userRepository.findByUsername(loginRequestDto.getUsername()).orElseThrow(
                    () -> new UsernameNotFoundException("User not found")
            );


            String oldAccessToken = jwtTokenCacheService.getAccessToken(user.getUsername());
            System.out.println("oldAccessToken" + oldAccessToken);
            String oldRefreshToken = jwtTokenCacheService.getRefreshToken(user.getUsername());
            System.out.println("oldRefreshToken" + oldRefreshToken);

            String newAccessToken = jwtTokenUtil.generateAccessToken(user);
            System.out.println("newAccessToken" + newAccessToken);
            String newRefreshToken = jwtTokenUtil.generateRefreshToken(user);
            System.out.println("newRefreshToken" + newRefreshToken);

            if (oldAccessToken != null && oldRefreshToken != null) {
                jwtTokenCacheService.invalidateOldTokenAndStoreNew(oldAccessToken,
                        newAccessToken, newRefreshToken,
                        user.getUsername());
            }

            jwtTokenCacheService.storeAccessToken(newAccessToken, user.getUsername());
            jwtTokenCacheService.storeRefreshToken(newRefreshToken, user.getUsername());

            return new LoginResponseDto(newAccessToken, newRefreshToken);
        } catch (Exception e) {
            log.error("Exception: {}", (Object) e.getStackTrace());
            throw new BadCredentialsException("Kullanıcı adı veya sifre yanlis.");
        }
    }


    public String updatePassword(UpdatePasswordRequestDto updatePasswordRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("Kimlik doğrulama boş veya kimlik doğrulanmamıştır.");
        }

        String username = authentication.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (!passwordEncoder.matches(updatePasswordRequestDto.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Eski sifre yanlis lütfen tekrar deneyiniz !");
        }

        if (!updatePasswordRequestDto.getNewPassword().equals(updatePasswordRequestDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Sifreler uyuşmuyor lütfen tekrar deneyiniz !");
        }


        user.setPassword(passwordEncoder.encode(updatePasswordRequestDto.getNewPassword()));
        userRepository.save(user);

        return "Sifre guncellendi";
    }

    public LoginResponseDto refreshAccessToken(String refreshToken) {

        String username = jwtTokenCacheService.getRefreshToken(refreshToken);

        if (username == null) {
            throw new RuntimeException("Invalid refresh token");
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));


        if (jwtTokenCacheService.isRefreshTokenValid(refreshToken, user.getUsername())) {
            String newRefreshAccessToken = jwtTokenUtil.generateAccessToken(user);
            String newRefreshRefreshToken = jwtTokenUtil.generateRefreshToken(user);

            jwtTokenCacheService.invalidateOldTokenAndStoreNew(refreshToken,
                    newRefreshAccessToken, newRefreshRefreshToken,
                    user.getUsername());

            jwtTokenCacheService.storeAccessToken(newRefreshAccessToken, user.getUsername());
            jwtTokenCacheService.storeRefreshToken(newRefreshRefreshToken, user.getUsername());

            return new LoginResponseDto(newRefreshAccessToken, newRefreshRefreshToken);

        } else {
            throw new RuntimeException("Invalid or expired refresh token");
        }
    }


    public void deleteAllKeys() {
        jwtTokenCacheService.deleteAllKeys();
    }


    public void logout(String token) {
        token.replace("Bearer ", "");
        jwtTokenCacheService.addToBlacklist(token);
        SecurityContextHolder.clearContext();
    }


}
