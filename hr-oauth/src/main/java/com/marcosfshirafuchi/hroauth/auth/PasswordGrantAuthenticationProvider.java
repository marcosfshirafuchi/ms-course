package com.marcosfshirafuchi.hroauth.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;

import java.security.Principal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PasswordGrantAuthenticationProvider implements org.springframework.security.authentication.AuthenticationProvider {

    private final AuthenticationManager userAuthManager;
    private final OAuth2AuthorizationService authorizationService;
    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    public PasswordGrantAuthenticationProvider(AuthenticationManager userAuthManager,
                                               OAuth2AuthorizationService authorizationService,
                                               OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        this.userAuthManager = userAuthManager;
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public Authentication authenticate(Authentication authentication) {
        PasswordGrantAuthenticationToken passwordAuth = (PasswordGrantAuthenticationToken) authentication;

        OAuth2ClientAuthenticationToken clientPrincipal =
                (OAuth2ClientAuthenticationToken) passwordAuth.getPrincipal();

        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        if (registeredClient == null ||
                !registeredClient.getAuthorizationGrantTypes().contains(new AuthorizationGrantType("password"))) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.UNAUTHORIZED_CLIENT);
        }

        Authentication user = userAuthManager.authenticate(
                new UsernamePasswordAuthenticationToken(passwordAuth.getUsername(), passwordAuth.getPassword()));

        Set<String> authorizedScopes = new HashSet<>(registeredClient.getScopes());
        if (passwordAuth.getScopes() != null && !passwordAuth.getScopes().isEmpty()) {
            authorizedScopes.retainAll(passwordAuth.getScopes());
        }

        OAuth2TokenContext accessContext = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(user)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .tokenType(OAuth2TokenType.ACCESS_TOKEN)
                .authorizedScopes(authorizedScopes)
                .authorizationGrantType(new AuthorizationGrantType("password"))
                .authorizationGrant(passwordAuth)
                .build();

        OAuth2Token access = tokenGenerator.generate(accessContext);
        if (access == null) {
            throw new OAuth2AuthenticationException(OAuth2ErrorCodes.SERVER_ERROR);
        }

        // ⬇️ Ajuste aqui
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plus(registeredClient.getTokenSettings().getAccessTokenTimeToLive());

        OAuth2AccessToken bearer = new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                access.getTokenValue(),
                issuedAt,
                expiresAt,
                authorizedScopes
        );

        OAuth2Authorization authorization = OAuth2Authorization.withRegisteredClient(registeredClient)
                .principalName(user.getName())
                .authorizationGrantType(new AuthorizationGrantType("password"))
                .attribute(Principal.class.getName(), user)
                .accessToken(bearer)
                .build();

        authorizationService.save(authorization);

        return new OAuth2AccessTokenAuthenticationToken(
                registeredClient, clientPrincipal, bearer, null, Map.of());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return PasswordGrantAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
