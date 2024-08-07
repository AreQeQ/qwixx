package org.example.org.example.engine

import org.example.org.example.util.ConsoleUtil

class GameEngine {

    private val gameLoop: GameLoop = GameLoop()

    fun startGame() {
        val numberOfPlayers = readNumberOfPlayers()
        gameLoop.start(numberOfPlayers)
        ConsoleUtil.clearScreen()
        println("Final Scores:")
        gameLoop.players.forEach { it.printPlayerState() }
        println()
        println("Player ${gameLoop.players.maxBy { it.calculateScore() }.name} WON! Congratulations!")
        while (true) {
            readln()
            println("Congratulations!")
        }
    }

    private fun readNumberOfPlayers(): Int {
        var number: Int? = null

        while (number == null || number !in 1..4) {
            print("Please enter a number of players (1-4): ")
            val input = readln()
            number = input.toIntOrNull()
        }
        return number
    }

}

