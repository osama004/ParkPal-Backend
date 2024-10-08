package at.technikum.parkpalbackend.security.principal;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtToPrincipalConverter {

    public UserPrincipal convert(DecodedJWT jwt) {
        return new UserPrincipal(
                jwt.getSubject(),
                jwt.getClaim("username").asString(),
                false,
                extractAuthoritiesFromClaim(jwt)
        );
    }

    private List<SimpleGrantedAuthority> extractAuthoritiesFromClaim(DecodedJWT jwt) {
        Claim claim = jwt.getClaim("roles");

        if (claim.isMissing() || claim.isNull()) {
            return List.of();
        }

        return claim.asList(SimpleGrantedAuthority.class);
    }
}