package io.testery

import io.kotlintest.shouldBe
import io.kotlintest.shouldNotBe
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TesteryPluginTests {
    private val project = ProjectBuilder.builder().build()

    @BeforeEach
    fun setup() {
        project.pluginManager.apply("java")
        project.pluginManager.apply("io.testery")
    }

    @Test
    fun testProjectConfiguration() {
        project.pluginManager.hasPlugin("io.testery") shouldBe true

        project.tasks.getByName("uploadBuild")::class shouldNotBe CreateBuildTask::class
        project.tasks.getByName("createDeploy")::class shouldNotBe CreateDeployTask::class
    }

    @Test
    fun testUploadTaskConfig() {
        val task = project.tasks.getByName("uploadBuild") as? CreateBuildTask

        task shouldNotBe null
        task?.archiveFileName?.get() shouldBe "testery.jar"
        task?.description shouldBe "Upload jar file to Testery"
    }

    @Test
    fun testCreateDeployTaskConfig() {
        val task = project.tasks.getByName("uploadBuild") as? CreateBuildTask

        task shouldNotBe null
        task?.archiveFileName?.get() shouldBe "testery.jar"
        task?.description shouldBe "Upload jar file to Testery"
    }
}