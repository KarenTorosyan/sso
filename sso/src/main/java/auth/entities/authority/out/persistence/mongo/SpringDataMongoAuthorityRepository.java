package auth.entities.authority.out.persistence.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SpringDataMongoAuthorityRepository extends MongoRepository<AuthorityDocument, String> {

    Optional<AuthorityDocument> findByName(String name);

    boolean existsByName(String name);

    Page<AuthorityDocument> findAllBy(TextCriteria textCriteria, Pageable pageable);
}
