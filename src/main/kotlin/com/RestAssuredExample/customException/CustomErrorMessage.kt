package com.RestAssuredExample.customException

import com.RestAssuredExample.customException.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*

/*
@ControllerAdvice é uma anotação que permite centralizar as exceptions nesta classe.
Quando ocorre alguma exceção o spring "entra" nesta classe e procura pelo Handler com a exceção correspondente.
Existe apenas um handler(extremamente genérico, pois server pra qualquer erro) porque é apenas para usar como exemplo nos testes.
 */
@ControllerAdvice
class CustomErrorMessage : ResponseEntityExceptionHandler()
{
    @ExceptionHandler(Exception::class)
    fun handleServerErrorException(ex: Exception, request: WebRequest?): ResponseEntity<Any?> {
        val details: MutableList<String> = ArrayList()
        details.add("O Id buscado não existe ou não foi possível realizar a operação devido a sintaxe")
        val error = ErrorResponse(Date(), 400, details)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }
}
