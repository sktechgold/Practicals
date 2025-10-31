SET VERIFY OFF;
SET SERVEROUTPUT ON;

ACCEPT user_option NUMBER PROMPT 'Enter 1 for FOR loop or 2 for WHILE loop: '

DECLARE
    v_option NUMBER := &user_option;
    v_radius NUMBER;
    v_area   NUMBER;
BEGIN
    -- Dynamically truncate the table before inserting new data
    EXECUTE IMMEDIATE 'TRUNCATE TABLE areas';

    DBMS_OUTPUT.PUT_LINE('Program started...');

    CASE v_option
        WHEN 1 THEN
            DBMS_OUTPUT.PUT_LINE('Using FOR loop...');
            FOR r IN 5..9 LOOP
                v_area := 3.14159 * r * r;
                INSERT INTO areas VALUES (r, v_area);
            END LOOP;

        WHEN 2 THEN
            DBMS_OUTPUT.PUT_LINE('Using WHILE loop...');
            v_radius := 5;
            WHILE v_radius <= 9 LOOP
                v_area := 3.14159 * v_radius * v_radius;
                INSERT INTO areas VALUES (v_radius, v_area);
                v_radius := v_radius + 1;
            END LOOP;

        ELSE
            DBMS_OUTPUT.PUT_LINE('Invalid option! Enter 1 or 2.');
    END CASE;

    COMMIT;
    DBMS_OUTPUT.PUT_LINE('Data inserted into AREAS table.');
END;
/
