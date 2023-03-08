fun main() {
    val chessBoard = createInitialBoard()

    println("Pawns-Only Chess")
    println("First Player's name:")
    player1 = readln()
    println("Second Player's name:")
    player2 = readln()
    printChessBoard(chessBoard)

    makeMove(chessBoard)
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

fun makeMove(chessBoard: Array<Array<String>>) {
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
            if (player == player1) {
                if (moveWhite(chessBoard, inputMove)) player = player2
            } else {
                if(moveBlack(chessBoard, inputMove)) player = player1
            }
        }
    }
}

//player move input is col,row,col,row whereas chessBoard takes row,col,row,col
// convert player move to game board coordinates e.g. b2b4 = [1, 1, 3, 1]
// corresponding to game board's positions chessBoard[1][1] and chessBoard[3][1]

fun getCoordinates(s: String): Array<Int> {
    return arrayOf(s[1].digitToInt() - 1, s[0].code - 97, s[3].digitToInt() - 1, s[2].code - 97)
}

fun moveWhite(chessBoard: Array<Array<String>>, inputMove: String): Boolean {
    val coord = getCoordinates(inputMove)
    var isValid = false
    if (chessBoard[coord[0]][coord[1]] != " W ") {
        println("No white pawn at ${inputMove.subSequence(0, 2)}")
    } else if(inputMove.subSequence(0, 1) != inputMove.subSequence(2, 3)) {
        println("Invalid Input")
    } else if (coord[0] == 1 && !((coord[2] - coord[0]) == 1 || (coord[2] - coord[0]) == 2)) {
        println("Invalid Input")
    } else if (coord[0] == 1 && chessBoard[2][coord[1]] != "   ") {
        println("Invalid Input")
    } else if (coord[0] > 1 && coord[2] - coord[0] != 1) {
        println("Invalid Input")
    } else if (chessBoard[coord[2]][coord[3]] != "   ") {
        println("Invalid Input")
    } else {
        chessBoard[coord[0]][coord[1]] = "   "
        chessBoard[coord[2]][coord[3]] = " W "
        printChessBoard(chessBoard)
        isValid = true
    }
    return isValid
}

fun moveBlack(chessBoard: Array<Array<String>>, inputMove: String): Boolean {
    val coord = getCoordinates(inputMove)
    var isValid = false
    if (chessBoard[coord[0]][coord[1]] != " B ") {
        println("No black pawn at ${inputMove.subSequence(0, 2)}")
    } else if(inputMove.subSequence(0, 1) != inputMove.subSequence(2, 3)) {
        println("Invalid Input")
    } else if (coord[0] == 6 && !((coord[0] - coord[2]) == 1 || (coord[0] - coord[2]) == 2)) {
        println("Invalid Input")
    } else if (coord[0] == 6 && chessBoard[5][coord[1]] != "   ") {
        println("Invalid Input")
    } else if (coord[0] < 6 && coord[0] - coord[2] != 1) {
        println("Invalid Input")
    } else if (chessBoard[coord[2]][coord[3]] != "   ") {
        println("Invalid Input")
    } else {
        chessBoard[coord[0]][coord[1]] = "   "
        chessBoard[coord[2]][coord[3]] = " B "
        printChessBoard(chessBoard)
        isValid = true
    }
    return isValid
}