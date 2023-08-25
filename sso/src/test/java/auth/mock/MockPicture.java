package auth.mock;

import auth.entities.picture.Picture;

public class MockPicture {

    public static Picture mock() {
        return new Picture("pictureUrl");
    }
}
