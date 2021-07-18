package com.maximbircu.friday.commands.android

import com.github.ajalt.clikt.core.CliktCommand
import com.maximbircu.friday.commands.android.AndroidUtils.getConnectedDevices

@Suppress("unused")
class DevicesCommand : CliktCommand(
    name = "devices",
    help = "Print the list of connected Android devices"
) {
    override fun run() {
        val devices = getConnectedDevices()
        val outputBuilder = StringBuilder()
        devices.forEach { device ->
            outputBuilder.appendLine("${device.id} - ${device.model}")
        }
        println(outputBuilder.toString())
    }
}
