CREATE TABLESPACE HOLMESWATSONHQ DATAFILE 'D:/galileo/HOLMESWATSONHQ01.dbf' SIZE 50M AUTOEXTEND ON NEXT 1M MAXSIZE 100M EXTENT MANAGEMENT LOCAL AUTOALLOCATE;

CREATE USER HOLMESWATSONHQ IDENTIFIED BY HOLMESWATSONHQ DEFAULT TABLESPACE HOLMESWATSONHQ QUOTA UNLIMITED ON HOLMESWATSONHQ;

grant all privileges to HOLMESWATSONHQ identified by HOLMESWATSONHQ;

connect HOLMESWATSONHQ/HOLMESWATSONHQ

create table AGENCY
(
	AGENCYID VARCHAR2(255) not null primary key,
	ADDRESS VARCHAR2(255) not null
);

create table CLIENT
(
	CLIENTID VARCHAR2(255) primary key,
	FIRSTNAME VARCHAR2(255) not null,
	PASSWORD VARCHAR2(255) not null,
	LASTNAME VARCHAR2(255) not null,
	ADDRESS VARCHAR2(255) not null
);

create table ACCOUNT
(
	ACCOUNTNUM VARCHAR2(255) primary key,
	CLIENTID VARCHAR2(255) not null,
	ACCOUNTBALANCE NUMBER(20,2) default 0.00  not null,
	status INTEGER CHECK(status IN (0, 1)),
	constraint client_account_fk foreign key (CLIENTID) references client(CLIENTID) ON DELETE CASCADE
);

CREATE TABLE transaction(
		 transactionId INTEGER PRIMARY KEY,
		 description VARCHAR(255) not null,
		 amount NUMBER(20,2) not null,
		 transactionDate DATE not null,
		 ACCOUNTNUM VARCHAR(255) not null,
		 transactionType VARCHAR(255) CHECK(transactionType IN ('WITHDRAW', 'DEPOSIT', 'TRANSFER', 'LOAN_PAYMENT')),
		 CONSTRAINT accnt_trans_fk FOREIGN KEY(ACCOUNTNUM) REFERENCES ACCOUNT(ACCOUNTNUM) ON DELETE CASCADE
);

CREATE SEQUENCE trans_seq;

CREATE OR REPLACE TRIGGER trans_pk 
BEFORE INSERT ON transaction 
FOR EACH ROW

BEGIN
  SELECT trans_seq.NEXTVAL
  INTO   :new.transactionId
  FROM   dual;
END;
/



CREATE OR REPLACE TRIGGER trans_update 
AFTER INSERT ON transaction 
FOR EACH ROW

DECLARE
	vBalance account.ACCOUNTBALANCE%TYPE;
	vAccount account%ROWTYPE;

BEGIN

  SELECT * into vAccount from account where accountnum = :new.accountnum;
  
  IF :new.transactionType = 'WITHDRAW'
	  THEN vBalance :=  vAccount.ACCOUNTBALANCE - :new.amount;
  ELSIF :new.transactionType = 'DEPOSIT'
	  THEN vBalance :=  vAccount.ACCOUNTBALANCE + :new.amount;
  ELSE RETURN;
  END if;
  
  if vBalance < 0 then
	return;
  end if;
	
  UPDATE account SET ACCOUNTBALANCE = vBalance WHERE accountnum = vAccount.accountnum;
  
END;
/
show errors;


