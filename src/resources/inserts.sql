// CUSTOMER
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

// PAYMENT_METHOD
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'1111222233334444','VISA','1','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'5050419498932433','MASTERCARD','2','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'3153616053432062','AMERICAN EXPRESS','3','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'6091477375001352','VISA','4','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'8685283959165550','MASTERCARD','5','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'1866781616441065','AMERICAN EXPRESS','6','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'9449115585826721','VISA','7','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'8274378241070747','MASTERCARD','8','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'1968434382696178','VISA','9','1');
INSERT INTO Payment_Method VALUES (seq_payment_method.nextval,'6298557773260753','VISA','10','1');

// CATEGORY
INSERT INTO Category VALUES (seq_category.nextval,'food and drink');
INSERT INTO Category VALUES (seq_category.nextval,'service');
INSERT INTO Category VALUES (seq_category.nextval,'automobile');
INSERT INTO Category VALUES (seq_category.nextval,'health and fitness');
INSERT INTO Category VALUES (seq_category.nextval,'children');
INSERT INTO Category VALUES (seq_category.nextval,'fun and adventure');
INSERT INTO Category VALUES (seq_category.nextval,'travel');
INSERT INTO Category VALUES (seq_category.nextval,'goods');
INSERT INTO Category VALUES (seq_category.nextval,'other');

// MERCHANT
INSERT INTO Merchant VALUES (seq_merchant.nextval,'McDonalds');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Fine Nails Nail Salon');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Jiffy Lube');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Golds Gym');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Little Tree Children Theater');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Zip Line Adventures');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'It is a Small World Travel Agency');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Laura Family Boutique');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Cash for Gold');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Barnes and Noble');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Home Depot');
INSERT INTO Merchant VALUES (seq_merchant.nextval,'Salsa Shack');

// LOCATION
INSERT INTO Location VALUES (seq_location.nextval,'Fairfax','VA','US','NA');
INSERT INTO Location VALUES (seq_location.nextval,'New York','NY','US','NA');
INSERT INTO Location VALUES (seq_location.nextval,'El Paso','TX','US','NA');
INSERT INTO Location VALUES (seq_location.nextval,'San Francisco','CA','US','NA');
INSERT INTO Location VALUES (seq_location.nextval,'Juneau','AK','US','NA');
INSERT INTO Location VALUES (seq_location.nextval,'Topeka','KS','US','NA');
INSERT INTO Location VALUES (seq_location.nextval,'Seattle','WA','US','NA');
INSERT INTO Location VALUES (seq_location.nextval,'Las Vegas','NV','US','NA');
INSERT INTO Location VALUES (seq_location.nextval,'Washington','DC','US','NA');
INSERT INTO Location VALUES (seq_location.nextval,'Helena','MT','US','NA');

// DEAL
INSERT INTO Deal VALUES (seq_deal.nextval,'01-Jun-2013','$3 off chipotle burrito.','20','8','5','01-Apr-2013','25-Apr-2013','1','1','1');
INSERT INTO Deal VALUES (seq_deal.nextval,'31-Dec-2013','Manicure and Pedicure for $25','25','35','25','01-Apr-2013','25-May-2013','8','2','2');
INSERT INTO Deal VALUES (seq_deal.nextval,'31-Jul-2014','50% off Oil Change and Spring Tune Up','20','70','35','01-Apr-2013','31-July-2013','10','3','3');
INSERT INTO Deal VALUES (seq_deal.nextval,'1-Oct-2013','10-pack Personal Training Sessions','15','250','175','01-Apr-2013','1-May-2013','9','4','4');
INSERT INTO Deal VALUES (seq_deal.nextval,'10-Apr-2014','50% off a Birthday Party for children','20','150','75','01-Apr-2013','31-Dec-2013','8','5','5');
INSERT INTO Deal VALUES (seq_deal.nextval,'10-Apr-2014','Snorkling lessons for two!','20','500','125','01-Apr-2013','31-July-2013','7','6','6');
INSERT INTO Deal VALUES (seq_deal.nextval,'10-Oct-2013','Spanish lessons!','20','100','15','01-Apr-2013','02-May-2013','6','7','7');
INSERT INTO Deal VALUES (seq_deal.nextval,'21-Apr-2014','24inch gold chain','20','75','45','01-Apr-2013','25-Apr-2013','5','8','8');
INSERT INTO Deal VALUES (seq_deal.nextval,'28-Apr-2014','Free energy-efficiency home audit','20','50','0.00','01-Apr-2013','30-Apr-2013','4','9','10');
INSERT INTO Deal VALUES (seq_deal.nextval,'30-Nov-2013','Personal Dance lessons','20','8','5','01-Apr-2013','01-May-2013','3','9','11');

// VOUCHER

// TRANSACTION
INSERT INTO Transaction VALUES (seq_transaction.nextval,'01-Apr-2013','10','1','1');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'05-Apr-2013','20','2','2');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'09-Apr-2013','30','3','3');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'21-Apr-2013','40','4','4');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'22-Apr-2013','50','5','5');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'19-Apr-2013','60','6','6');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'10-Apr-2013','70','7','7');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'30-Apr-2013','80','8','8');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'19-Apr-2013','90','9','9');
INSERT INTO Transaction VALUES (seq_transaction.nextval,'06-Apr-2013','100','10','10');


// REVIEW
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




