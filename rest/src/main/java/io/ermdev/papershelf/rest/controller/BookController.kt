package io.ermdev.papershelf.rest.controller

import io.ermdev.papershelf.data.entity.Book
import io.ermdev.papershelf.data.service.BookService
import io.ermdev.papershelf.exception.EntityException
import io.ermdev.papershelf.rest.dto.BookDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(val bookService: BookService) {

    @GetMapping
    fun getAll(): ResponseEntity<Any> {
        val dtoList = ArrayList<BookDto>()
        bookService.findAll().forEach { book ->
            val dto = BookDto(id = book.id, title = book.title)
            dtoList.add(dto)
        }
        return ResponseEntity(dtoList, HttpStatus.OK)
    }

    @GetMapping("/{bookId}")
    fun getById(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            val dto = BookDto(id = book.id, title = book.title)
            ResponseEntity(dto, HttpStatus.OK)
        } catch (e: EntityException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping
    fun addBook(@RequestBody body: Book): ResponseEntity<Any> {
        return try {
            bookService.save(body)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: EntityException) {
            ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }
    }

}