package admin.config.security;

import admin.repository.AdminRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private AdminRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = repository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(user.getLogin(), user.getPassword(), Collections.emptyList());
    }
}
