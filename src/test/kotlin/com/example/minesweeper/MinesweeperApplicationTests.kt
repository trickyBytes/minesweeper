package com.example.minesweeper

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory

class MinesweeperApplicationTests {
    private var log = LoggerFactory.getLogger(this::class.java)

    @Test
    fun equalityOfPosition() {
        val positionOne = Position(0, 0)
        val positionTwo = Position(0, 0)

        assertThat(positionOne).isEqualTo(positionTwo)
    }

    @Test
    fun canBuildBoardOfCorrectSize() {
        assertThat(Board.generateGrid(5).size)
            .describedAs("Grid of size 5 number of cells")
            .isEqualTo(25)

        assertThat(Board.generateGrid(1).size)
            .describedAs("Grid of size 1 number of cells")
            .isEqualTo(1)

        assertThat(Board.generateGrid(2).size)
            .describedAs("Grid of size 2 number of cells")
            .isEqualTo(4)

        assertThat(Board.generateGrid(3).size)
            .describedAs("Grid of size 3 number of cells")
            .isEqualTo(9)
    }

    @Test
    fun canListNeighbours() {
        var grid = Board.generateGrid(5)
        var neighbours = Board.getNeighbours(Position(1, 1), grid)
        assertThat(8).isEqualTo(neighbours.size)
    }

    @Test
    fun canPlaceMinesRandomly() {
        val minesToPlace = 5
        val grid = Board.generateGrid(5)
        val minePositions = Board.generateMinePositions(grid.keys, minesToPlace)

        assertThat(minesToPlace).isEqualTo(minePositions.size)
        minePositions.forEach { minePosition -> assertThat(grid[minePosition]).isNotNull }
    }

    @Test
    fun canBuildAGridWithMines() {
        val board = Board(gridSize = 3, numberOfMines = 1)
        assertThat(board.grid.size).describedAs("Board Size").isEqualTo(9)

        board.grid.forEach {
            log.debug("Position {}, Cell {}", it.key, it.value)
        }
    }

    @Test
    fun ifTopLeftCellHasMineNeighboursMineCountIsIncremented() {
        val grid = Board.generateGrid(3)
        val minePositions = setOf(Position(0, 0))

        Board.addMines(minePositions, grid);

        //Row 1
        assertThat(grid[Position(0,0)]?.mines).isEqualTo(0) //Has Mine
        assertThat(grid[Position(0,1)]?.mines).isEqualTo(1)
        assertThat(grid[Position(0,2)]?.mines).isEqualTo(0)

        //Row 2
        assertThat(grid[Position(1,0)]?.mines).isEqualTo(1)
        assertThat(grid[Position(1,1)]?.mines).isEqualTo(1)
        assertThat(grid[Position(1,2)]?.mines).isEqualTo(0)

        //Row 3
        assertThat(grid[Position(2,0)]?.mines).isEqualTo(0)
        assertThat(grid[Position(2,1)]?.mines).isEqualTo(0)
        assertThat(grid[Position(2,2)]?.mines).isEqualTo(0)
    }

    @Test
    fun ifTopRightCellHasMineNeighboursMineCountIsIncremented() {
        val grid3x3 = Board.generateGrid(3)
        Board.addMines(setOf(Position(0, 2)), grid3x3);
        //Row 1
        assertThat(grid3x3[Position(0,1)]?.mines).isEqualTo(1)
        //Row 2
        assertThat(grid3x3[Position(1,0)]?.mines).isEqualTo(0)
        assertThat(grid3x3[Position(1,1)]?.mines).isEqualTo(1)
        assertThat(grid3x3[Position(1,2)]?.mines).isEqualTo(1)
        //Row 3
        assertThat(grid3x3[Position(2,0)]?.mines).isEqualTo(0)

        val grid5x5 = Board.generateGrid(5)
        Board.addMines(setOf(Position(0, 4)), grid5x5);
        //Row 1
        assertThat(grid5x5[Position(0,3)]?.mines).isEqualTo(1)
        //Row 2
        assertThat(grid5x5[Position(1,2)]?.mines).isEqualTo(0)
        assertThat(grid5x5[Position(1,3)]?.mines).isEqualTo(1)
        assertThat(grid5x5[Position(1,4)]?.mines).isEqualTo(1)
    }

    @Test
    fun ifACentralCellHasMineNeighboursMineCountIsIncremented() {
        val grid5x5 = Board.generateGrid(5)
        Board.addMines(setOf(Position(3, 2)), grid5x5);

        //Row 2
        assertThat(grid5x5[Position(2,1)]?.mines).isEqualTo(1)
        assertThat(grid5x5[Position(2,2)]?.mines).isEqualTo(1)
        assertThat(grid5x5[Position(2,3)]?.mines).isEqualTo(1)

        //Row 3
        assertThat(grid5x5[Position(3,1)]?.mines).isEqualTo(1)
        assertThat(grid5x5[Position(3,2)]?.mines).isEqualTo(0) //Has Mine
        assertThat(grid5x5[Position(3,3)]?.mines).isEqualTo(1)

        //Row 4
        assertThat(grid5x5[Position(4,1)]?.mines).isEqualTo(1)
        assertThat(grid5x5[Position(4,2)]?.mines).isEqualTo(1)
        assertThat(grid5x5[Position(4,3)]?.mines).isEqualTo(1)
    }

