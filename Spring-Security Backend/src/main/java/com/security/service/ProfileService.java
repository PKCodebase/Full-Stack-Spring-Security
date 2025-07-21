package com.security.service;

import com.security.request.profilerequest.ProfileRequest;
import com.security.response.profileresponse.ProfileResponse;

public interface ProfileService {

    ProfileResponse createProfile(ProfileRequest profileRequest);

     ProfileResponse getProfile(String email);

    void sendResetOtp(String email);

    void resetPassword(String email,String otp,String newPassword);

    void sendOtp(String email);

    void verifyOtp(String email,String otp);

    String getLoggedInUserId(String email);


}