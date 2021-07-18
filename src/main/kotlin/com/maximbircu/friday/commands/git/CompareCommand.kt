package com.maximbircu.friday.commands.git

import com.github.ajalt.clikt.core.CliktCommand
import com.maximbircu.friday.infrastructure.executeBash
import com.maximbircu.friday.infrastructure.executeBashSilently

@Suppress("Unused")
class CompareCommand : CliktCommand(
    name = "compare",
    help = "Opens Github compare for the current branch"
) {
    override fun run() {
        val currentBranch = "git branch --show-current".executeBashSilently()
        val remote = "git config --get remote.origin.url".executeBashSilently()
        val compareUrl = remote.toHttpsGithubRemote().plus("/compare/$currentBranch")

        "open $compareUrl".executeBash()
    }

    private fun String.toHttpsGithubRemote(): String {
        return replace(":", "/")
            .replace("git@", "https://")
            .removeSuffix(".git")
    }
}
