DROP TABLE Customer CASCADE CONSTRAINTS;
DROP TABLE Status CASCADE CONSTRAINTS;
DROP TABLE Payment_Method CASCADE CONSTRAINTS;
DROP TABLE Cust_Trans CASCADE CONSTRAINTS;
DROP TABLE Transaction CASCADE CONSTRAINTS;
DROP TABLE Voucher CASCADE CONSTRAINTS;
DROP TABLE Review CASCADE CONSTRAINTS;
DROP TABLE Merchant CASCADE CONSTRAINTS;
DROP TABLE Deal CASCADE CONSTRAINTS;
DROP TABLE Location CASCADE CONSTRAINTS;
DROP TABLE Category CASCADE CONSTRAINTS;

DROP TYPE seq_customer FORCE;
DROP TYPE seq_payment_method FORCE;
DROP TYPE seq_transaction FORCE;
DROP TYPE seq_voucher FORCE;
DROP TYPE seq_review FORCE;
DROP TYPE seq_deal FORCE;
DROP TYPE seq_merchant FORCE;
DROP TYPE seq_location FORCE;
DROP TYPE seq_category FORCE;

CREATE SEQUENCE seq_customer
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE SEQUENCE seq_status
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE SEQUENCE seq_payment_method
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE SEQUENCE seq_transaction
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE SEQUENCE seq_voucher
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE SEQUENCE seq_review
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE SEQUENCE seq_merchant
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE SEQUENCE seq_deal
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE SEQUENCE seq_location
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE SEQUENCE seq_category
MINVALUE 1
START WITH 1
INCREMENT BY 1
CACHE 10;

CREATE TABLE Customer( 
customer_ID INTEGER, 
first_name VARCHAR2(30), 
last_name VARCHAR2(30), 
age INTEGER, 
email_address VARCHAR2(100), 
gender VARCHAR2(1), 
PRIMARY KEY (customer_ID,email_address)
);
INSERT INTO Customer VALUES (seq_voucher.nextval,'John','Doe','30','jdoe@gmu.edu','m');

CREATE TABLE Status(
status_ID INTEGER, 
status VARCHAR2(30),
PRIMARY KEY (status_ID)
);
INSERT INTO Status VALUES (seq_status.nextval,'current');

CREATE TABLE Payment_Method (
payment_ID INTEGER, 
cc_number VARCHAR2(16),
cc_default_number VARCHAR2(16),
cc_vendor VARCHAR2(30),
customer_ID INTEGER,
PRIMARY KEY (payment_ID,customer_ID),
FOREIGN KEY (customer_ID) REFERENCES Customer (customer_ID)
);
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'1111222233334444','1111222233334444','visa','1');

CREATE TABLE Cust_Trans (
payment_ID INTEGER, 
customer_ID INTEGER,
PRIMARY KEY (payment_ID),
FOREIGN KEY (customer_ID) REFERENCES Customer (customer_ID)
);
INSERT INTO Cust_Trans VALUES ('1','1');

CREATE TABLE Transaction (
transaction_ID INTEGER, 
trans_date DATE,
payment_ID INTEGER,
voucher_ID INTEGER,
PRIMARY KEY (transaction_ID),
FOREIGN KEY (payment_ID) REFERENCES Payment_Method (payment_ID),
FOREIGN KEY (voucher_ID) REFERENCES Voucher (voucher_ID)
);
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','1','1');

CREATE TABLE Voucher (
voucher_ID INTEGER, 
deal_ID INTEGER,
status_ID INTEGER,
PRIMARY KEY (voucher_ID),
FOREIGN KEY (deal_ID) REFERENCES Payment_Method (deal_ID),
FOREIGN KEY (status_ID) REFERENCES Payment_Method (status_ID)
);
INSERT INTO Voucher VALUES (seq_voucher.nextval,'1','1');

CREATE TABLE Review (
review_ID INTEGER, 
rating INTEGER,
comments VARCHAR2(500),
deal_ID INTEGER,
transaction_ID INTEGER,
PRIMARY KEY (review_ID),
FOREIGN KEY (deal_ID) REFERENCES Payment_Method (deal_ID),
FOREIGN KEY (transaction_ID) REFERENCES Payment_Method (transaction_ID)
);
INSERT INTO Review VALUES (seq_review.nextval,'5','Great deal!','1','1');

CREATE TABLE Merchant (
merchant_ID INTEGER, 
merchant_name VARCHAR2(30),
PRIMARY KEY (merchant_ID)
);
INSERT INTO Merchant VALUES (seq_merchant.nextval,'groupon');

CREATE TABLE Deal (
deal_ID INTEGER,
expiration_date DATE,
description VARCHAR2(500),
quantity_limit INTEGER,
orignal_price REAL,
deal_price REAL,
sale_start_time DATE,
sale_end_time DATE,
location_ID INTEGER,
category_ID INTEGER,
PRIMARY KEY (deal_ID),
FOREIGN KEY (location_ID) REFERENCES Payment_Method (location_ID),
FOREIGN KEY (category_ID) REFERENCES Payment_Method (category_ID)
);
INSERT INTO Deal VALUES (seq_deal.nextval,'10-Apr-2013','$3 off chipotle burrito.','20','8.50','5.50','01-Apr-2013','25-Apr-2013','1','1');

CREATE TABLE Location (
location_ID INTEGER, 
city VARCHAR2(30),
state VARCHAR2(2),
country VARCHAR2(2),
continent VARCHAR2(2),
merchant_id INTEGER,
PRIMARY KEY (location_ID),
FOREIGN KEY (merchant_ID) REFERENCES Payment_Method (merchant_ID)
);
INSERT INTO Location VALUES (seq_location.nextval,'Fairfax','VA','US','NA','1');

CREATE TABLE Category (
category_ID INTEGER, 
category VARCHAR2(30),
PRIMARY KEY (category_ID)
);
INSERT INTO Category VALUES (seq_category.next_val,'food');

INSERT INTO Voucher (voucher_id)
VALUES (seq_voucher.nextval);

INSERT INTO Voucher (voucher_id)
VALUES (seq_voucher.nextval);