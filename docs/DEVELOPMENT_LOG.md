# Development Log

## Step 1

### Setup, Training Entity and Docker

- **Project setup:** Created a Spring Boot application using Java 21, Maven and Spring Data JPA.
- **Training API:** Added the `Training` entity, DTOs, mapper, service and repository. Implemented endpoints to create a training, retrieve all trainings, retrieve one by ID and delete a training. Tested creation and retrieval with Postman.
- **Database:** Added an H2 file database to store trainings. Training details currently use temporary test data until CSV import is implemented.
- **Docker:** Added a Dockerfile and Docker Compose configuration. Ran the application locally in a container with a volume for database storage.

## Step 2

### Environment and Local Pi deployment

- **Environment configuration:** Moved the database password to a project-specific `.env` file and configured IntelliJ and Docker Compose to use it. Kept credentials outside version control.
- **Raspberry Pi deployment:** Installed Docker and Compose on a Raspberry Pi 4. Published a multi-platform image to Docker Hub and deployed the application on the Pi.
- **Compatibility fix:** Replaced the Amazon Corretto base image with Eclipse Temurin after discovering a processor compatibility issue on the Pi 4.
- **Deployment verification:** Tested the API from the laptop. Confirmed that training data survives container replacement and a Pi reboot, and that the application starts automatically.
- **Documentation:** Started documenting configuration, deployment and API usage.

## Step 3

### Training Creation and Unit Testing

- **Training creation:** Simplified training creation to create an empty training without a request body. Removed `TrainingRequest` from the controller, service and mapper flow.
- **Mapper tests:** Added unit tests using JUnit and AssertJ to verify empty training creation and the mapping of training fields to `TrainingResponse`.
- **Service tests:** Added unit tests using Mockito for creating, listing, retrieving and deleting trainings. Included cases for missing training IDs and verified repository interactions.
- **Test database:** Configured a separate in-memory H2 database for tests to keep test data separate from application data.
- **Dependency update:** Upgraded Spring Boot from version 4.1.1 to 4.1.2 to update managed dependencies and address reported vulnerabilities.


## Step 4

### Central Exception Handling

- **Custom exception:** Added a reusable `RecordNotFoundException` for database records that cannot be found.
- **Service refactoring:** Updated training retrieval by ID to return `TrainingResponse` directly and throw the exception when the training does not exist.
- **Central error handling:** Added `GlobalExceptionHandler` using `@RestControllerAdvice`. Missing records now produce an HTTP 404 response with a `ProblemDetail` body.
- **Service tests:** Updated the retrieval tests to verify the returned response for an existing training and the exception type and message for a missing training.