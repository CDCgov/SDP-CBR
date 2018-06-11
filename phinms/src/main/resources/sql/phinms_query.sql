OPEN SYMMETRIC KEY PHINMSKey decryption by certificate TOPLEVEL_CERT;
select recordId,
       CONVERT(varchar(max), dbo.DecryptLob(payloadBinaryContent), 0) as payloadTextContent,
       fromPartyId, receivedTime
  from dbo.message_inq
  where processingStatus = 'queued'
    and (applicationStatus is NULL or applicationStatus='NULL');
