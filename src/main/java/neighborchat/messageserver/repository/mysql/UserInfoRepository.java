package neighborchat.messageserver.repository.mysql;

import neighborchat.messageserver.domain.Provider;
import neighborchat.messageserver.domain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    UserInfo findUserInfoById(Long id);
    UserInfo findUserInfoByProviderAndProviderId(Provider provider, String providerId);

}
