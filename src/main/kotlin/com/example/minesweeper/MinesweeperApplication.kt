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

    @ShellMethod("Translate text from one language to another.")
    fun addship(@ShellOption() size: Int, direction: String): String {
        return size.toString()
    }
}