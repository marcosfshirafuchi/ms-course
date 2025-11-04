package com.marcosfshirafuchi.hroauth.auth;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PasswordGrantAuthenticationConverter implements AuthenticationConverter {

    @Override
    public Authentication convert(HttpServletRequest request) { // use o mesmo nome
        String grantType = request.getParameter("grant_type");
        if (!"password".equals(grantType)) return null;

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String scope = request.getParameter("scope");

        if (!StringUtils.hasText(username) || !StringUtils.hasText(password)) return null;

        Authentication clientPrincipal = (Authentication) request.getUserPrincipal();
        if (!(clientPrincipal instanceof OAuth2ClientAuthenticationToken)) return null;

        Set<String> scopes = null;
        if (StringUtils.hasText(scope)) {
            scopes = new HashSet<>(Arrays.asList(scope.split("\\s+")));
        }

        return new PasswordGrantAuthenticationToken(
                clientPrincipal, username, password, scopes, new HashMap<>()
        );
    }
}
