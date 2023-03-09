fun main() {
    val chessBoard = createInitialBoard()

    println("Pawns-Only Chess")
    println("First Player's name:")
    player1 = readln()
    println("Second Player's name:")
    player2 = readln()
    printChessBoard(chessBoard)

    play(chessBoard)
}

var player1 = ""
var player2 = ""
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
    val validMove = Regex("[a-h][1-8][a-h][1-8]")
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
                player = if (player == player1) player2 else player1
            }
        }
    }
}
fun movePawn(chessBoard: Array<Array<String>>, inputMove: String, player: String): Boolean {
    val row1 = inputMove[1].digitToInt() - 1
    val col1 = inputMove[0].code - 97
    val row2 = inputMove[3].digitToInt() - 1
    val col2 = inputMove[2].code - 97

    if(player == player1) {
        if (chessBoard[row1][col1] != " W ") {
            println("No white pawn at ${inputMove.subSequence(0, 2)}")
            return false
        }
        if (
            (row1 == 1 && row2 == 3 && col1 == col2 && chessBoard[row1 + 1][col2] == "   " && chessBoard[row2][col2] == "   ")
            || (row2 - row1 == 1 && col1 == col2 && chessBoard[row2][col2] == "   ")
        ) {
            chessBoard[row1][col1] = "   "
            chessBoard[row2][col2] = " W "
            printChessBoard(chessBoard)
            return true
        }
    } else {
        if (chessBoard[row1][col1] != " B ") {
            println("No black pawn at ${inputMove.subSequence(0, 2)}")
            return false
        }
        if (
            (row1 == 6 && row2 == 4 && col1 == col2 && chessBoard[row1 - 1][col2] == "   " && chessBoard[row2][col2] == "   ")
            || (row1 - row2 == 1 && col1 == col2 && chessBoard[row2][col2] == "   ")
        ) {
            chessBoard[row1][col1] = "   "
            chessBoard[row2][col2] = " B "
            printChessBoard(chessBoard)
            return true
        }
    }

    println("Invalid Input")
    return false
}
