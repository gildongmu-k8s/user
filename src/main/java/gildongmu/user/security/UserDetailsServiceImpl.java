package gildongmu.user.security;

import gildongmu.user.domain.user.repository.UserRepository;
import gildongmu.user.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new UserPrincipal(userRepository.findByEmail(username)
                .orElseThrow(
                        () -> new UsernameNotFoundException(ErrorCode.USER_NOT_FOUND.getMessage())));
    }
}
