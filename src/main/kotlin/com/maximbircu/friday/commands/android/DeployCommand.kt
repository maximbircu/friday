package com.maximbircu.friday.commands.android;

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.output.TermUi
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.path
import com.maximbircu.friday.commands.android.AndroidUtils.getConnectedDevices
import com.maximbircu.friday.infrastructure.executeBashSilently
import java.io.File

@Suppress("Unused")
class DeployCommand : CliktCommand(
    name = "deploy",
    help = "Deploy APK to Android device"
) {
    private val apkFilePath by option("-f", "--file-path")
        .help(help = "APK file path")
        .path(canBeFile = true, canBeDir = false, mustExist = true)
        .required()

    private val deviceId: String by option("-s", "--source-device")
        .help("Target device id")
        .default("")

    private val allowVersionDowngrade: Boolean by option("-d", "--allow-downgrade")
        .help("Allow version downgrade")
        .flag()

    private val run: Boolean by option("-r", "--run")
        .help("Run the app")
        .flag(default = false)

    override fun run() {
        val devices = getConnectedDevices()
        if (deviceId.isNotBlank()) {
            val device = devices.find { it.id == deviceId }
            if (device == null) {
                echo("No device with $deviceId, try running \"friday android devices\"")
                if (TermUi.confirm("Deploy on all connected devices?") == true) {
                    devices.forEach(::deploy)
                }
                return
            }
            deploy(device)
        } else {
            devices.forEach(::deploy)
        }
    }

    private fun deploy(device: AndroidDevice) {
        val allowVersionDowngrade = if (allowVersionDowngrade) "-d" else ""
        echo("Installing on ${device.model}")

        val command = "adb -s ${device.id} install -r -t $allowVersionDowngrade $apkFilePath"
        echo(command)
        command.executeBashSilently()
        if (run) {
            with(File(apkFilePath.toUri()).apkDump()) {
                "adb -s ${device.id} shell am start $appId/$activity".executeBashSilently()
            }
        }
    }
}
