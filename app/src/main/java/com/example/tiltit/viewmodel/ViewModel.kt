package com.example.tiltit.viewmodel

import android.R.attr.radius
import android.R.attr.x
import android.R.attr.y
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.ViewModel
import com.example.tiltit.Obstacle
import com.example.tiltit.accelerometer.baseclass.MeasurableSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val accelerometerSensor: MeasurableSensor
): ViewModel() {

    var isRunning by mutableStateOf(false)

    var x by mutableFloatStateOf(0f)
    var y by mutableFloatStateOf(0f)
    var showMenu by mutableStateOf(true)

    val ballRadius = 125f
    var screenSize by mutableStateOf(Size.Zero)
    var ballPosition by mutableStateOf(Offset.Zero)

    var fallingObstacle by mutableStateOf(listOf<Obstacle>())
        private set

    private var obstacleSpawnTimer = 0L

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
                isRunning = false
                showMenu = true
            }
        }

        val currTime = System.currentTimeMillis()
        if (currTime - obstacleSpawnTimer > 800) {
            spawnObstacle()
            obstacleSpawnTimer = currTime
        }
    }

    private fun isCollision(ball: Offset, obstacle: Obstacle): Boolean {
        val bx = ball.x + ballRadius
        val by = ball.y + ballRadius

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
        isRunning = true
    }

    fun stopListening() {
        accelerometerSensor.stopListening()
        isRunning = false
    }

}