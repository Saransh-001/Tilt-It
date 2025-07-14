package com.example.tiltit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tiltit.viewmodel.ViewModel
import kotlinx.coroutines.isActive

@Composable
fun MainScreen(
    viewModel: ViewModel
) {
    val ballRadius = viewModel.ballRadius
    val ballPosition = viewModel.ballPosition


    LaunchedEffect(viewModel.isRunning) {
        while (isActive && viewModel.isRunning && viewModel.ballPosition != Offset.Zero) {
            withFrameNanos {
                viewModel.updateBallPosition()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Blue)
            .onGloballyPositioned {
                val size = it.size
                viewModel.screenSize = Size(size.width.toFloat(), size.height.toFloat())

                if (ballPosition == Offset.Zero) {
                    viewModel.ballPosition = Offset(
                        (size.width - ballRadius * 2) / 2f,
                        size.height * 3 / 4f
                    )
                }
            }
    ) {
        if (viewModel.showMenu) {
            MenuDialog(
                onStart = {
                    viewModel.startListening()
                    viewModel.showMenu = false
                },
                onStop = { viewModel.stopListening() }
            )
        }
        PauseButton(
            onClick = {
                viewModel.showMenu = true
                viewModel.isRunning = false
            }, modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        )
        Ball(viewModel = viewModel)
        FallingObstacle(viewModel)
    }
}

@Composable
fun MenuDialog(
    onStart: () -> Unit,
    onStop: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Card {
            Column(
                modifier = Modifier
                    .background(Color.Gray.copy(alpha = 0.2f))
                    .padding(16.dp)
                    .height(300.dp)
                    .width(200.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Button(
                    onClick = { onStart() },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Start")
                }
                Button(
                    onClick = { onStop() },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Stop")
                }
            }
        }
    }
}

@Composable
fun PauseButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.PlayArrow,
            contentDescription = "Pause",
            modifier = Modifier
                .size(48.dp)
        )
    }
}


@Composable
@Preview
fun MenuDialog_Preview() {
    MenuDialog(
        onStart = {},
        onStop = {}
    )
}

