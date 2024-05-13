package hello.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.*;

@Getter
@ToString(callSuper = true)
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

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "userId")
    private UserAccount userAccount; // 유저 정보 (ID), DB 명 = user_account_id

    @Setter @Column(nullable = false) private String title;       // 제목
    @Setter @Column(nullable = false, length = 10000) private String content;     // 내용

    @ToString.Exclude
    @JoinTable(
            name = "article_hashtag",
            joinColumns = @JoinColumn(name = "articleId"),
            inverseJoinColumns = @JoinColumn(name = "hashtagId")
    )
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    protected Set<Hashtag> hashtags = new LinkedHashSet<>();

    // 양방향 바인딩
    @ToString.Exclude // 순환 참조 방지
    @OrderBy("createdAt DESC") // 정렬 기준
    @OneToMany(mappedBy = "article", cascade = CascadeType.ALL)
    private final Set<ArticleComment> articleComments = new LinkedHashSet<>();

    // 엔티티는 생성자를 가지고 있어야 함,
    // 평소에 오픈하지 않을 것을 의미
    protected Article() {}

    private Article(UserAccount userAccount, String title, String content) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
    }

    // Factory Mehtod
    public static Article of(UserAccount userAccount, String title, String content) {
        return new Article(userAccount, title, content);
    }

    public void addHashtag(Hashtag hashtag) {
        this.getHashtags().add(hashtag);
    }

    public void addHashtags(Collection<Hashtag> hashtags) {
        this.getHashtags().addAll(hashtags);
    }

    public void  clearHashtags() {
        this.getHashtags().clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Article that)) return false;
        return this.getId() != null & this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
