ACCEPT v_roll NUMBER PROMPT 'Enter Roll No: '
ACCEPT v_book CHAR PROMPT 'Enter Book Name: '

SET SERVEROUTPUT ON;

DECLARE
    v_rollno      NUMBER := &v_roll;
    v_bookname    VARCHAR2(50) := '&v_book';
    v_dateissue   DATE;
    v_days        NUMBER;
    v_fine        NUMBER := 0;

BEGIN
    -- Fetch Date of Issue
    SELECT DateofIssue INTO v_dateissue
    FROM Borrower
    WHERE Roll_no = v_rollno AND NameofBook = v_bookname AND Status = 'I';

    -- Days kept
    v_days := TRUNC(SYSDATE - v_dateissue);

    -- Fine Calculation
    IF v_days <= 15 THEN
        v_fine := 0;
    ELSIF v_days > 15 AND v_days <= 30 THEN
        v_fine := (v_days - 15) * 5;
    ELSE
        v_fine := (15 * 5) + ((v_days - 30) * 50);
    END IF;

    -- Update status
    UPDATE Borrower
    SET Status = 'R'
    WHERE Roll_no = v_rollno AND NameofBook = v_bookname;

    -- Insert fine if any
    IF v_fine > 0 THEN
        INSERT INTO Fine (Roll_no, FineDate, Amt)
        VALUES (v_rollno, SYSDATE, v_fine);
    END IF;

    COMMIT;

    DBMS_OUTPUT.PUT_LINE('Book Returned Successfully.');
    DBMS_OUTPUT.PUT_LINE('Days Kept: ' || v_days);
    DBMS_OUTPUT.PUT_LINE('Fine Amount: ' || v_fine);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('No record found for given Roll_no and Book.');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
