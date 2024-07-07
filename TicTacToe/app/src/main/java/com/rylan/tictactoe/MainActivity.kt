package com.rylan.tictactoe

import GameScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rylan.tictactoe.models.GameViewModel
import com.rylan.tictactoe.screens.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TicTacToeApp()
        }
    }
}

@Composable
fun TicTacToeApp() {
    val navController = rememberNavController()

    val gameViewModel = remember { GameViewModel() }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController = navController, gameViewModel = gameViewModel) }
        composable("game") { GameScreen(navController = navController, gameViewModel = gameViewModel) }
    }
}
