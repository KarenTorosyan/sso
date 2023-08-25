package auth.entities.authority.out.persistence.mongo;

import auth.configs.db.ConditionalOnSelectedDatabaseMongo;
import auth.entities.authority.Authority;
import auth.entities.authority.out.persistence.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@ConditionalOnSelectedDatabaseMongo
@Repository
@RequiredArgsConstructor
public class MongoAuthorityRepository implements AuthorityRepository {

    private final SpringDataMongoAuthorityRepository authorityRepository;

    @Override
    public Authority save(Authority authority) {
        return authorityRepository.save(AuthorityDocument.from(authority))
                .getAuthority();
    }

    @Override
    public Optional<Authority> findById(String id) {
        return authorityRepository.findById(id)
                .map(AuthorityDocument::getAuthority);
    }

    @Override
    public boolean existsById(String id) {
        return authorityRepository.existsById(id);
    }

    @Override
    public Optional<Authority> findByName(String name) {
        return authorityRepository.findByName(name)
                .map(AuthorityDocument::getAuthority);
    }

    @Override
    public boolean existsByName(String name) {
        return authorityRepository.existsByName(name);
    }

    @Override
    public long count() {
        return authorityRepository.count();
    }

    @Override
    public Page<Authority> findAll(Pageable pageable) {
        return authorityRepository.findAll(pageable)
                .map(AuthorityDocument::getAuthority);
    }

    @Override
    public Page<Authority> search(String text, Pageable pageable) {
        return authorityRepository.findAllBy(new TextCriteria().matching(text), pageable)
                .map(AuthorityDocument::getAuthority);
    }

    @Override
    public void deleteById(String id) {
        authorityRepository.deleteById(id);
    }
}
