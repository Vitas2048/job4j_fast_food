package project.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import project.message.auth.LoginRequest;
import project.message.auth.RefreshTokenRequest;
import project.message.auth.TokenResponse;
import project.message.auth.TokenType;
import project.repository.CustomerRepository;
import project.repository.CustomerTokenRepository;

import java.util.ArrayList;
import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {

    public static final int ACCESS_TIME = 1; //hours

    public static final int REFRESH_TIME = 2; //hours

    public static final String TOKEN_TYPE_CLAIM = "tokenType";

    public static final Algorithm ALGORITHM = Algorithm.HMAC256("secret-full-secret");

    private final CustomerRepository customerRepository;

    private final CustomerTokenRepository customerTokenRepository;

    private final AuthenticationManager authenticationManager;

    public TokenResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getLogin(),
                request.getPassword(),
                new ArrayList<>()));

        log.debug("Authenticate customer: {}", request.getLogin());

        return create(request.getLogin());
    }

    private TokenResponse create(String login) {
        final String access = createToken(login, TokenType.Access);
        final String refresh = createToken(login, TokenType.Refresh);

        final Customer customer = customerRepository.getByLogin(login);
        final CustomerToken customerToken = customer.getToken() == null
                ? new CustomerToken()
                : customer.getToken();
        customerToken.setAccessToken(access);
        customerToken.setRefreshToken(refresh);
        customerToken.setCustomer(customer);
        customerTokenRepository.save(customerToken);

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

        final Customer customer = (Customer) authentication.getPrincipal();
        return create(customer.getLogin());
    }

    public UsernamePasswordAuthenticationToken verify(String token, TokenType type) {
        try {
            final DecodedJWT decoded = JWT.require(ALGORITHM)
                    .build().verify(token);
            final TokenType decodedType = decoded.getClaim(TOKEN_TYPE_CLAIM).as(TokenType.class);

            if (type != decodedType) {
                log.error("Invalid token type");
                return null;
            }

            final String login = decoded.getSubject();
            final Customer customer = customerRepository.findByLoginAndToken(login, token).orElse(null);

            if (customer == null) {
                log.error("Token already replaced");
            }
            return new UsernamePasswordAuthenticationToken(customer, null, new ArrayList<>());
        } catch (JWTVerificationException e) {
            log.debug("Cannot parse JWT", e);
            return null;
        }
    }

    private String createToken(String login, TokenType tokenType) {
        final int lifetime = tokenType == TokenType.Access ? ACCESS_TIME : REFRESH_TIME;
        return JWT.create()
                .withSubject(login)
                .withClaim(TOKEN_TYPE_CLAIM, tokenType.name())
                .withExpiresAt(new Date(System.currentTimeMillis() + toMillis(lifetime)))
                .sign(ALGORITHM);
    }

    private long toMillis(int hours) {
        return (long) hours * 60 * 60 * 1000;
    }

}
