package com.maximbircu.friday.infrastructure

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

open class CommandsGroup(
    name: String = "",
    commands: List<CliktCommand> = emptyList()
) : CliktCommand(name = name) {
    init {
        subcommands(commands)
    }

    override fun run() = Unit
}
