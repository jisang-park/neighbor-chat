package neighborchat.messageserver.oauth2;

import lombok.RequiredArgsConstructor;
import neighborchat.messageserver.domain.Provider;
import neighborchat.messageserver.domain.Role;
import neighborchat.messageserver.domain.UserInfo;
import neighborchat.messageserver.repository.mysql.UserInfoRepository;
import neighborchat.messageserver.service.CookieService;
import neighborchat.messageserver.service.JwtService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CookieService cookieService;
    private final UserInfoRepository userInfoRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
        String name = principal.getName();
        String registrationId = ((OAuth2AuthenticationToken) authentication).getAuthorizedClientRegistrationId().toUpperCase();

        UserInfo userInfo = userInfoRepository.findUserInfoByProviderAndProviderId(Provider.valueOf(registrationId), name);

        if (userInfo == null) {
            userInfo = userInfoRepository.save(UserInfo.getInstance(registrationId, principal));
        }

        Set<GrantedAuthority> authorities = new LinkedHashSet<>();

        if (userInfo.getRole() == Role.ROLE_ADMIN) {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            cookieService.setAccessToken(response, jwtService.createAccessToken(userInfo.getId(), registrationId, "ROLE_USER", "ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            cookieService.setAccessToken(response, jwtService.createAccessToken(userInfo.getId(), registrationId, "ROLE_USER"));
        }
        cookieService.setRefreshToken(response, jwtService.createRefreshToken(userInfo.getId()));

        SecurityContextHolder.getContext()
                .setAuthentication(new OAuth2AuthenticationToken(new DefaultOAuth2User(authorities, Map.of("id", userInfo.getId()), "id"), authorities, registrationId));

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
