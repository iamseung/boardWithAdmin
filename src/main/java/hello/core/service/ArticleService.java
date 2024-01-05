package hello.core.service;

import hello.core.domain.Article;
import hello.core.domain.type.SearchType;
import hello.core.dto.ArticleDto;
import hello.core.dto.ArticleWithCommentsDto;
import hello.core.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Slf4j // logging
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    // 읽기 전용
    @Transactional(readOnly = true)
    public Page<ArticleDto> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {
        if(searchKeyword == null || searchKeyword.isBlank()) {
            // page 안의 내용을 형변환
            return articleRepository.findAll(pageable).map(ArticleDto::from);
        }

        // Dto 로 변환
        return switch (searchType) {
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword,pageable).map(ArticleDto::from);
            case CONTENT-> articleRepository.findByContentContaining(searchKeyword,pageable).map(ArticleDto::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword,pageable).map(ArticleDto::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword,pageable).map(ArticleDto::from);
            case HASHTAG -> articleRepository.findByHashtag("#" + searchKeyword,pageable).map(ArticleDto::from);
        };
    }

    @Transactional(readOnly = true)
    public ArticleWithCommentsDto getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId: "));
    }

    public void saveArticle(ArticleDto dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDto dto) {
        try {
            // findById 는 SELECT 쿼리가 발생하기 때문에 레퍼런스만 가져오는 getReferenceById 사용
            Article article = articleRepository.getReferenceById(dto.id());

            // java 13,14 부터 enum 타입은 get,set 을 자동으로 사용하기 때문에 아래와 같이 표기
            if(dto.title() != null) { article.setTitle(dto.title()); };
            if(dto.content() != null) { article.setContent(dto.content()); };
            article.setHashtag(dto.hashtag());

            // 트랜잭션 안에서 변경 감지로 save 생략
            // articleRepository.save(article);
        } catch (EntityNotFoundException e) {
            log.warn("게시글 업데이트 실패, 게시글을 찾을 수 없습니다 -dto : {}", dto);
        }

    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }
}
