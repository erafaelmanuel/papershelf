package io.ermdev.papershelf.data.service

import io.ermdev.papershelf.data.entity.Book
import io.ermdev.papershelf.data.repository.BookRepository
import io.ermdev.papershelf.exception.EntityException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class BookService(@Autowired val bookRepository: BookRepository) {

    fun findById(id: String): Book {
        return bookRepository.findById(id)
                .orElseThrow { EntityException("No book with id '$id' exists!") }
    }

    fun save(book: Book) {
        bookRepository.save(book)
    }

}