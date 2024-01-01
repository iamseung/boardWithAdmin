package hello.core.service;

import hello.core.domain.type.SearchType;
import hello.core.dto.ArticleDto;
import hello.core.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

//import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("비즈니스 로직 - 게시글")
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    // @InjectMocks, 주입하는 대상
    @InjectMocks private ArticleService sut; // System under Test
    @Mock private ArticleRepository articleRepository;

    /*
    검색
    각 게시글 페이지로 이동
    페이지네이션
    홈 버튼 -> 게시판 페이지로 리다이렉션
    정렬 기능
     */

    @Test
    @DisplayName("게시글을 검색하면, 게시글 리스트를 반환")
    void givenSearchParameters_whenSearchingArticles_thenReturnArticleList() {
        // Given

        // When
        List<ArticleDto> articles = sut.searchArticles(SearchType.TITLE, "keyword"); // 제목, 본문, ID, 닉네임, 해시태그

        // Then
        assertThat(articles).isNotNull();
    }

    @Test
    @DisplayName("게시글을 조회하면, 게시글 반환")
    void givenArticleId_whenSearchingArticles_thenReturnArticle() {
        // Given

        // When
        ArticleDto article = sut.searchArticle(1L);

        // Then
        assertThat(article).isNotNull();
    }
}