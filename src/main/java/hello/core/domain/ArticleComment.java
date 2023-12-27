package hello.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@ToString
// INDEX 전략
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt"),
        @Index(columnList = "modifiedAt")
})
@EntityListeners(AuditingEntityListener.class)
@Entity
public class ArticleComment {
    // MySQL의 AUTO INCREMENT 는 IDENTITY 방식
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시글 (ID)
    @Setter @ManyToOne(optional = false) private Article article;
    // 본문
    @Setter @Column(nullable = false, length = 500) private String content;

    // meta Data
    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt;             // 생성일시
    @CreatedBy @Column(nullable = false, length = 100)private String createdBy;         // 생성자
    @LastModifiedDate @Column(nullable = false) private LocalDateTime modifiedAt;       // 수정일시
    @LastModifiedBy @Column(nullable = false, length = 100) private String modifiedBy;  // 수정자

    protected ArticleComment() {}

    private ArticleComment(Article article, String content) {
        this.article = article;
        this.content = content;
    }

    public static ArticleComment of(Article article, String content) {
        return new ArticleComment(article,content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ArticleComment that)) return false;
        return id != null && id.equals(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
