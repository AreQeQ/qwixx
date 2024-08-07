package org.example.org.example.engine

private const val MAX_FAILS = 4
private const val MAX_CLOSED_COLORS = 2

class GameLoop {
    var players: ArrayList<Player> = ArrayList()
    private var round: Int = 0
    private var numberOfPlayers: Int = 0
    private var closedColors: MutableSet<Color> = HashSet()


    fun start(numberOfPlayers: Int) {
        initialize(numberOfPlayers)

        while (!endConditionTriggered()) {
            players.forEach { throwingPlayer ->
                val diceThrow = DiceThrow()
                val activePlayerSkippedJokerTurn = players.map { Pair(it, it.takeTurn(closedColors, diceThrow, RoundType.JOKER, throwingPlayer)) }.first { it.first == throwingPlayer }.second
                checkForClosedColors()

                val activePlayerSkippedColorTurn = throwingPlayer.takeTurn(closedColors, diceThrow, RoundType.COLOR, throwingPlayer)
                checkForClosedColors()

                if(activePlayerSkippedJokerTurn && activePlayerSkippedColorTurn){
                    throwingPlayer.fails++
                }
            }
        }
    }

    private fun checkForClosedColors() {
        Color.entries.forEach { color -> if (players.any { it.playerBoard.rows.get(color)?.crossedNumbers?.lastEntry()?.value == true }) closedColors.add(color) }
    }

    private fun initialize(numberOfPlayers: Int) {
        this.round = 0
        this.numberOfPlayers = numberOfPlayers

        players.clear()
        for (playerNumber in 1..numberOfPlayers) {
            println("Enter Player $playerNumber name: ")
            val playerName = readlnOrNull()
            players.add(Player(playerNumber, playerName))
        }
    }

    private fun endConditionTriggered(): Boolean {
        return players.any { it.fails >= MAX_FAILS } || closedColors.size >= MAX_CLOSED_COLORS
    }

}
