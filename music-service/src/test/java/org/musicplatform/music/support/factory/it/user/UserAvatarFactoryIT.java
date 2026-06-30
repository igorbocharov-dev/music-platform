package org.musicplatform.music.support.factory.it.user;

import org.musicplatform.music.entity.image.UserAvatar;
import org.musicplatform.music.entity.user.User;

import java.util.UUID;

public class UserAvatarFactoryIT {

    public static UserAvatar userAvatar(User user){
        return new UserAvatar(user, UUID.randomUUID() + ".jpg");
    }
}
