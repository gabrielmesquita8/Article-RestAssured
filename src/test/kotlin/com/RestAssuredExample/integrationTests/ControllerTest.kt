package com.RestAssuredExample.integrationTests

import com.RestAssuredExample.integrationTests.RestAssuredExampleApplication
import com.RestAssuredExample.model.turismo
import com.RestAssuredExample.repository.guiaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import io.restassured.RestAssured.*
import io.restassured.config.EncoderConfig.encoderConfig
import io.restassured.http.ContentType
import org.springframework.http.HttpStatus.OK
import org.apache.http.entity.ContentType.APPLICATION_JSON
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [RestAssuredExampleApplication::class])
class ControllerTest {

    @Autowired
    private lateinit var repository: guiaRepository
    private val PATH: String = "/turismo"

    @LocalServerPort
    internal var port: Int = 0

    @Test
    fun `Dado que a operação seja um sucesso é retornado uma lista com todos os guias turísticos`() {
        val findBD = repository.findAll()
        val findAPI = given()
                .config(
                    config()
                        .encoderConfig(encoderConfig().encodeContentTypeAs("application/json", ContentType.TEXT))
                )
                .port(port)
                .log().all()
                .contentType(APPLICATION_JSON.toString())
                .`when`()
                .get("$PATH/guias")
                .then()
                .log().all()
                .statusCode(OK.value())
                .spec(
                    expect()
                        .header("content-type", `is`(("application/json")))
                        .body("$", not(emptyOrNullString()))
                )
                .extract().body().jsonPath().getList(".", turismo::class.java)
        assertThat(findAPI).isEqualTo(findBD)
    }

}