package hello.core.repository;

import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import hello.core.domain.ArticleComment;
import hello.core.domain.QArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface ArticleCommentRepository extends
        JpaRepository<ArticleComment, Long>,
        QuerydslPredicateExecutor<ArticleComment>,
        QuerydslBinderCustomizer<QArticleComment> {

    List<ArticleComment> findByArticle_Id(Long articleId);

    void deleteByIdAndUserAccount_UserId(Long articleCommentId, String userId);
    /*
        [검색에 대한 세부적인 규칙 재구성]
        interface 라 구현을 넣을 수 없는데 java8 이후로 가능
        default 메서드로 변경
     */
    @Override
    default void customize(QuerydslBindings bindings, QArticleComment root) {
        // 리스팅을 하지 않은 프로퍼티는 검색에서 제외시키는 옵션
        // default, false
        bindings.excludeUnlistedProperties(true);

        // 해당 리스트를 검색에 포함
        bindings.including(
                root.content,
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
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase);
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); // 시분초까지 동일하게 입력
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase);
    }
}