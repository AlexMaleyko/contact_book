# contact_book
## System requirements:
* Apache Tomcat 8 or higher
* MySQL Server version 5.7 or higher
* Java 1.8 or newer
## To configure the application take further steps:
* Dump database_dump.sql file into a database, specify database name in url, name and password in config.xml file
which is located in contact_book\contactsbook\moduleB\src\main\webapp\META-INF folder;
* Dump test_database_dump.sql file into another database (if you want to run tests, or ignore all tests), 
specify database properties in database.properties file which is located in contact_book\contactsbook\moduleA\src\test\resources folder;
* Create in your file system folders root\contact book\system\default picture and copy there default.jpg 
from contact_book\contactsbook\moduleB\src\main\webapp\resources\pictures ;
* Package project with Maven and place .war file into Tomcat webapp folder;
