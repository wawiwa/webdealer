

CREATE TABLE Trans_Voucher_AG AS(
SELECT 	T.transaction_ID,
		T.trans_date,
		T.Payment_ID,
		T.customer_ID,
		V.voucher_ID, 
		V.deal_ID,
		V.status_ID
FROM Transaction T
INNER JOIN Voucher V
ON T.voucher_ID=V.voucher_ID)
PRIMARY KEY (T.transaction_ID,V.voucher_ID)
);

CREATE OR REPLACE FUNCTION f_customer_purchased_deal (deal IN INTEGER)
	RETURN INTEGER
	IS
		deal_count INTEGER;
	BEGIN
		SELECT R.customer_ID, R.deal_ID, TVA.deal_ID, TVA.customer_ID
		FROM Review R, Trans_Voucher_Ag TVA
		WHERE R.customer_ID=TVA.customer_ID AND R.Deal_ID=TVA.deal_ID

CREATE OR REPLACE FUNCTION customerPurchased 
    RETURN INTEGER;
 AS 
 BEGIN 
	SELECT R.customer_ID, R.deal_ID, TVA.deal_ID INTO deal, TVA.customer_ID
	FROM Review R, Trans_Voucher_Ag TVA
	WHERE R.customer_ID=TVA.customer_ID AND R.Deal_ID=TVA.deal_ID
 	RETURN deal;
END;

CREATE OR REPLACE FUNCTION customerPurchased
    RETURN VARCHAR;
BEGIN 
	SELECT C.last_name INTO Acustomer
	FROM Customer C
	WHERE C.last_name = 'doe';
 	RETURN Acustomer;
END;
/
CREATE OR REPLACE FUNCTION customerPurchased
BEGIN
  FOR cust IN (SELECT last_name 
                     FROM customer)
  LOOP
    dbms_output.put_line( 'Customer: ' || cust.last_name );
  END LOOP;
END;

CREATE OR REPLACE TRIGGER customerMustPurchaseDeal
AFTER INSERT
   ON Review
   FOR EACH ROW
   
DECLARE
   v_username varchar2(10);
   
BEGIN
   
   -- Find username of person performing the INSERT into the table
   SELECT user INTO v_username
   FROM dual;
   
   -- Insert record into audit table
   INSERT INTO orders_audit
   ( order_id,
     quantity,
     cost_per_item,
     total_cost,
     username )
   VALUES
   ( :new.order_id,
     :new.quantity,
     :new.cost_per_item,
     :new.total_cost,
     v_username );
     
END;

CREATE OR REPLACE TRIGGER customerMustPurchaseDeal BEFORE INSERT ON Review
REFERENCING NEW AS newRow 
FOR EACH ROW
WHEN (newRow.deal_ID <= 10)
BEGIN
INSERT INTO T5 VALUES(:newRow.b, :newRow.a); END trig1;

//ALTER TABLE Review
//ADD CONSTRAINT customerPurchased CHECK(
//	deal_ID IN(
//		SELECT R.customer_ID, R.deal_ID, TVA.deal_ID, TVA.customer_ID
//		FROM Review R, Trans_Voucher_Ag TVA
//		WHERE R.customer_ID=TVA.customer_ID AND R.Deal_ID=TVA.deal_ID)
//	)
//);
