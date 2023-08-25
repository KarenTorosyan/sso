package auth.mock;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;

public interface Mock<T> {

    T mock();

    default Collection<T> mockCollection() {
        return List.of(mock());
    }

    default Collection<T> mockCollection(Collection<T> collection) {
        return collection;
    }

    default Page<T> mockPage(Pageable pageable) {
        Collection<T> collection = mockCollection();
        return new PageImpl<>(List.copyOf(collection), pageable, collection.size());
    }

    default Page<T> mockPage(Pageable pageable, Collection<T> collection) {
        return new PageImpl<>(List.copyOf(collection), pageable, collection.size());
    }
}
