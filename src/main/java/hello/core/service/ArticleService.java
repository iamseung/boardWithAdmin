package hello.core.service;

import hello.core.domain.type.SearchType;
import hello.core.dto.ArticleDto;
import hello.core.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    // 읽기 전용
    @Transactional(readOnly = true)
    public List<ArticleDto> searchArticles(SearchType searchType, String keyword) {
        return List.of();
    }

    @Transactional(readOnly = true)
    public ArticleDto searchArticle(long l) {
        return null;
    }
}
