##Release 1.1

1. Added count of messages failed to the PHIN-MS callback.
	In application.properties, update phinms.sql as follows:
	
	select * from ${phinms.table} where processingStatus = 'queued' and  (applicationStatus is NULL or applicationStatus='NULL')?dataSource=phinMsDataSource&onConsume=update ${phinms.table} set applicationStatus='completed' where recordId=:#recordId&onConsumeFailed=update ${phinms.table} set applicationStatus='failed', errorMsg=:#errorMsg where recordId=:#recordId&delay=10000 