COMMIT;

DROP TRIGGER InsertNewVoucher;

DROP TABLE Customer CASCADE CONSTRAINTS;
DROP TABLE Payment_Method CASCADE CONSTRAINTS;
DROP TABLE Transaction CASCADE CONSTRAINTS;
DROP TABLE Voucher CASCADE CONSTRAINTS;
DROP TABLE Review CASCADE CONSTRAINTS;
DROP TABLE Merchant CASCADE CONSTRAINTS;
DROP TABLE Deal CASCADE CONSTRAINTS;
DROP TABLE Location CASCADE CONSTRAINTS;
DROP TABLE Category CASCADE CONSTRAINTS;	


DROP SEQUENCE seq_customer;
DROP SEQUENCE seq_payment_method;
DROP SEQUENCE seq_transaction;
DROP SEQUENCE seq_voucher;
DROP SEQUENCE seq_review;
DROP SEQUENCE seq_deal;
DROP SEQUENCE seq_merchant;
DROP SEQUENCE seq_location;
DROP SEQUENCE seq_category;

CREATE SEQUENCE seq_customer
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
PRIMARY KEY (customer_id)
);
ALTER TABLE Customer
add constraint Customer_email UNIQUE (email_address);


CREATE TABLE Payment_Method(
payment_ID INTEGER, 
cc_number VARCHAR2(18),
cc_vendor VARCHAR2(30),
customer_ID INTEGER,
cc_default INTEGER CHECK (cc_default=0 OR cc_default=1),
PRIMARY KEY (payment_ID,customer_ID),
FOREIGN KEY (customer_ID) REFERENCES Customer (customer_ID) on delete cascade
);


CREATE TABLE Category(
category_ID INTEGER, 
category VARCHAR2(30),
PRIMARY KEY (category_ID)
);


CREATE TABLE Merchant(
merchant_ID INTEGER, 
merchant_name VARCHAR2(50),
PRIMARY KEY (merchant_ID));
ALTER TABLE Merchant
add constraint Merchant_name UNIQUE (merchant_name);


CREATE TABLE Location(
location_ID INTEGER, 
city VARCHAR2(30),
state VARCHAR2(2),
country VARCHAR2(2),
continent VARCHAR2(2),
PRIMARY KEY (location_ID));
ALTER TABLE Location
add constraint Location_name UNIQUE (city, state);


CREATE TABLE Deal(
deal_ID INTEGER,
expiration_date DATE,
description VARCHAR2(500),
quantity_limit INTEGER,
original_price REAL,
deal_price REAL,
sale_start_time DATE,
sale_end_time DATE,
location_ID INTEGER,
category_ID INTEGER,
merchant_ID INTEGER,
PRIMARY KEY (deal_ID),
FOREIGN KEY (location_ID) REFERENCES Location (location_ID),
FOREIGN KEY (category_ID) REFERENCES Category (category_ID),
FOREIGN KEY (merchant_ID) REFERENCES Merchant (merchant_ID)
);

CREATE TABLE Voucher(
voucher_ID INTEGER, 
status VARCHAR2(30),
deal_ID INTEGER,
PRIMARY KEY (voucher_ID),
FOREIGN KEY (deal_ID) REFERENCES Deal (deal_ID) on delete cascade
);

CREATE TABLE Transaction(
transaction_ID INTEGER, 
trans_date DATE,
voucher_ID INTEGER,
payment_ID INTEGER,
customer_ID INTEGER,
PRIMARY KEY (transaction_ID),
FOREIGN KEY (voucher_ID) REFERENCES Voucher (voucher_ID),
FOREIGN KEY (payment_ID,customer_ID) REFERENCES Payment_Method (payment_ID,customer_ID) on delete cascade
);

CREATE TABLE Review(
review_ID INTEGER, 
rating INTEGER,
comments VARCHAR2(500),
deal_ID INTEGER,
customer_ID INTEGER,
PRIMARY KEY (review_ID),
FOREIGN KEY (deal_ID) REFERENCES Deal (deal_ID),
FOREIGN KEY (customer_ID) REFERENCES Customer (customer_ID) on delete cascade
);

//CREATE TABLE Shopping_Cart AS(
//SELECT T.transaction_ID, T.trans_date, T.voucher_ID, PD.deal_ID,PW.payment_ID,PW.customer_ID
//FROM Transaction T
//INNER JOIN Purchase_Deal PD
//ON T.voucher_ID=PD.voucher_ID
//INNER JOIN Purchase_With PW
//ON PD.voucher_ID=PW.voucher_ID);






