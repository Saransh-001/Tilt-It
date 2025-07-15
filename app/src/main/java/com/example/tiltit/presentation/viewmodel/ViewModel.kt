package com.example.tiltit.presentation.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiltit.DataStoreManager
import com.example.tiltit.presentation.Obstacle
import com.example.tiltit.accelerometer.baseclass.MeasurableSensor
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.nio.file.Files.size

@HiltViewModel
class ViewModel @Inject constructor(
    private val accelerometerSensor: MeasurableSensor,
    application: Application
): AndroidViewModel(application) {

    var x by mutableFloatStateOf(0f)
    var y by mutableFloatStateOf(0f)

    val ballRadius = 125f
    var screenSize by mutableStateOf(Size.Zero)
    var ballPosition by mutableStateOf(Offset.Zero)

    var fallingObstacle by mutableStateOf(listOf<Obstacle>())
        private set

    var gameState by mutableStateOf<GameState>(GameState.MainMenu)

    var score by mutableIntStateOf(0)
        private set

    private var scoreJob: Job? = null
    private var obstacleJob: Job? = null
    private var speedJob: Job? = null

    private var spawnSpeed: Long = 800L

    var highScore: Int = 0

    init {
        viewModelScope.launch {
            highScore = DataStoreManager.getHighScore(application)
        }
    }

    fun startScoring() {
        scoreJob?.cancel()
        scoreJob = viewModelScope.launch {
            while (isActive && gameState == GameState.Running) {
                score++
                delay(1000L)
            }
        }
    }

    fun stopScoring() {
        scoreJob?.cancel()
    }

    fun updateHighScoreIfNeeded() {
        if (score > highScore) {
            highScore = score
            viewModelScope.launch {
                DataStoreManager.saveHighScore(getApplication(), highScore)
            }
        }
    }

    fun updateBallPosition() {
        val dx = -x * 5
        val dy = y * 5

        val maxX = screenSize.width - ballRadius * 2
        val maxY = screenSize.height - ballRadius * 2
        val minY = screenSize.height * 4 / 5f

        val newX = (ballPosition.x + dx).coerceIn(0f, maxX)
        val newY = (ballPosition.y + dy).coerceIn(minY, maxY - 30f)

        ballPosition = Offset(newX, newY)

        updateFallingObstacles()
    }

    private fun updateFallingObstacles() {
        val updatedObst = fallingObstacle.map { obstracle ->
            obstracle.copy(position = obstracle.position.copy(y = obstracle.position.y + 10f))
        }.filter { obstracle ->
            obstracle.position.y < screenSize.height
        }
        fallingObstacle = updatedObst
        fallingObstacle.forEach { obs ->
            if (isCollision(ballPosition, obs)) {
                gameState = GameState.GameOver
                stopListening()
            }
        }
    }

    private fun startObstacleSpawning() {
        obstacleJob?.cancel()
        obstacleJob = viewModelScope.launch {
            while (isActive && gameState == GameState.Running) {
                delay(spawnSpeed)
                spawnObstacle()
            }
        }
    }

    private fun stopObstacleSpawning() {
        obstacleJob?.cancel()
    }

    private fun increaseSpawnSpeed() {
        speedJob?.cancel()
        speedJob = viewModelScope.launch {
            while (isActive && gameState == GameState.Running) {
                delay(10_000L)

                if (spawnSpeed > 500L) {
                    spawnSpeed -= 100L
                }
            }
        }
    }
    private fun stopIncreaseSpawnSpeed() {
        speedJob?.cancel()
    }

    private fun isCollision(ballPosition: Offset, obstacle: Obstacle): Boolean {
        val bx = ballPosition.x + ballRadius
        val by = ballPosition.y + ballRadius

        val rectLeft = obstacle.position.x
        val rectRight = rectLeft + obstacle.size.width
        val rectTop = obstacle.position.y
        val rectBottom = rectTop + obstacle.size.height

        return bx in rectLeft..rectRight && by in rectTop..rectBottom
    }

    fun spawnObstacle() {
        val obstWidth = 100f
        val obstX = (0..(screenSize.width - obstWidth).toInt()).random().toFloat()
        val obstacle = Obstacle(position = Offset(obstX, 0f))
        fallingObstacle = fallingObstacle + obstacle
    }

    fun startListening() {
        accelerometerSensor.startListening()
        accelerometerSensor.setOnSensorValuesChangedListener { values ->
            x = values[0]
            y = values[1]
        }
        gameState = GameState.Running
        startObstacleSpawning()
        startScoring()
        increaseSpawnSpeed()
    }

    fun stopListening() {
        accelerometerSensor.stopListening()
        updateHighScoreIfNeeded()
        stopObstacleSpawning()
        stopScoring()
        stopIncreaseSpawnSpeed()
    }

    fun resetGame() {
        ballPosition = Offset(
            (screenSize.width - ballRadius * 2) / 2f,
            screenSize.height * 3 / 4f
        )

        fallingObstacle = listOf()
        score = 0
        spawnSpeed = 800L
    }

}

sealed interface GameState {

    data object MainMenu: GameState

    data object PauseMenu: GameState

    data object Running: GameState

    data object GameOver: GameState

    data object HighScore: GameState

}