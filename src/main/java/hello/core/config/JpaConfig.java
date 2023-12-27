package hello.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

// JpaAuditing 기능 활성화
@EnableJpaAuditing
@Configuration
public class JpaConfig {

    // JpaAuditing 이 될 때마다 사람이름을 지정
    @Bean
    public AuditorAware<String> auditorAware() {
        // 스프링 시큐리티로 인증 기능을 붙일 때 수정
        return () -> Optional.of("seungseok");
    }
}
