package gildongmu.user.dto.transfer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserProfileUpdatedEvent {
    private Long userId;
}
