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
            println("Notified Testery about deploy of build ${options.buildId} of project ${options.projectKey} to environment ${options.environmentKey}")
        } else {
            throw Exception("Failed to notify Testery of deploy. Please check your configuration")
        }
    }
}