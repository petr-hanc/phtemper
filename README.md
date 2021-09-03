# phtemper
Application for testing work with REST API, Java and Spring Boot. User can do CRUD operations with pairs time-temperature and get the longest period with temperatures in certain range.

Contains unit and integration tests (with JUnit 4, EasyMock, MockMvc and TestRestTemplate) and Docker config. files.

API documentation: https://phtemper.docs.apiary.io/.

Uses internal H2 Database (data are stored in a file).
It is possible to connect to database with this JDBC URL when the program is running: jdbc:h2:tcp://localhost:9092/./db/phtemper;SCHEMA=public

Build command: mvnw package

Run command: mvnw spring-boot:run

Command for build and run in Docker container: docker-compose up



