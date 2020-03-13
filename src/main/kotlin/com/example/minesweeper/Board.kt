package com.example.minesweeper

import org.slf4j.LoggerFactory

class Board {
    private val log = LoggerFactory.getLogger(this::class.java)
    var grid: MutableMap<Position, Cell>

    constructor(gridSize: Int, numberOfMines: Int) {
        this.grid = generateGrid(gridSize)
            .apply {
                generateMinePositions(this.keys, numberOfMines)
                    .forEach { minePosition -> this[minePosition] = Cell(hasMine = true) }
            }
    }

    fun revealPosition(position: Position) = position
        .apply { log.trace("Revealing position {}", position) }
        .run {
            when (hasMine(this, this@Board.grid)) {
                true -> GameState.LOST
                false -> {
                    grid[position]?.apply { revealed = true }
                    grid.values
                        .count { cell -> cell.revealed || cell.hasMine }
                        .run { if (this >= grid.size) GameState.WON else GameState.IN_PROGRESS }
                }
            }
        }

    companion object {
        fun isRevealed(
            position: Position,
            grid: Map<Position, Cell>
        ) = grid[position]?.revealed ?: false

        fun hasMine(
            position: Position,
            grid: Map<Position, Cell>
        ) = grid[position]?.hasMine ?: false

        fun getNeighbours(
            position: Position,
            grid: Map<Position, Cell>
        ) = grid
            .filterKeys { it != position }
            .filterKeys { it.x >= position.x - 1 && it.x <= position.x + 1 }
            .filterKeys { it.y >= position.y - 1 && it.y <= position.y + 1 }

        fun generateMinePositions(
            positions: Set<Position>,
            minesToPlace: Int
        ) = positions
            .shuffled()
            .subList(0, minesToPlace) //TODO handle more mines that positions + negative minesToPlace

        fun generateGrid(gridSize: Int): MutableMap<Position, Cell> {
            val cells = mutableMapOf<Position, Cell>()
            var row = 0
            while (row < gridSize) {
                var column = 0
                while (column < gridSize) {
                    cells[Position(row, column)] = Cell()
                    ++column
                }
                ++row
            }

            return cells
        }
    }
}

enum class GameState {
    IN_PROGRESS,
    WON,
    LOST
}

data class Cell(
    var revealed: Boolean = false,
    val hasMine: Boolean = false)

data class Position(val x: Int, val y: Int) {
    override fun equals(other: Any?): Boolean {
        return when (other?.javaClass == javaClass) {
            true -> {
                var x = other as Position
                return x.x === this.x && x.y === this.y
            }
            false -> false
        }
    }
}