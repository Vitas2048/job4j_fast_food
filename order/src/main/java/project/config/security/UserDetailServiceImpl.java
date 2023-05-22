package project.config.security;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.repository.CustomerRepository;

import java.util.Collections;
@Service
@AllArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private CustomerRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        var user = repository.getByLogin(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new User(user.getLogin(), user.getPassword(), Collections.emptyList());
    }
}
