package com.maximbircu.friday.commands.docker

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.maximbircu.friday.infrastructure.executeBash
import com.maximbircu.friday.infrastructure.executeBashSilently

@Suppress("unused")
class DockerCleanCommand : CliktCommand(
    name = "clean",
    help = "Remove images and containers or both"
) {
    private val removeContainers: Boolean by option(
        "-c",
        "--containers",
        help = "Remove containers"
    ).flag(default = false)
    private val removeImages: Boolean by option(
        "-i",
        "--images",
        help = "Remove images"
    ).flag(default = false)

    private val force: Boolean by option(
        "-f",
        "--force",
        help = "Will force removed it"
    ).flag(default = false)

    private val images get() = "docker images -a -q".executeBashSilently().lines()
    private val containers get() = "docker ps -a -q".executeBashSilently().lines()

    override fun run() {
        if (removeContainers) removeContainers(force)
        if (removeImages) removeImages(force)
    }

    private fun removeContainers(force: Boolean) {
        "docker rm ${containers.joinToString(" ")} ${if (force) "-f" else ""}".executeBash()
    }

    private fun removeImages(force: Boolean) {
        "docker rmi ${images.joinToString(" ")} ${if (force) "-f" else ""}".executeBash()
    }
}
