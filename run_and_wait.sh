#!/bin/bash

# Run the application
docker-compose up -d

# Wait 1 hour
sleep 3600

# Check app logs
docker-compose logs app