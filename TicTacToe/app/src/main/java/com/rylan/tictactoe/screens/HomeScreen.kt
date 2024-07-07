package com.rylan.tictactoe.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rylan.tictactoe.models.GameViewModel

@Composable
fun HomeScreen(navController: NavHostController, gameViewModel: GameViewModel) {
    var playerXName by remember { mutableStateOf("") }
    var playerOName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Tic-Tac-Toe 1v1",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // Player X
        OutlinedTextField(
            value = playerXName,
            onValueChange = { playerXName = it },
            label = { Text("Player X Name", fontWeight = FontWeight.Bold, color = Color.Red) },
            singleLine = true,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        // Player O
        OutlinedTextField(
            value = playerOName,
            onValueChange = { playerOName = it },
            label = { Text("Player O Name", fontWeight = FontWeight.Bold, color = Color.Gray) },
            singleLine = true,
            modifier = Modifier.padding(bottom = 40.dp)
        )

        Button(
            onClick = {
                gameViewModel.updatePlayerXName(playerXName)
                gameViewModel.updatePlayerOName(playerOName)
                navController.navigate("game")
            },
            modifier = Modifier
                .padding(16.dp)
                .height(IntrinsicSize.Min), // Ensure button's height matches the text
            shape = RoundedCornerShape(percent = 50),
            colors = ButtonDefaults.buttonColors(contentColor = Color.Blue)
        ) {
            Text(
                "Start Game",
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.White
            )
        }


    }
}
