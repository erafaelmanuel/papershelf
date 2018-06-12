package io.ermdev.papershelf.data.service

import io.ermdev.papershelf.data.entity.Book
import io.ermdev.papershelf.data.repository.BookRepository
import io.ermdev.papershelf.exception.EntityException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*

@Service
class BookService(@Autowired val bookRepository: BookRepository) {

    fun findAll(): List<Book> = bookRepository.findAll()

    fun findById(id: String): Book {
        return bookRepository.findById(id)
                .orElseThrow { EntityException("No book with id '$id' exists!") }
    }

    fun findByAuthorId(authorId: String): List<Book> = bookRepository.findByAuthorId(authorId)

    fun findByGenreId(genreId: String): List<Book> = bookRepository.findByGenreId(genreId)

    fun findByAuthorIdAndGenreId(authorId: String, genreId: String): List<Book> {
        return bookRepository.findByAuthorIdAndGenreId(authorId, genreId)
    }

    fun save(book: Book) {
        if (StringUtils.isEmpty(book.title)) {
            throw EntityException("title cannot be empty")
        }
        if (!book.title.matches(Regex("^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$"))) {
            throw EntityException("title cannot contain special characters")
        }
        if (StringUtils.isEmpty(book.id)) {
            book.id = UUID.randomUUID().toString()
        }
        bookRepository.save(book)
    }

    fun deleteById(bookId: String) = bookRepository.deleteById(bookId)

}