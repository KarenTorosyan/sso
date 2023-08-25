package auth.utils;

import org.bson.Document;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MongoUtilsTest {

    @Test
    void shouldReceiveIdAttributeValueFromDocumentByActualAttributeNameAndType() {
        String specifiedId = "id";
        String id = MongoUtils.id(new Document()
                .append(MongoUtils.ID_ATTR, specifiedId));
        assertThat(id)
                .isEqualTo(specifiedId);
    }

    @Test
    void shouldThrowErrorWhenReceiveIdAttributeValueFromDocumentWhenAttributeNameDifferent() {
        String specifiedId = "id";
        assertThatThrownBy(() -> MongoUtils.id(new Document()
                .append("nonExpectedIdAttributeName", specifiedId)))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldThrowErrorWhenReceiveIdAttributeValueFromDocumentWhenAttributeTypeDifferent() {
        long nonExpectedIdAttributeType = 1;
        assertThatThrownBy(() -> MongoUtils.id(new Document()
                .append(MongoUtils.ID_ATTR, nonExpectedIdAttributeType)))
                .isInstanceOf(ClassCastException.class);
    }
}
