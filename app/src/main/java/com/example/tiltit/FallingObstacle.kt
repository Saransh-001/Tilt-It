package com.example.tiltit

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.tiltit.viewmodel.ViewModel

@Composable
fun FallingObstacle(
    viewModel: ViewModel
) {
    val obstacles = viewModel.fallingObstacle

    Canvas(modifier = Modifier.fillMaxSize()) {
        obstacles.forEach { obs ->
            drawRect(
                color = Color.Red,
                topLeft = obs.position,
                size = obs.size
            )
        }
    }
}