# Courtside - NBA Basketball Data Platform

A Spring Boot-based NBA data platform providing real-time game scores, player statistics, team analytics, AI-generated game reviews, and community features like ratings and comments.

## Features

- Real-time Game Data: Integrates balldontlie.io API to fetch live NBA game scores, schedules, and player statistics.
- Data Cleaning Pipeline: Groups home/away players, sorts by points, and recalculates team shooting percentages by aggregating FGM/FGA.
- AI-Generated Game Reviews: Integrates DeepSeek API to generate 200-300 word post-game reviews. Results are cached in Redis for 24 hours.
- Player Ratings: Users can rate players per game, with insert/update distinguished by an enum-based result type.
- Community Features: User login/registration, player comments, and posts.
- VIP Access Control: AI game reviews restricted to VIP users via Spring Security.

## Tech Stack

- Backend: Spring Boot 3.5, Spring MVC, MyBatis
- Security: Spring Security
- Database: MySQL
- Cache: Redis
- Frontend: Thymeleaf, jQuery, HTML/CSS
- Build: Maven
- External APIs: balldontlie.io (NBA data), DeepSeek (AI)

## Project Structure

```
courtside/
├── src/main/java/com/jr/
│   ├── client/          # External API clients
│   ├── config/          # Spring Security config
│   ├── controller/      # Controllers
│   ├── dto/             # Data Transfer Objects
│   ├── entity/          # Database entities
│   ├── enums/           # Enums
│   ├── mapper/          # MyBatis mappers
│   ├── service/         # Business logic
│   │   └── impl/        # Service implementations
│   └── util/            # Utilities
├── src/main/resources/
│   ├── mapper/          # MyBatis XML
│   ├── static/js/       # Frontend JavaScript
│   ├── templates/       # Thymeleaf templates
│   └── application.yml  # Configuration
└── pom.xml
```

## Core Implementation

### 1. Data Cleaning

The third-party API returns flat player stats without team grouping, and shooting percentages are individual-level. The service layer:

- Filters stats by home/away team using team_id.
- Sorts players by points descending.
- Recalculates team shooting percentages by aggregating FGM/FGA.
- Uses a TeamType enum to eliminate duplication.

### 2. AI Review Generation

- Builds a structured prompt from game info and player stats.
- Calls DeepSeek API with system and user prompts.
- Caches result in Redis for 24 hours.
- Provides a regenerate endpoint.

### 3. Rating System

- Checks whether the user has rated this player in this game.
- Inserts new rating or updates existing one.
- Returns a RatingResult enum for the frontend.

### 4. Security & VIP Access

- `/courtside/recap/**` requires VIP authority.
- `/courtside/rating/submitRating` requires authentication.
- 401 returns JSON, 403 returns JSON.
- Frontend handles 401 (redirect to login) and 403 (VIP prompt).

## Getting Started

Prerequisites:

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

Setup:

1. Clone the repository:

```
git clone https://github.com/zhaoyuming-glitch/courtside.git
cd courtside
```

2. Create MySQL database:

```
CREATE DATABASE courtside DEFAULT CHARACTER SET utf8mb4;
```

3. Configure application.yml:

```
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/courtside
    username: your_username
    password: your_password
  data:
    redis:
      host: 127.0.0.1
      port: 6379
nba:
  api:
    base-url: https://api.balldontlie.io/v1
    key: your_nba_api_key
ai:
  api:
    url: https://api.deepseek.com/chat/completions
    key: your_deepseek_api_key
    model: deepseek-chat
```

4. Build and run:

```
mvn clean package
java -jar target/courtside-0.0.1-SNAPSHOT.jar
```

5. Open browser:

```
http://localhost:8080
```

## Key Endpoints

- GET /api/games/scoreboard — Today's games
- GET /api/games/{id} — Game detail
- GET /api/games/date?date= — Games by date
- GET /api/stats/homeTeamStats?gameId= — Home team stats
- GET /api/stats/visitorTeamStats?gameId= — Visitor team stats
- GET /api/stats/SeasonAvg?playerId=&season= — Season averages
- GET /courtside/recap/{gameId} — AI review (VIP)
- POST /courtside/rating/submitRating — Submit rating

## Contact

- Author: YuMing Zhao (赵渝鸣)
- Email: kirito1951514298@gmail.com
- GitHub: https://github.com/zhaoyuming-glitch

## License

This project is for learning and portfolio purposes.
