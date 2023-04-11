package chess

import kotlin.math.abs

class Board {
    private val board = MutableList(8){ MutableList(8) {"   "} }
    init {
        fillSameValue(1, " B ")
        fillSameValue(6, " W ")
        showChessboard()
    }
    private var lastMove = ""

    fun checkPawnOnSquare(place: String, colorPawn: String): Boolean {
        val square = convertMove(place)
        return board[square[0]][square[1]] == colorPawn
    }

    fun goPawn(move: String, isWhite: Boolean, player1: Player, player2: Player): Boolean {
        return if (move[0] == move[2]) {
            goMove(move, isWhite, player1, player2)
        } else {
            goCapture(move, isWhite, player1, player2)
        }
    }

    fun showChessboard() {
        for (i in 0..board.size) {
            print("  +")
            repeat(8) {
                print("---+")
            }
            if (i == board.size) break
            println("\n${8 - i} |${board[i].joinToString("|")}|")
        }
        print("\n ")
        for (i in 'a'..'h') print("   $i")
        println("")
    }

    fun checkWin(isWhite: Boolean, player: Player): Boolean {
        if (lastMove[3] == (if (isWhite) '8' else '1') || checkNoPawns(isWhite)) {
            println("${if (isWhite) "White" else "Black"} Wins!")
            return true
        }
        return checkStalemate(isWhite, player)
    }

    private fun checkNoPawns(isWhite: Boolean): Boolean {
        var countPawn = 0
        for (i in board.indices) {
            if (board[i].indexOf(if (isWhite) " B " else " W ") != -1) countPawn++
        }
        return countPawn == 0
    }

    private fun checkStalemate(isWhite: Boolean, player: Player): Boolean {
        val movePawns = if (isWhite) -1 else 1
        val pawn = if (isWhite) " W " else " B "
        for (i in player.listPawns) {
            val step = i[1] + movePawns
            if (checkPawnOnSquare("${i[0]}$step", "   ")) return false
            if (i[0] != 'h' && checkPawnOnSquare("${i[0] + 1}$step", pawn)) return false
            if (i[0] != 'a' && checkPawnOnSquare("${i[0] - 1}$step", pawn)) return false
        }
        println("Stalemate!")
        return true
    }

    private fun goMove(move: String, isWhite: Boolean, player1: Player, player2: Player): Boolean {
         try {
             if (!checkPawnOnSquare(move.substring(2), "   ")) throw Exception()
             if (!changePlacePawnToMove(move, isWhite, player1, player2)) {
                 throw Exception()
             }
         } catch (e: Exception) {
             return false
         }
            return true
    }

    private fun changePlacePawnToMove(move: String, isWhite: Boolean, player1: Player, player2: Player): Boolean {
        val startPosition = if (isWhite) '2' else '7'
        val stepPawn = if (isWhite) move[3] - move[1] else move[1] - move[3]
        val isRightMove = if (move[1] == startPosition) stepPawn in 1..2 else stepPawn == 1
            if (isRightMove) {
                changeBoard(move)
                if (isWhite) player1.changeListPawns(move) else player2.changeListPawns(move)
            } else {
                return false
            }
        return true
    }

    private fun goCapture(move: String, isWhite: Boolean, player1: Player, player2: Player): Boolean {
        val passant = move[2].toString() + move[1]
        val stepLastMove = abs(lastMove[3] - lastMove[1])
        val isFirstMove = passant[1] == if (isWhite) '5' else '4'
        try {
            if (!checkPawnOnSquare(move.substring(2), if (isWhite) " B " else " W ")) {
                if (lastMove.substring(2) == passant && stepLastMove == 2 && isFirstMove) {
                    if (!changePlacePawnToCapture(move, isWhite, player1, player2)) throw Exception()
                    val square = convertMove(passant)
                    board[square[0]][square[1]] = "   "
                    if (isWhite) {
                        player2.changeListPawns(passant)
                    } else {
                        player1.changeListPawns(passant)
                    }
                } else {
                    throw Exception()
                }
            } else {
                if (changePlacePawnToCapture(move, isWhite, player1, player2)) {
                    if (isWhite) {
                        player2.changeListPawns(move.substring(2))
                    } else {
                        player1.changeListPawns(move.substring(2))
                    }
                } else {
                    throw Exception()
                }
            }
        } catch (e: Exception) {
            return false
        }
        return true
    }

    private fun changePlacePawnToCapture(move: String, isWhite: Boolean, player1: Player, player2: Player): Boolean {
        val stepPawnNum = if (isWhite) move[3] - move[1] else move[1] - move[3]
        val stepPawnLetter = abs(move[0] - move[2])
        if (stepPawnNum == 1 && stepPawnLetter == 1) {
            changeBoard(move)
            if (isWhite) player1.changeListPawns(move) else player2.changeListPawns(move)
        } else {
            return false
        }
        return true
    }

    private fun changeBoard(move: String) {
        val squareStart = convertMove(move.substring(0,2))
        val squareFinish = convertMove(move.substring(2))
        board[squareFinish[0]][squareFinish[1]] = board[squareStart[0]][squareStart[1]]
        board[squareStart[0]][squareStart[1]] = "   "
        this.lastMove = move
    }

    private fun convertMove(square: String): List<Int> {
        val x = "87654321".indexOf(square[1])
        val y = "abcdefgh".indexOf(square[0])
        return listOf(x, y)
    }

    private fun fillSameValue(rank: Int, pawn: String) {
        for (i in board[rank].indices) board[rank][i] = pawn
    }
}