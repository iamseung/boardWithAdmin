package hello.core.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
    @WebMvcTest
    슬라이스 테스트
    컨트롤러 외의 빈을 로드하지 않음, 404 에러 발생

*/

/*
    MockMvc는 웹 어플리케이션을 애플리케이션 서버에 배포하지 않고 테스트용 MVC환경을 만들어
    요청 및 전송, 응답기능을 제공해주는 유틸리티 클래스
 */
@DisplayName("Data REST - API 테스트")
@Transactional // 테스트에서 동작하는 트랜잭셔널의 기본 동작은 Rollback 을 위함
@AutoConfigureMockMvc
@SpringBootTest // 통합 테스트
public class DataRestTest {
    private final MockMvc mvc;

    public DataRestTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @DisplayName("[api] 게시글 리스트 조행")
    @Test
    void test() throws Exception {
        // Given

        // When & Then
        // mockMvc에게 컨트롤러에 대한 정보를 입력
    	mvc.perform(get("/api/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/hal+json")));
    }
}
