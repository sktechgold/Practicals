SET SERVEROUTPUT ON;

CREATE OR REPLACE FUNCTION fn_GetGrade(p_marks NUMBER)
RETURN VARCHAR2 IS
    v_class VARCHAR2(30);
BEGIN
    IF p_marks BETWEEN 990 AND 1500 THEN
        v_class := 'Distinction';
    ELSIF p_marks BETWEEN 900 AND 989 THEN
        v_class := 'First Class';
    ELSIF p_marks BETWEEN 825 AND 899 THEN
        v_class := 'Higher Second Class';
    ELSE
        v_class := 'Not Categorized';
    END IF;

    RETURN v_class;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 'No Data Found';
    WHEN OTHERS THEN
        RETURN 'Error Occurred';
END;
/
------------------------------------------------------------

CREATE OR REPLACE PROCEDURE proc_Grade IS
BEGIN
    DELETE FROM Result;

    INSERT INTO Result (roll, name, class)
    SELECT roll,
           name,
           fn_GetGrade(total_marks)
    FROM Stud_Marks;

    COMMIT;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('No Data Found in Stud_Marks');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/
------------------------------------------------------------

BEGIN
    proc_Grade;
    DBMS_OUTPUT.PUT_LINE('Student Grades Inserted into Result table');
EXCEPTION
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error in main block: ' || SQLERRM);
END;
/
