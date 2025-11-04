package com.marcosfshirafuchi.hroauth.security;

import com.marcosfshirafuchi.hroauth.dto.RoleDTO;
import com.marcosfshirafuchi.hroauth.dto.UserDTO;
import com.marcosfshirafuchi.hroauth.feign.UserFeignClient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class RemoteUserDetailsService implements UserDetailsService {

    private final UserFeignClient users;

    public RemoteUserDetailsService(UserFeignClient users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO dto = null;
        try {
            dto = users.findByEmailQuery(username);
        } catch (Exception ignored) {
        }
        if (dto == null) {
            try {
                dto = users.findByEmailPath(username);
            } catch (Exception ignored) {
            }
        }
        if (dto == null || dto.password() == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }
        List<GrantedAuthority> auths = dto.roles() == null ? List.of() :
                dto.roles().stream()
                        .map(RoleDTO::roleName)
                        .filter(Objects::nonNull)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return User.withUsername(dto.email())
                .password(dto.password())
                .authorities(auths)
                .accountExpired(false).accountLocked(false)
                .credentialsExpired(false).disabled(false)
                .build();
    }
}
