package ru.t1.school.second_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.t1.school.second_service.model.User;
import ru.t1.school.second_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = userRepository.findByIdWithRoles(Long.valueOf(userId))
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with userId: " + userId));
        return CustomUserDetails.build(user);
    }
}
