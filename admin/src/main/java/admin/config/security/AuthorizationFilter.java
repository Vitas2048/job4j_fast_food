package admin.config.security;

import admin.message.auth.TokenType;
import admin.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class AuthorizationFilter extends BasicAuthenticationFilter {

    private final AuthService authService;
    public AuthorizationFilter(AuthenticationManager authenticationManager, AuthService authService) {
        super(authenticationManager);
        this.authService = authService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            authenticate(header);
        } else {
            log.debug("No authorization header");
        }
        chain.doFilter(request, response);
    }

    private void authenticate(String header) {
        final String token = header.replace("Bearer ", "");
        final UsernamePasswordAuthenticationToken auth = authService.verify(token, TokenType.Access);
        if (auth != null) {
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            log.debug("Access token verify fail");
        }
    }
}
