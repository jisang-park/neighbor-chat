package neighborchat.messageserver.oauth2;

import lombok.RequiredArgsConstructor;
import neighborchat.messageserver.service.CookieService;
import neighborchat.messageserver.service.JwtService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CookieService cookieService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = cookieService.getAccessToken(request);

        if (StringUtils.hasText(accessToken) && jwtService.isExpired(accessToken)) {
            String refreshToken = cookieService.getRefreshToken(request);

            if (jwtService.isExpired(refreshToken)) {
                response.sendError(401, "재인증이 필요합니다");
                return;
            }

            cookieService.setAccessToken(response, jwtService.reissueAccessToken(accessToken));
            cookieService.setRefreshToken(response, jwtService.reissueRefreshToken(refreshToken));
        }

        SecurityContextHolder.getContext().setAuthentication(jwtService.getAuthentication(accessToken));

        filterChain.doFilter(request, response);
    }

}
