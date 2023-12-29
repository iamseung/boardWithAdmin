package hello.core.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import hello.core.domain.Article;
import hello.core.domain.QArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/*
QuerydslPredicateExecutor
: 해당 엔티티 안에 있는 모든 필드에 대한 기본 검색 기능을 추가

QuerydslBinderCustomizer
: 커스터 마이징
 */
@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle> {

    /*
        [검색에 대한 세부적인 규칙 재구성]
        interface 라 구현을 넣을 수 없는데 java8 이후로 가능
        default 메서드로 변경
     */
    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        // 리스팅을 하지 않은 프로퍼티는 검색에서 제외시키는 옵션
        // default, false
        bindings.excludeUnlistedProperties(true);

        // 해당 리스트를 검색에 포함
        bindings.including(
                root.title,
                root.content,
                root.hashtag,
                root.createdAt,
                root.createdBy)
        ;

        /*
            [exact match rule 수정]
            StringExpression::likeIgnoreCase
            : like '${v}'
            StringExpression::containsIgnoreCase
            : like '%${v}%', 부분 검색
         */
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 시분초까지 동일하게 입력
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}