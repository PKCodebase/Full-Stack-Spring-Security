package com.security.controller;

import com.security.request.authrequest.AuthRequest;
import com.security.request.resetpasswordrequest.ResetPasswordRequest;
import com.security.response.authresponse.AuthResponse;
import com.security.service.ProfileService;
import com.security.service.impl.AppUserDetailsServiceImpl;
import com.security.util.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsServiceImpl appUserDetailsService;
    private final JwtUtil jwtUtil;
    private final ProfileService profileService;

    public AuthController(AuthenticationManager authenticationManager, AppUserDetailsServiceImpl appUserDetailsService, JwtUtil jwtUtil, ProfileService profileService) {
        this.authenticationManager = authenticationManager;
        this.appUserDetailsService = appUserDetailsService;
        this.jwtUtil = jwtUtil;
        this.profileService = profileService;
    }

    //3.
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest authRequest){
        try{
            authenticate(authRequest.getEmail(),authRequest.getPassword());
            final UserDetails userDetails = appUserDetailsService.loadUserByUsername(authRequest.getEmail());
            final String token = jwtUtil.generateToken(userDetails);
            ResponseCookie cookie = ResponseCookie.from("jwt",token)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                    .body(new AuthResponse(authRequest.getEmail(),token));
        }catch (BadCredentialsException ex){
            Map<String,Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message","Incorrect Email or Password");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }catch(DisabledException ex){
            Map<String,Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message","Account is Disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }catch(Exception ex){
            Map<String,Object> error = new HashMap<>();
            error.put("error",true);
            error.put("message","Authentication failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

    }

    private void authenticate(String email, String password) throws Exception {
        try {
            authenticationManager.authenticate(new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }

    //4.
    @GetMapping("/isAuthenticated")
    public  ResponseEntity<Boolean> isAuthenticated(@CurrentSecurityContext(expression = "authentication?.name")String email){
        return ResponseEntity.ok(email != null);
}


    //5.
    @PostMapping("/send-reset-otp")
    public void sendResetOtp(@RequestParam String email){
        try{
            profileService.sendResetOtp(email);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
        }
    }

    //6.
    @PostMapping("/reset-password")
    public void resetPassword(@Valid @RequestBody ResetPasswordRequest request){
        try{
            profileService.resetPassword(request.getEmail(),request.getOtp(),request.getNewPassword());
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
        }
    }

    @PostMapping("/send-otp")
    public void sendVerifyOtp(@CurrentSecurityContext(expression = "authentication?.name") String email){
        try{
            profileService.sendOtp(email);
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
        }
    }
    @PostMapping("/verify-otp")
    public void verifyEmail(@RequestBody Map<String,Object> request,@CurrentSecurityContext(expression = "authentication?.name") String email){

        if(request.get("otp").toString() == null){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST,"Missing Details");
        }
        try{
            profileService.verifyOtp(email,request.get("otp").toString());
        }catch (Exception ex){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
    }

}


//http://localhost:9001/swagger-ui/index.html