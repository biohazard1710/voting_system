# Restaurant voting system
____
## Task Description
A voting system for deciding where to have lunch.
____
## Key Features

2 types of users: admin and regular users
Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
Menu changes each day (admins do the updates)
Users can vote for a restaurant they want to have lunch at today
Only one vote counted per user
If user votes again the same day:
If it is before 11:00 we assume that he changed his mind.
If it is after 11:00 then it is too late, vote can't be changed
Each restaurant provides a new menu each day.
____
## Running the Application
If you don't have IntelliJ IDEA and are using Maven only, follow these instructions to run the application.
1. Ensure you have [Maven](https://maven.apache.org/download.cgi) and [Java JDK 21](https://www.oracle.com/java/technologies/javase-downloads.html) installed.
2. Make sure the `JAVA_HOME` environment variable points to your JDK installation.
____
### Running the Application

1. **Clone the Repository**:

    ```bash
    git clone https://github.com/biohazard1710/voting_system.git
    cd voting_system
    ```

2. **Check Dependencies**:

   Run the following command to download all dependencies:

    ```bash
    mvn install
    ```

3. **Run the Application**:

   To start the Spring Boot application, run:

    ```bash
    mvn spring-boot:run
    ```

   This will start the application on a server, typically available at [http://localhost:8080](http://localhost:8080).

### Stopping the Application

To stop the application, simply press `Ctrl+C` in the terminal where Maven is running.
____
## API Documentation

Swagger UI is available at the following endpoint:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

You can use this interface to explore and interact with the API endpoints.