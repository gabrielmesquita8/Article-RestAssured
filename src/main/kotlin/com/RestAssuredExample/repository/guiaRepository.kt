package com.RestAssuredExample.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import com.RestAssuredExample.model.turismo


//Essa é a classe DAO, com a extensão do crudRepository todos os métodos CRUD são importados
@Repository
interface guiaRepository : CrudRepository<turismo, Long>
