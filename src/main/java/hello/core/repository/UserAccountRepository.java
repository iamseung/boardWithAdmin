package hello.core.repository;


import hello.core.domain.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

// UserAccount 의 PK, String userId
@RepositoryRestResource
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
}
