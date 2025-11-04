package com.marcosfshirafuchi.hroauth.config;

import com.marcosfshirafuchi.hroauth.auth.PasswordGrantAuthenticationConverter;
import com.marcosfshirafuchi.hroauth.auth.PasswordGrantAuthenticationProvider;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.UUID;
import java.util.stream.Collectors;

@Configuration
public class AuthorizationServerConfig {

    /* ========= Infra básica ========= */

    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Usa explicitamente o UDS remoto
    @Bean
    public AuthenticationManager authenticationManager(
            @Qualifier("remoteUserDetailsService") UserDetailsService uds,
            PasswordEncoder pe) {

        DaoAuthenticationProvider dao = new DaoAuthenticationProvider();
        dao.setUserDetailsService(uds);
        dao.setPasswordEncoder(pe);
        return new ProviderManager(dao);
    }

    /* ========= Beans exigidos pelo SAS ========= */

    // Clients registrados (inclui grant_type=password)
    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder encoder) {
        RegisteredClient myappname123 = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("myappname123")
                .clientSecret(encoder.encode("myappsecret123"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(new AuthorizationGrantType("password"))
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/frontend")
                .scope("read").scope("write")
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofHours(24))
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();
        return new InMemoryRegisteredClientRepository(myappname123);
    }

    // Chaves JWK em memória
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyPair kp = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) kp.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) kp.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return (selector, ctx) -> selector.select(jwkSet);
    }

    private static KeyPair generateRsaKey() {
        try {
            KeyPairGenerator g = KeyPairGenerator.getInstance("RSA");
            g.initialize(2048);
            return g.generateKeyPair();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:9000")
                .build();
    }

    // AuthorizationService explícito (memória)
    @Bean
    public OAuth2AuthorizationService authorizationService() {
        return new InMemoryOAuth2AuthorizationService();
    }

    // TokenGenerator explícito (JWT assinado)
    @Bean
    public OAuth2TokenGenerator<? extends org.springframework.security.oauth2.core.OAuth2Token> tokenGenerator(
            JWKSource<SecurityContext> jwkSource) {

        JwtEncoder jwtEncoder = new NimbusJwtEncoder(jwkSource);
        JwtGenerator jwtGenerator = new JwtGenerator(jwtEncoder);
        // Opcional: customizer global (claims extras)
        jwtGenerator.setJwtCustomizer(jwtCustomizer());

        // Gera AccessToken e também suportaria Refresh/ID se necessário
        return new DelegatingOAuth2TokenGenerator(jwtGenerator,
                new OAuth2AccessTokenGenerator(),
                new OAuth2RefreshTokenGenerator());
    }

    /* ========= Cadeias de segurança ========= */

    // Cadeia do Authorization Server
    @Bean
    @Order(1)
    public SecurityFilterChain asSecurityFilterChain(
            HttpSecurity http,
            AuthenticationManager authenticationManager,
            OAuth2AuthorizationService authorizationService,
            OAuth2TokenGenerator<? extends org.springframework.security.oauth2.core.OAuth2Token> tokenGenerator
    ) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .tokenEndpoint(token -> token
                        .accessTokenRequestConverters(list -> list.add(new PasswordGrantAuthenticationConverter()))
                        .authenticationProviders(list -> list.add(
                                new PasswordGrantAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator)))
                );

        return http.build();
    }

    // Cadeia padrão da app
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Libera os dois endpoints de token
                        .requestMatchers("/oauth/token", "/oauth2/token", "/actuator/**").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }


    /* ========= Customização opcional de claims ========= */

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            if (context.getPrincipal() != null) {
                context.getClaims().claim("client_id", context.getRegisteredClient().getClientId());
                context.getClaims().claim("authorities",
                        context.getPrincipal().getAuthorities().stream()
                                .map(Object::toString)
                                .collect(Collectors.toList()));
                context.getClaims().claim("user_name", context.getPrincipal().getName());
            }
        };
    }
}
