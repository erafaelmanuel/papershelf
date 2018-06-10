package io.ermdev.papershelf.rest.controller

import io.ermdev.papershelf.data.service.BookService
import io.ermdev.papershelf.exception.EntityException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("books")
class BookController(val bookService: BookService) {

    @GetMapping("{bookId}")
    fun getById(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            ResponseEntity(book, HttpStatus.OK)
        } catch (e: EntityException) {
            ResponseEntity(e.message, HttpStatus.NOT_FOUND)
        }
    }
}