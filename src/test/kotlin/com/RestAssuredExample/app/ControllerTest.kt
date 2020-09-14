package com.RestAssuredExample.app

import com.RestAssuredExample.model.turismo
import com.RestAssuredExample.repository.guiaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import io.restassured.RestAssured.*
import org.springframework.http.HttpStatus.OK
import org.apache.http.entity.ContentType.APPLICATION_JSON
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

/*
Essa é a classe que nos interessa, nela realizaremos todos os testes que envolvem a integração com nosso  banco de dados.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = [RestAssuredExampleApplication::class])
class ControllerTest {

    // Injeção de dependência
    @Autowired
    private lateinit var repository: guiaRepository
    private val ENDPOINT: String = "/turismo"

    @LocalServerPort
    internal var port: Int = 0

    @Test
    fun `Dado que a operação seja um sucesso é retornado uma lista com todos os guias turísticos`() {
        val before = repository.findAll()
        val after = given()
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
        assertThat(after).isEqualTo(before)
    }

    @Test
    fun `Dado que a operação seja um sucesso é retornado um guia turístico de acordo com o ID`() {
        val id: Long = 1
        val before = repository.findById(id)
        val after = given()
                .port(port)
                .log().all()
                .`when`()
                .get("$ENDPOINT/idGuia/$id")
                .then()
                .log().all()
                .statusCode(OK.value())
                .extract().`as`(turismo::class.java)

        assertThat(after).isEqualTo(before.get())
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
            .post(ENDPOINT)
            .then()
            .log().all()
            .statusCode(HttpStatus.CREATED.value())


        val id: Long = guia.id
        val validateGuia = repository.findById(id)

        assertThat(validateGuia.get()).usingRecursiveComparison().isEqualTo(guia)
    }

    @Test
    fun `Quando realiza uma operação PATCH para alterar o nome com dados corretos a operação deve ser executada com sucesso`() {
        val value = mapOf("nome" to "Matheus Oliveira") // mapeando nosso JSON para alterarmos o campo
        val id: Long = 1
        val before = repository.findById(id)
        val payload = com.fasterxml.jackson.databind.ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(value)

        assertThat(before.get().nome).isNotEqualTo(value["nome"])

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

    @Test
    fun `Quando realiza uma operação PATCH para alterar o genero com dados corretos a operação deve ser executada com sucesso`() {
        val value = mapOf("genero" to "M")
        val id: Long = 1
        val before = repository.findById(id)
        val payload = com.fasterxml.jackson.databind.ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(value)

        assertThat(before.get().nome).isNotEqualTo(value["genero"])

        given()
            .port(port)
            .log().all()
            .contentType(APPLICATION_JSON.toString())
            .`when`()
            .body(payload)
            .patch("$ENDPOINT/genero/$id")
            .then()
            .log().all()
            .statusCode(HttpStatus.NO_CONTENT.value())

        val after = repository.findById(id)
        assertThat(after.get().genero).isEqualTo(value["genero"])
        assertThat(after.get()).usingRecursiveComparison().ignoringFields("genero").isEqualTo(before.get())
    }

    @Test
    fun `Quando realiza uma operação PATCH para alterar o pontoturistico com dados corretos a operação deve ser executada com sucesso`() {
        val value = mapOf("pontoturistico" to "foz do iguaçu")
        val id: Long = 1
        val before = repository.findById(id)
        val payload = com.fasterxml.jackson.databind.ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(value)

        assertThat(before.get().pontoturistico).isNotEqualTo(value["pontoturistico"])

        given()
                .port(port)
                .log().all()
                .contentType(APPLICATION_JSON.toString())
                .`when`()
                .body(payload)
                .patch("$ENDPOINT/ponto/$id")
                .then()
                .log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())

        val after = repository.findById(id)
        assertThat(after.get().pontoturistico).isEqualTo(value["pontoturistico"])
        assertThat(after.get()).usingRecursiveComparison().ignoringFields("pontoturistico").isEqualTo(before.get())
    }

    @Test
    fun `Dado que seja feito uma busca por um ID inexistente é retornado erro 400`() {
        val id: Long = 18

        given()
            .port(port)
            .log().all()
            .contentType("application/json")
            .`when`()
            .get("$ENDPOINT/idGuia/$id")
            .then()
            .log().all()
            .statusCode(HttpStatus.NOT_FOUND.value())
            .spec(
                expect()
                    .header("content-type", `is`(("application/json")))
                    .body("timestamp", `is`(not(emptyOrNullString())))
                    .body("status", `is`(equalTo(HttpStatus.NOT_FOUND.value())))
                    .body("message", hasItem("O Id buscado não existe!"))
            )
    }

    @Test
    fun `Quando realiza uma operação PATCH para alterar o campo nome com dados incorretos a operação deve ser retornar erro 400`() {
        val value = mapOf("nome" to "")
        val id: Long = 1
        val payload = com.fasterxml.jackson.databind.ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(value)
        val before = repository.findById(id)

        assertThat(before.get().nome).isNotEqualTo(value["nome"])

        given()
            .port(port)
            .log().all()
            .contentType("application/json")
            .`when`()
            .body(payload)
            .patch("$ENDPOINT/nome/$id")
            .then()
            .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .spec(
                expect()
                    .header("content-type", `is`(("application/json")))
                    .body("timestamp", `is`(not(emptyOrNullString())))
                    .body("status", `is`(equalTo(HttpStatus.BAD_REQUEST.value())))
                    .body("message", hasItem("Ocorreu um erro em sua requisição, verifique sua sintaxe!"))
            )

        val after = repository.findById(id)
        assertThat(after.get().nome).isNotEqualTo(value["nome"])
        assertThat(after.get()).usingRecursiveComparison().ignoringFields("nome").isEqualTo(before.get())
    }

    @Test
    fun `Quando realiza uma operação PATCH para alterar o campo pontoturistico com dados incorretos a operação deve ser retornar erro 400`() {
        val value = mapOf("pontoturistico" to "")
        val id: Long = 1
        val payload = com.fasterxml.jackson.databind.ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(value)
        val before = repository.findById(id)

        assertThat(before.get().pontoturistico).isNotEqualTo(value["pontoturistico"])

        given()
                .port(port)
                .log().all()
                .contentType("application/json")
                .`when`()
                .body(payload)
                .patch("$ENDPOINT/nome/$id")
                .then()
                .log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .spec(
                    expect()
                        .header("content-type", `is`(("application/json")))
                        .body("timestamp", `is`(not(emptyOrNullString())))
                        .body("status", `is`(equalTo(HttpStatus.BAD_REQUEST.value())))
                        .body("message", hasItem("Ocorreu um erro em sua requisição, verifique sua sintaxe!"))
                )

        val after = repository.findById(id)
        assertThat(after.get().pontoturistico).isNotEqualTo(value["pontoturistico"])
        assertThat(after.get()).usingRecursiveComparison().ignoringFields("pontoturistico").isEqualTo(before.get())
    }
}