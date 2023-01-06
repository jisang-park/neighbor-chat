package neighborchat.messageserver.service;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class CookieService {

    private final String ACCESS_TOKEN_KEY = "access-token";
    private final String REFRESH_TOKEN_KEY = "refresh-token";
    private final int ACCESS_TOKEN_MAX_AGE = 600;
    private final int REFRESH_TOKEN_MAX_AGE = 2592000;

    public String getAccessToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(ACCESS_TOKEN_KEY)) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public void setAccessToken(HttpServletResponse response, String accessToken) {
        Cookie cookie = new Cookie(ACCESS_TOKEN_KEY, accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(ACCESS_TOKEN_MAX_AGE);
        response.addCookie(cookie);
    }

    public String getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(REFRESH_TOKEN_KEY)) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public void setRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie(REFRESH_TOKEN_KEY, refreshToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(REFRESH_TOKEN_MAX_AGE);
        response.addCookie(cookie);
    }

}
