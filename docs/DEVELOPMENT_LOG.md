# Development Log

## Day 1

### Setup, Training Entity and Docker

- **Project setup:** Created a Spring Boot application using Java 21, Maven and Spring Data JPA.
- **Training API:** Added the `Training` entity, DTOs, mapper, service and repository. Implemented endpoints to create a training, retrieve all trainings, retrieve one by ID and delete a training. Tested creation and retrieval with Postman.
- **Database:** Added an H2 file database to store trainings. Training details currently use temporary test data until CSV import is implemented.
- **Docker:** Added a Dockerfile and Docker Compose configuration. Ran the application locally in a container with a volume for database storage.

## Day 2

### Environment and Local Pi deployment

- **Environment configuration:** Moved the database password to a project-specific `.env` file and configured IntelliJ and Docker Compose to use it. Kept credentials outside version control.
- **Raspberry Pi deployment:** Installed Docker and Compose on a Raspberry Pi 4. Published a multi-platform image to Docker Hub and deployed the application on the Pi.
- **Compatibility fix:** Replaced the Amazon Corretto base image with Eclipse Temurin after discovering a processor compatibility issue on the Pi 4.
- **Deployment verification:** Tested the API from the laptop. Confirmed that training data survives container replacement and a Pi reboot, and that the application starts automatically.
- **Documentation:** Started documenting configuration, deployment and API usage.

