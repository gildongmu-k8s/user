package gildongmu.user.service;


import gildongmu.user.client.S3Client;
import gildongmu.user.domain.user.constant.Role;
import gildongmu.user.domain.user.entity.User;
import gildongmu.user.domain.user.repository.UserRepository;
import gildongmu.user.dto.request.LogInRequest;
import gildongmu.user.dto.request.SignUpRequest;
import gildongmu.user.dto.response.EmailCheckResponse;
import gildongmu.user.dto.response.TokenResponse;
import gildongmu.user.exception.AuthException;
import gildongmu.user.util.JwtTokenManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static gildongmu.user.exception.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenManager jwtTokenManager;
    private final S3Client s3Client;

    public void register(SignUpRequest request, MultipartFile profile) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new AuthException(ALREADY_REGISTERED_EMAIL);
        }

        userRepository.save(
                User.builder()
                        .role(Role.ROLE_USER)
                        .email(request.getEmail())
                        .bio(request.getBio())
                        .gender(request.getGender())
                        .dateOfBirth(request.getDayOfBirth())
                        .nickname(request.getNickname())
                        .profilePath(Optional.ofNullable(profile)
                                .map(s3Client::upload).orElse(null))
                        .password(passwordEncoder.encode(request.getPassword()))
                        .favoriteSpots(request.getFavoriteSpots())
                        .build()
        );
    }


    public TokenResponse login(LogInRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AuthException(USER_NOT_FOUND));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AuthException(PASSWORD_UNMATCHED);
        }

        return TokenResponse.of(jwtTokenManager.generateAccessToken(user.getEmail()),
                jwtTokenManager.generateRefreshToken(user.getEmail()));
    }

    public EmailCheckResponse checkEmail(String email) {
        return EmailCheckResponse.of(!userRepository.existsByEmail(email));
    }
}
