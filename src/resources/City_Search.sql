SELECT D.deal_id,D.expiration_date,D.description,D.quantity_limit,D.original_price,D.deal_price,D.sale_start_time,D.sale_end_time,D.location_ID,D.category_ID,D.merchant_ID,M.merchant_name,L.city,L.state,L.country,L.continent
FROM Deal D, Merchant M, Location L
WHERE D.merchant_ID=M.merchant_ID AND D.location_id=L.location_id AND L.city LIKE '%%';
