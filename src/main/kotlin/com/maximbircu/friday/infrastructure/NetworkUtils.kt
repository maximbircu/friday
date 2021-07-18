package com.maximbircu.friday.infrastructure

object NetworkUtils {
    fun getIp(): String? {
        return "ipconfig getifaddr en0".executeBashSilently().takeIf { it.isNotBlank() }
    }
}
