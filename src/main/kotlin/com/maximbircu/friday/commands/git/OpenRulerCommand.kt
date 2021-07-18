package com.maximbircu.friday.commands.git

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.output.TermUi

@Suppress("Unused")
class OpenRulerCommand : CliktCommand(
    name = "ruler",
    help = "Opens git ruler in editor"
) {
    override fun run() {
        TermUi.editText(
            """
            # Character count ruler
            #         1         2         3         4         5         6         7
            #123456789012345678901234567890123456789012345678901234567890123456789012
            

            # Optional extra details go here
            #
        """.trimIndent()
        )?.let { text ->
            val commitMessage = text.lines()[3]
            echo(commitMessage)
        }
    }
}
