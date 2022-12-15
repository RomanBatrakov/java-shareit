# Shareit project
Shareit project - Backend (rest api) of the application, in which users can:
- add things to share;
- book things;
- confirm or reject booking requests;
- receive booking data by status (waiting, approved, rejected);
- get data about user items;
- search for items by name and description;
- make requests for things that no one has added yet;
- offer things in response to a request.
## Tech stack:
- Java 11;
- Spring Boot;
- Spring Data, Hibernate;
- PostgreSQL, SQL;
- Maven (multi-module project);
- Mockito, Lombok
- Docker;
- Postman.
## Project Structure:
There are 2 microservices made as modules in project:

Gateway - works as proxy processing and validating income requests and redirecting them to main server. Depends on ShareIt-server. Runs on port 8080.

Server - main service makes all the work and application logic. Uses Postgres database. Runs on port 9090.
## Quick start:
The application uses ports: 8080, 9090, 5432.
While in the directory on the command line, type:

`mvn package`  
`docker-compose up`  
## Rest service:
<details>
    <summary><h3>Examples of methods and endpoints available for the API:</h3></summary>

- [(GET) get all owner items](http://localhost:8080/items)
- [(POST) create new item sending json info](http://localhost:8080/items)
- [(PATCH) update existing item sending json info with specified id](http://localhost:8080/items/{itemId})
- [(GET) get booking with specified id](http://localhost:8080/bookings/{bookingId})
- [(GET) get list of all user bookings](http://localhost:8080/bookings)
- [(POST) create new booking sending json info](http://localhost:8080/bookings)
</details>
