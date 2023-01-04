create table atm (
  id                   bigserial       NOT NULL PRIMARY KEY, 
  name                 VARCHAR(255)    NOT NULL,
  addr                 VARCHAR(1024)   NOT NULL, 
  city                 VARCHAR(255)    NOT NULL,   
  state                VARCHAR(64)     NOT NULL,   
  postalCode           VARCHAR(15)     NOT NULL,  
  inDoors              boolean         NOT NULL,   
  latitude             REAL            NOT NULL,
  longitude            REAL            NOT NULL,
  cord                 geography(POINT,4326)           NOT NULL,
  branchId             bigint);
CREATE INDEX IDX_ATM_CORD ON atm using GIST(cord);   
  
  
create table atmnotes(
  id                   bigserial       NOT NULL PRIMARY KEY, 
  note                 VARCHAR(1024)   NOT NULL,
  atmId                bigint          NOT NULL);
create index IDX_ATMNOTES_ATMID ON atmnotes(atmId);
 
create table atmdetails(
  id                   bigserial       NOT NULL PRIMARY KEY, 
  detail               VARCHAR(1024)   NOT NULL,
  atmId                bigint          NOT NULL);
create index IDX_ATMDETAILS_ATMID ON atmdetails(atmId);
