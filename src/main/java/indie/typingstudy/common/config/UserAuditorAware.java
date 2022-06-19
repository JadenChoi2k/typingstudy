package indie.typingstudy.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

// TODO: Spring Security 셋업 후 구현하기.
@Configuration
@EnableJpaAuditing
public class UserAuditorAware implements AuditorAware<Long> {

    // 현재 접속한 유저의 아이디를 반환한다.
    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.empty();
    }
}
