package com.rem.papershelf.rest.book

import com.rem.papershelf.data.author.AuthorService
import com.rem.papershelf.data.book.Book
import com.rem.papershelf.data.book.BookService
import com.rem.papershelf.data.book.BookSpecification
import com.rem.papershelf.data.genre.GenreService
import com.rem.papershelf.exception.PaperShelfException
import com.rem.papershelf.rest.Message
import com.rem.papershelf.rest.author.AuthorController
import com.rem.papershelf.rest.author.AuthorDto
import com.rem.papershelf.rest.chapter.ChapterController
import com.rem.papershelf.rest.chapter.ChapterDto
import com.rem.papershelf.rest.genre.GenreController
import com.rem.papershelf.rest.genre.GenreDto
import org.springframework.beans.factory.annotation.Autowired
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
@RequestMapping("/books")
class BookController(@Autowired val bookService: BookService,
                     @Autowired val authorService: AuthorService,
                     @Autowired val genreService: GenreService) {

    @GetMapping(produces = ["application/json"])
    fun getBooks(specification: BookSpecification,
                 @PageableDefault(sort = ["title"]) pageable: Pageable): ResponseEntity<Any> {
        val resources = ArrayList<BookDto>()
        bookService.findAll(specification, pageable).forEach({ book ->
            val dto = BookDto(id = book.id, title = book.title, status = book.status,
                    summary = book.summary, imageUrl = book.imageUrl)

            dto.add(linkTo(methodOn(this::class.java).getBookById(book.id)).withSelfRel())
            dto.add(linkTo(methodOn(this::class.java)
                    .getAuthorsById(book.id, Pageable.unpaged())).withRel("authors"))
            dto.add(linkTo(methodOn(this::class.java)
                    .getChaptersById(book.id, Pageable.unpaged())).withRel("chapters"))
            dto.add(linkTo(methodOn(this::class.java)
                    .getGenresById(book.id, Pageable.unpaged())).withRel("genres"))
            resources.add(dto)
        })
        return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
    }

    @GetMapping(value = ["/{bookId}"], produces = ["application/json"])
    fun getBookById(@PathVariable("bookId") bookId: String): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)
            val dto = BookDto(id = book.id, title = book.title, status = book.status,
                    summary = book.summary, imageUrl = book.imageUrl)

            dto.add(linkTo(methodOn(this::class.java).getBookById(book.id)).withSelfRel())
            dto.add(linkTo(methodOn(this::class.java)
                    .getAuthorsById(book.id, Pageable.unpaged())).withRel("authors"))
            dto.add(linkTo(methodOn(this::class.java)
                    .getChaptersById(book.id, Pageable.unpaged())).withRel("chapters"))
            dto.add(linkTo(methodOn(this::class.java)
                    .getGenresById(book.id, Pageable.unpaged())).withRel("genres"))
            ResponseEntity(Resource(dto), HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping(value = ["/{bookId}/authors"], produces = ["application/json"])
    fun getAuthorsById(@PathVariable("bookId") bookId: String,
                       @PageableDefault(sort = ["name"]) pageable: Pageable): ResponseEntity<Any> {
        return try {
            val resources = ArrayList<AuthorDto>()
            bookService.findAuthorsById(bookId, pageable).forEach({ author ->
                val dto = AuthorDto(id = author.id, name = author.name)

                dto.add(linkTo(methodOn(AuthorController::class.java).getAuthorById(author.id)).withSelfRel())
                resources.add(dto)
            })
            ResponseEntity(Resources(resources, linkTo(methodOn(this::class.java)
                    .getAuthorsById(bookId, Pageable.unpaged())).withRel("authors")), HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping(value = ["/{bookId}/chapters"], produces = ["application/json"])
    fun getChaptersById(@PathVariable("bookId") bookId: String,
                        @PageableDefault(sort = ["level"]) pageable: Pageable): ResponseEntity<Any> {
        return try {
            val resources = ArrayList<ChapterDto>()
            bookService.findChaptersById(bookId, pageable).forEach({ chapter ->
                val dto = ChapterDto(id = chapter.id, name = chapter.name, level = chapter.level,
                        uploadDate = chapter.uploadDate, bookId = chapter.book.id)

                dto.add(linkTo(methodOn(ChapterController::class.java).getChapterById(chapter.id)).withSelfRel())
                resources.add(dto)
            })
            return ResponseEntity(Resources(resources, linkTo(methodOn(this::class.java)
                    .getChaptersById(bookId, Pageable.unpaged())).withRel("chapters")), HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping(value = ["/{bookId}/genres"], produces = ["application/json"])
    fun getGenresById(@PathVariable("bookId") bookId: String,
                      @PageableDefault(sort = ["name"]) pageable: Pageable): ResponseEntity<Any> {
        return try {
            val resources = ArrayList<GenreDto>()
            bookService.findGenresById(bookId, pageable).forEach({ genre ->
                val dto = GenreDto(id = genre.id, name = genre.name, description = genre.description)

                dto.add(linkTo(methodOn(GenreController::class.java).getGenreById(genre.id)).withSelfRel())
                resources.add(dto)
            })
            return ResponseEntity(Resources(resources, linkTo(methodOn(this::class.java)
                    .getGenresById(bookId, Pageable.unpaged())).withRel("genres")), HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = ["application/json"])
    fun addBook(@RequestBody body: BookDto): ResponseEntity<Any> {
        return try {
            val book = Book(title = body.title, status = body.status, summary = body.summary,
                    imageUrl = body.imageUrl)

            bookService.save(book)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: PaperShelfException) {
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
        } catch (e: PaperShelfException) {
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
        } catch (e: PaperShelfException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping(value = ["/{bookId}"], consumes = ["application/json"])
    fun updateBookById(@PathVariable("bookId") bookId: String,
                       @RequestBody body: BookDto): ResponseEntity<Any> {
        return try {
            val book = bookService.findById(bookId)

            book.title = body.title
            book.status = body.status
            book.summary = body.summary
            book.imageUrl = body.imageUrl

            bookService.save(book)
            ResponseEntity(HttpStatus.OK)
        } catch (e: PaperShelfException) {
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
        } catch (e: PaperShelfException) {
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
        } catch (e: PaperShelfException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }
}