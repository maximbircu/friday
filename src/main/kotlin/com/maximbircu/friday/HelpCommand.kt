package com.maximbircu.friday

import com.github.ajalt.clikt.core.CliktCommand
import com.maximbircu.friday.infrastructure.CommandsGroup

class HelpCommand(private val rootCommand: CliktCommand) : CliktCommand(
    name = "help",
    help = "Prints a more comprehensive help"
) {
    override fun run() {
        echo(
            """
            Usage: friday [OPTIONS] COMMAND [ARGS]...

            Options:
              -h, --help  Show this message and exit
              
            Commands: 
        """.trimIndent()
        )
        printHelpMessage(rootCommand, tabSize = 3)
    }

    private fun printHelpMessage(rootCommand: CliktCommand, tabSize: Int = 3, depth: Int = 0) {
        if (rootCommand is CommandsGroup) {
            rootCommand.commandName.printCommandsGroupTitle(depth)

            val commands = rootCommand.registeredSubcommands()
            commands.filter { it !is CommandsGroup }.printCommandsHelp(tabSize, depth)

            commands.filterIsInstance<CommandsGroup>().forEach {
                printHelpMessage(it, tabSize, depth + tabSize)
            }
        }
    }

    private fun List<CliktCommand>.printCommandsHelp(tabSize: Int, depth: Int) {
        val commandsSpace = (1..depth + tabSize).joinToString("") { " " }
        val longestCommand = maxOf { it.commandName.length }
        forEach {
            val space = (0..longestCommand - it.commandName.length).joinToString("") { " " }
            println("${commandsSpace}${it.commandName} $space ${it.commandHelp}")
        }
    }

    private fun String.printCommandsGroupTitle(depth: Int) {
        if (this.isNotBlank()) {
            val separator = (1..100).joinToString("") { "-" }
            val leftSpace = (1..depth).joinToString("") { " " }
            println(
                "\n$leftSpace$this ${separator.substring(this.length, separator.length)}\n"
            )
        }
    }
}
