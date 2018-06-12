package io.ermdev.papershelf.rest.controller

import io.ermdev.papershelf.data.entity.Author
import io.ermdev.papershelf.data.service.AuthorService
import io.ermdev.papershelf.exception.EntityException
import io.ermdev.papershelf.rest.Message
import io.ermdev.papershelf.rest.dto.AuthorDto
import io.ermdev.papershelf.rest.hateoas.AuthorHateoas.Companion.getSelfLink
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/authors")
class AuthorController(val authorService: AuthorService) {

    @GetMapping(produces = ["application/json"])
    fun getAuthors(): ResponseEntity<Any> {
        val resources = ArrayList<AuthorDto>()
        authorService.findAll().forEach({ author ->
            val dto = AuthorDto(id = author.id, name = author.name)

            dto.add(getSelfLink(author.id))
            resources.add(dto)
        })
        return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
    }

    @GetMapping(value = ["/{authorId}"], produces = ["application/json"])
    fun getAuthorById(@PathVariable("authorId") authorId: String): ResponseEntity<Any> {
        return try {
            val author = authorService.findById(authorId)
            val dto = AuthorDto(id = author.id, name = author.name)

            dto.add(getSelfLink(author.id))
            ResponseEntity(Resource(dto), HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = ["application/json"])
    fun addAuthor(@RequestBody body: Author): ResponseEntity<Any> {
        return try {
            authorService.save(body)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping(value = ["/{authorId}"], consumes = ["application/json"])
    fun updateAuthorById(@PathVariable("authorId") authorId: String,
                         @RequestBody body: Author): ResponseEntity<Any> {
        return try {
            val author = authorService.findById(authorId)

            author.name = body.name
            authorService.save(author)
            ResponseEntity(HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{authorId}")
    fun deleteAuthorById(@PathVariable("authorId") authorId: String): ResponseEntity<Any> {
        authorService.deleteById(authorId)
        return ResponseEntity(HttpStatus.OK)
    }

}