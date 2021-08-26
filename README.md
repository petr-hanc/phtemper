# phtemper
Application for testing work with REST API, Java and Spring. User can make CRUD operations with pairs time-temperature ang get the longest period with temperatures in certain range.

Uses internal H2 Database (data are stored in a file).

API documentation: https://phtemper.docs.apiary.io/.

It is possible to connect to database with this JDBC URL when the program is running: jdbc:h2:tcp://localhost:9092/./db/phtemper;SCHEMA=public
