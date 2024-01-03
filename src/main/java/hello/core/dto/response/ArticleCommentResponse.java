package hello.core.dto.response;

import hello.core.dto.ArticleCommentDto;

import java.time.LocalDateTime;

/**
 * DTO for {@link hello.core.domain.ArticleComment}
 */
import java.io.Serializable;

public record ArticleCommentResponse(
        Long id,
        String content,
        LocalDateTime createdAt,
        String email,
        String nickname
) implements Serializable {

    public static ArticleCommentResponse of(Long id, String content, LocalDateTime createdAt, String email, String nickname) {
        return new ArticleCommentResponse(id, content, createdAt, email, nickname);
    }

    public static ArticleCommentResponse from(ArticleCommentDto dto) {
        String nickname = dto.userAccountDto().nickname();
        if (nickname == null || nickname.isBlank()) {
            nickname = dto.userAccountDto().userId();
        }

        return new ArticleCommentResponse(
                dto.id(),
                dto.content(),
                dto.createdAt(),
                dto.userAccountDto().email(),
                nickname
        );
    }

}