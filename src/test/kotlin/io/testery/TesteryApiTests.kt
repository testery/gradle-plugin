package io.testery

import io.kotlintest.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class TesteryApiTests {
    private val apiKey: String? = System.getenv("TESTERY_API_KEY")
    private val projectKey = "example-specflow-dotnet-core"
    private val environmentKey = "testing"

    @BeforeEach
    fun setup() {
        TesteryApi.useDevUrl = true
    }

    @Test
    fun testUploadFile() {
        if(apiKey == null) return

        val jarFile = "testery.jar"

        val url = TesteryApi.getUploadUrl(apiKey, projectKey, "1", jarFile)!!

        val result = TesteryApi.uploadJarFile(File("src/test/resources/$jarFile"), url)

        result shouldBe true
    }

    @Test
    fun testCreateDeploy() {
        if(apiKey == null) return

        val result = TesteryApi.createDeploy(apiKey, projectKey, environmentKey, "1")

        result shouldBe true
    }
}