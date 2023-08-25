package auth.utils;

import auth.errors.Errors;
import org.bson.Document;

public class MongoUtils {

    public static final String ID_ATTR = "_id";

    public static String id(Document document) {
        String idString = document.get(ID_ATTR, String.class);
        if (idString != null) {
            return idString;
        }
        throw Errors.cannotReceiveMongoDocumentId(ID_ATTR);
    }
}
