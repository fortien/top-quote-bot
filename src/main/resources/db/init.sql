CREATE SCHEMA quotes;

CREATE TABLE quotes(
    user_id VARCHAR NOT NULL,
    quote VARCHAR NOT NULL,
    source VARCHAR NULL,
    description VARCHAR NULL
);
