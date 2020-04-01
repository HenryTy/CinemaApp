CREATE TABLE hall (
    name           VARCHAR2(50 CHAR) NOT NULL,
    row_count      NUMBER(2) NOT NULL,
    seats_in_row   NUMBER(2) NOT NULL,
    hall_id        NUMBER NOT NULL
);

CREATE INDEX hall_name_idx ON
    hall (
        name
    ASC );

ALTER TABLE hall ADD CONSTRAINT hall_ck_row_count CHECK ( row_count > 0 );

ALTER TABLE hall ADD CONSTRAINT hall_ck_max_row_count CHECK ( row_count <= 20 );

ALTER TABLE hall ADD CONSTRAINT hall_ck_seats_in_row CHECK ( seats_in_row > 0 );

ALTER TABLE hall ADD CONSTRAINT hall_ck_max_seats_in_row CHECK ( seats_in_row <= 30 );

ALTER TABLE hall ADD CONSTRAINT hall_pk PRIMARY KEY ( hall_id );

ALTER TABLE hall ADD CONSTRAINT hall_name_un UNIQUE ( name );

CREATE TABLE movie (
    title              VARCHAR2(50 CHAR) NOT NULL,
    production_year    NUMBER(4) NOT NULL,
    genre              VARCHAR2(50 CHAR) NOT NULL,
    length_minutes     NUMBER(3) NOT NULL,
    director           VARCHAR2(50 CHAR),
    allowed_from_age   NUMBER(2),
    movie_id           NUMBER NOT NULL
);

ALTER TABLE movie
    ADD CHECK ( genre IN (
        'ACTION',
        'ADVENTURE',
        'ANIMATION',
        'BIOGRAPHICAL',
        'COMEDY',
        'CRIME',
        'DRAMA',
        'FAMILY',
        'FANTASY',
        'HORROR',
        'ROMANTIC_COMEDY',
        'SCI_FI',
        'SPORT',
        'THRILLER'
    ) );

CREATE INDEX movie_title_idx ON
    movie (
        title
    ASC );

CREATE INDEX movie_genre_idx ON
    movie (
        genre
    ASC );

ALTER TABLE movie
    ADD CONSTRAINT movie_ck_production_year CHECK ( production_year >= 1900
                                                    AND production_year <= 2500 );

ALTER TABLE movie ADD CONSTRAINT movie_ck_length_minutes CHECK ( length_minutes > 0 );

ALTER TABLE movie ADD CONSTRAINT movie_ck_age CHECK ( allowed_from_age > 0 );

ALTER TABLE movie ADD CONSTRAINT movie_pk PRIMARY KEY ( movie_id );

ALTER TABLE movie ADD CONSTRAINT movie_title_production_year_un UNIQUE ( title,
                                                                         production_year );

CREATE TABLE notification (
    id        NUMBER(10) NOT NULL,
    title     VARCHAR2(255 CHAR) NOT NULL,
    message   VARCHAR2(255 CHAR) NOT NULL
);

ALTER TABLE notification ADD CONSTRAINT notification_pk PRIMARY KEY ( id );

CREATE TABLE rating (
    value      NUMBER(2) NOT NULL,
    movie_id   NUMBER NOT NULL,
    user_id    NUMBER NOT NULL
);

ALTER TABLE rating
    ADD CONSTRAINT rating_ck_value CHECK ( value BETWEEN 0 AND 10 );

ALTER TABLE rating ADD CONSTRAINT rating_pk PRIMARY KEY ( user_id,
                                                          movie_id );

CREATE TABLE reservation (
    row_number      NUMBER(2) NOT NULL,
    seat_in_row     NUMBER(2) NOT NULL,
    ticket_number   VARCHAR2(255 CHAR) NOT NULL
);

ALTER TABLE reservation ADD CONSTRAINT reservation_ck_row CHECK ( row_number > 0 );

ALTER TABLE reservation ADD CONSTRAINT reservation_ck_seat CHECK ( seat_in_row > 0 );

ALTER TABLE reservation
    ADD CONSTRAINT reservation_pk PRIMARY KEY ( row_number,
                                                seat_in_row,
                                                ticket_number );

CREATE TABLE showing (
    showing_date   DATE NOT NULL,
    showing_id     NUMBER NOT NULL,
    hall_id        NUMBER NOT NULL,
    movie_id       NUMBER NOT NULL
);

CREATE INDEX showing_movie_idx ON
    showing (
        movie_id
    ASC );

