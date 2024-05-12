package example.user;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KeycloakJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private final Converter<Jwt, Collection<GrantedAuthority>> delegate = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        List<GrantedAuthority> extractedAuthorities = extractRoles(jwt);
        Collection<GrantedAuthority> authorities = delegate.convert(jwt);
        if (authorities != null) {
            authorities.addAll(extractedAuthorities);
        }
        return new JwtAuthenticationToken(jwt, authorities);
    }

    private List<GrantedAuthority> extractRoles(Jwt jwt) {
        //noinspection unchecked
        return Optional.of(jwt)
                .map(Jwt::getClaims)
                .map(claims -> (Map<String, Object>) claims.get("realm_access"))
                .map(it -> (List<String>) it.get("roles"))
                .map(roles -> roles.stream()
                        .filter(role -> role.startsWith("ROLE_"))
                        .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                        .toList())
                .orElse(List.of());
    }
}