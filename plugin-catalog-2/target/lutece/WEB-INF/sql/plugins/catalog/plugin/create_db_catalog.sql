
--
-- Structure for table catalog_
--

DROP TABLE IF EXISTS catalog_;
CREATE TABLE catalog_ (
id_catalog int AUTO_INCREMENT,
name varchar(50) default '' NOT NULL,
description long varchar NOT NULL,
price int default '0' NOT NULL,
vat int default '0' NOT NULL,
PRIMARY KEY (id_catalog)
);
