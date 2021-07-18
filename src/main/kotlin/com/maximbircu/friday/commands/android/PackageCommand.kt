package com.maximbircu.friday.commands.android

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.path
import com.maximbircu.friday.commands.android.AndroidUtils.aapt
import com.maximbircu.friday.infrastructure.executeBash

@Suppress("Unused")
class PackageCommand : CliktCommand(
    name = "package",
    help = "Print info about APK file"
) {
    private val apkFilePath by option("-f", "--file-path")
        .help(help = "APK file path")
        .path(canBeFile = true, canBeDir = false, mustExist = true)
        .required()

    override fun run() {
        "$aapt dump badging $apkFilePath".executeBash()
    }
}
