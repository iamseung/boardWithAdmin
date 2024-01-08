package hello.core.controller;

import hello.core.config.SecurityConfig;
import hello.core.domain.type.SearchType;
import hello.core.dto.ArticleWithCommentsDto;
import hello.core.dto.UserAccountDto;
import hello.core.service.ArticleService;
import hello.core.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("View 컨트롤러 - 게시판")
@Import(SecurityConfig.class)
// 대상, ArticleController 컨트롤러만 빈으로 읽어들여 테스트 진행
@WebMvcTest(ArticleController.class)
class ArticleControllerTest {
    private final MockMvc mvc;

    @MockBean private ArticleService articleService;
    @MockBean private PaginationService paginationService;

    public ArticleControllerTest(@Autowired MockMvc mockMvc) {
        this.mvc = mockMvc;
    }

    // jUnit5, 테스트 제외 어노테이션
    // @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 리스트 {게시판} 페이지 - 검색어와 함께 호출")
    @Test
    void givenNothing_whenRequestingArticlesView_thenReturnArticlesView1() throws Exception {
        // Given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";

        given(articleService.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(anyInt(), anyInt())).willReturn(List.of(0,1,2,3,4));

        // When & then
        mvc.perform(
                get("/articles")
                    .queryParam("searchType", searchType.name())
                    .queryParam("searchValue", searchValue)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML)) // 호환되는 타입까지 전부
                .andExpect(view().name("articles/index"))
//                .andExpect(model().attributeExists("paginationBarNumbers"))
                .andExpect(model().attributeExists("searchTypes"))
                .andExpect(model().attributeExists("articles")); // key 가 있는지 검사
        then(articleService).should().searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumbers(anyInt(), anyInt());
    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(
                        get("/articles")
                                .queryParam("page", String.valueOf(pageNumber))
                                .queryParam("size", String.valueOf(pageSize))
                                .queryParam("sort", sortName + "," + direction)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(articleService).should().searchArticles(null, null, pageable);
        then(paginationService).should().getPaginationBarNumbers(pageable.getPageNumber(), Page.empty().getTotalPages());
    }

//    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 상세 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticlesView_thenReturnArticlesView2() throws Exception {
        // Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());

        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attributeExists("article")); // key 가 있는지 검사

        then(articleService).should().getArticle(articleId);
    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 검색 전용 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticlesSearchView_thenReturnArticlesView3() throws Exception {
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    @Disabled("구현 중")
    @DisplayName("[view] [GET] 게시글 해시태그 전용 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticlesHashTagSearchView_thenReturnArticlesHashTagSearchView() throws Exception {
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/search"))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML));
    }

    public ArticleWithCommentsDto createArticleWithCommentsDto() {
        return ArticleWithCommentsDto.of(
                1L,
                createUserAccountDto(),
                Set.of(),
                "title",
                "content",
                "#java",
                LocalDateTime.now(),
                "seungseok",
                LocalDateTime.now(),
                "seungseok"
        );
    }

    public UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                1L,
                "seungseok",
                "pw",
                "seungseok@naver.com",
                "seungseok",
                "memo",
                LocalDateTime.now(),
                "seungseok",
                LocalDateTime.now(),
                "seungsoek"
        );
    }
}