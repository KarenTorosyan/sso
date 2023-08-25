package auth.entities.user.out.persistence.mongo;

import auth.entities.authority.out.persistence.mongo.AuthorityDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpringDataMongoUserAuthorityRepository extends MongoRepository<UserAuthorityDocument, String> {

    Page<UserAuthorityDocument> findAllByUserId(String userId, Pageable pageable);

    Optional<UserAuthorityDocument> findByUserIdAndAuthorityDocument(String userId, AuthorityDocument authorityDocument);

    boolean existsByUserIdAndAuthorityDocument(String userId, AuthorityDocument authorityDocument);

    void deleteByAuthorityDocumentId(String id);
}
