package io.testery

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.plugins.JavaPluginConvention

object TesteryPluginConstants {
    const val jarFile = "testery.jar"
}

class TesteryPlugin  : Plugin<Project> {
    private val taskGroup = "testery"

    override fun apply(project: Project) {
        val extension = project.extensions.create("testery", TesteryPluginExtension::class.java)

        project.tasks.register("uploadBuildToTestery", CreateBuildTask::class.java) {
            val convention = project.convention.getPlugin(JavaPluginConvention::class.java)
            it.archiveFileName.set(TesteryPluginConstants.jarFile)
            it.group = taskGroup
            it.description = "Upload jar file to Testery for testing"
            it.configurations = extension.configurations ?: listOf(
                project.configurations.getByName("testRuntimeClasspath"),
                project.configurations.getByName("runtimeClasspath")
            )
            it.manifest.inheritFrom(((project.tasks.getByName("jar") as org.gradle.jvm.tasks.Jar).manifest))
            it.from(convention.sourceSets.named("test").get().output, convention.sourceSets.named("main").get().output)
            it.exclude("META-INF/INDEX.LIST", "META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA", "module-info.class")
            it.doLast { _ ->
                it.uploadToTestery(extension)
            }
        }

        project.tasks.register("notifyTesteryOfDeploy", CreateDeployTask::class.java) {
            it.group = taskGroup
            it.description = "Notify Testery of a deploy"
            it.doLast { _ ->
                it.createDeploy(extension)
            }
        }
    }
}



fun TesteryPluginExtension.validate(requireEnvironment: Boolean = false) {
    val defaultConfig = """
        testery {
            apiToken = System.getenv("TESTERY_API_TOKEN") // required
            projectKey = "my-testery-project-key" // required
            buildId = "myBuildId" // required
            commitHash = System.getenv("GIT_COMMIT") // optional
            branch = System.getenv("GIT_BRANCH") // optional
            environmentKey = "my-testery-environment-key" // required for createDeploy
        }
    """.trimIndent()

    val messages = mutableListOf<String>()

    if(apiToken == null) messages.add("You must configure your Testery API token.")
    if(projectKey == null) messages.add("You must configure your Testery project key.")
    if(buildId == null) messages.add("You must configure a buildId.")
    if(environmentKey == null && requireEnvironment) messages.add("You must configure an environmentKey.")

    if(messages.isNotEmpty()) {
        messages.add("Example:")
        messages.add(defaultConfig)
        throw Exception(messages.joinToString(System.lineSeparator()))
    }
}

open class TesteryPluginExtension {
    var apiToken: String? = null
    var projectKey: String? = null
    var buildId: String? = null
    var commitHash: String? = null
    var branch: String? = null
    var environmentKey: String? = null
    var configurations: List<Configuration>? = null
}