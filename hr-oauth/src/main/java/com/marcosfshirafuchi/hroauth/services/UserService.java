package com.marcosfshirafuchi.hroauth.services;

import com.marcosfshirafuchi.hroauth.dto.RoleDTO;
import com.marcosfshirafuchi.hroauth.dto.UserDTO;
import com.marcosfshirafuchi.hroauth.feign.UserFeignClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserFeignClient userFeignClient;

    public UserDTO findByEmail(String email) {
        UserDTO user = userFeignClient.findByEmailQuery(email);
        if (user == null) {
            logger.error("Email not found: {}", email);
            throw new IllegalArgumentException("Email not found");
        }
        logger.info("Email found: {}", email);
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userFeignClient.findByEmailQuery(username);
        if (user == null) {
            logger.error("Email not found: {}", username);
            throw new UsernameNotFoundException("Email not found");
        }
        logger.info("Email found: {}", username);

        // Converte a lista de roles do DTO em authorities para o Spring Security
        List<GrantedAuthority> authorities = user.roles().stream()
                .map(RoleDTO::roleName)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Cria o User do Spring Security com as authorities corretas
        return new User(user.email(), user.password(), authorities);
    }
}
