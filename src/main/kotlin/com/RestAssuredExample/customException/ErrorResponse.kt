package com.RestAssuredExample.customException

import java.util.*
//classe modelo para exibir mensagem de erro customizada
data class ErrorResponse(
        val timestamp: Date,
        val status: Int,
        val Message: MutableList<String>
)