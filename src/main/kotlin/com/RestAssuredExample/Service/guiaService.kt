package com.RestAssuredExample.service

import com.RestAssuredExample.model.turismo
import com.RestAssuredExample.repository.guiaRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import java.util.*
import javax.persistence.EntityNotFoundException

/*
Classe que realiza as validações de nossos endpoints, como por exemplo validar se o dado inserido foi nulo ou está vazio
Ele também realiza os principais métodos que permite a inserção no banco e outros métodos de uma API
 */
@Service
class guiaService (
        @Autowired
        private val guiaRepository: guiaRepository
){

    fun getAllGuias(): MutableIterable<turismo>
    {
        return guiaRepository.findAll()
    }

    fun getGuiaById(id: Long) : turismo
    {
        return guiaRepository.findById(id).orElseThrow {
            EntityNotFoundException()
        }
    }

    @Transactional
    fun createNewGuia(guia: turismo ) : turismo
    {
        if(guia.nome.trim().isEmpty() || guia.pontoturistico.trim().isEmpty())
        {
            throw Exception()
        }

        return turismo(
                nome = guia.nome,
                genero = guia.genero,
                pontoturistico = guia.pontoturistico
        ).let {
            val savedPlayer = guiaRepository.save(guia)
            savedPlayer
        }
    }

    @Transactional
    fun UpdateName(@PathVariable(value = "id") id: Long,
                   @RequestBody newNome: turismo): ResponseEntity<turismo>
    {
        return guiaRepository.findById(id).map { tag ->
            val updateNome: turismo = tag
                    .copy(nome = newNome.nome)
            if (newNome.nome.trim().isEmpty() )
            {
                throw Exception()
            }
            ResponseEntity.ok().body(guiaRepository.save(updateNome))
        }.orElseThrow { EntityNotFoundException() }
    }

    @Transactional
    fun UpdateGenero(@PathVariable(value = "id") id: Long,
                     @RequestBody newG: turismo): ResponseEntity<turismo>
    {
        return guiaRepository.findById(id).map { tag ->
            val updateG: turismo = tag
                    .copy(genero = newG.genero)
            ResponseEntity.ok().body(guiaRepository.save(updateG))
        }.orElseThrow { EntityNotFoundException() }
    }

    @Transactional
    fun UpdatePonto(@PathVariable(value = "id") id: Long,
                    @RequestBody newPonto: turismo): ResponseEntity<turismo>
    {
        return guiaRepository.findById(id).map { tag ->
            val updatePonto: turismo = tag
                    .copy(pontoturistico = newPonto.pontoturistico)
            if (newPonto.pontoturistico.trim().isEmpty() )
            {
                throw Exception()
            }
            ResponseEntity.ok().body(guiaRepository.save(updatePonto))
        }.orElseThrow { EntityNotFoundException() }
    }

    @DeleteMapping("/delGuia/{id}")
    fun deleteGuia(@PathVariable(value = "id") id: Long): ResponseEntity<Void>
    {
        return guiaRepository.findById(id).map { play  ->
            guiaRepository.delete(play)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElseThrow { EntityNotFoundException() }

    }
}
