package com.RestAssuredExample.customException

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*
import javax.persistence.EntityNotFoundException

/*
Esta classe utiliza a anotação @ControllerAdvice porque ela que cuidará de lançar nossas exceptions.
Abaixo temos duas exceptions que serão usadas para customizar a mensagem de erro.
 */
@ControllerAdvice
class CustomErrorMessage : ResponseEntityExceptionHandler()
{
    @ExceptionHandler(EntityNotFoundException::class)
    fun handlerEntityNotFound(ex: Exception, request: WebRequest?): ResponseEntity<Any?> {
        val details: MutableList<String> = ArrayList()
        details.add("O Id buscado não existe!")
        val error = ErrorResponse(Date(), 404, details)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(Exception::class)
    fun handlerBadRequest(ex: Exception, request: WebRequest?): ResponseEntity<Any?> {
        val details: MutableList<String> = ArrayList()
        details.add("Ocorreu um erro em sua requisição, verifique sua sintaxe!")
        val error = ErrorResponse(Date(), 400, details)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }
}
