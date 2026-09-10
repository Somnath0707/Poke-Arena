# PokeArena

A turn-based Pokémon battle simulation platform and roster forge built with Java, Spring Boot, Spring Data JPA, and PostgreSQL.

PokeArena models full-scale team combat across all 9 generations (1,215 species). Instead of acting as a simple database wrapper, the backend implements a state-driven combat engine featuring an 18-type compound effectiveness chart, dynamic move arsenals, speed-priority turn resolution, stat scaling by level, tactical switch-in heuristics, and round-by-round combat logging for replay.

---

## Screenshots

### 1. Battle Arena
Live visual replay of the backend simulation log with animated Pokémon Showdown sprites, HP gauges, damage popups, move announcements, and Web Audio API synthesized sound effects.

![PokeArena Battle Field](docs/screenshots/arena.png)

### 2. Pokédex (1,215 Species)
Catalogue of all 9 generations ingested on startup from CSV, including base stats (HP, Attack, Defense, Speed), primary and secondary typing, regional variants, and Mega Evolutions.

![Pokédex Browser](docs/screenshots/pokedex.png)

### 3. Team Lab (Roster Forge)
Draft 1 to 6 Pokémon into custom squads, calibrate levels from 1 to 100, inspect elemental coverage, and deploy to the arena.

![Team Lab Squad Builder](docs/screenshots/team-builder.png)

### 4. Ranked Standings & Match History
Trainer win/loss tracking, top-10 leaderboard, and full historical combat log retrieval for playback.

![Leaderboard and Match History](docs/screenshots/rankings.png)

---

## Architecture & System Design

The application follows a layered Spring Boot architecture with clear separation between web APIs, business services, the standalone combat simulation engine, and persistence.

```
Client (Single-Page App)
       │
       ▼
[ JwtAuthenticationFilter ]  ──> [ Spring Security Context ]
       │
       ▼
[ Controllers ]
 ├── AuthController        (/api/auth/**)
 ├── SpeciesController     (/api/species)
 ├── TeamController        (/api/teams)
 ├── BattleController      (/api/battles/**)
 └── TrainerController     (/api/trainers, /api/leaderboard)
       │
       ▼
[ Service Layer ]
 ├── TrainerService
 ├── SpeciesService
 ├── TeamService           (Validation & Entity-DTO Mapping)
 └── BattleService         (Transaction Boundary & Record Updates)
       │
       ├───────────────────────────────┐
       ▼                               ▼
[ Combat Engine ]              [ Spring Data JPA Repositories ]
 ├── BattleEngine               ├── TrainerRepository
 ├── TypeEffectivenessMatrix    ├── PokemonSpeciesRepository
 ├── BattlePokemon              ├── TeamRepository
 └── Move & MoveTier (Enums)    └── BattleHistoryRepository
                                       │
                                       ▼
                              [ PostgreSQL Database ]
```

---

## Core Engineering Highlights

### 1. State-Driven Combat Engine
Battles are resolved turn-by-turn inside `BattleEngine`:
- **Speed Priority**: Compares active combatants' calculated speed stats each round to determine attack order.
- **Accurate Damage Formula**: Scaled by attacker level, move power, attacker attack vs. defender defense, same-type attack bonus, and type effectiveness:
  $$\text{Damage} = \left(\left(\frac{2 \times \text{Level}}{5} + 2\right) \times \text{Power} \times \frac{\text{Attack}}{\text{Defense}} \times \frac{1}{50} + 2\right) \times \text{STAB} \times \text{Multiplier}$$
- **STAB (Same-Type Attack Bonus)**: Grants a 1.5× damage multiplier when a Pokémon uses a move matching its primary or secondary elemental typing.
- **Accuracy Rolls**: High-risk, heavy-damage moves have realistic miss probabilities determined at runtime.
- **Event Journaling**: Every combat event (moves, misses, effectiveness alerts, damage, faints, switch-ins) is sequentially recorded into a `List<String> battleLog` for client replay.

### 2. Dynamic Elemental Move Arsenal
Rather than constraining automated combatants to 4 static move slots, Pokémon dynamically draw from an elemental arsenal composed of their primary type, secondary type, and universal combat moves:
- **Move Tiers**: Moves are categorized into `LIGHT` (40 power, 100% accuracy), `STANDARD` (75–90 power, 100% accuracy), `HEAVY` (100–120 power, 75–85% accuracy), and `ULTIMATE` (130–150 power, 60–80% accuracy).
- **Desperation / Clutch Mechanic**: When a Pokémon's HP drops below 25%, probability weighting shifts dramatically toward heavy and ultimate attacks, simulating an anime-style last stand.

