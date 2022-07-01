package indie.typingstudy.common.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import indie.typingstudy.common.config.auth.PrincipalDetails;
import indie.typingstudy.domain.user.User;
import indie.typingstudy.infrastructure.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String jwt = request.getHeader(JwtProperty.JWT_HEADER);
        if (jwt == null || !jwt.startsWith(JwtProperty.JWT_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        jwt = jwt.replace(JwtProperty.JWT_PREFIX, "");
        String email = JWT.require(Algorithm.HMAC512(JwtProperty.SECRET)).build()
                .verify(jwt).getSubject();
        if (email != null) {
            Optional<User> userEntity = userRepository.findByEmail(email);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity.get());
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request, response);
        }
    }
}
