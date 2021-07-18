package com.maximbircu.friday.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.maximbircu.friday.friday
import com.maximbircu.friday.infrastructure.CommandsGroup
import java.io.File

@Suppress("Unused")
class GenerateBashProfileCommand : CliktCommand(
    name = "generate-bash-profile",
    help = "Generates a neat bash profile containing all Friday commands"
) {
    private val bashProfileFileName by option("--file-name")
        .help("Bash profile file name")
        .default(".friday_bash_profile")

    private val bashProfileFileLocation by option("--file-location")
        .help("Bash profile file location")
        .default(System.getenv("HOME"))

    override fun run() {
        val aliases = StringBuilder()
        buildAliases(aliases, friday, mutableListOf())
        val profile = File(getFilePath()).apply { writeText(aliases.toString()) }
        printBashProfileSetupInstructions(profile)
    }

    private fun printBashProfileSetupInstructions(profile: File) {
        val helpMessage = StringBuilder()
        helpMessage.appendLine(
            """
                Bash profile generated successfully!
                
                ################################################################################
                ### Check out: ${profile.absolutePath}
                ################################################################################
                
            """.trimIndent()
        )
        helpMessage.appendLine(profile.readText().trim())
        helpMessage.appendLine(
            """
            
            ################################################################################
            ### Add this at the bottom of your .zshrc
            ################################################################################

            if [ -f ${profile.absolutePath} ]; then . ${profile.absolutePath}; fi
            """.trimIndent()
        )
        println(helpMessage.toString())
    }

    private fun buildAliases(
        result: StringBuilder,
        rootCommand: CliktCommand,
        parents: List<String>
    ) {
        if (rootCommand is CommandsGroup) {
            rootCommand.registeredSubcommands().filter { it !is CommandsGroup }.forEach { command ->
                result.appendLine(command.toAlias(parents + listOf(rootCommand.commandName)))
            }
            rootCommand.registeredSubcommands().filterIsInstance<CommandsGroup>().forEach {
                buildAliases(result, it, parents + listOf(rootCommand.commandName))
            }
        }
    }

    private fun CliktCommand.toAlias(
        parents: List<String>,
        addPrefix: Boolean = true,
        params: String = ""
    ): String {
        var prefix = ""
        var parentCommands = ""
        if (parents.isNotEmpty()) {
            prefix = if (addPrefix) "${parents.last()}-" else ""
            parentCommands = "${parents.joinToString(" ")} "
        }
        val aliasName = "$prefix$commandName"
        return "alias $aliasName=\"$parentCommands$commandName$params\""
    }

    private fun getFilePath(): String {
        return if (bashProfileFileLocation.isNotBlank()) {
            if (bashProfileFileLocation.endsWith("/")) {
                "${bashProfileFileLocation}${bashProfileFileName}"
            } else {
                "${bashProfileFileLocation}/${bashProfileFileName}"
            }
        } else {
            bashProfileFileName
        }
    }
}
