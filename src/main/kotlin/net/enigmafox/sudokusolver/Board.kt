package net.enigmafox.sudokusolver

const val NUM_ROWS = 9

enum class Direction {
    FORWARD,
    BACKWARD,
}

// TODO !!! doc
class Board {

    data class Cell(
        val value: Int,
        val isLocked: Boolean
    )

    private val contents: MutableList<MutableList<Cell>>

    init {
        val tempContents = mutableListOf<MutableList<Cell>>()
        for (r in 0 until NUM_ROWS) {
            tempContents += Array(NUM_ROWS) { Cell(0, false) }.toMutableList()
        }
        contents = tempContents
    }

    override fun toString(): String {
        val builder = StringBuilder()
        contents.forEach { row ->
            builder.append(row
                .map { it.value }
                .joinToString(", ")).append("\n")
        }
        return builder.toString().trim()
    }

    fun populate(board: List<List<Int>>): Board {
        board.forEachIndexed { rowIndex: Int, row: List<Int> ->
            row.forEachIndexed { colIndex: Int, value: Int ->
                contents[rowIndex][colIndex] = Cell(value, true)
            }
        }
        return this
    }

    // TODO !!! test
    fun getNextUnlockedCell(curRow: Int, curCol: Int, direction: Direction): Pair<Int, Int> {
        var row = curRow
        var col = curCol

        if (direction == Direction.FORWARD) {
            while (row < NUM_ROWS) {
                col++
                if (col == NUM_ROWS) {
                    row++
                    col = 0
                    if (row == NUM_ROWS) {
                        row = NUM_ROWS
                        col = NUM_ROWS
                        break
                    }
                }
                if (row < NUM_ROWS && !contents[row][col].isLocked) {
                    break
                }
            }
        } else { // Direction.BACKWARD
            while (0 <= row) {
                col--
                if (col < 0) {
                    row--
                    col = NUM_ROWS - 1
                    if (row < 0) {
                        row = -1
                        col = -1
                    }
                    if (0 <= row && !contents[row][col].isLocked) {
                        break
                    }
                }
            }
        }

        return Pair(row, col)
    }

    fun getCell(row: Int, col: Int) = contents[row][col].value

    fun setCell(row: Int, col: Int, value: Int) {
        contents[row][col] = Cell(value, false)
    }

    fun isValid() = areRowsValid() && areColsValid() && areSubboardsValid()

    private fun areRowsValid(): Boolean {
        for (r in 0 until NUM_ROWS) {
            val rowIsNotValid = contents[r]
                .map { it.value }
                .filter { it > 0 }
                .groupingBy { it }
                .eachCount()
                .filterValues { it > 1 }
                .isNotEmpty()
            if (rowIsNotValid) {
                return false
            }
        }
        return true
    }

    private fun areColsValid(): Boolean {
        for (c in 0 until NUM_ROWS) {
            val encountered = mutableSetOf<Int>()
            for (r in 0 until NUM_ROWS) {
                val colsAreValid = contents[r][c].takeIf { it.value > 0 }
                    .run {
                        when {
                            this == null ->
                                // value in board was 0
                                true
                            value in encountered ->
                                // already encountered this value
                                false
                            else -> {
                                // first time encountering this value
                                encountered += value
                                true
                            }
                        }
                    }
                if (!colsAreValid) {
                    return false
                }
            }
        }
        return true
    }

    private fun areSubboardsValid(): Boolean {
        for (r in 0..2) {
            for (c in 0..2) {
                if (!isSubboardValid(r, c)) {
                    return false
                }
            }
        }
        return true
    }

    private fun isSubboardValid(rowOffset: Int, colOffset: Int): Boolean {
        val minRow = rowOffset * 3
        val maxRow = minRow + 3
        val minCol = colOffset * 3
        val maxCol = minCol + 3

        val encountered = mutableSetOf<Int>()
        for (r in minRow until maxRow) {
            for (c in minCol until maxCol) {
                val value = contents[r][c].value
                if (value > 0) {
                    if (value in encountered) {
                        return false
                    }
                    encountered += value
                }
            }
        }

        return true
    }

}
