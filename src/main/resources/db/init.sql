CREATE SCHEMA quotes;

--CREATE TABLE quotes(
--    user_id VARCHAR NOT NULL,
--    quote VARCHAR NOT NULL,
--    source VARCHAR NULL,
--    description VARCHAR NULL
--);


CREATE TABLE users (
    chat_id BIGINT PRIMARY KEY,
    username VARCHAR(255),
    first_name VARCHAR(255),
    last_name VARCHAR(255)
);

CREATE TABLE quotes (
    id BIGSERIAL PRIMARY KEY,
    text TEXT NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    user_chat_id BIGINT REFERENCES users(chat_id)
);

CREATE INDEX idx_quotes_user ON quotes(user_chat_id);