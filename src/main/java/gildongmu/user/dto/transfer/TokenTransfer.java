package gildongmu.user.dto.transfer;

import lombok.Builder;


@Builder
public record TokenTransfer(
        String accessToken,
        String refreshToken
) {
    public static TokenTransfer of(String accessToken, String refreshToken) {
        return TokenTransfer.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

}
