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

## Step 5

### CSV Upload and Training Metadata

- **CSV upload:** Updated training creation to accept a CSV file as `MultipartFile` instead of creating an empty training.
- **CSV parsing:** Added OpenCSV and a custom reader to read uploaded files using UTF-8 encoding.
- **Metadata extraction:** Combined the headers and values from the first two CSV rows into a metadata map.
- **Training mapping:** Mapped CSV metadata to training fields, including sport, date, start time, duration, distance and average heart rate.
- **Manual verification:** Tested CSV upload and the returned training metadata with Postman.

## Step 6

### DataRow Import and Relationship Management

- **DataRow entity:** Added `DataRow` to store individual training measurements and linked each measurement to its training.
- **Measurement mapping:** Imported measurements starting from the fourth CSV row and mapped the measurement columns to DataRow fields.
- **Missing values:** Added parsing helpers that convert missing or blank numeric values to `null`.
- **Time indexing:** Assigned sequential `indexSeconds` values starting at zero to identify measurements within the training.
- **Data removal:** Added functionality to delete all measurements belonging to a training while retaining the training itself. Updated both sides of the relationship and used transactional orphan removal to delete the records.

## Step 7

### Updated Docker Deployment

- **Image update:** Built and published version `v0.1.2` as a multi-platform Docker image for the laptop and Raspberry Pi.
- **Compose configuration:** Updated local and Pi deployment configurations to use `em26/polar-app:latest`.
- **Test database reset:** Removed obsolete test-data volumes before deploying the updated application.
- **Deployment verification:** Manually tested the updated application locally and on the Raspberry Pi using Postman.
- **Database inspection:** Verified access to the container's H2 database through the H2 Console.

## Step 8

### Phase Creation, Intervals and Initial Analysis

- **Phase API:** Added the `Phase` entity, request and response DTOs, mapper, repository, service and controller, with a relationship to `Training`.
- **Automatic positioning:** Calculated inclusive start and stop times so that each new phase follows the previous phase without gaps.
- **Interval creation:** Added support for repeating an ordered list of phase requests using a repetition factor.
- **Duration validation:** Checked the remaining training time before creating a phase or a complete interval, taking existing phases into account.
- **Shared creation logic:** Reused phase creation logic for individual phases and intervals, passing the already loaded training to the shared method.
- **Relationship consistency:** Added `Training.addPhase()` to update both sides of the relationship.
- **Initial statistics:** Calculated average heart rate over each phase's time range using a reusable statistics helper. Returned `null` when the selected measurements were empty or contained a missing heart-rate value.
- **Transactional processing:** Used transactions to keep lazy-loaded relationships available and ensure interval creation is rolled back if an operation fails.
- **Duration error handling:** Added `TrainingDurationExceededException` and an HTTP 409 ProblemDetail response that reports the number of seconds by which the requested duration exceeds the available time.