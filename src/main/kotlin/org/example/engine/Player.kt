package org.example.org.example.engine

import org.example.org.example.util.ConsoleUtil

class Player(private val playerNumber: Int, playerName: String?) {
    val name = playerName
    var fails = 0
    val playerBoard = PlayerBoard()
    val colorPrefixes = Color.entries.map { it.name.first() }.toList()

    fun printPlayerState() {
        println("---------------------- $name (player $playerNumber)  --------------------------")
        playerBoard.prettyPrint()
        println("        Fails: $fails/4                                Total points|  ${calculateScore()}")
    }

    fun takeTurn(closedColors: Set<Color>, diceThrow: DiceThrow, roundType: RoundType, throwingPlayer: Player): Boolean {
        ConsoleUtil.clearScreen()
        printRoundStatus(closedColors, diceThrow, roundType, throwingPlayer)
        printPlayerState()
        val skippedTurn = parseInputAndUpdateState(closedColors, diceThrow, roundType)
        return skippedTurn
    }

    private fun printRoundStatus(closedColors: Set<Color>, diceThrow: DiceThrow, roundType: RoundType, throwingPlayer: Player) {
        println("Throwing Player ${throwingPlayer.playerNumber}: ${throwingPlayer.name}.")
        println("Numbers thrown: $diceThrow")
        if (closedColors.isNotEmpty()) {
            println("Closed colors: $closedColors")
        }
        println()
        println("$name (player $playerNumber)  DECISION!")
        println()
        if (roundType == RoundType.JOKER) {
            println("JOKER time. $name (player $playerNumber)  can mark ${diceThrow.calculateJoker()} as joker now.")
        } else if (roundType == RoundType.COLOR) {
            println("Player $name, you can mark one of colored dice now. Your options are: ${diceThrow.coloredOptions()}")
        }
    }

    private fun parseInputAndUpdateState(closedColors: Set<Color>, diceThrow: DiceThrow, roundType: RoundType): Boolean {
        val skippedTurn = readUntilValidMove(closedColors, diceThrow, roundType)
        return skippedTurn
    }

    private fun readUntilValidMove(closedColors: Set<Color>, diceThrow: DiceThrow, roundType: RoundType): Boolean {
        var validMove = false
        var skippedTurn = false

        while (!validMove) {
            println("\nWhat number to cross? S=skip X=fail")
            val move = readlnOrNull()
            if (move.equals("s", ignoreCase = true) || move.equals("x", ignoreCase = true)) {
                validMove = true
                skippedTurn = true
            } else if (colorPrefixes.stream().anyMatch { move.toString().startsWith(it, ignoreCase = true) }) {
                val color = Color.entries.stream().filter { move.toString().startsWith(it.name.first(), ignoreCase = true) }.findAny()
                val number = move?.padEnd(3)?.substring(1..2)?.trim()?.toIntOrNull()
                if (color.isPresent && !closedColors.contains(color.get())) {
                    if (number != null) {
                        if (checkIfNumberWasThrown(diceThrow, color.get(), number, roundType)) {
                            if (number in 2..12 && checkBoardForHigherNumbers(color.get(), number)) {
                                if (playerBoard.rows[color.get()]?.closingCheckFailed(number) == false) {
                                    playerBoard.rows[color.get()]?.crossedNumbers?.set(number, true)
                                    validMove = true
                                } else {
                                    println("Cannot close a row until 5 numbers are crossed!")
                                }
                            } else {
                                println("Later number is already checked")
                            }
                        } else {
                            println("This number was not thrown")
                        }
                    } else {
                        println("Number must be between 2 and 12")
                    }
                } else {
                    println("Color is closed!")
                }
            }
        }
        return skippedTurn
    }

    fun calculateScore(): Int {
        return playerBoard.rows.values.stream().mapToInt { it.calculateRowScore() }.sum() - fails * 5
    }

    private fun checkBoardForHigherNumbers(color: Color, number: Int): Boolean {
        return playerBoard.rows[color]?.canCross(number) == true
    }

    private fun checkIfNumberWasThrown(diceThrow: DiceThrow, color: Color, number: Int, roundType: RoundType): Boolean {
        return if (roundType == RoundType.COLOR) diceThrow.coloredOptions().contains(Pair(color, number)) else diceThrow.calculateJoker() == number
    }
}
