package org.example.org.example.engine

import org.example.org.example.engine.Color.*
import kotlin.random.Random

class DiceThrow {

    private val whites = Array(2) { Random.nextInt(1, 7) }

    private val red = Random.nextInt(1, 7)
    private val yellow = Random.nextInt(1, 7)
    private val green = Random.nextInt(1, 7)
    private val blue = Random.nextInt(1, 7)

    fun calculateJoker() = whites[0] + whites[1]

    fun coloredOptions() = listOf(
        Pair(RED, whites[0] + red),
        Pair(RED, whites[1] + red),
        Pair(YELLOW, whites[0] + yellow),
        Pair(YELLOW, whites[1] + yellow),
        Pair(GREEN, whites[0] + green),
        Pair(GREEN, whites[1] + green),
        Pair(BLUE, whites[0] + blue),
        Pair(BLUE, whites[1] + blue)
    )

    override fun toString(): String {
        return "Whites=${whites.contentToString()}, Red=$red, Yellow=$yellow, Blue=$blue, Green=$green"
    }

}

