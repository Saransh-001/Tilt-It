package com.example.tiltit.presentation

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.example.tiltit.presentation.viewmodel.ViewModel

@Composable
fun Ball(
    viewModel: ViewModel
) {
    val ballRadius = viewModel.ballRadius
    val ballPosition = viewModel.ballPosition

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = Color.Cyan,
            radius = ballRadius,
            center = Offset(
                ballPosition.x + ballRadius,
                ballPosition.y + ballRadius
            )
        )
    }
}