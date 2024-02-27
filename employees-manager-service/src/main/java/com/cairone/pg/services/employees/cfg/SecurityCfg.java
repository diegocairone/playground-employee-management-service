package com.cairone.pg.services.employees.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityCfg extends WebSecurityConfigurerAdapter {

    @Value("${app.jwt.claim.username:sub}")
    private String usernameClaim;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf(e -> e.disable())
                .sessionManagement(e -> e.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(j -> j.jwtAuthenticationConverter(jwtAuthenticationConverter())))
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/swagger-ui/**", "/v3/api-docs/**")
                            .permitAll()
                        .anyRequest()
                            .authenticated());
    }

    private Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return jwt ->
            new JwtAuthenticationToken(
                    jwt,
                    jwtConverter.convert(jwt),
                    jwt.getClaim(usernameClaim));
    }

    private final KeycloakJwtGrantedAuthoritiesConverter jwtConverter = new KeycloakJwtGrantedAuthoritiesConverter();
}
