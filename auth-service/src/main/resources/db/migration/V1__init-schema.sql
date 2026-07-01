CREATE TABLE users(
    id       bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    username varchar(55)  NOT NULL UNIQUE,
    email    varchar(255) NOT NULL UNIQUE,
    password varchar(255) NOT NULL,
    date_of_birth    date NOT NULL CHECK ( date_of_birth > date '1900-01-01' ),
    failed_login_attempts int NOT NULL CHECK ( failed_login_attempts >= 0 ) default 0,
    lock_time timestamptz DEFAULT NULL,
    enabled   boolean NOT NULL default false,
    role_name varchar(55)  NOT NULL
);

CREATE TABLE user_avatar(
    id      bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    s3_key  varchar(512)  NOT NULL default 'default_avatar.jpg',
    user_id bigint UNIQUE NOT NULL REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE refresh_token(
    id          bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id     bigint       NOT NULL UNIQUE,
    token_hash  varchar(255) NOT NULL UNIQUE,
    expiry_date timestamptz  NOT NULL,
    revoked     boolean      NOT NULL default false
);

CREATE TABLE verification_token(
    id          bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id     bigint          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token       varchar(255) NOT NULL UNIQUE,
    expiry_date timestamptz  NOT NULL
);