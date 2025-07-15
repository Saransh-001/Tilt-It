package com.example.tiltit.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tiltit.presentation.components.PauseButton
import com.example.tiltit.presentation.components.menus.MainMenu
import com.example.tiltit.presentation.components.menus.GameOverMenuDialog
import com.example.tiltit.presentation.components.menus.HighScoreMenuDialog
import com.example.tiltit.presentation.components.menus.PauseMenuDialog
import com.example.tiltit.presentation.viewmodel.GameState
import com.example.tiltit.presentation.viewmodel.ViewModel
import kotlinx.coroutines.isActive

@Composable
fun MainScreen(
    viewModel: ViewModel
) {
    val ballRadius = viewModel.ballRadius
    val ballPosition = viewModel.ballPosition
    val score = viewModel.score

    LaunchedEffect(viewModel.gameState) {
        while (isActive && viewModel.gameState == GameState.Running && viewModel.ballPosition != Offset.Zero) {
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
        when (viewModel.gameState) {
            GameState.MainMenu -> {
                MainMenu(
                    onStartClick = {
                        viewModel.startListening()
                        viewModel.resetGame()
                        viewModel.gameState = GameState.Running
                    },
                    onHighScoreClick = {
                        viewModel.gameState = GameState.HighScore
                    }
                )
            }

            GameState.PauseMenu -> {
                PauseMenuDialog(
                    onResumeClick = {
                        viewModel.startListening()
                        viewModel.gameState = GameState.Running
                    },
                    onQuitClick = {
                        viewModel.gameState = GameState.MainMenu
                    },
                    onRestartClick = {
                        viewModel.gameState = GameState.Running
                        viewModel.startListening()
                        viewModel.resetGame()
                    }
                )
            }

            GameState.Running -> {
                PauseButton(
                    onClick = {
                        viewModel.gameState = GameState.PauseMenu
                        viewModel.stopListening()
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                )

                Text(
                    text = "${score}",
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 100.dp),
                    fontSize = 50.sp,
                    color = Color.White
                )

                Ball(viewModel = viewModel)
                FallingObstacle(viewModel)
            }

            GameState.GameOver -> {
                GameOverMenuDialog(
                    onMainMenuClick = {
                        viewModel.gameState = GameState.MainMenu
                    },
                    onRetryClick = {
                        viewModel.gameState = GameState.Running
                        viewModel.startListening()
                        viewModel.resetGame()
                    },
                    score = score
                )
            }

            GameState.HighScore -> {
                HighScoreMenuDialog(
                    onMainMenuClick = {
                        viewModel.gameState = GameState.MainMenu
                    },
                    highScore = viewModel.highScore
                )
            }
        }
    }
}






