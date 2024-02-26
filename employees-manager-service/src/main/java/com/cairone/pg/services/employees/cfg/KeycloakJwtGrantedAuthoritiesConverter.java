package com.cairone.pg.services.employees.cfg;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class KeycloakJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String MAP_KEY_NAME_FOR_ROLES = "roles";

    @Override
    public Collection<GrantedAuthority> convert(Jwt source) {

        Collection<String> roles = new ArrayList<>();
        Map<String, ?> root = source.getClaims();
        root.entrySet().stream().forEach(entry -> getList(entry, roles));

        Collection<GrantedAuthority> grantedAuthorities = defaultConverter.convert(source);
        roles.forEach(authority -> grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + authority)));
        return grantedAuthorities;
    }

    private void getList(Map.Entry<String, ?> value, Collection<String> roles) {
        if (value.getKey().equals(MAP_KEY_NAME_FOR_ROLES) && value.getValue() instanceof List) {
            roles.addAll((List<String>) value.getValue());
        } else if (value.getValue() instanceof Map) {
            Map<String, ?> child = (Map<String, ?>) value.getValue();
            child.entrySet().forEach(e -> getList(e, roles));
        }
    }

    private final JwtGrantedAuthoritiesConverter defaultConverter = new JwtGrantedAuthoritiesConverter();
}