    @Test
    fun aCentralAndBottomRightHaveMines() {
        val grid5x5 = Board.generateGrid(5)
        Board.addMines(setOf(Position(3, 2), Position(4, 4)), grid5x5);

        //Row 3
        assertThat(grid5x5[Position(3,3)]?.mines).isEqualTo(2)
        assertThat(grid5x5[Position(3,4)]?.mines).isEqualTo(1)

        //Row 4
        assertThat(grid5x5[Position(4,3)]?.mines).isEqualTo(2)
    }

    @Test
    fun adjacentCellsHaveMines() {
        val grid5x5 = Board.generateGrid(5)
        Board.addMines(setOf(Position(3, 2), Position(3, 3)), grid5x5);

        //Row 2
        assertThat(grid5x5[Position(2,1)]?.mines).isEqualTo(1)
        assertThat(grid5x5[Position(2,2)]?.mines).isEqualTo(2)
        assertThat(grid5x5[Position(2,3)]?.mines).isEqualTo(2)
        assertThat(grid5x5[Position(2,4)]?.mines).isEqualTo(1)

        //Row 3
        assertThat(grid5x5[Position(3,1)]?.mines).isEqualTo(1)
        assertThat(grid5x5[Position(3,2)]?.mines).isEqualTo(1) //mine
        assertThat(grid5x5[Position(3,3)]?.mines).isEqualTo(0) //mine
        assertThat(grid5x5[Position(3,4)]?.mines).isEqualTo(1)

        //Row 4
        assertThat(grid5x5[Position(4,1)]?.mines).isEqualTo(1)
        assertThat(grid5x5[Position(4,2)]?.mines).isEqualTo(2)
        assertThat(grid5x5[Position(4,3)]?.mines).isEqualTo(2)
        assertThat(grid5x5[Position(4,4)]?.mines).isEqualTo(1)
    }

    @Test
    fun canFindIfCellInGridHasMine() {
        val grid = mapOf(
            Pair(Position(0, 0), Cell()),
            Pair(Position(0, 1), Cell(hasMine = true))
        )

        assertThat(Board.hasMine(Position(0, 1), grid)).isEqualTo(true)
        assertThat(Board.hasMine(Position(4, 4), grid)).isEqualTo(false)
    }

    @Test
    fun ifAPositionWithoutAMineIsRevealedItsStateIsSetToRevealed() {
        val board = Board(gridSize = 1, numberOfMines = 0)
        board.revealPosition(Position(0, 0))

        assertThat(Board.isRevealed(Position(0, 0), board.grid)).isEqualTo(true)
    }

    @Test
    fun ifAPositionWithAMineIsSelectedGameIsOver() {
        var board = Board(gridSize = 5, numberOfMines = 25)
        assertThat(board.revealPosition(Position(0, 0)))
            .describedAs("Game State")
            .isEqualTo(GameState.LOST)
    }

    @Test
    fun ifAPositionWithoutAMineIsSelectedGameContinues() {
        var board = Board(gridSize = 2, numberOfMines = 0)
        assertThat(board.revealPosition(Position(0, 0)))
            .describedAs("Game State")
            .isEqualTo(GameState.IN_PROGRESS)
    }

    @Test
    fun ifAllCellsHaveBeenRevealedGameIsWon() {
        var board = Board(gridSize = 2, numberOfMines = 0)

        assertThat(board.revealPosition(Position(0, 0))).isEqualTo(GameState.IN_PROGRESS)
        assertThat(board.revealPosition(Position(0, 1))).isEqualTo(GameState.IN_PROGRESS)
        assertThat(board.revealPosition(Position(1, 0))).isEqualTo(GameState.IN_PROGRESS)
        assertThat(board.revealPosition(Position(1, 1))).isEqualTo(GameState.WON)
    }

    @Test
    fun playSomeRandomGames() {
        val portionsToReveal = listOf(
            Position(0, 0),
            Position(0, 1),
            Position(0, 2),
            Position(1, 0),
            Position(1, 1),
            Position(1, 2),
            Position(2, 0),
            Position(2, 1),
            Position(2, 2)
        )

        var gamesToPlay = 1
        while (gamesToPlay <= 20) {
            var boardToPlay = Board(3, 1)
            var gameState = GameState.IN_PROGRESS
            var movesMade = 0

            portionsToReveal
                .shuffled()
                .forEach {
                    if (gameState === GameState.IN_PROGRESS)
                        if(!Board.isRevealed(it, boardToPlay.grid)) {
                            gameState = boardToPlay.revealPosition(it)
                            movesMade++
                        }
                }
            log.info("Game no {} Moves Made {}, Game State {}", gamesToPlay, movesMade, gameState)
            gamesToPlay++
        }
    }
}
