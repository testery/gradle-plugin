package io.testery

import org.gradle.api.DefaultTask

open class CreateDeployTask : DefaultTask() {
    fun createDeploy(options: TesteryPluginExtension) {
        options.validate(true)

        if(TesteryApi.createDeploy(
                apiKey = options.apiKey!!,
                projectKey = options.projectKey!!,
                environmentKey = options.environmentKey!!,
                buildId = options.buildId!!,
                commit = options.commitHash,
                branch = options.branch
        )) {
            println("Created deploy in Testery")
        } else {
            throw Exception("Failed to create deploy in Testery. Please check your configuration")
        }
    }
}