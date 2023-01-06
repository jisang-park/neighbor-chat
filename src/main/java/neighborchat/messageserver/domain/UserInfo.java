package neighborchat.messageserver.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String providerId;

    private String username;

    private String imageUrl;

    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private Integer reported;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    private LocalDateTime createdTime;

    public static UserInfo getInstance(String provider, DefaultOAuth2User principal) {
        return switch (provider.toUpperCase()) {
            case "GOOGLE" -> UserInfo.builder()
                    .provider(Provider.GOOGLE)
                    .role(Role.ROLE_USER)
                    .username(principal.getAttribute("name"))
                    .email(principal.getAttribute("email"))
                    .imageUrl(principal.getAttribute("picture"))
                    .reported(0)
                    .providerId(principal.getName())
                    .createdTime(LocalDateTime.now())
                    .build();
            case "GITHUB" -> UserInfo.builder()
                    .provider(Provider.GITHUB)
                    .role(Role.ROLE_USER)
                    .username(principal.getAttribute("name"))
                    .email(principal.getAttribute("email"))
                    .imageUrl(principal.getAttribute("avatar_url"))
                    .reported(0)
                    .providerId(principal.getName())
                    .createdTime(LocalDateTime.now())
                    .build();
            default -> throw new RuntimeException();
        };
    }

}
