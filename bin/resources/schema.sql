
DROP TABLE Voucher CASCADE CONSTRAINTS;

DROP TYPE seq_voucher FORCE;

CREATE SEQUENCE seq_voucher
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE Voucher (
voucher_ID INTEGER, 
Primary Key (voucher_ID));

INSERT INTO Voucher (voucher_id)
VALUES (seq_voucher.nextval);

INSERT INTO Voucher (voucher_id)
VALUES (seq_voucher.nextval);