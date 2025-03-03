# TV Show Tracking App

A web application for tracking and discussing TV shows. Built with Spring Boot and Java.

## Features

- User authentication and authorization
- TV show database with ratings
- User watchlists
- Discussion forums for shows
- Modern and responsive UI

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- H2 Database (included)

## Setup

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```
4. Access the application at `http://localhost:8080`

## Default Admin Credentials

- Username: admin
- Password: admin

## Database

The application uses H2 Database. You can access the H2 Console at `http://localhost:8080/h2-console` with the following credentials:
- JDBC URL: jdbc:h2:file:./tvappdb
- Username: sa
- Password: password

## Project Structure

- `src/main/java/com/tvapp/`
  - `model/` - Entity classes
  - `repository/` - Data access layer
  - `service/` - Business logic
  - `controller/` - Web controllers
  - `config/` - Configuration classes
  - `security/` - Security configuration

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request 