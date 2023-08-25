package auth.entities.user.out.persistence.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpringDataMongoUserRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByEmailDocumentAddress(String email);

    boolean existsByEmailDocumentAddress(String email);

    Page<UserDocument> findAllBy(TextCriteria textCriteria, Pageable pageable);
}
