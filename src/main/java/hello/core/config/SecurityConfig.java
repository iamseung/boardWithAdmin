package hello.core.config;

import hello.core.dto.UserAccountDto;
import hello.core.dto.security.BoardPrincipal;
import hello.core.repository.UserAccountRepository;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /*
    mvcMatchers
    => antMatchers 호환 + Spring Pattern Matching Rules

    Spring Boot 2.7, Spring Security 5.7 이상에 대한 Release Note
    https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        // 정적 리소스들은 모두 허용
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        // 아래의 방식도 모두 허용
                        .mvcMatchers(
                                HttpMethod.GET,
                                "/",
                                "/articles",
                                "/articles-hashtag"
                        ).permitAll()
                        // 제외한 나머지 모두 인
                        .anyRequest().authenticated()
                )
                // 로그인 페이지 만들게 유도
                .formLogin().and()
                .logout()
                        .logoutSuccessUrl("/")
                        .and()
                .build();
    }

    /*
    Spring Security 검사에서 제외
    하지만 이 방식은 아래의 warning 과 같이 권장되지 않는 방식이라 제외한다
    WARN 50232 --- [  restartedMain] o.s.s.c.a.web.builders.WebSecurity       : You are asking Spring Security to ignore org.springframework.boot.autoconfigure.security.servlet.StaticResourceRequest$StaticResourceRequestMatcher@77285b26. This is not recommended -- please use permitAll via HttpSecurity#authorizeHttpRequests instead.

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // static resource(정적 리소스), css, js
        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    */


    @Bean
    public UserDetailsService userDetailsService(UserAccountRepository userAccountRepository) {
        return username -> userAccountRepository
                .findById(username)
                .map(UserAccountDto::from)
                .map(BoardPrincipal::from)
                .orElseThrow(() -> new UsernameNotFoundException("유저를 찾을 수 없습니다 - username : " + username));
    }

    // Spring Security 인증 기능을 사용할 때는 필수
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}