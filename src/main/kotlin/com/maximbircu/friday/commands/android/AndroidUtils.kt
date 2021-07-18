package com.maximbircu.friday.commands.android

import com.maximbircu.friday.commands.android.AndroidUtils.aapt
import com.maximbircu.friday.infrastructure.executeBashSilently
import java.io.File

object AndroidUtils {
    val androidHome: String = System.getenv("ANDROID_HOME")
    val buildTools = File(androidHome, "build-tools").listFiles().maxOrNull()?.absolutePath
    val aapt = "$buildTools/aapt"

    fun getConnectedDevices(): List<AndroidDevice> {
        val devices: MutableList<AndroidDevice> = mutableListOf()
        val devicesOutput = "adb devices -l".executeBashSilently()
        val lines =
            devicesOutput.lines().toMutableList().apply { removeAt(0) }.filter { it.isNotBlank() }
        lines.forEach { line ->
            val deviceId = line.substringBefore(" ")
            val deviceName =
                line.split(" ").find { it.startsWith("model") }?.substringAfter(":") ?: "unknown"
            devices.add(AndroidDevice(deviceId, deviceName))
        }
        return devices
    }
}

class AndroidDevice(val id: String, val model: String)

class ApkDump(dump: String) {
    val appId = dump.lines().first { it.contains("package") }
        .substringAfter("package: name='")
        .substringBefore("'")

    val activity = dump.lines().first { it.contains("launchable-activity") }
        .substringAfter("launchable-activity: name='")
        .substringBefore("'")
}

fun File.apkDump(): ApkDump = ApkDump("$aapt dump badging $absolutePath".executeBashSilently())
