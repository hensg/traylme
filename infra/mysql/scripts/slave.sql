ALTER USER 'root'@'localhost' IDENTIFIED BY 'root';

CREATE USER IF NOT EXISTS 'redirect_service'@'%' IDENTIFIED BY 'redirect_service' REQUIRE X509;
GRANT SELECT ON traylme.* TO 'redirect_service'@'%';
