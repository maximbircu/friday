package com.maximbircu.friday.infrastructure

import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception

fun String.executeBashSilently(): String {
    val process = Runtime.getRuntime().exec(this)
    val output = InputStreamReader(process.inputStream).readText()
    val error = InputStreamReader(process.errorStream).readText()
    if (error.isNotBlank()) throw Exception(error)
    return output.trim()
}

fun String.executeBash() {
    val rt = Runtime.getRuntime()
    val proc = rt.exec(this)

    val stdInput = BufferedReader(InputStreamReader(proc.inputStream))

    val stdError = BufferedReader(InputStreamReader(proc.errorStream))

    var text: String?
    while (stdInput.readLine().also { text = it } != null) {
        println(text)
    }

    while (stdError.readLine().also { text = it } != null) {
        println(text)
    }
}
