DROP VIEW view_customer;

// CLEAR COLUMNS;

// SET lines 256;
// SET trimout on;
// SET space 1;
// SET tab off;
// SET UNDERLINE =;

CREATE VIEW view_customer AS 
   SELECT customer_ID, last_name, first_name, age, email_address, gender
   FROM Customer; 

// COLUMN customer_ID FORMAT 999 HEADING 'Customer ID';
// COLUMN last_name FORMAT A10 HEADING 'Last|Name';
// COLUMN first_name FORMAT A10 HEADING 'First|Name';
// COLUMN age FORMAT 999 HEADING 'Age';
// COLUMN email_address FORMAT A15 HEADING 'Email';
// COLUMN gender FORMAT A1 HEADING 'Gender';




