DROP TABLE IF EXISTS users, requests, items, comments, bookings;
CREATE TABLE IF NOT EXISTS users
(
    id    INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name  VARCHAR(255)                             NOT NULL,
    email VARCHAR(255)                             NOT NULL,
    CONSTRAINT pk_user PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    description  VARCHAR(255)                             NOT NULL,
    requestor_id INTEGER,
    created      TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_request PRIMARY KEY (id),
    CONSTRAINT fk_requests_to_users FOREIGN KEY (requestor_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS items
(
    id           INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name         VARCHAR(255)                             NOT NULL,
    description  VARCHAR(512)                             NOT NULL,
    is_available BOOLEAN,
    owner_id     INTEGER,
    request_id   INTEGER,
    CONSTRAINT pk_item PRIMARY KEY (id),
    CONSTRAINT fk_items_to_users FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS bookings
(
    id         INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    start_date TIMESTAMP WITHOUT TIME ZONE              NOT NULL,
    end_date   TIMESTAMP WITHOUT TIME ZONE              NOT NULL,
    item_id    INTEGER                                  NOT NULL,
    booker_id  INTEGER                                  NOT NULL,
    status     VARCHAR(255)                             NOT NULL,
    CONSTRAINT pk_booking PRIMARY KEY (id),
    CONSTRAINT fk_bookings_to_items FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
    CONSTRAINT fk_bookings_to_users FOREIGN KEY (booker_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS comments
(
    id        INTEGER GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text1      VARCHAR(512)                             NOT NULL,
    item_id   INTEGER                                  NOT NULL,
    author_id INTEGER                                  NOT NULL,
    created   TIMESTAMP WITHOUT TIME ZONE              NOT NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id),
    CONSTRAINT fk_comments_to_items FOREIGN KEY (item_id) REFERENCES items (id) ON DELETE CASCADE,
    CONSTRAINT fk_comments_to_users FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
);