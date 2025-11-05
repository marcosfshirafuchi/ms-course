package com.marcosfshirafuchi.hrapigatewayzuul.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        // Converte claims -> GrantedAuthorities
        JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();
        // use "scope" (padrão do Spring Authorization Server)
        scopes.setAuthoritiesClaimName("scope");
        scopes.setAuthorityPrefix("SCOPE_");
        // Se o seu token usa "authorities", troque para:
        // scopes.setAuthoritiesClaimName("authorities");
        // scopes.setAuthorityPrefix("");

        // Converter imperativo padrão do Spring Security
        JwtAuthenticationConverter jwtAuth = new JwtAuthenticationConverter();
        jwtAuth.setJwtGrantedAuthoritiesConverter(scopes);

        // Adaptador reativo correto
        var reactiveConverter = new ReactiveJwtAuthenticationConverterAdapter(jwtAuth);

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(auth -> auth
                        // Endpoints do Authorization Server expostos via gateway (liberados)
                        .pathMatchers(
                                "/actuator/**",
                                "/hr-oauth/oauth/token",
                                "/hr-oauth/oauth2/token",
                                "/hr-oauth/oauth2/jwks",
                                "/hr-oauth/.well-known/**"
                        ).permitAll()
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth ->
                        oauth.jwt(jwt -> jwt.jwtAuthenticationConverter(reactiveConverter))
                )
                .build();
    }
}
