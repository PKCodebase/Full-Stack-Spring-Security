package com.security.service.impl;


import com.security.entity.User;
import com.security.repository.UserRepository;
import com.security.request.profilerequest.ProfileRequest;
import com.security.response.profileresponse.ProfileResponse;
import com.security.service.ProfileService;
import com.security.exception.UnableToSendEmailException;
import com.security.exception.InvalidOtpException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public ProfileServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    public ProfileResponse createProfile(ProfileRequest profileRequest) {
        User newProfile = convertToUserEntity(profileRequest);
        if (!userRepository.existsByEmail(profileRequest.getEmail().toLowerCase())) {
            newProfile = userRepository.save(newProfile);
            return convertToProfileResponse(newProfile);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exist");

    }

    @Override
    public ProfileResponse getProfile(String email) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found" + email));
        return convertToProfileResponse(existingUser);
    }

    @Override
    public void sendResetOtp(String email) {
      User existingUser =  userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found  : " + email));

        //Generate Otp

      String otp =  String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        //Calculate expiry time(current time + 15min in milliseconds)
        long expiryTime = System.currentTimeMillis() + (15 * 60 * 1000);

        //Update the profile/user
        existingUser.setResetOtp(otp);
        existingUser.setResetOtpExpireAt(expiryTime);

        //save into the database
        userRepository.save(existingUser);

      try{
          emailService.sendResetOtpEmail(existingUser.getEmail(),otp);
      }catch (Exception ex){
          throw new UnableToSendEmailException("Unable to send email");
      }


    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
        //First check user is exist or not
        User existingUser =  userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found  : " + email));

        //Check otp is matching or not
        if(existingUser.getResetOtp() == null || !existingUser.getResetOtp().equals(otp)){
            throw new InvalidOtpException(HttpStatus.BAD_REQUEST, "Invalid OTP");
        }

        //Check expire time
        if(existingUser.getResetOtpExpireAt() < System.currentTimeMillis()){
            throw new InvalidOtpException(HttpStatus.BAD_REQUEST, "OTP Expired");
        }

        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setResetOtp(null);
        existingUser.setResetOtpExpireAt(0L);

        userRepository.save(existingUser);

    }

    @Override
    public void sendOtp(String email) {
       User existingUser =  userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found : "+email));
        if(existingUser.getIsAccountVerified() != null && existingUser.getIsAccountVerified()){
            return;
        }

        //Generate Otp
        String otp =  String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        //Calculate expiry time(current time + 15min in milliseconds)
        long expiryTime = System.currentTimeMillis() + (15 * 60 * 1000);

        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpireAt(expiryTime);
        userRepository.save(existingUser);

    }

    @Override
    public void verifyOtp(String email, String otp) {

    }

    @Override
    public String getLoggedInUserId(String email) {
      User existingUser =  userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found : "+email));
        return existingUser.getUserId();
    }


    private ProfileResponse convertToProfileResponse(User newProfile) {
        return ProfileResponse.builder()
                .userId(newProfile.getUserId())
                .name(newProfile.getName())
                .email(newProfile.getEmail())
                .isAccountVerified(newProfile.getIsAccountVerified())
                .build();
    }

    private User convertToUserEntity(ProfileRequest profileRequest) {
        return User.builder()
                .userId(UUID.randomUUID().toString())
                .name(profileRequest.getName())
                .email(profileRequest.getEmail().toLowerCase()) // Normalize email
                .password(passwordEncoder.encode(profileRequest.getPassword()))
                .isAccountVerified(false)
                .resetOtp(null)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .build();
    }
}
