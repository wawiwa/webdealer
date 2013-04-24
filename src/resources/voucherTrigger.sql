
CREATE OR REPLACE TRIGGER InsertNewVoucher
AFTER INSERT
ON Deal
FOR EACH ROW 

DECLARE

BEGIN 

	FOR counter IN 1..:new.quantity_limit
	LOOP 
		INSERT INTO VOUCHER VALUES
		(seq_voucher.nextval,'current',:new.deal_ID);
		
	END LOOP;

END;