# Deployment

## Overview

The application runs locally and on a Raspberry Pi 4 using Docker Compose.

| Component | Configuration |
|---|---|
| Pi operating system | Debian GNU/Linux 13 (trixie), ARM64 |
| Java base image | `eclipse-temurin:21-jre-jammy` |
| Docker Hub repository | `your-dockerhub-user/polar-app` |
| Deployment image tag | `latest` |
| Last documented, manually tested release | `v0.1.2` |
| Published image platforms | `linux/amd64`, `linux/arm64` |
| Database | H2 file database |
| Schema management | `create-drop` |
| Application port | `8080` |
| Pi deployment directory | `~/polar-app` |

The deployment is currently intended for development and testing on
the home network. Authentication is not implemented.

The H2 Console is enabled for database inspection.

Application commits do not automatically update the deployment.
A new JAR and Docker image must be built and deployed separately.

## Documentation placeholders

Replace these placeholders with your own settings:

| Placeholder | Meaning |
|---|---|
| `your-dockerhub-user` | Your Docker Hub account name |
| `your-pi-user` | Your Raspberry Pi SSH user |
| `your-pi-hostname.local` | Your Raspberry Pi hostname |

These placeholders are used only in this documentation.
They do not need to be added to `.env`.

## Configuration files

The project uses two Compose configurations:

- The laptop's `docker-compose.yml` can build and run the application.
- The Pi's `compose.yaml` downloads and runs a published image.

The Pi deployment directory contains:

```text
~/polar-app/
  compose.yaml
  .env
```

Use `.env.example` from the project as a template for `.env`.
Replace the example password before starting the application.

The real `.env` must remain outside Git and the Docker build context.
Commit `.env.example` with example values only.

On the Pi, restrict access to `.env`:

```bash
chmod 600 .env
```

The laptop and Pi have separate environment files, Docker images,
containers and volumes. Removing a laptop volume does not remove
the Pi's volume.

## Pi Compose configuration

Use the following configuration in `~/polar-app/compose.yaml`,
replacing `your-dockerhub-user`:

```yaml
services:
  polar-app:
    image: your-dockerhub-user/polar-app:latest
    ports:
      - "8080:8080"
    environment:
      POLAR_APP_DB_PASSWORD: "${POLAR_APP_DB_PASSWORD:?Set POLAR_APP_DB_PASSWORD in .env}"
      SPRING_H2_CONSOLE_ENABLED: "true"
    volumes:
      - polar-data:/app/data
    restart: unless-stopped

volumes:
  polar-data:
```

The Pi downloads the published image. It does not require the
application source code or a separate JAR file on the host.

The JAR is included in the image at `/app/app.jar`.

The deployment uses `latest`, so `POLAR_APP_IMAGE_TAG` is no longer
needed. Version tags are still published to identify releases.

`latest` is assigned explicitly during publishing. It does not
automatically select the highest version number.

## Database configuration and lifecycle

The current application configuration includes:

```properties
spring.datasource.url=jdbc:h2:file:./data/polar-db
spring.datasource.username=sa
spring.datasource.password=${POLAR_APP_DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.open-in-view=false

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true
```

Inside the container, the database file is located at:

```text
/app/data/polar-db.mv.db
```

The named volume stores database files outside the container's
writable layer.

However, `create-drop` recreates the application schema at startup
and drops it on a normal application shutdown. Training records
are therefore not expected to survive an application restart,
even when the volume remains attached.

This is intentional while the database contains disposable test data.
Persistent application data will require different schema management.

## Connect to the Pi

Connect from PowerShell using your configured SSH credentials:

```powershell
ssh your-pi-user@your-pi-hostname.local
```

Before running Compose commands:

```bash
cd ~/polar-app
```

Edit the Compose configuration:

```bash
nano compose.yaml
```

Edit the environment file:

```bash
nano .env
```

In nano:

- Press `Ctrl+O`, then Enter to save.
- Press `Ctrl+X` to exit.

For a private Docker Hub repository, authenticate before pulling:

```bash
docker login
```

## Build and test locally

Run Maven from the project directory:

```powershell
.\mvnw.cmd clean package
```

The Dockerfile copies `target/polar-app.jar`. Rebuild this JAR
after application changes and before building the image.

While existing tests are awaiting updates, packaging can temporarily
skip test execution:

```powershell
.\mvnw.cmd clean package "-DskipTests"
```

A build with skipped tests does not verify that the tests pass.

Build the local image using the project's Compose configuration:

```powershell
docker compose build polar-app
```

Start the application using that image:

```powershell
docker compose up -d --no-build
```

Check the container and startup logs:

```powershell
docker compose ps
docker compose logs --tail 50 polar-app
```

Test with Postman:

