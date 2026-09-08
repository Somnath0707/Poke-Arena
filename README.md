<div align="center">

# ⚡ POKEARENA

### Full-Stack Turn-Based Pokémon Battle Simulator & Squad Lab

*Built with Java 25, Spring Boot, Spring Data JPA, and a retro cyberpunk arena UI.*

[![Java](https://img.shields.io/badge/Java-25-orange?style=for-the-badge&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x%20%2F%204.x-6DB33F?style=for-the-badge&logo=springboot)](https://spring.io/projects/spring-boot)
[![Database](https://img.shields.io/badge/Database-H2%20In--Memory-blue?style=for-the-badge&logo=databricks)](https://www.h2database.com/)
[![Pokédex](https://img.shields.io/badge/Pokédex-1%2C215%20Species-red?style=for-the-badge&logo=pokemon)](https://github.com)
[![License](https://img.shields.io/badge/License-MIT-purple?style=for-the-badge)](LICENSE)

<br/>

**PokeArena** is a turn-based Pokémon battle engine and roster forge. It packs all **9 generations (1,215 Pokémon)**, official **18-type combat effectiveness**, stat scaling by level, trainer win/loss tracking, and a live visual arena that turns server-side simulation logs into animated sprite battles with authentic sound effects.

No corporate fluff, no 20-container microservice bloat—just clean OOP backend code, snappy REST endpoints, and a battle simulator you can spin up in 5 seconds.

</div>

---

## 📸 Screenshots

### ⚔️ 1. The Battle Arena
> Live visual replay of the backend combat engine with animated Pokémon Showdown sprites, HP gauges, damage popups, attack beams, and Web Audio API synthesized SFX.

![PokeArena Battle Field](docs/screenshots/arena.png)

---

### 📖 2. Complete 9-Gen Pokédex (1,215 Pokémon)
> Direct access to all 1,215 species loaded from CSV on boot—featuring base stats (HP, Attack, Defense, Speed), dual typing, regional forms, and Mega Evolutions with instant search and type filters.

![Pokédex Browser](docs/screenshots/pokedex.png)

---

### 🛠️ 3. Team Lab (Draft Room)
> Recruit 1 to 6 Pokémon from the catalogue, calibrate levels from 1 to 100, analyze type coverage, name your squad, and deploy directly to the battle arena.

![Team Lab Squad Builder](docs/screenshots/team-builder.png)

---

### 🏆 4. Leaderboard & Battle History
> Real-time competitive leaderboard calculating trainer win rates and storing full round-by-round match histories for instant replay.

![Leaderboard and Match History](docs/screenshots/rankings.png)

---

## ✨ Features

- **🎮 Combat Simulation Engine**:
  - Turn resolution based on calculated speed stats.
  - Authentic damage formula scaled with level, attack power, and target defense.
  - Complete **18-Type Effectiveness Matrix** (`Fire`, `Water`, `Grass`, `Electric`, `Normal`, `Fighting`, `Flying`, `Poison`, `Ground`, `Rock`, `Bug`, `Ghost`, `Steel`, `Psychic`, `Ice`, `Dragon`, `Dark`, `Fairy`) including **2.0× super-effective**, **0.5× resistance**, and **0.0× full immunity**.
  - Turn-by-turn combat event logging saved directly to match history.
- **⚡ 1,215 Pokémon Dataset**:
  - Automatically seeded on first boot from `Pokemon.csv`.
  - Full support for alternate forms: Mega Evolutions, Alolan, Galarian, and Hisuian forms with distinct typing and stats.
  - Zero duplicate IDs or constraint conflicts.
- **🛡️ Team Builder**:
  - 1–6 slot squads with custom level adjustment (1–100).
  - Dynamic type coverage calculator.
  - Backend validation using Jakarta Bean Validation.
- **🏆 Ranked Arena & History**:
  - Top-10 win leaderboard.
  - Battle history lookup by trainer name.
  - Stored combat logs for review and playback.
- **🚨 Clean Error Architecture**:
  - Centralized `@RestControllerAdvice` returning structured RFC 7807 problem details (`404 Not Found`, `400 Bad Request`).
- **🔊 Zero-Dependency Sound**:
  - Web Audio API synthesizer built directly into the client—no external audio files needed.

---

## 🏗️ Architecture

```
com.pokearena
├── config
│   └── DataSeeder.java            # CSV stream parser & DB seeder
├── controller
│   ├── BattleController.java      # Match simulation & history endpoints
│   ├── SpeciesController.java     # Species catalogue query
│   ├── TeamController.java        # Squad assembly & validation
│   └── TrainerController.java     # Trainer registration & leaderboard
├── engine
│   ├── BattleEngine.java          # Turn loop, speed priority & damage math
│   ├── BattlePokemon.java         # In-memory combatant state & scaling
│   ├── BattleResult.java          # Immutable match output record
│   └── TypeEffectivenessMatrix.java # 18x18 official Pokémon type chart
├── entity
│   ├── BattleHistory.java         # Persisted match outcomes & log events
│   ├── PokemonSpecies.java        # Species entity with base stats & typing
│   ├── Team.java                  # Squad entity (cascades to members)
│   ├── TeamMember.java            # Individual Pokémon in a squad
│   └── Trainer.java               # Trainer profile, wins & losses
├── exception
│   ├── GlobalExceptionHandler.java # @RestControllerAdvice
│   ├── InvalidBattleException.java # 400 Bad Request
│   └── ResourceNotFoundException.java # 404 Not Found
├── model
│   ├── PokemonType.java           # 18 Elemental types enum
│   └── dto                        # Immutable Java Record DTOs
└── repository                     # Spring Data JPA repositories
```

---

## 🚀 Quickstart

### Prerequisites
- **Java 21 or Java 25**
- That's it. Maven is included via the bundled `./mvnw` wrapper.

### 1. Clone the repo
```bash
git clone https://github.com/Somnath0707/Poke-Arena.git
cd poke-arena
```

### 2. Run the application
```bash
./mvnw spring-boot:run
```

Once you see:
```text
✅ Successfully seeded 1215 Pokémon from CSV into H2!
PokeArenaApplication started successfully!
```

### 3. Open the Battle Arena
Navigate to **`http://localhost:8088`** in your browser and start battling!

---

## 📡 REST API Reference

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/species` | Fetch all 1,215 species with base stats and typing |
| `POST` | `/api/trainers` | Register a new trainer (`name`, `email`) |
| `GET` | `/api/leaderboard` | Get Top 10 trainers ranked by wins |
| `POST` | `/api/teams` | Create a squad (1–6 Pokémon with level & slot order) |
| `POST` | `/api/battles/simulate` | Simulate combat between two teams |
| `GET` | `/api/battles/history/{trainerName}` | Get battle history & match logs for a trainer |

### Example Payloads

#### Register Trainer
```bash
curl -X POST http://localhost:8088/api/trainers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Ash Ketchum",
    "email": "ash@pallet.town"
  }'
```

#### Build a Team
```bash
curl -X POST http://localhost:8088/api/teams \
  -H "Content-Type: application/json" \
  -d '{
    "trainerId": 1,
    "teamName": "Pallet Champions",
    "members": [
      { "speciesId": 35, "level": 88, "slotOrder": 1 },
      { "speciesId": 6,  "level": 85, "slotOrder": 2 },
      { "speciesId": 175,"level": 82, "slotOrder": 3 }
    ]
  }'
```

#### Simulate a Battle
```bash
curl -X POST http://localhost:8088/api/battles/simulate \
  -H "Content-Type: application/json" \
  -d '{
    "teamAId": 1,
    "teamBId": 2
  }'
```

---

## 🗺️ What's Cooking (Roadmap)

- [ ] **Move System**: Assigning 4 distinct moves (Power, Accuracy, Type, PP) like *Thunderbolt*, *Flamethrower*, and *Hydro Pump*.
- [ ] **PostgreSQL + Flyway**: Production persistence so records survive server restarts.
- [ ] **Swagger / OpenAPI**: Live interactive docs at `/swagger-ui.html`.
- [ ] **WebSockets (STOMP)**: Live interactive mode where two players choose moves turn-by-turn in real time.
- [ ] **Docker & Compose**: One-command `docker compose up` deployment.

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE). Pokémon and Pokémon character names are trademarks of Nintendo / Creatures Inc. / GAME FREAK inc.
