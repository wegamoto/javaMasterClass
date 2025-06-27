SELECT table_schema AS "ecommerce_db",
       ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS "Size (MB)"
FROM information_schema.tables
GROUP BY table_schema
ORDER BY `Size (MB)` DESC;
