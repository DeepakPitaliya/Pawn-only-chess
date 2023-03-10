var player1 = ""
var player2 = ""
var whitePawnCount = 8
var blackPawnCount = 8
val whitePawnPositionList = mutableListOf(Pair(1, 0), Pair(1, 1), Pair(1, 2), Pair(1, 3), Pair(1, 4), Pair(1, 5), Pair(1, 6), Pair(1, 7))
val blackPawnPositionList = mutableListOf(Pair(6, 0), Pair(6, 1), Pair(6, 2), Pair(6, 3), Pair(6, 4), Pair(6, 5), Pair(6, 6), Pair(6, 7))
val moveList = mutableListOf<MutableList<Int>>()  // for en passant
var whiteWins = false
var blackWins = false
var isStalemate = false

fun main() {
    // initialize game
    val chessBoard = createInitialBoard()
    println("Pawns-Only Chess")
    println("First Player's name:")
    player1 = readln()
    println("Second Player's name:")
    player2 = readln()
    printChessBoard(chessBoard)
    // play game
    play(chessBoard)
}

fun createInitialBoard(): Array<Array<String>> {
    val chessBoard = Array(8) { Array(8) { "   " } }
    for (i in 0..7) {
        chessBoard[1][i] = " W "
        chessBoard[6][i] = " B "
    }
    return chessBoard
}

fun printChessBoard(chessBoard: Array<Array<String>>) {
    val separator = "  " + "+---".repeat(8) + "+"
    println(separator)
    for (i in 8 downTo 1) {
        print("$i ")
        for (j in 1..8) {
            print("|" + chessBoard[i - 1][j - 1])
        }
        println("|")
        println(separator)
    }
    println("    a   b   c   d   e   f   g   h")
}

fun play(chessBoard: Array<Array<String>>) {
    val validMove = Regex("[a-h][1-8][a-h][1-8]") // to check validity of input

    // start with player1 and loop until win/draw
    var player = player1
    while (true) {
        println("$player's turn:")
        val inputMove = readln()
        if (inputMove == "exit") {
            println("Bye!")
            break
        }
        if (validMove.matches(inputMove)) {
            if (movePawn(chessBoard, inputMove, player)) {
                // after each move check for win or draw
                if (whiteWins) {
                    println("White Wins!\nBye!")
                    break
                }
                if (blackWins) {
                    println("Black Wins!\nBye!")
                    break
                }
                for (pair in if (player == player1) blackPawnPositionList else whitePawnPositionList) {
                    if (canMove(chessBoard, pair, player = if (player == player1) player2 else player1)) break
                    else isStalemate = true
                }
                if (isStalemate) {
                    println("Stalemate!\nBye!")
                    break
                }
                // change player after successful move and not win/draw
                player = if (player == player1) player2 else player1
            }
        } else {
            println("Invalid Input")
        }
    }
}