```http
GET http://localhost:8080/api/training
```

Expected response: `200 OK`, containing a list of trainings or `[]`.

Test the relevant application functionality before publishing.

## Publish an update

After local verification, publish a multi-platform image from the
project directory.

Replace `your-dockerhub-user` with your account name.
The following example uses `v0.1.3` as the next release tag;
it does not indicate that this version has already been published.

```powershell
docker buildx build --platform linux/amd64,linux/arm64 -t your-dockerhub-user/polar-app:v0.1.3 -t your-dockerhub-user/polar-app:latest --push .
```

Use a new version tag for each release.

This command publishes the release under both the version tag and
`latest`. Building without assigning `latest` does not update that tag.

Verify the published platforms:

```powershell
docker buildx imagetools inspect your-dockerhub-user/polar-app:v0.1.3
```

The output should include `linux/amd64` and `linux/arm64`.

## Deploy an update on the Pi

From `~/polar-app`, validate the Compose configuration without printing
resolved credentials:

```bash
docker compose config --quiet
```

Download the image currently tagged `latest`:

```bash
docker compose pull polar-app
```

After a successful download, deploy it:

```bash
docker compose up -d
```

Check the container and startup logs:

```bash
docker compose ps
docker compose logs --tail 50 polar-app
```

Successful startup includes a message similar to:

```text
Started PolarApplication in ... seconds
```

Test from Postman on the laptop:

```http
GET http://your-pi-hostname.local:8080/api/training
```

With the current `create-drop` configuration, expect a fresh
application database after startup.

## Inspect images and deployment status

Show the deployment's containers:

```bash
docker compose ps
```

Show the images used by those containers:

```bash
docker compose images
```

List images stored on the Pi:

```bash
docker images
```

The Pi deployment uses the `linux/arm64` platform.

A container showing the `latest` tag does not by itself identify
the corresponding release number. Older images may remain stored
until explicitly removed.

## Follow logs

Follow application logs continuously:

```bash
docker compose logs -f polar-app
```

Press `Ctrl+C` to stop following logs.
This does not stop the container.

## Inspect the H2 database

For the local Docker deployment, open:

```text
http://localhost:8080/h2-console
```

For the Pi deployment, open:

```text
http://your-pi-hostname.local:8080/h2-console
```

Use these connection settings:

| Setting | Value |
|---|---|
| Driver class | `org.h2.Driver` |
| JDBC URL | `jdbc:h2:file:/app/data/polar-db` |
| User name | `sa` |
| Password | `POLAR_APP_DB_PASSWORD` from that deployment's `.env` |

The console connects to the database of the application serving
that URL. The laptop and Pi databases are separate.

The H2 Console and remote console access are development settings.

## Stop, start and restart

Stop the existing container without removing it:

```bash
docker compose stop polar-app
```

Start the stopped container again:

```bash
docker compose start polar-app
```

Restart the existing container:

```bash
docker compose restart polar-app
```

With `create-drop`, application restarts reset the training records.

To apply changed Compose environment settings:

```bash
docker compose up -d
```

A restart alone does not apply changed environment settings.

To deploy a newly published image under `latest`, first run
`docker compose pull polar-app`, then `docker compose up -d`.

## Remove containers or reset test storage

Stop and remove the deployment's containers and network:

```bash
docker compose down
```

The named volume remains available.

To also delete the deployment's test-data volume:

```bash
docker compose down --volumes
```

This permanently removes that volume's contents.
Use it only when intentionally discarding test data.

Deleting the volume is not required for every update.
The current `create-drop` setting already resets the application
schema at startup.

List volumes:

```bash
docker volume ls
```

With the deployment directory named `polar-app` and default Compose
project naming, the volume is named:

```text
polar-app_polar-data
```

Inspect the database files through the running container:

```bash
docker compose exec polar-app ls -lh /app/data
```

## Verify automatic startup

The Compose configuration uses:

```yaml
restart: unless-stopped
```

To verify automatic startup, leave the container running and reboot
the Pi:

```bash
sudo reboot
```

The SSH connection will close.

After the Pi and application have started, test the API again from
Postman without manually starting the container.

Verify application availability rather than preservation of training
records: the current database configuration resets those records.

Automatic startup and database persistence were previously verified
with `v0.1.1`. Record persistence is not expected under the current
`create-drop` configuration.

## Known limitations

- An earlier Amazon Corretto-based image failed on the Pi 4.
  `eclipse-temurin:21-jre-jammy` was tested successfully.
- The application currently uses disposable H2 test data.
- Authentication and database backups are not yet implemented.
- A persistent volume is not a backup.
- New Git commits are not deployed automatically.
- The `latest` tag can point to a different release after publishing.
- Rolling back an image does not restore database contents or undo
  database changes.