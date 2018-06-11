package io.ermdev.papershelf.rest.controller

import io.ermdev.papershelf.data.entity.Genre
import io.ermdev.papershelf.data.service.GenreService
import io.ermdev.papershelf.exception.EntityException
import io.ermdev.papershelf.rest.Message
import io.ermdev.papershelf.rest.dto.GenreDto
import io.ermdev.papershelf.rest.hateoas.GenreHateoas.Companion.getSelfLink
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/genres")
class GenreController(@Autowired val genreService: GenreService) {

    @GetMapping
    fun getGenres(): ResponseEntity<Any> {
        val resources = ArrayList<GenreDto>()
        genreService.findAll().forEach({ genre ->
            val dto = GenreDto(id = genre.id, name = genre.name, description = genre.description)

            dto.add(getSelfLink(genre.id))
            resources.add(dto)
        })
        return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
    }

    @GetMapping("/{genreId}")
    fun getGenreById(@PathVariable("genreId") genreId: String): ResponseEntity<Any> {
        return try {
            val genre = genreService.findById(genreId)
            val dto = GenreDto(id = genre.id, name = genre.name, description = genre.description)

            dto.add(getSelfLink(genre.id))
            ResponseEntity(Resource(dto), HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping
    fun addGenre(@RequestBody body: Genre): ResponseEntity<Any> {
        return try {
            genreService.save(body)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }
}