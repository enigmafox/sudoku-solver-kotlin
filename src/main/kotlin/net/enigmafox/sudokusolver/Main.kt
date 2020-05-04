package net.enigmafox.sudokusolver

fun main(args: Array<String>) {
    println("Result: " + solve(Board()))
}

fun solve(board: Board): Direction {
    val nextUnlockedCell = board.getNextUnlockedCell(-1, -1, Direction.FORWARD)
    val finalDirection = solveRecur(board, nextUnlockedCell.first, nextUnlockedCell.second)
    if (Direction.FORWARD == finalDirection) {
        // successfully solved the puzzle
    } else {
        // unsolvable puzzle
    }
    return finalDirection
}

private fun solveRecur(board: Board, row: Int, col: Int): Direction {
    var value = board.getCell(row, col)
    if (value == NUM_ROWS) {
        // Out of options for this cell.
        // Reset cell to 0 and take step back.
        board.setCell(row, col, 0)
        return Direction.BACKWARD
    }

    // Still values in the current cell that can be tested.
    var boardIsValid = board.isValid()
    while (!boardIsValid && (++value) < NUM_ROWS) {
        // While the board is valid and there is still a value that can be tested...
        board.setCell(row, col, value)
        boardIsValid = board.isValid()
    }

    return if (value <= NUM_ROWS) {
        // Found a value that makes the value valid.
        val nextUnlockedCell = board.getNextUnlockedCell(row, col, Direction.FORWARD)
        if (nextUnlockedCell.first < NUM_ROWS) { // move forward by one cell
            val dir = solveRecur(board, nextUnlockedCell.first, nextUnlockedCell.second)
            if (dir == Direction.FORWARD) { // ran off bottom of board - puzzle is solved
                dir
            } else { // need to take step back
                val lastUnlockedCell = board.getNextUnlockedCell(row, col, Direction.BACKWARD)
                if (lastUnlockedCell.first < 0) { // ran off top of board - unsolvable
                    Direction.BACKWARD
                } else { // step back and try last cell
                    solveRecur(board, lastUnlockedCell.first, lastUnlockedCell.second)
                }
            }
        } else { // ran off bottom of board - puzzle is solved
            Direction.FORWARD
        }
    } else {
        // Ran out of values that make this cell work.
        // Reset cell to zero and take a step backwards.
        board.setCell(row, col, 0)
        Direction.BACKWARD
    }
}

