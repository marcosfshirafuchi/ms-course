package com.marcosfshirafuchi.hrapigatewayzuul.config;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import reactor.core.publisher.Flux;

@Configuration
@RefreshScope
public class AppConfig {

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        // conversor padrão (sincrono) que lê claims -> authorities
        JwtGrantedAuthoritiesConverter delegate = new JwtGrantedAuthoritiesConverter();
        // seus tokens usam o claim "authorities" (não "scope")
        delegate.setAuthoritiesClaimName("authorities");
        delegate.setAuthorityPrefix(""); // não prefixar com SCOPE_/ROLE_

        // ADAPTADOR para reativo: Collection -> Flux
        Converter<Jwt, Flux<GrantedAuthority>> reactiveAuthorities =
                jwt -> Flux.fromIterable(delegate.convert(jwt));

        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(reactiveAuthorities);
        return converter;
    }
}
