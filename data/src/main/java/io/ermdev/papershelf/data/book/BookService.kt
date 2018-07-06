package io.ermdev.papershelf.data.book

import io.ermdev.papershelf.data.author.Author
import io.ermdev.papershelf.data.chapter.Chapter
import io.ermdev.papershelf.data.genre.Genre
import io.ermdev.papershelf.exception.EntityException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*

@Service
class BookService(@Autowired val bookRepository: BookRepository) {

    fun findAll(pageable: Pageable): Page<Book> {
        return bookRepository.findAll(pageable)
    }

    @Throws(exceptionClasses = [EntityException::class])
    fun findById(id: String): Book {
        return bookRepository.findById(id).orElseThrow({
            EntityException("No book with id '$id' exists!")
        })
    }

    fun findByAuthorId(authorId: String, pageable: Pageable): Page<Book> {
        return bookRepository.findByAuthorId(authorId, pageable)
    }

    fun findByGenreId(genreId: String, pageable: Pageable): Page<Book> {
        return bookRepository.findByGenreId(genreId, pageable)
    }

    fun findByAuthorIdAndGenreId(authorId: String, genreId: String, pageable: Pageable): Page<Book> {
        return bookRepository.findByAuthorIdAndGenreId(authorId, genreId, pageable)
    }

    fun findAuthorsById(bookId: String, pageable: Pageable): Page<Author> {
        return bookRepository.findAuthorsById(bookId, pageable)
    }

    fun findChaptersById(bookId: String, pageable: Pageable): Page<Chapter> {
        return bookRepository.findChaptersById(bookId, pageable)
    }

    fun findGenresById(bookId: String, pageable: Pageable): Page<Genre> {
        return bookRepository.findGenresById(bookId, pageable)
    }

    @Throws(exceptionClasses = [EntityException::class])
    fun save(book: Book) {
        if (StringUtils.isEmpty(book.title)) {
            throw EntityException("title cannot be empty")
        }
        if (!book.title.matches(Regex("^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$"))) {
            throw EntityException("title cannot contain special characters")
        }
        if (StringUtils.isEmpty(book.status)) {
            throw EntityException("status cannot be empty")
        }
        if (!book.status.matches(Regex("^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$"))) {
            throw EntityException("status cannot contain special characters")
        }
        if (StringUtils.isEmpty(book.id)) {
            book.id = UUID.randomUUID().toString()
        }
        bookRepository.save(book)
    }

    fun deleteById(bookId: String) {
        bookRepository.deleteById(bookId)
    }

}