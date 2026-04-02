# GamePicker

An Android app that learns your video game preferences through pairwise comparisons and recommends a game you'll love.

## How It Works

GamePicker presents you with two games at a time and asks which one you prefer. After 10 rounds, a scoring algorithm analyzes your choices — including genre affinities — and surfaces a personalized recommendation. Past recommendations are saved locally so you can look back at your history.

## Features

- **Genre filtering** — narrow the game pool to genres you actually enjoy before starting
- **Pairwise comparison loop** — tap the cover art of whichever game appeals to you more
- **Genre-weighted scoring** — choosing a game boosts similar games in the same genre pool
- **Persistent history** — every recommendation is saved to a local Room database
- **Smooth animations** — pairs slide in with a fade/translate transition between rounds

## Tech Stack

| Layer | Library |
|---|---|
| Language | Kotlin |
| Architecture | MVVM, shared `AndroidViewModel` across fragments |
| Navigation | Jetpack Navigation Component |
| Networking | Retrofit 2 + OkHttp |
| Image loading | Coil |
| Local storage | Room |
| API | [IGDB](https://api-docs.igdb.com/) via Twitch OAuth |

## Setup

### 1. Get IGDB credentials

IGDB requires a Twitch developer account. Follow the [IGDB authentication guide](https://api-docs.igdb.com/#authentication) to obtain a `client_id` and an OAuth `access_token`.

### 2. Add credentials to `local.properties`

In the root of the project, open (or create) `local.properties` and add:

```
igdb_client_id=YOUR_CLIENT_ID_HERE
igdb_token=YOUR_ACCESS_TOKEN_HERE
```

> `local.properties` is excluded from version control — never commit your credentials.

### 3. Build and run

Open the project in Android Studio, sync Gradle, and run on a device or emulator with API 24+.

## Project Structure

```
app/src/main/java/com/example/videogame/
├── data/
│   ├── engine/          # PreferenceEngine — scoring & pair selection logic
│   ├── local/           # Room database, DAO, and entity
│   ├── model/           # Game, Cover, Genre data classes
│   └── network/         # Retrofit client and IGDB API service
├── ui/
│   ├── compare/         # CompareFragment — the main comparison screen
│   ├── genre/           # GenreFilterFragment + RecyclerView adapter
│   ├── history/         # HistoryFragment + adapter
│   └── result/          # ResultFragment — displays the final recommendation
├── viewmodel/           # GameViewModel — shared state across all fragments
└── MainActivity.kt
```

## Scoring Algorithm

`PreferenceEngine` maintains a score map keyed by game ID. Each time a game is chosen:

- The winning game receives **+1.0**
- Every other game in the pool that shares a genre with the winner receives **+0.3**

After 10 rounds, the game with the highest cumulative score is returned as the recommendation. Previously shown pairs are tracked to avoid repetition.

## Requirements

- Android Studio Hedgehog or newer
- Android SDK 24+
- A valid IGDB API token (tokens expire — regenerate as needed)
