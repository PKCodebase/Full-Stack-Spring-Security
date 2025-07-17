package com.security.controller;

import com.security.request.profilerequest.ProfileRequest;
import com.security.response.profileresponse.ProfileResponse;
import com.security.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid @RequestBody ProfileRequest profileRequest){
        return profileService.createProfile(profileRequest);

    }

    @GetMapping
    public String test(){
        return "Api is working";
    }

}
