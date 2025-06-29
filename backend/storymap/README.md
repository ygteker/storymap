# StoryMap - GitLab Issue Mapping Service

This is a Quarkus-based backend service designed to fetch GitLab issues and associate them with user journeys and steps, persisting that data in a PostgreSQL database. It includes JWT-based authentication and caching support.

---

## ✅ Features

- Fetch GitLab issues via GitLab REST API (with caching)
- Store User Journeys, User Steps, Releases, and Issue Assignments
- Expose REST endpoints for issue assignment and mapping
- JWT login/signup/logout with RSA encryption
- Unit and integration test coverage
- Dockerized for deployment

---

## 🚀 Running Locally (Dev Mode)

```bash
./gradlew quarkusDev
```

Make sure you have a running PostgreSQL instance with the following settings (or update `application.yaml`):

```yaml
quarkus:
  datasource:
    db-kind: postgresql
    jdbc:
      url: jdbc:postgresql://localhost:5432/storymap
    username: admin
    password: admin
```

---

## 🐳 Docker Deployment (Production Mode)

### 1. Build Docker Image

```bash
docker build -f src/main/docker/Dockerfile.jvm -t storymap-app .
```

### 2. Use Docker Compose to Spin Up App + PostgreSQL

Create a file called `docker-compose.yml`:

```yaml
version: '3.8'

services:
  db:
    image: postgres:15
    container_name: storymap-db
    restart: always
    environment:
      POSTGRES_DB: storymap
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql/data

  app:
    image: storymap-app
    container_name: storymap-app
    build:
      context: .
      dockerfile: src/main/docker/Dockerfile.jvm
    depends_on:
      - db
    ports:
      - "8080:8080"
    environment:
      DB_USER: admin
      DB_PASSWORD: ${POSTGRES_PASSWORD}
      QUARKUS_DATASOURCE_JDBC_URL: jdbc:postgresql://db:5432/storymap
      PRIVATE_KEY: ${PRIVATE_KEY}
    env_file:
      - .env

volumes:
  pgdata:
```

Then run:

```bash
docker-compose up --build
```

---

## 🧪 Running Tests

```bash
./gradlew test
```

---

## 🔐 JWT Authentication

To generate a new keypair:

```bash
openssl genpkey -algorithm RSA -out privateKey.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in privateKey.pem -out publicKey.pem
```

Base64-encode the private key for `.env`:

```bash
base64 -i privateKey.pem
```

Then add it to `.env`:

```
PRIVATE_KEY=<your-base64-private-key>
```

---

## 📦 API Endpoints

| Method | Path                    | Description                                      |
|--------|-------------------------|--------------------------------------------------|
| POST   | /auth/register          | Register new user                                |
| POST   | /auth/login             | Login and receive JWT                            |
| POST   | /auth/logout            | (Stateless; handled on client)                   |
| GET    | /api/issues             | List GitLab issues with optional mapping         |
| POST   | /api/assignments        | Assign issue to user step and optional release   |
| GET    | /api/user-story-map     | View full user journey map                       |
