package hello.core.config;

import hello.core.domain.UserAccount;
import hello.core.repository.UserAccountRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private UserAccountRepository userAccountRepository;

    // Spring Boot Test 에서 각 테스트 메소드가 실행되기 직전에 이 코드를 수행해서 인증 정보를 넣어준다!
    // findById 메서드가 실행되면 아래의 정보를 내려준다
    @BeforeTestMethod
    public void securitySetUp() {
        given(userAccountRepository.findById(anyString())).willReturn(Optional.of(UserAccount.of(
                "leeseungseok",
                "pw",
                "seung-seokTest@naver.com",
                "seungseok-test",
                "test-memo"
        )));
    }
}
