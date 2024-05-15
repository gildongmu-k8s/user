package gildongmu.user.controller;

import gildongmu.user.dto.request.PasswordCheckRequest;
import gildongmu.user.dto.request.UserProfileRequest;
import gildongmu.user.dto.response.PasswordCheckResponse;
import gildongmu.user.dto.response.UserProfileResponse;
import gildongmu.user.security.UserPrincipal;
import gildongmu.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping(value = "/me")
    private ResponseEntity<UserProfileResponse> retrieveMyProfile(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.retrieveMyProfile(principal.getUser()));
    }

    @GetMapping(value = "/{userId}")
    private ResponseEntity<UserProfileResponse> retrieveMyProfile(@PathVariable Long userId,
                                                                  @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.retrieveUsersProfile(userId, principal.getUser()));
    }


    @PostMapping("/me/check-password")
    private ResponseEntity<PasswordCheckResponse> checkMyPassword(@Valid @RequestBody PasswordCheckRequest request, @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(userService.checkMyPassword(request, principal.getUser()));
    }

    @PutMapping(value = "/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<Void> modifyProfile(@Valid @RequestPart UserProfileRequest request,
                                               @RequestPart(required = false) MultipartFile image,
                                               @AuthenticationPrincipal UserPrincipal principal) {
        userService.modifyProfile(request, image, principal.getUser());
        return ResponseEntity.ok().build();
    }
}
