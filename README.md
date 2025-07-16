Tilt-Controlled Android Game  
A 2D Android game implemented in Kotlin and Jetpack Compose where the player navigates a ball by tilting the device using the accelerometer sensor. The game features real-time obstacle spawning, time-based scoring, high score persistence, and immersive audio.

Features:  
- Tilt Controls: Move the ball using device tilt (accelerometer).
- Obstacle Spawning: Dynamic obstacles spawn at increasing speed to challenge the player.
- Time-Based Scoring: Score increases by 1 point every second while playing.
- High Score Persistence: Stores and retrieves the high score locally using DataStore.
- Reactive State: Uses StateFlow and Compose State for UI updates and game state management.
- Audio:
  - Background music loops during gameplay.
  - Collision sound effect on obstacle hit (SoundPool).
  - Game-over music plays once on collision (MediaPlayer).
- Pause and Main Menus: Main menu at start, pause menu during play, and game-over flow.

Architecture & Tech Stack:  
- Language: Kotlin 
- UI: Jetpack Compose 
- Dependency Injection: Hilt (@HiltViewModel) 
- State Management:
  - StateFlow for sensor data (x, y).
  - mutableStateOf for ballPosition, fallingObstacle list, and gameState.
- Coroutines: viewModelScope for obstacle spawning, scoring, and difficulty scaling jobs.
- Data Storage: Jetpack DataStore (Preferences) for high score.
- Audio:
  - MediaPlayer for background and game-over music.
  - SoundPool for collision sounds.

Clone the Repository:  
`https://github.com/Saransh-001/Tilt-It.git`

Build and Run:
- Open the project in Android Studio.
- Let Gradle sync and build the project.
- Run on physical device with accelerometer support.
