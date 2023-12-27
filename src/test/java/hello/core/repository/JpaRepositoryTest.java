package hello.core.repository;

import hello.core.config.JpaConfig;
import hello.core.domain.Article;
import hello.core.domain.ArticleComment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

//import static org.junit.jupiter.api.Assertions.*; // jUnit 5 테스트 도구
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*; // AssertJ

@DisplayName("JPA 연결 테스트")
// Auditing Test 를 위해 import
@Import(JpaConfig.class)
@DataJpaTest // 생성자 주입, @Transactional 등
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // 테스트 DB 로 바꾸지 않고 설정 값을 사용
class JpaRepositoryTest {
    private final ArticleRepository articleRepository;
    private final ArticleCommentRepository articleCommentRepository;

    public JpaRepositoryTest(
            @Autowired ArticleRepository articleRepository,
            @Autowired ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @DisplayName("select Test")
    @Test
    void givenTestDta_whenSelecting_thenWorksFine() {
        // Given

        // WhenA
        List<Article> articles = articleRepository.findAll();

        // Then
        assertThat(articles)
                .isNotNull()
                .hasSize(0);
    }

    @DisplayName("insert Test")
    @Test
    void givenTestDta_wheninserting_thenWorksFine() {
        // Given
        long previousCnt = articleRepository.count();

        // When
        Article savedArticle = articleRepository.save(Article.of("new article", "new content", "#Spring"));

        // Then
        assertThat(articleRepository.count())
                .isEqualTo(1);
    }

    @DisplayName("update Test")
    @Test
    void givenTestDta_whenUpdating_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        String updateHashtag = "#springboot";
        article.setHashtag(updateHashtag);

        // When
        // 트랜잭션이 걸려있기 때문에, 기본 동작인 rollback 이 되어버림
        // flush 를 해서 쿼리를 조회하지만 결국엔 Rollback
        Article savedArticle = articleRepository.saveAndFlush(article);
        // articleRepository.flush();

        // Then
        assertThat(savedArticle).hasFieldOrPropertyWithValue("hashtag", updateHashtag);
    }

    @DisplayName("delete Test")
    @Test
    void givenTestDta_whenDeleting_thenWorksFine() {
        // Given
        Article article = articleRepository.findById(1L).orElseThrow();
        long PreviousArticleCnt = articleRepository.count();
        long PreviousArticleCommentCnt = articleCommentRepository.count();

        // 1L 게시글에 적힌 댓글 수
        int deletedCommentSize =  article.getArticleComments().size();

        // When
        articleRepository.delete(article);

        // Then
        assertThat(articleRepository.count())
                .isEqualTo(PreviousArticleCnt - 1);

        // cascade 옵션으로 관련 댓글 전부 삭제
        assertThat(articleCommentRepository.count())
                .isEqualTo(PreviousArticleCommentCnt - deletedCommentSize);

    }
}