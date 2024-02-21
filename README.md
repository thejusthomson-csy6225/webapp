# Webapp

## Health Check RESTful API

The Health Check RESTful API is designed to answer the question: "How to detect that a running service instance is unable to handle requests?". The health check API is a crucial tool for monitoring the health of the application instance and alerting when something is not working as expected. It helps in stopping traffic to unhealthy instances, automatically replacing/repairing them, and improving user experience by not routing requests to unhealthy instances.

## Features

1. **/healthz Endpoint:**
    - Checks if the application has connectivity to the database.
    - Returns HTTP 200 OK if the connection is successful.
    - Returns HTTP 503 Service Unavailable if the connection is unsuccessful.
    - Does not allow for any payload in the request. Responds with HTTP 400 Bad Request if the request includes any payload.
    - Does not include any payload in the response.
    - Supports only HTTP GET method.
    - Adds `cache-control: 'no-cache'` header to the response.

## Example Requests

- **GET /healthz:**
    - Request: `GET /healthz`
    - Response:
        - HTTP 200 OK (Successful connection)
        - HTTP 503 Service Unavailable (Unsuccessful connection)
        - HTTP 400 Bad Request (Invalid request)

## Authentication

Th web application supports Token-Based Basic authentication. Users must provide a basic authentication token when making an API call to the authenticated endpoint.

## Create a New User

As a user, you can create an account by providing the following information:
- Email Address
- Password
- First Name
- Last Name

Account creation sets the `account_created` field to the current time. Users cannot set values for `account_created` and `account_updated`.

## Update User Information

As a user, you can update your account information. You are only allowed to update the following fields:
- First Name
- Last Name
- Password

Any attempt to update other fields returns HTTP 400 Bad Request. `account_updated` is updated upon a successful user update.

## Get User Information

As a user, you can get your account information. The response payload returns all fields for the user except for the password.

## Continuous Integration (CI) with GitHub Actions

The project includes GitHub Actions workflow for:
- Simple checks (compile code) for each pull request.
- Preventing pull request merges if the workflow execution fails.

## Prerequisites for Building and Deploying Locally

## Prerequisites for Building and Deploying Locally

Before you proceed with building and deploying the application locally, make sure you have the following prerequisites:

1. **Java Development Kit (JDK):**
    - Install JDK 17.

      ```bash
      sudo apt-get install openjdk-17-jdk
      ```

2. **Database Configuration:**
    - Configure your MySQL database settings in `application.properties`. Make sure you have a MySQL database instance running.

      ```properties
      # Example MySQL Configuration
      spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
      spring.datasource.username=your_mysql_username
      spring.datasource.password=your_mysql_password
      ```

3. **Maven Build Tool:**
    - The project uses Maven. If you don't have Maven installed globally, you can use the provided Maven Wrapper (`./mvnw`) included in the project.

      ```bash
      # Example command to set up Maven Wrapper
      chmod +x mvnw
      ```

4. **Clone the Repository:**
    - Clone the repository using SSH with the following command:

      ```bash
      git clone git@github.com:thejusthomson-csy6225/webapp.git
      ```

5. **Run with Maven:**
    - Navigate to the project directory and use Maven to run the application:

      ```bash
      cd webapp
      ./mvnw spring-boot:run
      ```

   If you encounter permission issues with the Maven Wrapper, make it executable:

   ```bash
   chmod +x mvnw
