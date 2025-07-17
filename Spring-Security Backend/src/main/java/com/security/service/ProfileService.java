package com.security.service;

import com.security.request.profilerequest.ProfileRequest;
import com.security.response.profileresponse.ProfileResponse;

public interface ProfileService {

    ProfileResponse createProfile(ProfileRequest profileRequest);
}