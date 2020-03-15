package com.example.minesweeper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.shell.standard.ShellComponent
import org.springframework.shell.standard.ShellMethod
import org.springframework.shell.standard.ShellOption

@SpringBootApplication
class MinesweeperApplication

fun main(args: Array<String>) {
    runApplication<MinesweeperApplication>(*args)
}

@ShellComponent
class TranslationCommands {
    lateinit var board: Board


    @ShellMethod("startgame --size 5 --mines 1")
    fun startgame(@ShellOption() size: Int, mines: Int): String {
        board = Board(size, mines)

        return drawBoard(board)
    }

    fun drawBoard(board: Board): String {
        val stringBoard: StringBuilder = java.lang.StringBuilder()

        var row = 0
        while(row < 5) {
            stringBoard.append(String.format("Row [%s] ", row))
            var column = 0
            while(column < 5) {
                var position = board.grid[Position(row, column)]
                when(position?.revealed) {
                    true -> "| |"
                    else -> stringBoard.append(String.format("|%s|", position?.mines ?: ""))
                }
                ++column
            }
            stringBoard.append("\n")
            ++row
        }

        return stringBoard.toString();
    }
}