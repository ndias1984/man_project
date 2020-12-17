--CREATION OF DATABASE TO SUPPORT HARDWARE AND SOFTWARE FILES -> RUN THE SCRIPT IN MYSQL

--Create Schema that will hold all MAN Challenge tables
CREATE SCHEMA IF NOT EXISTS `man_vw` ;

#Create the tables that will hold MAN Challenge data
CREATE TABLE IF NOT EXISTS `man_vw`.`truckconfiguration` (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
vin CHAR (17) NOT NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
UNIQUE KEY `UC_vin` (`vin`)
)
COMMENT = 'Table that holds all truck vin, each of them connected with Software and Hardware requirements';

CREATE TABLE IF NOT EXISTS `man_vw`.`trucksoftwareconf` (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
vin_id BIGINT NOT NULL,
softwarecode CHAR(6) NOT NULL,
present BOOLEAN NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
UNIQUE KEY `UC_vin_software` (`vin_id`,`softwarecode`),
FOREIGN KEY `FK_vin_software` (vin_id)
        REFERENCES `man_vw`.`truckconfiguration` (id)
)
COMMENT = 'Table that holds all truck configurations, based on Software requirements';

CREATE TABLE IF NOT EXISTS `man_vw`.`truckhardwareconf` (
id BIGINT AUTO_INCREMENT PRIMARY KEY,
vin_id BIGINT NOT NULL,
hardwarecode CHAR(6) NOT NULL,
present BOOLEAN NULL,
created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
UNIQUE KEY `UC_vin_hardware` (`vin_id`,`hardwarecode`),
FOREIGN KEY `FK_vin_hardware` (vin_id)
        REFERENCES `man_vw`.`truckconfiguration` (id)
)
COMMENT = 'Table that holds all truck configurations, based on Hardware requirements';