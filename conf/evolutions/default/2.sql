# conf/evolutions/default/2.sql
# --- !Ups

CREATE TABLE password_reset_tokens (
                                       email VARCHAR(255) NOT NULL,
                                       token VARCHAR(255) PRIMARY KEY,
                                       expiry_date DATETIME NOT NULL,
                                       INDEX idx_password_reset_tokens_email (email),
                                       INDEX idx_password_reset_tokens_expiry (expiry_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;