// move pawn according to input and change properties
fun movePawn(chessBoard: Array<Array<String>>, inputMove: String, player: String): Boolean {
    // convert input to chess board array coordinates
    val row1 = inputMove[1].digitToInt() - 1
    val col1 = inputMove[0].code - 97
    val row2 = inputMove[3].digitToInt() - 1
    val col2 = inputMove[2].code - 97
    // record move to later save in moveList if move is valid
    val move = mutableListOf(row1, col1, row2, col2)

    if (player == player1) {
        if (chessBoard[row1][col1] != " W ") {
            println("No white pawn at ${inputMove.subSequence(0, 2)}")
            return false
        }
        // white pawn forward straight move
        if (
            (row1 == 1 && row2 == 3 && col1 == col2 && chessBoard[row1 + 1][col2] == "   " && chessBoard[row2][col2] == "   ")
            || (row2 - row1 == 1 && col1 == col2 && chessBoard[row2][col2] == "   ")
        ) {
            chessBoard[row1][col1] = "   "
            whitePawnPositionList.remove(Pair(row1, col1))
            chessBoard[row2][col2] = " W "
            whitePawnPositionList.add(Pair(row2, col2))
            printChessBoard(chessBoard)
            if (row2 == 7) whiteWins = true
            moveList.add(move)
            return true
        }
        // white pawn captures black pawn
        if (row2 - row1 == 1 && Math.abs(col2 - col1) == 1 && chessBoard[row2][col2] == " B ") {
            chessBoard[row1][col1] = "   "
            whitePawnPositionList.remove(Pair(row1, col1))
            chessBoard[row2][col2] = " W "
            whitePawnPositionList.add(Pair(row2, col2))
            blackPawnPositionList.remove(Pair(row2, col2))
            blackPawnCount -= 1
            if (blackPawnCount == 0) whiteWins = true
            printChessBoard(chessBoard)
            moveList.add(move)
            return true
        }
        // white pawn captures black en passant
        if (Math.abs(col2 - col1) == 1 && chessBoard[row1][col2] == " B " && moveList.last()[0] - moveList.last()[2] == 2) {
            chessBoard[row1][col1] = "   "
            whitePawnPositionList.remove(Pair(row1, col1))
            chessBoard[row2][col2] = " W "
            whitePawnPositionList.add(Pair(row2, col2))
            chessBoard[row1][col2] = "   "
            blackPawnPositionList.remove(Pair(row1, col2))
            blackPawnCount -= 1
            if (blackPawnCount == 0) whiteWins = true
            printChessBoard(chessBoard)
            moveList.add(move)
            return true
        }
    } else {
        if (chessBoard[row1][col1] != " B ") {
            println("No black pawn at ${inputMove.subSequence(0, 2)}")
            return false
        }
        // black pawn forward straight move
        if (
            (row1 == 6 && row2 == 4 && col1 == col2 && chessBoard[row1 - 1][col2] == "   " && chessBoard[row2][col2] == "   ")
            || (row1 - row2 == 1 && col1 == col2 && chessBoard[row2][col2] == "   ")
        ) {
            chessBoard[row1][col1] = "   "
            blackPawnPositionList.remove(Pair(row1, col1))
            chessBoard[row2][col2] = " B "
            blackPawnPositionList.add(Pair(row2, col2))
            printChessBoard(chessBoard)
            if (row2 == 0) blackWins = true
            moveList.add(move)
            return true
        }
        // white pawn captures black pawn
        if (row1 - row2 == 1 && Math.abs(col2 - col1) == 1 && chessBoard[row2][col2] == " W ") {
            chessBoard[row1][col1] = "   "
            blackPawnPositionList.remove(Pair(row1, col1))
            chessBoard[row2][col2] = " B "
            blackPawnPositionList.add(Pair(row2, col2))
            whitePawnPositionList.remove(Pair(row2, col2))
            whitePawnCount -= 1
            if (whitePawnCount == 0) blackWins = true
            printChessBoard(chessBoard)
            moveList.add(move)
            return true
        }
        // white pawn captures black pawn en passant
        if (Math.abs(col2 - col1) == 1 && chessBoard[row1][col2] == " W " && moveList.last()[2] - moveList.last()[0] == 2) {
            chessBoard[row1][col1] = "   "
            blackPawnPositionList.remove(Pair(row1, col1))
            chessBoard[row2][col2] = " B "
            blackPawnPositionList.add(Pair(row2, col2))
            chessBoard[row1][col2] = "   "
            whitePawnPositionList.remove(Pair(row1, col2))
            whitePawnCount -= 1
            if (whitePawnCount == 0) blackWins = true
            printChessBoard(chessBoard)
            moveList.add(move)
            return true
        }
    }
    println("Invalid Input")
    return false
}

// check if a pawn at a particular position has valid move
fun canMove(chessBoard: Array<Array<String>>, pair: Pair<Int, Int>, player: String): Boolean {
    val row = pair.first
    val col = pair.second
    if (player == player1) {
        if (chessBoard[row + 1][col] == "   ") return true
        else if (col == 0 && chessBoard[row + 1][col + 1] == " B ") return true
        else if (col == 0 && chessBoard[row][col + 1] == " B " && moveList.last()[2] - moveList.last()[0] == 2) return true
        else if (col == 7 && chessBoard[row + 1][col - 1] == " B " ) return true
        else if (col == 7 && chessBoard[row][col - 1] == " B " && moveList.last()[2] - moveList.last()[0] == 2) return true
        else if ((col != 0 && chessBoard[row + 1][col - 1] == " B ") || (col != 7 && chessBoard[row + 1][col + 1] == " B ")) return true
    } else if (player == player2) {
        if (chessBoard[row - 1][col] == "   ") return true
        else if (col == 0 && chessBoard[row - 1][col + 1] == " W ") return true
        else if (col == 0 && chessBoard[row][col + 1] == " W " && moveList.last()[2] - moveList.last()[0] == -2) return true
        else if (col == 7 && chessBoard[row - 1][col - 1] == " W " ) return true
        else if (col == 7 && chessBoard[row][col - 1] == " W " && moveList.last()[2] - moveList.last()[0] == -2) return true
        else if ((col != 0 && chessBoard[row - 1][col - 1] == " W ") || (col != 7 && chessBoard[row - 1][col + 1] == " W ")) return true
    }
    return false
}