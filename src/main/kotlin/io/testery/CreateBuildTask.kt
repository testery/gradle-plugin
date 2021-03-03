package io.testery

import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

open class CreateBuildTask : ShadowJar() {
    private val failedMessage = "Failed to upload build jar to Testery. Please check your configuration."
    
    fun uploadToTestery(options: TesteryPluginExtension) {
        options.validate(false)

        val url = TesteryApi.getUploadUrl(
            apiKey = options.apiKey!!,
            projectKey = options.projectKey!!,
            buildId = options.buildId!!,
            fileName = TesteryPluginConstants.jarFile,
            commit = options.commitHash,
            branch = options.branch
        ) ?: throw Exception(failedMessage)

        val file = project.file("build/libs/${TesteryPluginConstants.jarFile}")

        if(TesteryApi.uploadJarFile(file, url)){
            println("Uploaded artifact to Testery for build ${options.buildId} of project ${options.projectKey}")
        } else {
            throw Exception(failedMessage)
        }
    }
}