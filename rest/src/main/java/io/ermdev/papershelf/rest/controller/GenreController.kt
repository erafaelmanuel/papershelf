package io.ermdev.papershelf.rest.controller

import io.ermdev.papershelf.data.service.GenreService
import io.ermdev.papershelf.exception.EntityException
import io.ermdev.papershelf.rest.Message
import io.ermdev.papershelf.rest.dto.GenreDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/genres")
class GenreController(@Autowired val genreService: GenreService) {

    @GetMapping
    fun getGenres(): ResponseEntity<Any> {
        val dtoList = ArrayList<GenreDto>()
        genreService.findAll().forEach { genre ->
            val dto = GenreDto(id = genre.id, name = genre.name, description = genre.description)
            dtoList.add(dto)
        }
        return ResponseEntity(dtoList, HttpStatus.OK)
    }

    @GetMapping("/{genreId}")
    fun getGenreById(@PathVariable("genreId") genreId: String): ResponseEntity<Any> {
        return try {
            val genre = genreService.findById(genreId)
            val dto = GenreDto(id = genre.id, name = genre.name, description = genre.description)
            ResponseEntity(dto, HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }
}