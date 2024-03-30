package com.gabriel.course.projectapi2.jwt;

import com.gabriel.course.projectapi2.model.User;
import com.gabriel.course.projectapi2.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {
    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        return new JwtUserDetails(user);
    }

    public JwtToken getToken(String username) {
        User.Role role = userService.findRoleByUsername(username);
        return  JwtUtils.createToken(username, role.name().substring("ROLE_".length()));
    }
}