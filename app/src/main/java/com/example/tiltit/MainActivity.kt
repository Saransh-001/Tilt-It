package com.example.tiltit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tiltit.ui.theme.TiltItTheme
import com.example.tiltit.viewmodel.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TiltItTheme {
                val viewModel: ViewModel = hiltViewModel()
                MainScreen(viewModel = viewModel)
            }
        }
    }
}
