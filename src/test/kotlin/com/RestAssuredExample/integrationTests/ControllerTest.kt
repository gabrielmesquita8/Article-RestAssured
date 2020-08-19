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
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [RestAssuredExampleApplication::class])
class ControllerTest {

    @Autowired
    private lateinit var repository: guiaRepository
    private val ENDPOINT: String = "/turismo"

    @LocalServerPort
    internal var port: Int = 0

    @Test
    fun `Dado que a operação seja um sucesso é retornado uma lista com todos os guias turísticos`() {
        val findBD = repository.findAll()
        val findAPI = given()
                .port(port)
                .log().all()
                .`when`()
                .get("$ENDPOINT/guias")
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

    @Test
    fun `Quando um usuário realiza um POST com dados corretos a operação deve ser realizada com sucesso`() {
        val guia = turismo(5, "Alberto Silva", "M", "Armação dos Búzios")
        val payload = com.fasterxml.jackson.databind.ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(guia)

        given()
            .port(port)
            .log().all()
            .contentType(APPLICATION_JSON.toString())
            .`when`()
            .body(payload)
            .post("$ENDPOINT")
            .then()
            .log().all()
            .statusCode(HttpStatus.CREATED.value())


        val id: Long = guia.id
        val validatePlayer = repository.findById(id)

        assertThat(validatePlayer.get()).usingRecursiveComparison().isEqualTo(guia)
    }

    @Test
    fun `Quando realiza uma operação PATCH para alterar o nome com dados corretos a operação deve ser executada com sucesso`() {
        val value = mapOf("nome" to "Shrek")
        val id: Long = 1
        val before = repository.findById(id)
        val payload = com.fasterxml.jackson.databind.ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(value)

        assertThat(before.get().nome).isNotEqualTo(payload)

        given()
            .port(port)
            .log().all()
            .contentType(APPLICATION_JSON.toString())
            .`when`()
            .body(payload)
            .patch("$ENDPOINT/nome/$id")
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())

        val after = repository.findById(id)
        assertThat(after.get().nome).isEqualTo(value["nome"])
        assertThat(after.get()).usingRecursiveComparison().ignoringFields("nome").isEqualTo(before.get())
    }

}