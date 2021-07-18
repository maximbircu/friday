package com.maximbircu.friday.commands.android

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.maximbircu.friday.infrastructure.NetworkUtils
import com.maximbircu.friday.infrastructure.executeBashSilently

@Suppress("Unused")
class ProxyCommand : CliktCommand(
    name = "proxy",
    help = "Configure proxy on Android device"
) {
    private val deviceId: String by option("-s", "--source-device")
        .help("Target device id")
        .default("")

    private val reset: Boolean by option("-r", "--reset")
        .help("Reset")
        .flag()

    private val proxyIp: String by option("-p", "--proxy-ip")
        .help("Proxy Ip(192.168.2.1:1111)")
        .defaultLazy { if (reset) ":0" else "${NetworkUtils.getIp() ?: throw Exception()}:8888" }

    override fun run() {
        "adb -s $deviceId shell settings put global http_proxy $proxyIp".executeBashSilently()
        println("Proxying ${getDeviceName(deviceId) ?: ""} to $proxyIp")
    }

    private fun getDeviceName(deviceId: String): String? {
        val devices = "adb devices -l".executeBashSilently()
        return devices.lines().find { it.contains(deviceId) }?.split(" ")
            ?.find { it.startsWith("model") }
            ?.substringAfter(":")
    }
}
