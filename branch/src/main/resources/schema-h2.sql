create table IF NOT EXISTS branch (
  id                   bigint          NOT NULL AUTO_INCREMENT PRIMARY KEY, 
  name                 VARCHAR(255)    NOT NULL,
  addr                 VARCHAR(1024)   NOT NULL, 
  city                 VARCHAR(255)    NOT NULL,   
  state                VARCHAR(64)     NOT NULL,   
  postalCode           VARCHAR(15)     NOT NULL);
  
create table IF NOT EXISTS branchNotes(
  id                   bigint          NOT NULL AUTO_INCREMENT PRIMARY KEY, 
  note                 VARCHAR(1024)   NOT NULL,
  branchId             bigint          NOT NULL);
create index IF NOT EXISTS IDX_BRANCHNOTES_BRANCHID ON branchNotes(branchId);
 
create table IF NOT EXISTS branchDetails(
  id                   bigint          NOT NULL AUTO_INCREMENT PRIMARY KEY, 
  detail               VARCHAR(1024)   NOT NULL,
  branchId             bigint          NOT NULL);
create index IF NOT EXISTS IDX_BRANCHDETAILS_BRANCHID ON branchDetails(branchId);