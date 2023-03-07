fun main() {
    printChessBoard()
    makeMove()
}

var player1 = ""
var player2 = ""
fun printChessBoard() {
    val separator = "  " + "+---".repeat(8) + "+"
    val arr = Array(8) { Array(8) { "   " } }
    for (i in 0..7) {
        arr[1][i] = " W "
        arr[6][i] = " B "
    }
    println("Pawns-Only Chess")
    println("First Player's name:")
    player1 = readln()
    println("Second Player's name:")
    player2 = readln()
    println(separator)
    for (i in 8 downTo 1) {
        print("$i ")
        for (j in 8 downTo 1) {
            print("|" + arr[i - 1][j - 1])
        }
        println("|")
        println(separator)
    }
    println("    a   b   c   d   e   f   g   h")
}

fun makeMove() {
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
            player = if (player == player1) player2 else player1
        } else {
            println("Invalid Input")
        }
    }
}