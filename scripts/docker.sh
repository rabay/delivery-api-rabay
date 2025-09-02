#!/bin/bash

# Build
DOCKER_BUILDKIT=1 docker build -t delivery-api-rabay-app:latest .
# ou
# docker compose up --build -d

# Run app
docker compose up -d