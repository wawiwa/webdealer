DROP TABLE Customer CASCADE CONSTRAINTS;
DROP TABLE Payment_Method CASCADE CONSTRAINTS;
DROP TABLE Transaction CASCADE CONSTRAINTS;
DROP TABLE Voucher CASCADE CONSTRAINTS;
DROP TABLE Review CASCADE CONSTRAINTS;
DROP TABLE Merchant CASCADE CONSTRAINTS;
DROP TABLE Deal CASCADE CONSTRAINTS;
DROP TABLE Location CASCADE CONSTRAINTS;
DROP TABLE Category CASCADE CONSTRAINTS;	
DROP TABLE Purchase	 CASCADE CONSTRAINTS;
DROP TABLE Purchase_Deal CASCADE CONSTRAINTS;
DROP TABLE Purchase_With CASCADE CONSTRAINTS;


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
PRIMARY KEY (customer_ID)
);
INSERT INTO Customer VALUES (seq_customer.nextval,'John','Doe','30','jdoe@gmu.edu','m');
INSERT INTO Customer VALUES (seq_customer.nextval,'Betsy','Smith','19','bsmith@gmu.edu','f');
INSERT INTO Customer VALUES (seq_customer.nextval,'Eogan','Snyders','52','esnyders@gmu.edu','m');
INSERT INTO Customer VALUES (seq_customer.nextval,'Agnes','Haroldson','67','aharoldson@gmu.edu','f');
INSERT INTO Customer VALUES (seq_customer.nextval,'Heimirich','Niemec','25','hniemec@gmu.edu','m');
INSERT INTO Customer VALUES (seq_customer.nextval,'Gunda','Dieter','39','gdieter@gmu.edu','f');
INSERT INTO Customer VALUES (seq_customer.nextval,'Cezary','Rios','42','crios@gmu.edu','m');
INSERT INTO Customer VALUES (seq_customer.nextval,'Thekla','Araya','29','taraya@gmu.edu','f');
INSERT INTO Customer VALUES (seq_customer.nextval,'Ovadyah','Haugen','53','ohaugen@gmu.edu','m');
INSERT INTO Customer VALUES (seq_customer.nextval,'Peggy','Ortiz','25','portiz@gmu.edu','f');

CREATE TABLE Payment_Method(
payment_ID INTEGER, 
cc_number VARCHAR2(16),
cc_default_number VARCHAR2(16),
cc_vendor VARCHAR2(30),
customer_ID INTEGER,
cc_default INTEGER CHECK (cc_default=0 OR cc_default=1),
PRIMARY KEY (payment_ID,customer_ID),
FOREIGN KEY (customer_ID) REFERENCES Customer (customer_ID)
);
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'1111222233334444','1111222233334444','VISA','1','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'5050419498932433','5050419498932433','MASTERCARD','2','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'3153616053432062','3153616053432062','AMERICAN EXPRESS','3','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'6091477375001352','6091477375001352','VISA','4','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'8685283959165550','8685283959165550','MASTERCARD','5','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'1866781616441065','1866781616441065','AMERICAN EXPRESS','6','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'9449115585826721','9449115585826721','VISA','7','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'8274378241070747','8274378241070747','MASTERCARD','8','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'1968434382696178','1968434382696178','VISA','9','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'6298557773260753','6298557773260753','VISA','10','1');

CREATE TABLE Category(
category_ID INTEGER, 
category VARCHAR2(30),
PRIMARY KEY (category_ID)
);
INSERT INTO Category VALUES (seq_category.nextval,'food and drink');
INSERT INTO Category VALUES (seq_category.nextval,'service');
INSERT INTO Category VALUES (seq_category.nextval,'automobile');
INSERT INTO Category VALUES (seq_category.nextval,'health and fitness');
INSERT INTO Category VALUES (seq_category.nextval,'children');
INSERT INTO Category VALUES (seq_category.nextval,'fun and adventure');
INSERT INTO Category VALUES (seq_category.nextval,'travel');
INSERT INTO Category VALUES (seq_category.nextval,'goods');
INSERT INTO Category VALUES (seq_category.nextval,'other');


