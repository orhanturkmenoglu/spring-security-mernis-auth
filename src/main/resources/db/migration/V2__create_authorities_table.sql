CREATE TABLE authorities
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    authority VARCHAR(255),
    user_id   BIGINT NOT NULL,
    CONSTRAINT fk_authority_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);
