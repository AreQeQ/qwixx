package org.example.org.example.engine

import org.example.org.example.engine.Color.*
import java.util.*

class PlayerBoard {
    var rows: Map<Color, Row> = mapOf(
        RED to AscendingRow(),
        YELLOW to AscendingRow(),
        GREEN to DescendingRow(),
        BLUE to DescendingRow(),
    )

    fun prettyPrint() {
        println("______________________________________________________________|SCORE")
        prettyPrintAllRows(rows)
    }

    private fun prettyPrintAllRows(rows: Map<Color, Row>) {
        rows.forEach { (color, row) -> prettyPrintSingleRow(color, row) }
    }

    private fun prettyPrintSingleRow(color: Color, row: Row) {
        val rowScore = row.calculateRowScore().toString()
        println("${color.toString().padEnd(7)}|${row.crossedNumbers.entries.joinToString("|") { b -> if (b.value) "XXXX" else if (!row.canCross(b.key)) "----" else " " + b.key.toString().padEnd(3) }}|  $rowScore")
    }
}

abstract class Row {
    abstract var crossedNumbers: SortedMap<Int, Boolean>
    abstract fun calculateRowScore(): Int
    abstract fun canCross(number: Int): Boolean
    abstract fun closingCheckFailed(number: Int): Boolean
}

class AscendingRow : Row() {
    override var crossedNumbers = (2..12).associateWith { false }.toSortedMap()

    override fun calculateRowScore(): Int {
        var n = crossedNumbers.values.count { it }
        if (crossedNumbers.lastEntry().value == true) n++
        return n * (n + 1) / 2
    }

    override fun canCross(number: Int): Boolean {
        return !crossedNumbers.any { a -> a.key >= number && a.value }
    }

    override fun closingCheckFailed(number: Int): Boolean {
        return number == 12 && crossedNumbers.count { it.value } < 5
    }

}

class DescendingRow : Row() {
    override var crossedNumbers = (12 downTo 2).associateWith { false }.toSortedMap(Comparator.reverseOrder())

    override fun calculateRowScore(): Int {
        var n = crossedNumbers.values.count { it }
        if (crossedNumbers.lastEntry().value == true) n++
        return n * (n + 1) / 2
    }

    override fun canCross(number: Int): Boolean {
        return !crossedNumbers.any { a -> a.key <= number && a.value }
    }

    override fun closingCheckFailed(number: Int): Boolean {
        return number == 2 && crossedNumbers.count { it.value } < 5
    }
}

