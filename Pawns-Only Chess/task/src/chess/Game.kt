package chess

import java.lang.IllegalArgumentException

class Game {
    init {
        println("Pawns-Only Chess")
    }
    private val player1 = Player("First")
    private val player2 = Player("Second")
    private val gameBoard = Board()

    fun gameStart() {
        var isPlayer1 = true
        var moveString: String
        while (true) {
            moveString = makeMove(isPlayer1)
            isPlayer1 = !isPlayer1
            if (moveString == "exit") {
                println("Bye!")
                break
            }
        }
    }

    private fun makeMove(isPlayer1: Boolean): String {
        var move: String
        val player = if (isPlayer1) " W " else " B "
        while (true) {
            println("${if (isPlayer1) player1.name else player2.name}'s turn:")
            move = readln()
            if (move == "exit") break
            try {
                if (!equalsRegex(move)) throw Exception()
                if (!gameBoard.checkPawnOnSquare(move.substring(0, 2), player)) throw IllegalArgumentException()
                if (move.substring(0, 2) == move.substring(2)) throw Exception()
                if (gameBoard.goPawn(move, isPlayer1, player1, player2)) {
                    gameBoard.showChessboard()
                    if (gameBoard.checkWin(isPlayer1, if (isPlayer1) player2 else player1)) move = "exit"
                    break
                } else {
                    throw Exception()
                }
            } catch (el: IllegalArgumentException) {
                println("No ${if (isPlayer1) "white" else "black"} pawn at ${move.substring(0, 2)}")
                continue
            } catch (e: Exception) {
                println("Invalid Input")
                continue
            }
        }
        return move
    }

    private fun equalsRegex(text: String): Boolean {
        val regex = Regex("[a-h][1-8][a-h][1-8]")
        return regex.matches(text)
    }
}