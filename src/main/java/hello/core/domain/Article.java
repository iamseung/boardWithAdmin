package hello.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Getter
@ToString
// INDEX 전략
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "hashtag"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "modifiedAt")
})

@Entity
public class Article extends AuditingFields {
    // MySQL의 AUTO INCREMENT 는 IDENTITY 방식
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter @Column(nullable = false) private String title;       // 제목
    @Setter @Column(nullable = false, length = 10000) private String content;     // 내용

    @Setter private String hashtag;     // 해시태그

    // 양방향 바인딩
    @ToString.Exclude // 순환 참조!
    @OrderBy("id") // 정렬 기준
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    // 엔티티는 생성자를 가지고 있어야 함,
    // 평소에 오픈하지 않을 것을 의미
    protected Article() {}

    private Article(String title, String content, String hashtag) {
        this.title = title;
        this.content = content;
        this.hashtag = hashtag;
    }

    // Factory Mehtod
    public static Article of(String title, String content, String hashtag) {
        return new Article(title, content, hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        // Pattern Variable
        if (!(o instanceof Article article)) return false;
//        return Objects.equals(id, article.id);
        // 아직 영속화되지 않은 값은 일치하지 않는다고 간주한다는 의미
        return id != null & id.equals(article.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
