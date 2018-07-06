package io.ermdev.papershelf.rest.author

import io.ermdev.papershelf.data.author.Author
import io.ermdev.papershelf.data.author.AuthorService
import io.ermdev.papershelf.exception.PaperShelfException
import io.ermdev.papershelf.rest.Message
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/authors")
class AuthorController(val authorService: AuthorService) {

    @GetMapping(produces = ["application/json"])
    fun getAuthors(@PageableDefault(sort = ["name"]) pageable: Pageable): ResponseEntity<Any> {
        val resources = ArrayList<AuthorDto>()
        authorService.findAll(pageable).forEach({ author ->
            val dto = AuthorDto(id = author.id, name = author.name)

            dto.add(linkTo(methodOn(this::class.java).getAuthorById(author.id)).withSelfRel())
            resources.add(dto)
        })
        return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
    }

    @GetMapping(value = ["/{authorId}"], produces = ["application/json"])
    fun getAuthorById(@PathVariable("authorId") authorId: String): ResponseEntity<Any> {
        return try {
            val author = authorService.findById(authorId)
            val dto = AuthorDto(id = author.id, name = author.name)

            dto.add(linkTo(methodOn(this::class.java).getAuthorById(author.id)).withSelfRel())
            ResponseEntity(Resource(dto), HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = ["application/json"])
    fun addAuthor(@RequestBody body: AuthorDto): ResponseEntity<Any> {
        return try {
            val author = Author(name = body.id)

            authorService.save(author)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: PaperShelfException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping(value = ["/{authorId}"], consumes = ["application/json"])
    fun updateAuthorById(@PathVariable("authorId") authorId: String,
                         @RequestBody body: AuthorDto): ResponseEntity<Any> {
        return try {
            val author = authorService.findById(authorId)

            author.name = body.name
            authorService.save(author)
            ResponseEntity(HttpStatus.OK)
        } catch (e: PaperShelfException) {
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