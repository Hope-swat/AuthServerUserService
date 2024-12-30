package com.scaler.userservicejuly24.security.services;

import com.scaler.userservicejuly24.models.User;
import com.scaler.userservicejuly24.repositories.UserRepository;
import com.scaler.userservicejuly24.security.models.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService  {
    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    
    //will get email as username from request, will fetch data from Users table based on email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("User with the email : " + email + " not found");
        }

        //Convert user object into an object of type UserDetails.
        return new CustomUserDetails(optionalUser.get());
    }
}
