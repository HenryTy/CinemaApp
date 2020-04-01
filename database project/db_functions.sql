CREATE OR REPLACE TYPE NUMBER_ARRAY IS TABLE OF NUMBER;

CREATE OR REPLACE FUNCTION add_ticket(p_showing_id showing.showing_id%TYPE,
                            p_user_id users.user_id%TYPE,
                            p_rows NUMBER_ARRAY,
                            p_seats_in_row NUMBER_ARRAY)
RETURN ticket.ticket_number%TYPE
IS
    v_generated_number VARCHAR2(255 CHAR);
    v_count INTEGER;
    i INTEGER;
    j INTEGER;
BEGIN
    i := p_rows.FIRST;
    j := p_seats_in_row.FIRST;
    WHILE i IS NOT NULL LOOP
        SELECT COUNT(r.row_number) INTO v_count
        FROM reservation r INNER JOIN ticket t USING(ticket_number)
        WHERE r.row_number = p_rows(i) AND r.seat_in_row = p_seats_in_row(i)
            AND t.showing_id = p_showing_id;
        IF v_count > 0 THEN
            RAISE_APPLICATION_ERROR(-20000, 'Miejsce '
            || p_seats_in_row(i) || ' w rzedzie '  || p_rows(i) || ' jest juz zarezerwowane na podany seans');
        END IF;
        i := p_rows.NEXT(i);
        j := p_seats_in_row.NEXT(j);
    END LOOP;
    
    LOOP
        v_generated_number := DBMS_RANDOM.string('x', 255);
        INSERT INTO ticket VALUES(v_generated_number, CURRENT_DATE, 'VALID', p_showing_id, p_user_id);
        EXIT WHEN SQL%FOUND;
    END LOOP;
    
    i := p_rows.FIRST;
    j := p_seats_in_row.FIRST;
    WHILE i IS NOT NULL LOOP
        INSERT INTO reservation VALUES(p_rows(i), p_seats_in_row(i), v_generated_number);
        i := p_rows.NEXT(i);
        j := p_seats_in_row.NEXT(j);
    END LOOP;
    
    RETURN v_generated_number;
END;

CREATE OR REPLACE PROCEDURE cancel_showing(p_showing_id showing.showing_id%TYPE, p_message notification.message%TYPE)
IS
    v_notification_id NUMBER(10);
    v_notification_title VARCHAR2(255 CHAR);
    v_movie_title VARCHAR2(50 CHAR);
    v_showing_date DATE;
BEGIN
    SELECT m.title, sh.showing_date INTO v_movie_title, v_showing_date
    FROM showing sh INNER JOIN movie m USING(Movie_ID)
    WHERE sh.Showing_ID = p_showing_id;
    v_notification_id := seq_notification.nextval;
    v_notification_title := 'Odwo³anie seansu filmu ' || v_movie_title || TO_CHAR(v_showing_date, '(dd.mm.yyyy HH24:MI)');
    INSERT INTO notification VALUES(v_notification_id, v_notification_title, p_message);
    
    FOR u IN(SELECT DISTINCT User_ID FROM ticket WHERE Showing_ID = p_showing_id) LOOP
        INSERT INTO us_not_rel VALUES(u.User_ID, v_notification_id);
    END LOOP;
    
    DELETE FROM showing WHERE Showing_ID = p_showing_id;
END;

CREATE OR REPLACE PROCEDURE mark_notification_as_read(p_user_id users.user_id%TYPE, p_notification_id notification.id%TYPE)
IS
    v_count INTEGER;
BEGIN
    DELETE FROM us_not_rel WHERE user_id = p_user_id AND notification_id = p_notification_id;
    
    SELECT COUNT(user_id) INTO v_count FROM us_not_rel WHERE notification_id = p_notification_id;
    
    IF v_count = 0 THEN
        DELETE FROM notification WHERE id = p_notification_id;
    END IF;
END;

CREATE OR REPLACE TRIGGER check_hall_has_future_showings
    BEFORE DELETE OR UPDATE ON hall
    FOR EACH ROW
DECLARE
    v_count INTEGER;
BEGIN
    SELECT COUNT(hall_id) INTO v_count FROM showing WHERE hall_id = :OLD.hall_id AND showing_date > current_date;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20000, 'Nie mo¿na zmodyfikowaæ sali, w której zaplanowane s¹ przysz³e seanse');
    END IF;
    CASE
        WHEN DELETING THEN
            DELETE FROM showing WHERE hall_id = :OLD.hall_id;
        WHEN UPDATING THEN null;
    END CASE;
END;

CREATE OR REPLACE TRIGGER check_movie_has_future_showings
    BEFORE DELETE OR UPDATE ON movie
    FOR EACH ROW
DECLARE
    v_count INTEGER;
BEGIN
    SELECT COUNT(movie_id) INTO v_count FROM showing WHERE movie_id = :OLD.movie_id AND showing_date > current_date;
    IF v_count > 0 THEN
        RAISE_APPLICATION_ERROR(-20000, 'Nie mo¿na zmodyfikowaæ filmu, na który zaplanowane s¹ przysz³e seanse');
    END IF;
    CASE
        WHEN DELETING THEN
            DELETE FROM showing WHERE movie_id = :OLD.movie_id;
        WHEN UPDATING THEN null;
    END CASE;
END;