CREATE INDEX showing_date_hall_idx ON
    showing (
        showing_date
    ASC,
        hall_id
    ASC );

ALTER TABLE showing ADD CONSTRAINT showing_pk PRIMARY KEY ( showing_id );

ALTER TABLE showing ADD CONSTRAINT showing_hall_date_un UNIQUE ( showing_date,
                                                                 hall_id );

CREATE TABLE ticket (
    ticket_number   VARCHAR2(255 CHAR) NOT NULL,
    purchase_date   DATE NOT NULL,
    status          VARCHAR2(20 CHAR) NOT NULL,
    showing_id      NUMBER NOT NULL,
    user_id         NUMBER NOT NULL
);

ALTER TABLE ticket
    ADD CHECK ( status IN (
        'USED',
        'VALID'
    ) );

CREATE INDEX ticket_showing_idx ON
    ticket (
        showing_id
    ASC );

CREATE INDEX ticket_user_idx ON
    ticket (
        user_id
    ASC );

ALTER TABLE ticket ADD CONSTRAINT ticket_pk PRIMARY KEY ( ticket_number );

CREATE TABLE us_not_rel (
    user_id           NUMBER NOT NULL,
    notification_id   NUMBER(10) NOT NULL
);

ALTER TABLE us_not_rel ADD CONSTRAINT us_not_rel_pk PRIMARY KEY ( user_id,
                                                                  notification_id );

CREATE TABLE users (
    email           VARCHAR2(50 CHAR) NOT NULL,
    password        VARCHAR2(255 CHAR) NOT NULL,
    name            VARCHAR2(50 CHAR) NOT NULL,
    surname         VARCHAR2(50 CHAR) NOT NULL,
    date_of_birth   DATE NOT NULL,
    role            VARCHAR2(20 CHAR) NOT NULL,
    points          NUMBER(5) NOT NULL,
    user_id         NUMBER NOT NULL
);

ALTER TABLE users
    ADD CHECK ( role IN (
        'ROLE_ADMIN',
        'ROLE_USER'
    ) );

CREATE INDEX user_email_idx ON
    users (
        email
    ASC );

ALTER TABLE users ADD CONSTRAINT user_ck_points CHECK ( points >= 0 );

ALTER TABLE users ADD CONSTRAINT user_ck_max_points CHECK ( points <= 100 );

ALTER TABLE users ADD CONSTRAINT user_pk PRIMARY KEY ( user_id );

ALTER TABLE users ADD CONSTRAINT user_email_un UNIQUE ( email );

ALTER TABLE rating
    ADD CONSTRAINT rating_movie_fk FOREIGN KEY ( movie_id )
        REFERENCES movie ( movie_id )
            ON DELETE CASCADE;

ALTER TABLE rating
    ADD CONSTRAINT rating_user_fk FOREIGN KEY ( user_id )
        REFERENCES users ( user_id )
            ON DELETE CASCADE;

ALTER TABLE reservation
    ADD CONSTRAINT reservation_ticket_fk FOREIGN KEY ( ticket_number )
        REFERENCES ticket ( ticket_number )
            ON DELETE CASCADE;

ALTER TABLE showing
    ADD CONSTRAINT showing_hall_fk FOREIGN KEY ( hall_id )
        REFERENCES hall ( hall_id );

ALTER TABLE showing
    ADD CONSTRAINT showing_movie_fk FOREIGN KEY ( movie_id )
        REFERENCES movie ( movie_id );

ALTER TABLE ticket
    ADD CONSTRAINT ticket_showing_fk FOREIGN KEY ( showing_id )
        REFERENCES showing ( showing_id )
            ON DELETE CASCADE;

ALTER TABLE ticket
    ADD CONSTRAINT ticket_user_fk FOREIGN KEY ( user_id )
        REFERENCES users ( user_id )
            ON DELETE CASCADE;

ALTER TABLE us_not_rel
    ADD CONSTRAINT us_not_rel_notification_fk FOREIGN KEY ( notification_id )
        REFERENCES notification ( id )
            ON DELETE CASCADE;

ALTER TABLE us_not_rel
    ADD CONSTRAINT us_not_rel_user_fk FOREIGN KEY ( user_id )
        REFERENCES users ( user_id )
            ON DELETE CASCADE;

CREATE SEQUENCE seq_hall;

CREATE SEQUENCE seq_movie;

CREATE SEQUENCE seq_notification;

CREATE SEQUENCE seq_showing;

CREATE SEQUENCE seq_user;
