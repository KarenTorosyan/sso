package auth.entities.user.out.persistence.mongo;

import auth.configs.db.ConditionalOnSelectedDatabaseMongo;
import auth.entities.user.User;
import auth.entities.user.out.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@ConditionalOnSelectedDatabaseMongo
@Repository
@RequiredArgsConstructor
public class MongoUserRepository implements UserRepository {

    private final SpringDataMongoUserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(UserDocument.from(user))
                .getUser();
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id)
                .map(UserDocument::getUser);
    }

    @Override
    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmailDocumentAddress(email)
                .map(UserDocument::getUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailDocumentAddress(email);
    }

    @Override
    public long count() {
        return userRepository.count();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(UserDocument::getUser);
    }

    @Override
    public Page<User> search(String text, Pageable pageable) {
        return userRepository.findAllBy(new TextCriteria().matching(text), pageable)
                .map(UserDocument::getUser);
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }
}
