package admin.service;

import admin.message.auth.LoginRequest;
import admin.message.auth.RefreshTokenRequest;
import admin.message.auth.TokenResponse;
import admin.message.auth.TokenType;
import model.*;
import admin.repository.AdminRepository;
import admin.repository.AdminTokenRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    private static final int ACCESS_TIME = 1; // hours
    private static final int REFRESH_TIME = 2; // hours
    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final Algorithm ALGORITHM = Algorithm.HMAC256("my-very-strong-secret");

    private final AdminRepository adminRepository;
    private final AdminTokenRepository adminTokenRepository;
    private final AuthenticationManager authenticationManager;

    public TokenResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(),
                request.getPassword(),
                new ArrayList<>()));

        log.debug("Authenticate admin: {}", request.getLogin());

        return create(request.getLogin());
    }

    private TokenResponse create(String login) {
        final String access = createToken(login, TokenType.Access);
        final String refresh = createToken(login, TokenType.Refresh);

        final Admin admin = adminRepository.getByLogin(login);
        final AdminToken adminToken = admin.getToken() == null
                ? new AdminToken()
                : admin.getToken();
        adminToken.setAccessToken(access);
        adminToken.setRefreshToken(refresh);
        adminToken.setAdmin(admin);
        adminTokenRepository.save(adminToken);

        return new TokenResponse() {{
            setAccessToken(access);
            setRefreshToken(refresh);
            setAccessExpiresIn(toMillis(ACCESS_TIME));
            setRefreshExpiresIn(toMillis(REFRESH_TIME));
        }};
    }

    public TokenResponse refresh(RefreshTokenRequest request) {
        final Authentication authentication = verify(request.getRefreshToken(), TokenType.Refresh);

        if (authentication == null) {
            return null;
        }

        final Admin admin = (Admin) authentication.getPrincipal();
        return create(admin.getLogin());
    }

    public UsernamePasswordAuthenticationToken verify(String token, TokenType type) {
        try {
            final DecodedJWT decoded = JWT.require(ALGORITHM)
                    .build()
                    .verify(token);

            final TokenType decodedType = decoded.getClaim(TOKEN_TYPE_CLAIM).as(TokenType.class);

            if (type != decodedType) {
                log.error("Invalid token type");
                return null;
            }

            final String login = decoded.getSubject();
            final Admin admin = adminRepository.findByLoginAndToken(login, token).orElse(null);

            if (admin == null) {
                log.error("Token already replaced");
                return null;
            }

            return new UsernamePasswordAuthenticationToken(admin, null, new ArrayList<>());
        } catch (JWTVerificationException e) {
            log.debug("Cannot parse JWT", e);
            return null;
        }
    }

    private String createToken(String login, TokenType tokenType) {
        final int lifeTime = tokenType == TokenType.Access ? ACCESS_TIME : REFRESH_TIME;
        return JWT.create()
                .withSubject(login)
                .withClaim(TOKEN_TYPE_CLAIM, tokenType.name())
                .withExpiresAt(new Date(System.currentTimeMillis() + toMillis(lifeTime)))
                .sign(ALGORITHM);
    }

    private long toMillis(int hours) {
        return (long) hours * 60 * 60 * 1000;
    }


}
