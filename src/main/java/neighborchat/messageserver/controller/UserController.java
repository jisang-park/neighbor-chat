package neighborchat.messageserver.controller;

import lombok.RequiredArgsConstructor;
import neighborchat.messageserver.repository.mysql.UserInfoRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserInfoRepository userInfoRepository;

    @GetMapping("/")
    public String home(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            return "로그인이 필요합니다";
        }

        return userInfoRepository.findUserInfoById(Long.valueOf(principal.getName())).getUsername() + "님 환영합니다";
    }

}
