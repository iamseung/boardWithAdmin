package hello.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;

/*
엔티티 객체가 생성이 되거나 변경이 되었을 때 @EnableJpaAuditing 어노테이션을 활용하여 자동으로 값을 등록할 수 있습니다
 */
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
