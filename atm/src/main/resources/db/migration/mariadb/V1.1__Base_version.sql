create table atm (
  id                   bigint          NOT NULL AUTO_INCREMENT PRIMARY KEY, 
  name                 VARCHAR(255)    NOT NULL,
  addr                 VARCHAR(1024)   NOT NULL, 
  city                 VARCHAR(255)    NOT NULL,   
  state                VARCHAR(64)     NOT NULL,   
  postalCode           VARCHAR(15)     NOT NULL,  
  inDoors              boolean         NOT NULL,   
  latitude             REAL            NOT NULL,
  longitude            REAL            NOT NULL,
  cord                 POINT NOT NULL SRID 4326       NOT NULL,
  branchId             bigint);
CREATE SPATIAL INDEX IDX_ATM_CORD ON atm(cord);   
  
  
create table atmnotes(
  id                   bigint          NOT NULL AUTO_INCREMENT PRIMARY KEY, 
  note                 VARCHAR(1024)   NOT NULL,
  atmId                bigint          NOT NULL);
create index IDX_ATMNOTES_ATMID ON atmnotes(atmId);
 
create table atmdetails(
  id                   bigint          NOT NULL AUTO_INCREMENT PRIMARY KEY, 
  detail               VARCHAR(1024)   NOT NULL,
  atmId                bigint          NOT NULL);
create index IDX_ATMDETAILS_ATMID ON atmdetails(atmId);
