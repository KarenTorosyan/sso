package auth.entities.user.out.persistence.mongo.events;

import auth.entities.authority.out.persistence.mongo.AuthorityDocument;
import auth.entities.user.out.persistence.mongo.SpringDataMongoUserAuthorityRepository;
import auth.utils.MongoUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MongoUserAuthorityEvents extends AbstractMongoEventListener<AuthorityDocument> {

    private final SpringDataMongoUserAuthorityRepository mongoUserAuthorityRepository;

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<AuthorityDocument> event) {
        deleteUserAuthorityWhenDeletingAuthority(event);
    }

    private void deleteUserAuthorityWhenDeletingAuthority(BeforeDeleteEvent<AuthorityDocument> event) {
        mongoUserAuthorityRepository.deleteByAuthorityDocumentId(MongoUtils.id(event.getSource()));
    }
}
