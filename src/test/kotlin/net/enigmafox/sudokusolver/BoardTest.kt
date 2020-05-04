package net.enigmafox.sudokusolver

import kotlin.test.*

class BoardTest {

    private lateinit var board: Board

    @BeforeTest
    fun init() {
        board = Board()
    }

    @Test
    fun `assert populate() populates the board`() {
        val elements = listOf(
            listOf(4, 6, 1, 5, 9, 8, 7, 3, 2),
            listOf(4, 2, 8, 9, 7, 5, 3, 6, 1),
            listOf(3, 8, 7, 5, 9, 2, 4, 6, 1),
            listOf(7, 6, 5, 4, 3, 9, 1, 2, 8),
            listOf(3, 1, 7, 4, 2, 8, 9, 6, 5),
            listOf(2, 4, 1, 7, 9, 3, 6, 5, 8),
            listOf(5, 6, 3, 2, 1, 8, 4, 7, 9),
            listOf(5, 4, 8, 3, 6, 7, 2, 1, 9),
            listOf(4, 9, 8, 1, 2, 7, 5, 3, 6)
        )
        val expectedString = "4, 6, 1, 5, 9, 8, 7, 3, 2\n" +
                "4, 2, 8, 9, 7, 5, 3, 6, 1\n" +
                "3, 8, 7, 5, 9, 2, 4, 6, 1\n" +
                "7, 6, 5, 4, 3, 9, 1, 2, 8\n" +
                "3, 1, 7, 4, 2, 8, 9, 6, 5\n" +
                "2, 4, 1, 7, 9, 3, 6, 5, 8\n" +
                "5, 6, 3, 2, 1, 8, 4, 7, 9\n" +
                "5, 4, 8, 3, 6, 7, 2, 1, 9\n" +
                "4, 9, 8, 1, 2, 7, 5, 3, 6"

        assertEquals(expectedString, board.populate(elements).toString())
    }

    @Test
    fun `assert isValid() returns true given a valid row configuration`() {
        // all zero board
        assertTrue { board.isValid() }

        // single value in the row
        board.setCell(2, 5, 9)
        assertTrue { board.isValid() }

        // fully populated row
        var i = NUM_ROWS
        val values = generateSequence { (i--).takeIf { i >= 0 } }
            .toList()
            .shuffled()
        for (x in 0 until NUM_ROWS) {
            board.setCell(2, x, values[x])
        }
        assertTrue { board.isValid() }
    }

    @Test
    fun `assert isValid() returns false given an invalid row configuration`() {
        // same value appears twice in same row
        board.setCell(2, 1, 1)
        board.setCell(2, 2, 1)

        assertFalse { board.isValid() }
    }

    @Test
    fun `assert isValid() returns true given a valid col configuration`() {
        var i = NUM_ROWS
        val values = generateSequence { (i--).takeIf { i >= 0 } }
            .toList()
            .shuffled()
        for (x in 0 until NUM_ROWS) {
            board.setCell(x, 2, values[x])
        }
        assertTrue { board.isValid() }
    }

    @Test
    fun `assert isValid() returns false given an invalid col configuration`() {
        // same value appears twice in same row
        board.setCell(2, 1, 1)
        board.setCell(1, 1, 1)

        assertFalse { board.isValid() }
    }

    @Test
    fun `assert isValid() returns true given a valid subboard configuration`() {
        var i = NUM_ROWS
        val values = generateSequence { (i--).takeIf { i >= 0 } }
            .toList()
            .shuffled()

        i = 0
        for (r in 3..5) {
            for (c in 3..5) {
                board.setCell(r, c, values[i++])
            }
        }
        assertTrue { board.isValid() }
    }

    @Test
    fun `assert isValid() returns false given an invalid subboard configuration`() {
        // same value appears twice in same row
        board.setCell(3, 3, 1)
        board.setCell(5, 5, 1)

        assertFalse { board.isValid() }
    }
}
