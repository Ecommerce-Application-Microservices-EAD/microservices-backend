SELECT 'CREATE DATABASE inventory_service'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'inventory_service')\gexec

SELECT 'CREATE DATABASE order_service'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'order_service')\gexec