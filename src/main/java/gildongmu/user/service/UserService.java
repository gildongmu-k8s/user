package gildongmu.user.service;

import gildongmu.user.client.S3Client;
import gildongmu.user.domain.user.entity.User;
import gildongmu.user.domain.user.repository.UserRepository;
import gildongmu.user.dto.request.PasswordCheckRequest;
import gildongmu.user.dto.request.UserProfileRequest;
import gildongmu.user.dto.response.PasswordCheckResponse;
import gildongmu.user.dto.response.UserProfileResponse;
import gildongmu.user.dto.transfer.UserProfileUpdatedEvent;
import gildongmu.user.exception.ErrorCode;
import gildongmu.user.exception.UserException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3Client s3Client;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UserProfileResponse retrieveMyProfile(User user) {
        return UserProfileResponse.from(user);
    }

    public UserProfileResponse retrieveUsersProfile(Long userId, User user) {
        return UserProfileResponse.from(userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND)), user.getId());
    }

    @Transactional
    public void modifyProfile(UserProfileRequest request, MultipartFile image, User user) {
        User dbUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        dbUser.update(request.getNickname(), request.getBio(), request.getFavoriteSpots());
        updatePasswordIfNeeded(request.getIsPasswordChanged(), request.getPassword(), dbUser);
        updateProfileImageIfNeeded(Optional.ofNullable(image), dbUser);

        applicationEventPublisher.publishEvent(new UserProfileUpdatedEvent(user.getId()));

    }

    public void updatePasswordIfNeeded(Boolean isPasswordChanged, Optional<String> maybePassword, User dbUser) {
        if (isPasswordChanged)
            dbUser.updatePassword(passwordEncoder.encode(
                    maybePassword.orElseThrow(() -> new UserException(ErrorCode.PASSWORD_NOT_VALID))));
    }

    public void updateProfileImageIfNeeded(Optional<MultipartFile> maybeImage, User dbUser) {
        if (maybeImage.isEmpty()) return;
        s3Client.delete(dbUser.getProfilePath());
        dbUser.updateProfilePath(s3Client.upload(maybeImage.get()));
    }

    public PasswordCheckResponse checkMyPassword(PasswordCheckRequest request, User user) {
        User dbUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return PasswordCheckResponse.of(passwordEncoder.matches(request.getPassword(), dbUser.getPassword()));
    }
}
