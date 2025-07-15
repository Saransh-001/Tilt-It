package com.example.tiltit.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size

data class Obstacle(
    var position: Offset,
    val size: Size = Size(100f, 100f)
)