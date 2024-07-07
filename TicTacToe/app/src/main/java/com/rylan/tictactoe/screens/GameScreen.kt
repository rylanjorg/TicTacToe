import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.rylan.tictactoe.models.CellValue
import com.rylan.tictactoe.models.GameStatus
import com.rylan.tictactoe.models.GameViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GameScreen(navController: NavHostController, gameViewModel: GameViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                PlayerTurnDisplay(gameViewModel)
                Board(gameViewModel)
                UndoButton(gameViewModel)
                Scoreboard(gameViewModel)


                if (showDialog) {
                    EndGameDialog(navController = navController, viewModel = gameViewModel) {
                        showDialog = false
                    }
                }
            }
        }
    )

    if (gameViewModel.getGameStatus() != GameStatus.IN_PROGRESS) {
        showDialog = true
    }
}

@Composable
fun UndoButton(gameViewModel: GameViewModel) {
    Button(
        onClick = {
            gameViewModel.undoMove()
        },
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults.buttonColors(contentColor = Color.Blue)
    ) {
        Text(
            "Undo Move",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Composable
fun PlayerTurnDisplay(gameViewModel: GameViewModel) {
    val currentPlayer = gameViewModel.getCurrentPlayer()
    val playerName = if (currentPlayer == CellValue.X) gameViewModel.playerXName.value else gameViewModel.playerOName.value
    val text = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = if (currentPlayer == CellValue.X) Color.Red else Color.Gray,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp
            )
        ) {
            append("$playerName's turn")
        }
    }

    Text(
        text = text,
        fontSize = 20.sp,
        modifier = Modifier.padding(bottom = 16.dp)
    )
}

@Composable
fun Scoreboard(gameViewModel: GameViewModel) {
    Column(
        modifier = Modifier.padding(top = 16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Score:",
            fontWeight = FontWeight.Bold,
            fontSize = 36.sp,
            color = Color.Black
        )
        Text(
            text = "${gameViewModel.playerXName.value}: ${gameViewModel.getXWins()}",
            fontSize = 24.sp,
            color = Color.Red,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${gameViewModel.playerOName.value}: ${gameViewModel.getOWins()}",
            fontSize = 24.sp,
            color = Color.Gray,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun Board(gameViewModel: GameViewModel) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        for (i in gameViewModel.getBoard().indices) {
            Row {
                for (j in gameViewModel.getBoard()[i].indices) {
                    Cell(
                        value = gameViewModel.getBoard()[i][j],
                        onCellClick = { gameViewModel.makeMove(i, j) }
                    )
                }
            }
        }
    }
}

@Composable
fun Cell(value: CellValue, onCellClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .border(
                width = 1.dp,
                color = Color.Black
            )
            .background(getBackgroundColor(value))
            .clickable {
                onCellClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (value) {
                CellValue.X -> "X"
                CellValue.O -> "O"
                else -> ""
            },
            fontSize = 64.sp,
            color = Color.Black,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun getBackgroundColor(value: CellValue): Color {
    return when (value) {
        CellValue.X -> Color.Red
        CellValue.O -> Color.Gray
        else -> Color.Transparent
    }
}

@Composable
fun EndGameDialog(navController: NavHostController, viewModel: GameViewModel, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = "Game Over")
        },
        text = {
            Text(
                text = when (viewModel.getGameStatus()) {
                    GameStatus.X_WINS -> "${viewModel.playerXName.value} wins!"
                    GameStatus.O_WINS -> "${viewModel.playerOName.value} wins!"
                    GameStatus.DRAW -> "It's a draw!"
                    else -> ""
                }
            )
        },
        confirmButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = {
                        viewModel.resetGame()
                        onDismiss()
                    }
                ) {
                    Text("Play Again")
                }
                Button(
                    onClick = {
                        viewModel.resetGame()
                        viewModel.resetScore()
                        onDismiss()
                        navController.navigate("home")
                    }
                ) {
                    Text("Home")
                }
            }
        }
    )
}
