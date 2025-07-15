package com.example.tiltit.presentation.components.menus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun MainMenu(
    onStartClick: () -> Unit,
    onHighScoreClick: () -> Unit
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
                    .size(width = 250.dp, height = 300.dp)
                    .background(Color.Gray.copy(alpha = 0.8f))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Main Menu"
                )

                HorizontalDivider(
                    modifier = Modifier.padding(15.dp),
                    thickness = 1.dp,
                    color = Color.Gray
                )
                Button(
                    onClick = { onStartClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "Start")
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { onHighScoreClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = "High Score")
                }
            }
        }
    }
}

@Composable
@Preview
fun MainMenuDialog_Preview() {
    MainMenu(
        onStartClick = {},
        onHighScoreClick = {}
    )
}