### 3. Compound 18-Type Effectiveness Matrix
`TypeEffectivenessMatrix` implements the complete official 18×18 Pokémon type chart and compounds multipliers across dual-type defenders:
- **4.0× Double Super-Effective**: e.g., Rock vs. Charizard (Fire / Flying)
- **2.0× Super-Effective**: e.g., Water vs. Fire
- **1.0× Neutral**: e.g., Water vs. Grass/Flying (2.0× and 0.5× cancel out)
- **0.5× Resisted**: e.g., Fire vs. Water
- **0.25× Double Resisted**: e.g., Grass vs. Charizard (Fire / Flying)
- **0.0× Full Immunity**: e.g., Electric vs. Swampert (Water / Ground)

### 4. Tactical Counter-Switching Heuristic
When an active Pokémon faints, the trainer does not simply pop the next index off an array. The engine evaluates surviving bench members:
- **75% Tactical Counter Pick**: Searches the remaining bench for any Pokémon whose typing deals $\ge 2.0\times$ damage against the active opponent on the field. If found, deploys the counter.
- **25% Wildcard / Fallback**: If no counter exists or on a wildcard roll, selects randomly from surviving bench Pokémon.

### 5. Automated Data Ingestion
On boot, `DataSeeder` streams and parses `Pokemon.csv` into database entities:
- Handles composite names, Megas, and regional forms (e.g., `Charizard (Mega Charizard X)`).
- Idempotent seeding: executes only if the species table is empty, avoiding duplicate key constraint errors.

### 6. Authentication & Security
- Stateless JWT authentication via Spring Security and JJWT.
- Passwords hashed using `BCryptPasswordEncoder`.
- Custom `JwtAuthenticationFilter` validating Bearer tokens on protected squad creation and simulation endpoints.

---

## Domain Model & Database Schema

The relational schema is normalized across 5 core entities:
- **`Trainer`**: Profile, BCrypt password hash, wins, losses, role.
- **`PokemonSpecies`**: Canonical species dictionary (1,215 rows) with base HP, Attack, Defense, Speed, and primary/secondary elemental types.
- **`Team`**: Squad entity associated with a `Trainer` ($N:1$). Cascades all lifecycle operations to its members.
- **`TeamMember`**: Roster slot entity ($N:1$ with `Team`, $N:1$ with `PokemonSpecies`) holding custom level ($1\text{–}100$) and slot order ($1\text{–}6$).
- **`BattleHistory`**: Match outcomes, timestamps, round count, and denormalized match log events (`@ElementCollection`).

---

## REST API Reference

### Public Endpoints
| Method | Path | Description |
| :--- | :--- | :--- |
| `POST` | `/api/auth/register` | Register a new trainer account with name, email, and password |
| `POST` | `/api/auth/login` | Authenticate credentials and receive a stateless JWT Bearer token |
| `GET` | `/api/species` | Retrieve all 1,215 catalogued Pokémon species with base stats |
| `GET` | `/api/leaderboard` | Retrieve top 10 trainers ranked by total wins |

### Protected Endpoints (Requires `Authorization: Bearer <token>`)
| Method | Path | Description |
| :--- | :--- | :--- |
| `POST` | `/api/teams` | Create a squad with 1 to 6 Pokémon members, levels, and slot orders |
| `POST` | `/api/battles/simulate` | Execute a battle simulation between two team IDs |
| `GET` | `/api/battles/history/{trainerName}` | Retrieve historical match logs for a specific trainer |

---

## Local Development Setup

### Prerequisites
- **Java 21 or Java 25**
- **PostgreSQL 14+** running locally on port `5432`
- Maven (bundled via `./mvnw`)

### 1. Database Setup
Create a PostgreSQL database named `pokearena`:

```sql
CREATE DATABASE pokearena;
```

### 2. Configure Credentials
Check `src/main/resources/application.properties` and ensure database settings match your local environment:

```properties
server.port=8088

spring.datasource.url=jdbc:postgresql://localhost:5432/pokearena
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### 3. Build & Run
```bash
./mvnw spring-boot:run
```

Once started, navigate to `http://localhost:8088` in your browser.

---

## Tech Stack

- **Language**: Java 25
- **Framework**: Spring Boot 3.x / 4.x
- **Persistence**: Spring Data JPA, Hibernate ORM
- **Database**: PostgreSQL
- **Security**: Spring Security, JJWT (JSON Web Token), BCrypt
- **Validation**: Jakarta Bean Validation
- **Frontend Client**: Vanilla JavaScript, CSS Grid/Flexbox, Web Audio API

---

## License

This project is open-source under the [MIT License](LICENSE). Pokémon and Pokémon character names are trademarks of Nintendo, Creatures Inc., and GAME FREAK Inc.
