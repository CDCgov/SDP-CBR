DROP TABLE IF EXISTS message_inq;

DROP TABLE IF EXISTS testdb;

CREATE TABLE testdb (
  recordId bigserial primary key,
  status varchar (255) default 'new',
  routing varchar (255));
            
CREATE TABLE message_inq (
recordId SERIAL,
messageId varchar(255) default NULL,
payloadName varchar(255) default NULL,
payloadBinaryContent BYTEA default NULL,
payloadTextContent text default NULL,
localFileName varchar (255) NOT NULL,
service varchar (255) NOT NULL,
action varchar (255) NOT NULL,
arguments varchar (255) default NULL,
fromPartyId varchar (255) default NULL,
messageRecipient varchar (255) default NULL,
errorCode varchar (255) default NULL,
errorMessage varchar (255) default NULL,
processingStatus varchar (255) default NULL,
applicationStatus varchar (255) default NULL,
encryption varchar (10) NOT NULL,
receivedTime varchar (255) default NULL,
lastUpdateTime varchar (255) default NULL,
processId varchar (255) default NULL,
 PRIMARY KEY (recordId))