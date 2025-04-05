# StudySmart: Collaborative Study Planner

## Overview
StudySmart is a microservices-based collaborative study planner that helps students manage their study schedules efficiently. The platform integrates AI-driven recommendations, gamification, and real-time collaboration.

## Microservices & Their Purpose

### 1️⃣ User Management Service (Port: 8081)
- Manages user registration, authentication, and roles.
- Implements JWT authentication for security.

### 2️⃣ Study Planner Service (Port: 8082)
- Manages study plans, schedules, and task assignments.
- Sends email notifications for study reminders.

### 3️⃣ Collaboration & Gamification Service (Port: 8083)
- Handles real-time discussions, leaderboards, and study group interactions.
- Uses WebSockets for real-time communication.

### 4️⃣ Notification Service (Port: 8084)
- Sends push notifications, emails, and SMS reminders.
- Uses Kafka for event-driven messaging.

### API Gateway (Port: 8080)
- Routes external API requests to the appropriate microservices.
- Uses Spring Cloud Gateway for dynamic routing.

### Eureka Server (Port: 8761)
- Handles service discovery and registration for all microservices.

### Config Server (Port: 8888)
- Centralized configuration management for all services.

## API Endpoints

### User Management Service
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/users/register` | Register a new user |
| POST | `/api/users/login` | Authenticate a user |
| GET | `/api/users/{email}` | Get user details by email |

### Study Planner Service
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/study-plans/create` | Create a new study plan |
| GET | `/api/study-plans/user/{userId}` | Retrieve study plan for a user |

### Collaboration & Gamification Service
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/collaboration/chat/send` | Send a chat message |
| GET | `/api/collaboration/leaderboard` | Retrieve leaderboard data |

### Notification Service
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/notifications/send/{userId}` | Send a notification to a user |

### Resource Management Service
| Method | Endpoint | Description |
|--------|---------|-------------|
| POST | `/api/resources/upload` | Upload a study material |
| GET | `/api/resources/download/{fileId}` | Download a study material |

## Deployment & Running Instructions
1. Start **Eureka Server** (`8761`)
2. Start **Config Server** (`8888`)
3. Start all **Microservices**
4. Start **API Gateway** (`8080`)
5. Access APIs via **Gateway** (e.g., `http://localhost:8080/api/users/register`)

## Collaborators
- **C142, Naman Shah**  
- **C159, Jash Sikligar**  
- **C162, Aarya Pandya**  
