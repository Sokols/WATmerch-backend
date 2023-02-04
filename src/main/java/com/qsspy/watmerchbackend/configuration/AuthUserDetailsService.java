package com.qsspy.watmerchbackend.configuration;

import com.qsspy.watmerchbackend.entity.ShopUser;
import com.qsspy.watmerchbackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public AuthUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        ShopUser user = userRepository.findByUsername(s);

        if(user == null) {
            throw new UsernameNotFoundException(s);
        }

        return new AuthUser(user);
    }
}
