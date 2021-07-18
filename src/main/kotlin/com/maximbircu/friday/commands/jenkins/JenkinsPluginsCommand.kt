package com.maximbircu.friday.commands.jenkins

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.help
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.maximbircu.friday.infrastructure.executeBash

@Suppress("unused")
class JenkinsPluginsCommand : CliktCommand(
    name = "jenkins-plugins",
    help = "Opens plugins list for a jenkins instance"
) {
    private val jenkinsUrl: String by option("-u", "--url").help("Jenkins URL").required()

    override fun run() {
        val plg = "/pluginManager/api/xml?depth=1&xpath=/*/*/shortName|/*/*/version&wrapper=plugins"
        "open ${jenkinsUrl}/$plg".executeBash()
    }
}
