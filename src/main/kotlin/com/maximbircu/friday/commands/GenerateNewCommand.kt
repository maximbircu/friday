package com.maximbircu.friday.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import java.io.File

@Suppress("Unused")
class NewCommandCommand : CliktCommand(
    name = "new-command",
    help = "Generates a new command template"
) {
    private val commandsPath = "src/main/kotlin/com/maximbircu/friday/commands/"

    private val newCommandName: String by option("-n", "--name").prompt("Command name")
    private val helpMessage: String by option("-h", "--help").prompt("Help message")

    override fun run() {
        if (newCommandName.contains(" ")) {
            throw IllegalArgumentException("Command name can not contain empty spaces")
        }
        if (TermUi.confirm("Confirm creating command: $newCommandName $commandHelp") == true) {
            val file = File("$commandsPath/${newCommandName.kamelCase()}Command.kt")
            file.writeText(getCommandClassContent())
        }
    }

    private fun getCommandClassContent(): String {
        val kamelCaseCommandName = newCommandName.kamelCase()
        val commandClassName = "${kamelCaseCommandName}Command"
        return """
            package com.maximbircu.friday.commands

            import com.github.ajalt.clikt.core.CliktCommand
            
            @Suppress("Unused")
            class $commandClassName : CliktCommand(
                name = "$newCommandName",
                help = "$helpMessage"
            ) {
                override fun run() {
                    TODO("Not yet implemented")
                }
            }
        """.trimIndent()
    }
}

private fun String.kamelCase(): String {
    var result = ""
    val commandNameIterator = this.iterator()
    result += commandNameIterator.nextChar().toUpperCase()
    while (commandNameIterator.hasNext()) {
        val char = commandNameIterator.nextChar()
        result += if (char == '_' || char == '-') commandNameIterator.nextChar()
            .toUpperCase() else char
    }
    return result
}
