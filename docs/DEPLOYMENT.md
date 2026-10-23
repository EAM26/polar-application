# Deployment

## Overview

The application runs on a Raspberry Pi 4 using Docker Compose.

| Component | Configuration |
|---|---|
| Operating system | Debian 13, ARM64 |
| Java base image | `eclipse-temurin:21-jre-jammy` |
| Docker Hub repository | `em26/polar-app` |
| First working Pi image | `v0.1.1` |
| Database | H2 file database |
| Application port | `8080` |
| Deployment directory | `~/polar-app` |

The current deployment is intended for testing on the home network.
API-key authentication is not implemented in this version.
The H2 console is disabled on the Pi.

## Configuration files

The project uses two different Compose configurations:

- The laptop's `docker-compose.yml` builds and runs the local application.
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

Changing `POLAR_APP_DB_PASSWORD` does not change the password of an
existing H2 database.

## Pi Compose configuration

Use the following configuration in `~/polar-app/compose.yaml`:

```yaml
services:
  polar-app:
    image: em26/polar-app:${POLAR_APP_IMAGE_TAG:-v0.1.1}
    ports:
      - "8080:8080"
    environment:
      POLAR_APP_DB_PASSWORD: "${POLAR_APP_DB_PASSWORD:?Set POLAR_APP_DB_PASSWORD in .env}"
      SPRING_H2_CONSOLE_ENABLED: "false"
    volumes:
      - polar-data:/app/data
    restart: unless-stopped

volumes:
  polar-data:
```

The database files are stored in `/app/data` inside the container.
The named volume keeps these files when the container is replaced.

The application uses:

```properties
spring.datasource.url=jdbc:h2:file:./data/polar-db
spring.datasource.username=sa
spring.datasource.password=${POLAR_APP_DB_PASSWORD}
spring.jpa.hibernate.ddl-auto=update
```

## Connect to the Pi

From PowerShell on the laptop, use your own SSH settings:

```powershell
ssh -i "$env:USERPROFILE\.ssh\your-private-key" -o IdentitiesOnly=yes your-pi-user@your-pi-hostname.local
```

Replace `your-private-key`, `your-pi-user` and `your-pi-hostname`
with your actual SSH details.

These are documentation placeholders; they do not belong in `.env`.

Before running Compose commands on the Pi:

```bash
cd ~/polar-app
```

For a private Docker Hub repository, authenticate on the Pi before
downloading images:

```bash
docker login
```

## Start and check the application

Validate the configuration without printing resolved credentials:

```bash
docker compose config --quiet
```

Download the selected image:

```bash
docker compose pull
```

After a successful download, start the application:

```bash
docker compose up -d
```

Check the container status:

```bash
docker compose ps
```

Read recent logs:

```bash
docker compose logs --tail 50 polar-app
```

Successful startup includes:

```text
Started PolarApplication in ... seconds
```

Follow logs continuously:

```bash
docker compose logs -f polar-app
```

Press `Ctrl+C` to stop following logs.
This does not stop the application.

Test from Postman on the laptop, replacing the hostname:

```http
GET http://your-pi-hostname.local:8080/api/training
```

Expected response: `200 OK`, containing a list of trainings or `[]`.

## Stop and restart

Stop the container without removing it:

```bash
docker compose stop
```

Start it again:

```bash
docker compose up -d
```

Restart the existing container:

```bash
docker compose restart polar-app
```

To apply a changed image tag or environment configuration, use:

```bash
docker compose up -d
```

Stop and remove the deployment's containers and network:

```bash
docker compose down
```

The database volume remains available.

## Publish an update

If application code changed, build and test a new JAR first.

The Dockerfile copies `target/polar-app.jar`, so it must contain the
intended application version.

The environment configuration for an IntelliJ application run does not
automatically apply to a separate Maven terminal session.
Make sure the build's tests have their required configuration.

From PowerShell in the project directory, publish a new image version.
For example:

```powershell
docker buildx build --platform linux/amd64,linux/arm64 -t em26/polar-app:latest -t em26/polar-app:v0.1.2 --push .
```

Use a new version tag for each release.

Verify the published platforms:

```powershell
docker buildx imagetools inspect em26/polar-app:v0.1.2
```

The output should include `linux/amd64` and `linux/arm64`.

## Deploy an update

After publishing successfully, edit `.env` on the Pi:

```bash
nano .env
```

Select the new version:

```dotenv
POLAR_APP_IMAGE_TAG=v0.1.2
```

Download the image:

```bash
docker compose pull
```

After a successful download, deploy it:

```bash
docker compose up -d
```

Check the logs and test the API again.
The existing database volume remains attached.

## Verify persistence and automatic startup

Create a training through the API and note its ID.

Recreate the container:

```bash
docker compose up -d --force-recreate
```

After startup, confirm that the same training is still available.

Reboot the Pi:

```bash
sudo reboot
```

The SSH connection will close.
Allow approximately two minutes for the Pi, Docker and application
to start.

Confirm from Postman that the same training is available without
manually starting the application.

Both checks passed with image version `v0.1.1`.

## Known limitations

- `amazoncorretto:21` failed on the Pi 4 because its Amazon Linux base
  required newer ARM processor features.
  `eclipse-temurin:21-jre-jammy` was tested successfully.
- Image version `v0.1.0` does not run on this Pi 4.
- `docker compose down --volumes` deletes the deployment's database
  volume. Use it only when intentionally discarding test data.
- A persistent volume is not a backup.
  Database backups are not yet configured.
- Rolling back an image does not reverse database changes.