CREATE TABLE Deal(
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
FOREIGN KEY (location_ID) REFERENCES Location (location_ID),
FOREIGN KEY (category_ID) REFERENCES Category (category_ID),
FOREIGN KEY (merchant_ID) REFERENCES Merchant (merchant_id)
);
INSERT INTO Deal VALUES (seq_deal.nextval,'1-June-2013','$3 off chipotle burrito.','20','8.50','5.50','01-Apr-2013','25-Apr-2013','1','1');
INSERT INTO Deal VALUES (seq_deal.nextval,'31-Dec-2013','Manicure and Pedicure for $25','50','35.00','25.00','01-Apr-2013','25-Apr-2013','8','2');
INSERT INTO Deal VALUES (seq_deal.nextval,'31-Jul-2014','50% off Oil Change and Spring Tune Up','20','70.00','35.00','01-Apr-2013','25-Apr-2013','10','3');
INSERT INTO Deal VALUES (seq_deal.nextval,'1-Oct-2013','10-pack Personal Training Sessions','100','250.00','175.00','01-Apr-2013','25-Apr-2013','9','4');
INSERT INTO Deal VALUES (seq_deal.nextval,'10-Apr-2013','50% off a Child''s Birthday Party','20','150.00','75.00','01-Apr-2013','25-Apr-2013','8','5');
INSERT INTO Deal VALUES (seq_deal.nextval,'10-Apr-2013','Snorkling lessons for two!','20','500.00','125.50','01-Apr-2013','25-Apr-2013','7','6');
INSERT INTO Deal VALUES (seq_deal.nextval,'10-Apr-2013','Spanish lessons!','20','100.00','15.00','01-Apr-2013','25-Apr-2013','6','7');
INSERT INTO Deal VALUES (seq_deal.nextval,'10-Apr-2013','24inch gold chain','20','75.00','45.00','01-Apr-2013','25-Apr-2013','5','8');
INSERT INTO Deal VALUES (seq_deal.nextval,'10-Apr-2013','Free energy-efficiency home audit','20','50.00','0.00','01-Apr-2013','25-Apr-2013','4','9');
INSERT INTO Deal VALUES (seq_deal.nextval,'10-Apr-2013','Personal Dance lessons','20','8.50','5.50','01-Apr-2013','25-Apr-2013','3','9');

CREATE TABLE Merchant(
merchant_ID INTEGER, 
merchant_name VARCHAR2(50),
PRIMARY KEY (merchant_ID)
FOREIGN KEY (deal_id) REFERENCES DEAL(deal_id));
INSERT INTO Merchant VALUES (seq_merchant.nextval,'McDonalds');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Fine Nails Nail Salon');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Jiffy Lube');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Golds Gym');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Little Tree Children Theater');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Zip Line Adventures');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'It''s a Small World Travel Agency');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Laura''s Boutique');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Cash for Gold');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Barnes and Noble');

CREATE TABLE Location(
location_ID INTEGER, 
city VARCHAR2(30),
state VARCHAR2(2),
country VARCHAR2(2),
continent VARCHAR2(2),
merchant_ID INTEGER,
PRIMARY KEY (location_ID),
FOREIGN KEY (deal_id) REFERENCES Deal(deal_ID)
);
INSERT INTO Location VALUES (seq_location.nextval,'Fairfax','VA','US','NA','1');
INSERT INTO Location VALUES (seq_location.nextval,'New York','NY','US','NA','2');
INSERT INTO Location VALUES (seq_location.nextval,'El Paso','TX','US','NA','3');
INSERT INTO Location VALUES (seq_location.nextval,'San Francisco','CA','US','NA','4');
INSERT INTO Location VALUES (seq_location.nextval,'Juneau','AK','US','NA','5');
INSERT INTO Location VALUES (seq_location.nextval,'Topeka','KS','US','NA','6');
INSERT INTO Location VALUES (seq_location.nextval,'Seattle','WA','US','NA','7');
INSERT INTO Location VALUES (seq_location.nextval,'Las Vegas','NV','US','NA','8');
INSERT INTO Location VALUES (seq_location.nextval,'Washington','DC','US','NA','9');
INSERT INTO Location VALUES (seq_location.nextval,'Helena','MT','US','NA','10');


CREATE TABLE Voucher(
voucher_ID INTEGER, 
status VARCHAR2(30),
PRIMARY KEY (voucher_ID)
);
INSERT INTO Voucher VALUES (seq_voucher.nextval,'current');
INSERT INTO Voucher VALUES (seq_voucher.nextval,'expired');
INSERT INTO Voucher VALUES (seq_voucher.nextval,'refunded');
INSERT INTO Voucher VALUES (seq_voucher.nextval,'used');
INSERT INTO Voucher VALUES (seq_voucher.nextval,'used');
INSERT INTO Voucher VALUES (seq_voucher.nextval,'expired');
INSERT INTO Voucher VALUES (seq_voucher.nextval,'refunded');
INSERT INTO Voucher VALUES (seq_voucher.nextval,'current');
INSERT INTO Voucher VALUES (seq_voucher.nextval,'expired');
INSERT INTO Voucher VALUES (seq_voucher.nextval,'used');

