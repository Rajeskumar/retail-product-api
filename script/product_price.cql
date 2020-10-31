//Create Keyspace
CREATE KEYSPACE retail_product_ks
WITH replication = {'class': 'SimpleStrategy', 'replication_factor' : 3};

use retail_product_ks;

CREATE TABLE prod_price_table (prod_id int PRIMARY KEY, prod_price text);


INSERT INTO retail_product_ks.prod_price_table (prod_id, prod_price) VALUES (54456119, '{"value": 12.99, "currency_code":"USD"}');
INSERT INTO retail_product_ks.prod_price_table (prod_id, prod_price) VALUES (13264003, '{"value": 10.99,"currency_code":"USD"}');
INSERT INTO retail_product_ks.prod_price_table (prod_id, prod_price) VALUES (12954218, '{"value": 24.99,"currency_code":"CAD"}');
INSERT INTO retail_product_ks.prod_price_table (prod_id, prod_price) VALUES (13860428, '{"value": 49.99, "currency_code":"USD"}');
