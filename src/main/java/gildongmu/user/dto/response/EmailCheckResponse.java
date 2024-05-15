package gildongmu.user.dto.response;

import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record EmailCheckResponse(
    boolean isUsable,
    LocalDateTime timestamp
) {
    public static EmailCheckResponse of(boolean isUsable) {
        return EmailCheckResponse.builder()
            .isUsable(isUsable)
            .timestamp(LocalDateTime.now())
            .build();
    }
}
