package gildongmu.user.controller;

import gildongmu.user.dto.request.EmailCheckRequest;
import gildongmu.user.dto.request.LogInRequest;
import gildongmu.user.dto.request.SignUpRequest;
import gildongmu.user.dto.response.EmailCheckResponse;
import gildongmu.user.dto.response.TokenResponse;
import gildongmu.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping(value = "/signup", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Void> register(@Valid @RequestPart SignUpRequest request,
                                          @Valid @RequestPart(required = false) MultipartFile profile) {
        authService.register(request, profile);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    private ResponseEntity<TokenResponse> login(@Valid @RequestBody LogInRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, response.generateCookie())
                .body(response);
    }

    @PostMapping("/check-email")
    private ResponseEntity<EmailCheckResponse> checkEmail(
            @Valid @RequestBody EmailCheckRequest request) {
        return ResponseEntity.ok(authService.checkEmail(request.getEmail()));
    }

}
