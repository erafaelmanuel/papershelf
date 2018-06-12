package io.ermdev.papershelf.rest.controller

import io.ermdev.papershelf.data.entity.Book
import io.ermdev.papershelf.data.service.AuthorService
import io.ermdev.papershelf.data.service.BookService
import io.ermdev.papershelf.data.service.GenreService
import io.ermdev.papershelf.exception.EntityException
import io.ermdev.papershelf.rest.Message
import io.ermdev.papershelf.rest.dto.AuthorDto
import io.ermdev.papershelf.rest.dto.BookDto
import io.ermdev.papershelf.rest.dto.GenreDto
import io.ermdev.papershelf.rest.hateoas.AuthorHateoas
import io.ermdev.papershelf.rest.hateoas.BookHateoas.Companion.getAuthorLink
import io.ermdev.papershelf.rest.hateoas.BookHateoas.Companion.getGenreLink
import io.ermdev.papershelf.rest.hateoas.BookHateoas.Companion.getSelfLink
import io.ermdev.papershelf.rest.hateoas.GenreHateoas
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/books")
class BookController(@Autowired val bookService: BookService,
                     @Autowired val authorService: AuthorService,
                     @Autowired val genreService: GenreService) {

    @GetMapping
    fun getBooks(@RequestParam(value = "authorId", required = false) authorId: String?,
                 @RequestParam(value = "genreId", required = false) genreId: String?): ResponseEntity<Any> {
        val resources = ArrayList<BookDto>()
        if (!StringUtils.isEmpty(authorId) && !StringUtils.isEmpty(genreId)) {
            bookService.findByAuthorIdAndGenreId(authorId!!, genreId!!).forEach({ book ->
                val dto = BookDto(id = book.id, title = book.title)

                dto.add(getSelfLink(book.id))
                dto.add(getAuthorLink(book.id))
                dto.add(getGenreLink(book.id))
                resources.add(dto)
            })
        } else if (!StringUtils.isEmpty(authorId)) {
            bookService.findByAuthorId(authorId!!).forEach({ book ->
                val dto = BookDto(id = book.id, title = book.title)

                dto.add(getSelfLink(book.id))
                dto.add(getAuthorLink(book.id))
                dto.add(getGenreLink(book.id))
                resources.add(dto)
            })
        } else if (!StringUtils.isEmpty(genreId)) {
            bookService.findByGenreId(genreId!!).forEach({ book ->
                val dto = BookDto(id = book.id, title = book.title)

                dto.add(getSelfLink(book.id))
                dto.add(getAuthorLink(book.id))
                dto.add(getGenreLink(book.id))
                resources.add(dto)
            })
        } else {
            bookService.findAll().forEach({ book ->
                val dto = BookDto(id = book.id, title = book.title)

                dto.add(getSelfLink(book.id))
                dto.add(getAuthorLink(book.id))
                dto.add(getGenreLink(book.id))
                resources.add(dto)
            })
        }
        return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
    }

    @GetMapping("/{bookId}")
    fun getBookById(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            val dto = BookDto(id = book.id, title = book.title)

            dto.add(getSelfLink(book.id))
            dto.add(getAuthorLink(book.id))
            dto.add(getGenreLink(book.id))
            ResponseEntity(Resource(dto), HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/{bookId}/authors")
    fun getAuthors(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        return try {
            val resources = ArrayList<AuthorDto>()
            bookService.findById(bookId).authors.forEach({ author ->
                val dto = AuthorDto(id = author.id, name = author.name)

                dto.add(AuthorHateoas.getSelfLink(author.id))
                resources.add(dto)
            })
            ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/{bookId}/genres")
    fun getGenres(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        return try {
            val resources = ArrayList<GenreDto>()
            bookService.findById(bookId).genres.forEach({ genre ->
                val dto = GenreDto(id = genre.id, name = genre.name, description = genre.description)

                dto.add(GenreHateoas.getSelfLink(genre.id))
                resources.add(dto)
            })
            return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
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

    @PostMapping("/{bookId}/authors/{authorId}")
    fun addAuthor(@PathVariable("bookId") bookId: String,
                  @PathVariable("authorId") authorId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            val author = authorService.findById(authorId)

            book.authors.add(author)
            bookService.save(book)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("/{bookId}/genres/{genreId}")
    fun addGenre(@PathVariable("bookId") bookId: String,
                 @PathVariable("genreId") genreId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            val genre = genreService.findById(genreId)

            book.genres.add(genre)
            bookService.save(book)
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

    @DeleteMapping("/{bookId}/authors")
    fun deleteAuthor(@PathVariable("bookId") bookId: String,
                     @PathVariable("authorId") authorId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            val author = authorService.findById(authorId)

            book.authors.remove(author)
            bookService.save(book)
            ResponseEntity(HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{bookId}/genres/{genreId}")
    fun deleteGenre(@PathVariable("bookId") bookId: String,
                    @PathVariable("genreId") genreId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            val genre = genreService.findById(genreId)

            book.genres.remove(genre)
            bookService.save(book)
            ResponseEntity(HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

}