CREATE TABLE Transaction(
transaction_ID INTEGER, 
trans_date DATE,
voucher_ID INTEGER,
PRIMARY KEY (transaction_ID),
FOREIGN KEY (voucher_ID) REFERENCES Voucher (voucher_ID)
);
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','1');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','2');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','3');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','4');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','5');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','6');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','7');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','8');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','9');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','10');


CREATE TABLE Purchase(
	transaction_ID INTEGER,
	voucher_ID INTEGER,
	PRIMARY KEY (voucher_ID),
	FOREIGN KEY (transaction_ID) REFERENCES Transaction (transaction_ID),
	FOREIGN KEY (voucher_ID) REFERENCES Voucher (voucher_ID)
);
INSERT INTO Purchase VALUES ('1','1');
INSERT INTO Purchase VALUES ('2','2');
INSERT INTO Purchase VALUES ('3','3');
INSERT INTO Purchase VALUES ('4','4');
INSERT INTO Purchase VALUES ('5','5');
INSERT INTO Purchase VALUES ('6','6');
INSERT INTO Purchase VALUES ('7','7');
INSERT INTO Purchase VALUES ('8','8');
INSERT INTO Purchase VALUES ('9','9');
INSERT INTO Purchase VALUES ('10','10');


CREATE TABLE Purchase_Deal(
	voucher_ID INTEGER,
	deal_ID INTEGER,
	PRIMARY KEY (voucher_ID),
	FOREIGN KEY (voucher_ID) REFERENCES Purchase,
	FOREIGN KEY (deal_ID) REFERENCES Deal
);
INSERT INTO Purchase_Deal VALUES ('1','1');
INSERT INTO Purchase_Deal VALUES ('2','2');
INSERT INTO Purchase_Deal VALUES ('3','3');
INSERT INTO Purchase_Deal VALUES ('4','4');
INSERT INTO Purchase_Deal VALUES ('5','5');
INSERT INTO Purchase_Deal VALUES ('6','6');
INSERT INTO Purchase_Deal VALUES ('7','7');
INSERT INTO Purchase_Deal VALUES ('8','8');
INSERT INTO Purchase_Deal VALUES ('9','9');
INSERT INTO Purchase_Deal VALUES ('10','10');


CREATE TABLE Purchase_With(
	voucher_ID INTEGER,
	payment_ID INTEGER,
	customer_ID INTEGER,
	PRIMARY KEY (voucher_ID),
	FOREIGN KEY (voucher_ID) REFERENCES Purchase,
	FOREIGN KEY (payment_ID,customer_ID) REFERENCES Payment_Method
);
INSERT INTO Purchase_With VALUES ('1','1','1');
INSERT INTO Purchase_With VALUES ('2','2','2');
INSERT INTO Purchase_With VALUES ('3','3','3');
INSERT INTO Purchase_With VALUES ('4','4','4');
INSERT INTO Purchase_With VALUES ('5','5','5');
INSERT INTO Purchase_With VALUES ('6','6','6');
INSERT INTO Purchase_With VALUES ('7','7','7');
INSERT INTO Purchase_With VALUES ('8','8','8');
INSERT INTO Purchase_With VALUES ('9','9','9');
INSERT INTO Purchase_With VALUES ('10','10','10');


CREATE TABLE Review(
review_ID INTEGER, 
rating INTEGER,
comments VARCHAR2(500),
deal_ID INTEGER,
customer_ID INTEGER,
PRIMARY KEY (review_ID),
FOREIGN KEY (deal_ID) REFERENCES Deal (deal_ID),
FOREIGN KEY (customer_ID) REFERENCES Customer (customer_ID)
);

INSERT INTO Review VALUES (seq_review.nextval,'5','Great deal!','1','1');
INSERT INTO Review VALUES (seq_review.nextval,'1','Worst purchase I ever made','2','1');
INSERT INTO Review VALUES (seq_review.nextval,'1','A bunch of crooks!','3','1');
INSERT INTO Review VALUES (seq_review.nextval,'4','Great deal!','4','1');
INSERT INTO Review VALUES (seq_review.nextval,'3','Could have gotten this at Walmart for the same price','5','1');
INSERT INTO Review VALUES (seq_review.nextval,'5','Great deal!','6','1');
INSERT INTO Review VALUES (seq_review.nextval,'3','Met expectations','7','1');
INSERT INTO Review VALUES (seq_review.nextval,'3','Allowed to to save some money for another project','8','1');
INSERT INTO Review VALUES (seq_review.nextval,'2','Burning your money for warmth would be a better use.','9','1');
INSERT INTO Review VALUES (seq_review.nextval,'2','Horrible!','10','10');

CREATE TABLE 






