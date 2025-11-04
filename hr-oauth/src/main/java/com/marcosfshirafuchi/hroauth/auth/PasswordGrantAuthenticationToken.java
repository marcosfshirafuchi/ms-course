package com.marcosfshirafuchi.hroauth.auth;

import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationGrantAuthenticationToken;

import java.util.Map;
import java.util.Set;

public class PasswordGrantAuthenticationToken extends OAuth2AuthorizationGrantAuthenticationToken {

    private final String username;
    private final String password;
    private final Set<String> scopes;

    public PasswordGrantAuthenticationToken(Authentication clientPrincipal,
                                            String username,
                                            String password,
                                            @Nullable Set<String> scopes,
                                            @Nullable Map<String, Object> additionalParameters) {
        super(new AuthorizationGrantType("password"), clientPrincipal, additionalParameters);
        this.username = username;
        this.password = password;
        this.scopes = scopes;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getScopes() {
        return scopes;
    }
}
