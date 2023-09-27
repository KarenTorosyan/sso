package auth.entities.user.out.persistence.mongo.events;

import auth.configs.security.InitialAuthorities;
import auth.entities.authority.out.persistence.mongo.AuthorityDocument;
import auth.entities.authority.out.persistence.mongo.SpringDataMongoAuthorityRepository;
import auth.entities.user.out.persistence.mongo.SpringDataMongoUserAuthorityRepository;
import auth.entities.user.out.persistence.mongo.SpringDataMongoUserRepository;
import auth.entities.user.out.persistence.mongo.UserAuthorityDocument;
import auth.entities.user.out.persistence.mongo.UserDocument;
import auth.utils.MongoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.mapping.event.*;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class MongoUserEvents extends AbstractMongoEventListener<UserDocument> {

    private final SpringDataMongoAuthorityRepository mongoAuthorityRepository;
    private final SpringDataMongoUserAuthorityRepository mongoUserAuthorityRepository;
    private final SpringDataMongoUserRepository mongoUserRepository;

    private boolean userNew;

    @Override
    public void onBeforeSave(BeforeSaveEvent<UserDocument> event) {
        userNew = !mongoUserRepository.existsById(event.getSource().getId());
    }

    @Override
    public void onAfterSave(AfterSaveEvent<UserDocument> event) {
        UserDocument userDocument = event.getSource();
        if (userNew) {
            applyInitialAuthorities(userDocument);
        }
    }

    private void applyInitialAuthorities(UserDocument userDocument) {
        if (mongoUserRepository.count() == 1) {
            Stream.of(InitialAuthorities.admin())
                    .filter(mongoAuthorityRepository::existsByName)
                    .map(authority -> mongoAuthorityRepository.findByName(authority).orElseThrow())
                    .forEach(authorityDocument -> saveUserAuthority(userDocument, authorityDocument));
        } else Stream.of(InitialAuthorities.user())
                .filter(mongoAuthorityRepository::existsByName)
                .map(authority -> mongoAuthorityRepository.findByName(authority).orElseThrow())
                .forEach(authorityDocument -> saveUserAuthority(userDocument, authorityDocument));
    }

    private void saveUserAuthority(UserDocument userDocument, AuthorityDocument authorityDocument) {
        var userAuthorityDocument = new UserAuthorityDocument(userDocument.getId(), authorityDocument);
        mongoUserAuthorityRepository.save(userAuthorityDocument);
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<UserDocument> event) {
        String id = MongoUtils.id(event.getSource());
        deleteUserAuthoritiesOnDeleteUser(id);
    }

    private void deleteUserAuthoritiesOnDeleteUser(String id) {
        mongoUserAuthorityRepository.deleteAll(mongoUserAuthorityRepository.findAllByUserId(id, Pageable.unpaged()));
    }
}
