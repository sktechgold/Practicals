SET SERVEROUTPUT ON;
SET VERIFY OFF;

-- Drop existing objects if already present
DROP SEQUENCE Audit_seq;
DROP TRIGGER before_update_library;
DROP TRIGGER after_update_library;
DROP TRIGGER before_delete_library;
DROP TRIGGER after_delete_library;

-------------------------------------------------------

CREATE SEQUENCE Audit_seq START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER before_update_library
BEFORE UPDATE ON Library
FOR EACH ROW
BEGIN
    INSERT INTO Library_Audit (AuditID, BookID, Title, Author, Price, ActionType)
    VALUES (Audit_seq.NEXTVAL, :OLD.BookID, :OLD.Title, :OLD.Author, :OLD.Price, 'BEFORE UPDATE');
END;
/

CREATE OR REPLACE TRIGGER after_update_library
AFTER UPDATE ON Library
FOR EACH ROW
BEGIN
    INSERT INTO Library_Audit (AuditID, BookID, Title, Author, Price, ActionType)
    VALUES (Audit_seq.NEXTVAL, :NEW.BookID, :NEW.Title, :NEW.Author, :NEW.Price, 'AFTER UPDATE');
END;
/

CREATE OR REPLACE TRIGGER before_delete_library
BEFORE DELETE ON Library
FOR EACH ROW
BEGIN
    INSERT INTO Library_Audit (AuditID, BookID, Title, Author, Price, ActionType)
    VALUES (Audit_seq.NEXTVAL, :OLD.BookID, :OLD.Title, :OLD.Author, :OLD.Price, 'BEFORE DELETE');
END;
/

CREATE OR REPLACE TRIGGER after_delete_library
AFTER DELETE ON Library
FOR EACH ROW
BEGIN
    INSERT INTO Library_Audit (AuditID, BookID, Title, Author, Price, ActionType)
    VALUES (Audit_seq.NEXTVAL, :OLD.BookID, :OLD.Title, :OLD.Author, :OLD.Price, 'AFTER DELETE');
END;
/
