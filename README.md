<<<<<<< HEAD
# IT Job Offer Aggregator 🔍

[![Docker](https://img.shields.io/badge/Docker-✓-blue?logo=docker)](https://www.docker.com)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-2.6-6BA539?logo=openapi-initiative)](https://swagger.io/specification/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.1-6DB33F?logo=spring)](https://spring.io/projects/spring-boot)

This application fetches job offers from various popular job boards, processes them, and provides a unified API to access all aggregated job listings in one place. It's particularly focused on IT and tech-related positions.

## 🌟 Features
- Automated job offer fetching from multiple sources
- Aggregation and storage of job listings
- REST API for accessing the aggregated data
- Filtering by technology, seniority level, location, and date range
- Docker-based deployment for easy setup and portability
- API documentation with Swagger UI

## 🛠 Technologies
- **Backend**: Java 17, Spring Boot 3.3.1
- **API Documentation**: OpenAPI 2.6, Swagger UI
- **Containerization**: Docker, Docker Compose
- **Database**: PostgreSQL

## 🚀 Getting Started

### Prerequisites

- Docker and Docker Compose
- Git

### How to Run

1. Clone the repository:
```bash
git clone https://github.com/OGHaruki/ITJobAggregator.git
```
2.  Build the Docker image:
```bash
docker build -t job-offers:latest .
```
3.  Start the Application with Docker Compose
```bash
docker-compose up -d
```
4. The application will be available at: `http://localhost:8080`
5. The Swagger UI documentation can be accessed at: `http://localhost:8080/swagger-ui.html`

## 📚 API Endpoints

The application provides the following main endpoints:
- `GET /api/fetch-job-offers`: Trigger the job offer fetching process
- `GET /api/offers`: Get job offers with optional filtering parameters:
- `tech`: List of technologies (e.g., Java, Python)
- `seniority`: List of seniority levels (e.g., Junior, Senior)
- `location`: List of locations
- `from`: Start date for job postings
- `to`: End date for job postings

Example request:
```http
GET /api/offers?tech=Java,Python&seniority=Senior&location=Remote&from=2023-01-01&to=2023-12-31
```

Response:
```json
[
  {
    "id": 1,
    "title": "Java Developer (Mid)",
    "company": "TechCorp",
    "location": "Warszawa",
    "technologies": ["java", "spring"],
    "seniority": "senior",
    "datePosted": "2023-03-15"
  }
]
```

## 📌 Roadmap

Here are the planned future enhancements for the IT Job Aggregator:

1. **Q2 2025:**
   - Add support for more job listing sources
   - Implement user accounts and saved job searches
   - Develop a modern frontend interface using React
     
## 📄 Author
MIT License © 2024 Adam
=======
# ITJobAggregator
>>>>>>> 4dc2845f49b47c6f8d93ef4b9bd8659169807b0c
