package com.RestAssuredExample.model

import com.sun.istack.NotNull
import javax.persistence.*

//data class automaticamente gera toString, equals e hash
@Entity
@Table(name="turismo")
data class turismo(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long = 0,
        @field:NotNull
        val nome: String = "",
        val genero: String = "",
        @field:NotNull
        val pontoturistico: String = ""
)