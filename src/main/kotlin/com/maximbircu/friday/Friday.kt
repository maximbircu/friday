package com.maximbircu.friday

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.versionOption
import com.maximbircu.friday.infrastructure.CommandsGroup
import org.reflections.Reflections

class Friday : CommandsGroup(name = "friday") {
    private val commands: MutableMap<String, Any> = mutableMapOf()

    init {
        subcommands(HelpCommand(this))
        versionOption("1.0.0")
        registerCommands()
    }

    override fun run() = Unit

    private fun registerCommands() {
        val reflections = Reflections("com.maximbircu.friday.commands")
        val commandsTypes: List<Class<out CliktCommand>> =
            reflections.getSubTypesOf(CliktCommand::class.java)
                .filter { it !== CommandsGroup::class.java && it !== HelpCommand::class.java }

        commandsTypes.sortedBy { it.`package`.name.split(".").size }.forEach { type ->
            val pkg = type.`package`.name.substringAfter("friday.")
            getDestination(pkg, commands)[type.name] = type
        }

        registerCommands(this, commands["commands"] as MutableMap<String, Any>)
    }

    private fun getDestination(
        pkg: String,
        root: MutableMap<String, Any>,
        iteration: Int = 1
    ): MutableMap<String, Any> {
        val parts = pkg.split(".").toList()

        return if (iteration < parts.size) {
            getDestination(
                pkg = pkg,
                iteration = iteration + 1,
                root = root.getOrPut(
                    parts.take(iteration).joinToString(".")
                ) { mutableMapOf<String, Any>() } as MutableMap<String, Any>
            )
        } else {
            root.getOrPut(parts.joinToString(".")) {
                mutableMapOf<String, Any>()
            } as MutableMap<String, Any>
        }
    }

    private fun registerCommands(rootCommand: CliktCommand, commands: Map<String, Any>) {
        rootCommand.subcommands(
            commands.values.filterIsInstance<Class<CliktCommand>>().map { it.newInstance() }
        )

        commands.filter { it.value is Map<*, *> }.forEach { (key, value) ->
            val commandsGroup = CommandsGroup(name = key.substringAfterLast("."))
            rootCommand.subcommands(commandsGroup)
            registerCommands(commandsGroup, value as Map<String, Any>)
        }
    }
}
