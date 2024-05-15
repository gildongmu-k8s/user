package gildongmu.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import gildongmu.user.domain.user.constant.Gender;
import gildongmu.user.domain.user.entity.User;
import lombok.Builder;

import java.util.List;
import java.util.Objects;


@Builder
public record UserProfileResponse(
        Long id,
        String email,
        String nickname,
        String profilePath,
        String bio,
        @JsonInclude(JsonInclude.Include.NON_NULL) Gender gender,
        List<String> favoriteSpots,
        @JsonInclude(JsonInclude.Include.NON_NULL) Boolean isCurrentUser
) {
    public static UserProfileResponse from(User user) {
        return UserProfileResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .id(user.getId())
                .bio(user.getBio())
                .profilePath(user.getProfilePath())
                .favoriteSpots(user.getFavoriteSpots())
                .build();
    }

    public static UserProfileResponse from(User user, Long userId) {
        return UserProfileResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .gender(user.getGender())
                .id(user.getId())
                .bio(user.getBio())
                .profilePath(user.getProfilePath())
                .favoriteSpots(user.getFavoriteSpots())
                .isCurrentUser(Objects.equals(user.getId(), userId))
                .build();
    }
}
