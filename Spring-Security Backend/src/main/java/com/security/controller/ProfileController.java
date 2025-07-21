package com.security.controller;

import com.security.request.profilerequest.ProfileRequest;
import com.security.response.profileresponse.ProfileResponse;
import com.security.service.ProfileService;
import com.security.service.impl.EmailService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ProfileController {

    private final ProfileService profileService;

    private final EmailService emailService;



    public ProfileController(ProfileService profileService, EmailService emailService) {
        this.profileService = profileService;
        this.emailService = emailService;
    }


    //1.
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid @RequestBody ProfileRequest profileRequest){

        ProfileResponse response = profileService.createProfile(profileRequest);
        emailService.sendWelcomeEmail(response.getEmail(),response.getName());
        return response;

    }

    //2.
    @GetMapping("/profile")
   public ProfileResponse getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email){
        return profileService.getProfile(email);

   }


}
