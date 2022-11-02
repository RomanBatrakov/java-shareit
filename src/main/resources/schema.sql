CREATE TABLE IF NOT EXISTS users
(
    id    INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                             NOT NULL,
    email VARCHAR(512)                             NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS items
(
    id          INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name        VARCHAR(255)                             NOT NULL,
    description VARCHAR(512)                             NOT NULL,
    available   BOOLEAN                                  NOT NULL,
    owner_id    INTEGER REFERENCES users,
    request_id  INTEGER,
    CONSTRAINT pk_item PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP                                NOT NULL,
    end_date   TIMESTAMP                                NOT NULL,
    item_id    INTEGER                                  NOT NULL REFERENCES items ON DELETE CASCADE,
    booker_id  INTEGER                                  NOT NULL REFERENCES users ON DELETE CASCADE,
    status     INTEGER                                  NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR(512),
    requestor_id INTEGER REFERENCES users ON DELETE CASCADE,
    CONSTRAINT pk_request PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id        INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text      VARCHAR(512),
    item_id   INTEGER REFERENCES items ON DELETE CASCADE,
    author_id INTEGER REFERENCES users ON DELETE CASCADE,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS statuses
(
    id    INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title VARCHAR(30)                              NOT NULL,
    CONSTRAINT pk_status PRIMARY KEY (id)
);

ALTER TABLE items
    ADD FOREIGN KEY (request_id) REFERENCES requests;

MERGE INTO statuses (id, title)
    VALUES (1, 'WAITING'),
           (2, 'APPROVED'),
           (3, 'REJECTED'),
           (4, 'CANCELED');