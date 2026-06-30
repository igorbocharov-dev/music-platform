package org.musicplatform.music.repository.image;

import org.musicplatform.music.entity.image.UserAvatar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAvatarRepository extends JpaRepository<UserAvatar, Long> {

    UserAvatar findByUserId(Long userId);
}
