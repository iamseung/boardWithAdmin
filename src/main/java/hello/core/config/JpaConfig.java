package hello.core.config;

import hello.core.dto.security.BoardPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

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
        // SecurityContextHolder => 인증 정보를 모두 가지고 있는 Class
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated) // 인증된 것들만
                .map(Authentication::getPrincipal)// 인증 정보 Get
                .map(BoardPrincipal.class::cast) // Type Cast, .map(x -> (BoardPrincipal) x)
                .map(BoardPrincipal::getUsername);
    }
}
