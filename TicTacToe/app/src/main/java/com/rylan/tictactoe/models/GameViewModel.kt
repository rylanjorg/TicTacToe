package com.rylan.tictactoe.models
import androidx.compose.runtime.mutableStateOf

class GameViewModel {
    private val boardSize = 3
    private val board = mutableStateOf(Array(boardSize) { Array(boardSize) { CellValue.EMPTY } })
    private var currentPlayer = mutableStateOf(CellValue.X)
    private var gameStatus = mutableStateOf(GameStatus.IN_PROGRESS)
    private var xWins = mutableStateOf(0)
    private var oWins = mutableStateOf(0)

    var playerXName = mutableStateOf("")
    var playerOName = mutableStateOf("")

    fun updatePlayerXName(name: String) {
        playerXName.value = name
    }
    fun updatePlayerOName(name: String) {
        playerOName.value = name
    }

    fun undoMove() {
        for (i in boardSize - 1 downTo 0) {
            for (j in boardSize - 1 downTo 0) {
                if (board.value[i][j] != CellValue.EMPTY) {
                    board.value[i][j] = CellValue.EMPTY
                    currentPlayer.value = if (currentPlayer.value == CellValue.X) CellValue.O else CellValue.X
                    // update UI
                    board.value = board.value.copyOf()
                    return
                }
            }
        }
    }

    init {
        resetGame()
    }

    fun resetGame() {
        for (i in 0 until boardSize) {
            for (j in 0 until boardSize) {
                board.value[i][j] = CellValue.EMPTY
            }
        }
        currentPlayer.value = CellValue.X
        gameStatus.value = GameStatus.IN_PROGRESS
    }

    fun makeMove(row: Int, col: Int) {
        if (gameStatus.value != GameStatus.IN_PROGRESS || board.value[row][col] != CellValue.EMPTY) {
            return
        }

        board.value[row][col] = currentPlayer.value

        if (checkWin(row, col)) {
            gameStatus.value = if (currentPlayer.value == CellValue.X) GameStatus.X_WINS else GameStatus.O_WINS
            if (currentPlayer.value == CellValue.X) {
                xWins.value++
            } else {
                oWins.value++
            }
        } else if (checkDraw()) {
            gameStatus.value = GameStatus.DRAW
        } else {
            currentPlayer.value = if (currentPlayer.value == CellValue.X) CellValue.O else CellValue.X
        }

        // update UI
        board.value = board.value.copyOf()
    }

    private fun checkWin(row: Int, col: Int): Boolean {
        // Check row
        if (board.value[row].all { it == currentPlayer.value }) return true

        // Check column
        if ((0 until boardSize).all { board.value[it][col] == currentPlayer.value }) return true

        // Check diagonal
        if ((0 until boardSize).all { board.value[it][it] == currentPlayer.value }) return true
        if ((0 until boardSize).all { board.value[it][boardSize - 1 - it] == currentPlayer.value }) return true

        return false
    }

    private fun checkDraw(): Boolean {
        return board.value.all { row -> row.all { it != CellValue.EMPTY } }
    }

    fun getBoard(): Array<Array<CellValue>> {
        return board.value
    }

    fun getCurrentPlayer(): CellValue {
        return currentPlayer.value
    }

    fun getGameStatus(): GameStatus {
        return gameStatus.value
    }

    fun getXWins(): Int {
        return xWins.value
    }

    fun getOWins(): Int {
        return oWins.value
    }

    fun resetScore() {
        oWins.value = 0
        xWins.value = 0
    }

}

enum class CellValue {
    X, O, EMPTY
}

enum class GameStatus {
    IN_PROGRESS, X_WINS, O_WINS, DRAW
}
