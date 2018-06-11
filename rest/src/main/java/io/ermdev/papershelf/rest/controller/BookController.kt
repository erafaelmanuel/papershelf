package io.ermdev.papershelf.rest.controller

import io.ermdev.papershelf.data.entity.Book
import io.ermdev.papershelf.data.service.BookService
import io.ermdev.papershelf.exception.EntityException
import io.ermdev.papershelf.rest.Message
import io.ermdev.papershelf.rest.dto.BookDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(val bookService: BookService) {

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

    @DeleteMapping("/{bookId}")
    fun deleteBookById(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        bookService.deleteById(bookId)
        return ResponseEntity(HttpStatus.OK)
    }

}