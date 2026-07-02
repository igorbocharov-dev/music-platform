ALTER TABLE sound_like DROP CONSTRAINT sound_like_user_id_fkey;
ALTER TABLE album_like DROP CONSTRAINT album_like_user_id_fkey;

DROP TABLE verification_token;
DROP TABLE refresh_token;
DROP TABLE user_avatar;
DROP TABLE users;