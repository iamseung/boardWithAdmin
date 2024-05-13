package hello.core.repository.querydsl;

import com.querydsl.jpa.JPQLQuery;
import hello.core.domain.Article;
import hello.core.domain.QArticle;
import hello.core.domain.QHashtag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Collection;
import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom {

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    /*
        QueryDsl 로 article 의 중복되지 않은 hashtag 를 반환하는 메서드
     */
    @Override
    public List<String> findAllDistinctHashtags() {
        QArticle article = QArticle.article;

        // 제네릭 타입은 리턴 타입과 동일하게 설정
        // JPQLQuery<String> query
        return from(article)
                .distinct()
                .select(article.hashtags.any().hashtagName)
                .fetch();
    }

    @Override
    public Page<Article> findByHashtagNames(Collection<String> hashtagNames, Pageable pageable) {
        QHashtag hashtag = QHashtag.hashtag;
        QArticle article = QArticle.article;

        JPQLQuery<Article> query = from(article)
                .innerJoin(article.hashtags, hashtag)
                .where(hashtag.hashtagName.in(hashtagNames));
        List<Article> articles = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(articles, pageable, query.fetchCount());
    }
}
