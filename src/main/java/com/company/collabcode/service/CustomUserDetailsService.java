package com.company.collabcode.service;

import com.company.collabcode.database.UserRepository;
import com.company.collabcode.model.CustomUserDetails;
import com.company.collabcode.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {
        // handle if user not found too...
        User user = userRepository.findByEmailAddress(emailAddress);
        if(user == null)
            throw new UsernameNotFoundException("User not found!!!");
        return new CustomUserDetails(user);
    }

}
