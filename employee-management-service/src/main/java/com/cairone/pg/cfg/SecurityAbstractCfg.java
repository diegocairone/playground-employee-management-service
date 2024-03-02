package com.cairone.pg.cfg;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class SecurityAbstractCfg {

    protected Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        return jwt ->
                new JwtAuthenticationToken(
                        jwt,
                        jwtConverter().convert(jwt),
                        jwt.getClaim("preferred_username"));
    }

    protected Converter<Jwt, Collection<GrantedAuthority>> jwtConverter() {
        return jwt -> {
            Collection<String> roles = new ArrayList<>();
            Map<String, ?> root = jwt.getClaims();
            root.entrySet().stream().forEach(entry -> getList(entry, roles));

            Collection<GrantedAuthority> grantedAuthorities = defaultConverter.convert(jwt);
            roles.forEach(authority -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + authority)));
            return grantedAuthorities;
        };
    }

    protected void getList(Map.Entry<String, ?> value, Collection<String> roles) {
        if (value.getKey().equals(mapKeyNameForRoles) && value.getValue() instanceof List) {
            roles.addAll((List<String>) value.getValue());
        } else if (value.getValue() instanceof Map) {
            Map<String, ?> child = (Map<String, ?>) value.getValue();
            child.entrySet().forEach(e -> getList(e, roles));
        }
    }

    @Value("${app.security.authorities-converter.map-key-name-for-roles}")
    protected String mapKeyNameForRoles;

    @Value("${app.security.principal-name:sub}")
    protected String principalName;

    protected JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();

}
