package hello.core.controller;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시판")
// 대상, ArticleController 컨트롤러만 빈으로 읽어들여 테스트 진행
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    private final MockMvc mvc;

    public ArticleControllerTest(@Autowired MockMvc mockMvc) {
        this.mvc = mockMvc;
    }

    // jUnit5, 테스트 제외 어노테이션
    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 리스트")
    @Test
    void givenNothing_whenRequestingArticlesView_thenReturnArticlesView1() throws Exception {
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("aritcles")); // key 가 있는지 검사
    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticlesView_thenReturnArticlesView2() throws Exception {
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("aritcleComment"))
                .andExpect(model().attributeExists("aritcles")); // key 가 있는지 검사
    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticlesSearchView_thenReturnArticlesView3() throws Exception {
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.TEXT_HTML));
    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 해시태그 전용 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticlesHashTagSearchView_thenReturnArticlesHashTagSearchView() throws Exception {
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/search"))
                .andExpect(content().contentType(MediaType.TEXT_HTML));
    }
}