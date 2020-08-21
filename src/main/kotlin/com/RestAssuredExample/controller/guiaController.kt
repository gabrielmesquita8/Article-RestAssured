package com.RestAssuredExample.controller



import org.springframework.http.ResponseEntity
import com.RestAssuredExample.model.turismo
import com.RestAssuredExample.repository.guiaRepository
import com.RestAssuredExample.service.guiaService

import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
/*
Classe que possui os endpoint que irão executar as operações de nossa API.
 */
@RestController
@RequestMapping("/turismo")
class PSController(private val guiaRepository: guiaRepository, private val guiaService: guiaService)
{
    @GetMapping("/guias")
    fun getAllPlayers(): MutableIterable<turismo> =
            guiaService.getAllGuias()

    @GetMapping("/idGuia/{id}")
    fun getById(@PathVariable id: Long): ResponseEntity<turismo>
    {
        val guia = guiaService.getGuiaById(id)
        return ResponseEntity.ok(guia)
    }

    @PostMapping
    fun addGuia(
            @RequestBody  ps: turismo
    ): ResponseEntity<Any?>
    {
        val guiaID = guiaService.createNewGuia(ps)
        val location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{guiaID}")
                .build(guiaID)
        return ResponseEntity.created(location).build()
    }

    @PatchMapping("/nome/{id}")
    fun UpdateName(@PathVariable id: Long, @RequestBody name: turismo): ResponseEntity<Any?>
    {
        guiaService.UpdateName(id, name)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/genero/{id}")
    fun UpdateG(@PathVariable id: Long, @RequestBody g: turismo): ResponseEntity<Any?>
    {
        guiaService.UpdateGenero(id, g)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/ponto/{id}")
    fun UpdatePonto(@PathVariable id: Long, @RequestBody tag: turismo): ResponseEntity<Any?>
    {
        guiaService.UpdatePonto(id, tag)
        return ResponseEntity.noContent().build()
    }

    @DeleteMapping("/delGuia/{id}")
    fun deletePlayerById(@PathVariable(value = "id") id: Long): ResponseEntity<Any?>
    {
        guiaService.deleteGuia(id)
        return ResponseEntity.ok().build()
    }
}
