package auth.entities.user.out.persistence.mongo;

import auth.configs.db.ConditionalOnSelectedDatabaseMongo;
import auth.entities.authority.Authority;
import auth.entities.authority.out.persistence.mongo.AuthorityDocument;
import auth.entities.user.UserAuthority;
import auth.entities.user.out.persistence.UserAuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@ConditionalOnSelectedDatabaseMongo
@Repository
@RequiredArgsConstructor
public class MongoUserAuthorityRepository implements UserAuthorityRepository {

    private final SpringDataMongoUserAuthorityRepository userAuthorityRepository;

    @Override
    public UserAuthority save(UserAuthority userAuthority) {
        return userAuthorityRepository.save(UserAuthorityDocument.from(userAuthority))
                .getUserAuthority();
    }

    @Override
    public Optional<UserAuthority> find(UserAuthority userAuthority) {
        return userAuthorityRepository.findByUserIdAndAuthorityDocument(userAuthority.getUserId(),
                        AuthorityDocument.from(userAuthority.getAuthority()))
                .map(UserAuthorityDocument::getUserAuthority);
    }

    @Override
    public boolean existsByUserIdAndAuthority(String userId, Authority authority) {
        return userAuthorityRepository.existsByUserIdAndAuthorityDocument(userId, AuthorityDocument.from(authority));
    }

    @Override
    public Page<UserAuthority> findAllByUserId(String userId, Pageable pageable) {
        return userAuthorityRepository.findAllByUserId(userId, pageable)
                .map(UserAuthorityDocument::getUserAuthority);
    }

    @Override
    public void delete(UserAuthority userAuthority) {
        if (existsByUserIdAndAuthority(userAuthority.getUserId(), userAuthority.getAuthority())) {
            userAuthorityRepository.delete(UserAuthorityDocument.from(userAuthority));
        }
    }
}
