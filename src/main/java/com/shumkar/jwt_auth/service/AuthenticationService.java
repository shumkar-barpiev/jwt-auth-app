package com.shumkar.jwt_auth.service;

import com.shumkar.jwt_auth.dto.LoginUserDto;
import com.shumkar.jwt_auth.dto.RegisterUserDto;
import com.shumkar.jwt_auth.dto.VerifyUserDto;
import com.shumkar.jwt_auth.model.User;
import com.shumkar.jwt_auth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;

    public AuthenticationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.emailService = emailService;
    }

    public User signup(RegisterUserDto registerUserDto) {
        User user = new User(
                registerUserDto.getUsername(),
                registerUserDto.getEmail(),
                this.passwordEncoder.encode(registerUserDto.getPassword())
        );

        user.setVerificationCode(generateVerificationCode());
        user.setVerificationExpiration(LocalDateTime.now().plusMinutes(60));
        user.setEnabled(false);
        sendVerificationEmail(user);

        return this.userRepository.save(user);
    }

    public User authenticate(LoginUserDto loginUserDto) {
        User user  = this.userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow(() -> new RuntimeException("user not found"));

        if(!user.isEnabled()) {
            throw new RuntimeException("Account not verified, please verify your email.");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUserDto.getEmail(),
                        loginUserDto.getPassword()
                )
        );

        return user;
    }

    public void verifyUser(VerifyUserDto verifyUserDto) {
        Optional<User> optionalUser = this.userRepository.findByEmail(verifyUserDto.getEmail());

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            if(user.getVerificationExpiration().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Verification code has been expired");
            }

            if(user.getVerificationCode().equals(verifyUserDto.getVerificationCode())) {
                user.setEnabled(true);
                user.setVerificationCode(null);
                user.setVerificationExpiration(null);

                this.userRepository.save(user);
            } else {
                throw new RuntimeException("Invalid verification code");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public void resendVerificationCode(String email) {
        Optional<User> optionalUser = this.userRepository.findByEmail(email);

        if(optionalUser.isPresent()) {
            User user = optionalUser.get();

            if(user.isEnabled()) {
                throw new RuntimeException("Account is already verified");
            }

            user.setVerificationCode(generateVerificationCode());
            user.setVerificationExpiration(LocalDateTime.now().plusMinutes(60));
            sendVerificationEmail(user);
            this.userRepository.save(user);
        } else{
            throw new RuntimeException("User not found");
        }

    }

    private String generateVerificationCode() {
        Random random = new Random();
        int randomInt = random.nextInt(999999) * 100000;

        return String.valueOf(randomInt);
    }

    public void sendVerificationEmail(User user) {
        String subject = "Account verfication";
        String verificationCode = user.getVerificationCode();

        String textBody = "<p>Dear " + user.getUsername() + ",</p>"
                + "<p>Thank you for registering. Please use the following verification code to activate your account:</p>"
                + "<h3>" + verificationCode + "</h3>"
                + "<p>This code will expire in 60 minutes.</p>"
                + "<br>"
                + "<p>Best regards,<br>The Team</p>";
        try {
            this.emailService.sendVerificationEmail(user.getEmail(), subject, textBody);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send verification email", e);
        }

    }

}
