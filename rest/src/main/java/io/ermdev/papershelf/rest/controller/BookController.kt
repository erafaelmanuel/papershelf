package io.ermdev.papershelf.rest.controller

import io.ermdev.papershelf.data.entity.Book
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
@RequestMapping("/books")
class BookController(@Autowired val bookService: BookService,
                     @Autowired val authorService: AuthorService) {

    @GetMapping
    fun getBooks(): ResponseEntity<Any> {
        val dtoList = ArrayList<BookDto>()
        bookService.findAll().forEach { book ->
            val dto = BookDto(id = book.id, title = book.title)
            dtoList.add(dto)
        }
        return ResponseEntity(dtoList, HttpStatus.OK)
    }

    @GetMapping("/{bookId}")
    fun getBookById(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            val dto = BookDto(id = book.id, title = book.title)
            ResponseEntity(dto, HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/{bookId}/authors")
    fun getAuthor(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            val author = book.author
            if (author != null) {
                val dto = AuthorDto(id = author.id, name = author.name)
                ResponseEntity(dto, HttpStatus.OK)
            } else {
                ResponseEntity("No Author Found", HttpStatus.NOT_FOUND)
            }
        } catch (e: EntityException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping
    fun addBook(@RequestBody body: Book): ResponseEntity<Any> {
        return try {
            bookService.save(body)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/{bookId}")
    fun updateBookById(@PathVariable("bookId") bookId: String, @RequestBody body: Book): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            book.title = body.title
            bookService.save(book)
            ResponseEntity(HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping("/{bookId}/authors/{authorId}")
    fun updateAuthor(@PathVariable("bookId") bookId: String,
                     @PathVariable("authorId") authorId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            book.author = authorService.findById(authorId)
            bookService.save(book)
            ResponseEntity(HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{bookId}")
    fun deleteBookById(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        bookService.deleteById(bookId)
        return ResponseEntity(HttpStatus.OK)
    }

    @DeleteMapping("/{bookId}/authors")
    fun deleteAuthor(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            book.author = null
            bookService.save(book)
            ResponseEntity(HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

}