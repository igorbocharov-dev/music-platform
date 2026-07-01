package org.musicplatform.auth.support.factory.it.user;


import org.musicplatform.auth.entity.User;
import org.musicplatform.auth.entity.UserAvatar;

import java.util.UUID;

public class UserAvatarFactoryIT {

    public static UserAvatar userAvatar(User user){
        return new UserAvatar(user, UUID.randomUUID() + ".jpg");
    }
}
