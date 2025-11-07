package com.marcosfshirafuchi.hrapigatewayzuul.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebFluxSecurity
public class GatewaySecurityConfig {

    private static final String[] PUBLIC = {
            "/hr-oauth/oauth/token",          // legado
            "/hr-oauth/oauth2/token",         // SAS atual
            "/hr-oauth/oauth2/jwks",
            "/hr-oauth/.well-known/**",
            "/actuator/**"
    };

    private static final String[] OPERATOR_GET = {
            "/hr-worker/**"
    };

    private static final String[] ADMIN = {
            "/hr-payroll/**", "/hr-user/**",
            "/actuator/**", "/hr-worker/actuator/**", "/hr-oauth/actuator/**"
    };

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(
            ServerHttpSecurity http,
            ReactiveJwtAuthenticationConverter jwtAuthenticationConverter
    ) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> {}) // integra com o CorsWebFilter/CorsConfiguration abaixo
                .authorizeExchange(auth -> auth
                        // libere o preflight sempre
                        .pathMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .pathMatchers(PUBLIC).permitAll()
                        .pathMatchers(HttpMethod.GET, OPERATOR_GET).hasAnyRole("OPERATOR", "ADMIN")
                        .pathMatchers(ADMIN).hasRole("ADMIN")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth -> oauth
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter))
                );

        return http.build();
    }

    /**
     * CORS para WebFlux (reactive).
     * Use origens explícitas (ex.: http://localhost:3000) quando allowCredentials=true.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration cors = new CorsConfiguration();

        // Se você precisa usar credenciais (cookies/Authorization), NÃO use "*".
        // Liste explicitamente as origens do seu front:
        // cors.setAllowedOrigins(List.of("http://localhost:3000"));
        // ou, se estiver em Spring 5.3+ e realmente precisar padrão curinga:
        // cors.setAllowedOriginPatterns(List.of("*"));

        cors.setAllowedOrigins(List.of("http://localhost:3000")); // recomendado
        cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        cors.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        cors.setExposedHeaders(List.of("Authorization", "Location"));
        cors.setAllowCredentials(true);
        cors.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", cors);
        return new CorsWebFilter(source);
    }
}
