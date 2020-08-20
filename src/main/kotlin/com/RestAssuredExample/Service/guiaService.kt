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


@Service
class guiaService (
        @Autowired
        private val guiaRepository: guiaRepository
){

    fun getAllGuias(): MutableIterable<turismo>
    {
        return guiaRepository.findAll()
    }

    fun getGuiaById(id: Long) : Optional<turismo>
    {
        validateGuia(id)
        return guiaRepository.findById(id)
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
        validateGuia(id)
        return guiaRepository.findById(id).map { tag ->
            val updateNome: turismo = tag
                    .copy(nome = newNome.nome)
            if (newNome.nome.trim().isEmpty() )
            {
                throw Exception()
            }
            ResponseEntity.ok().body(guiaRepository.save(updateNome))
        }.orElse(ResponseEntity.notFound().build())
    }

    @Transactional
    fun UpdateGenero(@PathVariable(value = "id") id: Long,
                     @RequestBody newG: turismo): ResponseEntity<turismo>
    {
        validateGuia(id)
        return guiaRepository.findById(id).map { tag ->
            val updateG: turismo = tag
                    .copy(genero = newG.genero)
            if (newG.genero.trim().isEmpty() )
            {
                throw Exception()
            }
            ResponseEntity.ok().body(guiaRepository.save(updateG))
        }.orElse(ResponseEntity.notFound().build())
    }

    @Transactional
    fun UpdatePonto(@PathVariable(value = "id") id: Long,
                    @RequestBody newPonto: turismo): ResponseEntity<turismo>
    {
        validateGuia(id)
        return guiaRepository.findById(id).map { tag ->
            val updatePonto: turismo = tag
                    .copy(pontoturistico = newPonto.pontoturistico)
            if (newPonto.pontoturistico.trim().isEmpty() )
            {
                throw Exception()
            }
            ResponseEntity.ok().body(guiaRepository.save(updatePonto))
        }.orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/delGuia/{id}")
    fun deleteGuia(@PathVariable(value = "id") id: Long): ResponseEntity<Void>
    {
        validateGuia(id)
        return guiaRepository.findById(id).map { play  ->
            guiaRepository.delete(play)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())

    }

//Método que verifica a existência do ID na aplicação

    private fun validateGuia(id: Long): Boolean
    {
        if(guiaRepository.existsById(id))
        {
            return true
        }
        else
        {
            throw EntityNotFoundException("Player com $id não encontrado")
        }
    }

}
