package io.testery

import io.kotlintest.shouldBe
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.File

class TesteryApiTests {
    private val apiToken: String? = System.getenv("TESTERY_API_TOKEN")
    private val projectKey = "example-specflow-dotnet-core"
    private val environmentKey = "testing"

    @BeforeEach
    fun setup() {
        TesteryApi.useDevUrl = true
    }

    @Test
    fun testUploadFile() {
        if(apiToken == null) return

        val jarFile = "testery.jar"

        val url = TesteryApi.getUploadUrl(apiToken, projectKey, "1", jarFile)!!

        val result = TesteryApi.uploadJarFile(File("src/test/resources/$jarFile"), url)

        result shouldBe true
    }

    @Test
    fun testCreateDeploy() {
        if(apiToken == null) return

        val result = TesteryApi.createDeploy(apiToken, projectKey, environmentKey, "1")

        result shouldBe true
    }
}