package auth.entities.user.out.persistence.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Optional;

public interface SpringDataMongoUserRepository extends MongoRepository<UserDocument, String> {

    Optional<UserDocument> findByEmailDocumentAddress(String email);

    boolean existsByEmailDocumentAddress(String email);

    Page<UserDocument> findAllBy(TextCriteria textCriteria, Pageable pageable);

    @Query("{'_id': {$in: ?0}}")
    Page<UserDocument> findAllById(Iterable<String> ids, Pageable pageable);

    @Query("{'email.address': {$in: ?0}}")
    Page<UserDocument> findAllByEmailDocumentAddress(Iterable<String> emails, Pageable pageable);
}
