package io.ermdev.papershelf.rest.controller

import io.ermdev.papershelf.data.entity.Author
import io.ermdev.papershelf.data.service.AuthorService
import io.ermdev.papershelf.data.service.BookService
import io.ermdev.papershelf.exception.EntityException
import io.ermdev.papershelf.rest.Message
import io.ermdev.papershelf.rest.dto.AuthorDto
import io.ermdev.papershelf.rest.dto.BookDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/authors")
class AuthorController(@Autowired val authorService: AuthorService,
                       @Autowired val bookService: BookService) {

    @GetMapping
    fun getAuthors(): ResponseEntity<Any> {
        val dtoList = ArrayList<AuthorDto>()
        authorService.findAll().forEach { author ->
            val dto = AuthorDto(id = author.id, name = author.name)
            dtoList.add(dto)
        }
        return ResponseEntity(dtoList, HttpStatus.OK)
    }

    @GetMapping("/{authorId}")
    fun getAuthorById(@PathVariable("authorId") authorId: String): ResponseEntity<Any> {
        return try {
            val author = authorService.findById(authorId)
            val dto = AuthorDto(id = author.id, name = author.name)
            ResponseEntity(dto, HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/{authorId}/books")
    fun getBooks(@PathVariable("authorId") authorId: String): ResponseEntity<Any> {
        val dtoList = ArrayList<BookDto>()
        bookService.findByAuthorId(authorId).forEach { book ->
            val dto = BookDto(id = book.id, title = book.title)
            dtoList.add(dto)
        }
        return ResponseEntity(dtoList, HttpStatus.OK)
    }

    @PostMapping
    fun addAuthor(@RequestBody body: Author): ResponseEntity<Any> {
        return try {
            authorService.save(body)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/{authorId}")
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