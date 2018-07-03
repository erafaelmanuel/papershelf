package io.ermdev.papershelf.rest.chapter

import io.ermdev.papershelf.data.entity.Chapter
import io.ermdev.papershelf.data.service.BookService
import io.ermdev.papershelf.data.service.ChapterService
import io.ermdev.papershelf.exception.PaperShelfException
import io.ermdev.papershelf.exception.ResourceException
import io.ermdev.papershelf.rest.Message
import io.ermdev.papershelf.rest.page.PageController
import io.ermdev.papershelf.rest.page.PageDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chapters")
class ChapterController(@Autowired val chapterService: ChapterService,
                        @Autowired val bookService: BookService) {

    @GetMapping(produces = ["application/json"])
    fun getChapters(pageable: Pageable): ResponseEntity<Any> {
        val resources = ArrayList<ChapterDto>()
        chapterService.findAll(pageable).forEach({ chapter ->
            val dto = ChapterDto(id = chapter.id, name = chapter.name, order = chapter.order,
                    uploadDate = chapter.uploadDate, bookId = chapter.book.id)

            dto.add(linkTo(methodOn(this::class.java).getChapterById(chapter.id)).withSelfRel())
            dto.add(linkTo(methodOn(this::class.java).getPages(chapter.id)).withRel("pages"))
            resources.add(dto)
        })
        return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
    }

    @GetMapping(value = ["/{chapterId}"], produces = ["application/json"])
    fun getChapterById(@PathVariable("chapterId") chapterId: String): ResponseEntity<Any> {
        return try {
            val chapter = chapterService.findById(chapterId)
            val dto = ChapterDto(id = chapter.id, name = chapter.name, order = chapter.order,
                    uploadDate = chapter.uploadDate, bookId = chapter.book.id)

            dto.add(linkTo(methodOn(this::class.java).getChapterById(chapter.id)).withSelfRel())
            dto.add(linkTo(methodOn(this::class.java).getPages(chapter.id)).withRel("pages"))
            ResponseEntity(Resource(dto), HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping(value = ["/{chapterId}/pages"], produces = ["application/json"])
    fun getPages(@PathVariable("chapterId") chapterId: String): ResponseEntity<Any> {
        return try {
            val resources = ArrayList<PageDto>()

            chapterService.findById(chapterId).pages.forEach({ page ->
                val dto = PageDto(id = page.id, order = page.order, image = page.image, chapterId = page.chapter.id)

                dto.add(linkTo(methodOn(PageController::class.java).getPageById(page.id)).withSelfRel())
                resources.add(dto)
            })
            ResponseEntity(Resources(resources, linkTo(methodOn(this::class.java).getPages(chapterId))
                    .withRel("pages")), HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = ["application/json"])
    fun addChapter(@RequestBody body: ChapterDto): ResponseEntity<Any> {
        return try {
            if (StringUtils.isEmpty(body.bookId)) {
                throw ResourceException("bookId cannot be empty")
            }
            val book = Chapter(name = body.name, order = body.order, book = bookService.findById(body.bookId))

            chapterService.save(book)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: PaperShelfException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping(value = ["/{chapterId}"], consumes = ["application/json"])
    fun updateChapterById(@PathVariable("chapterId") chapterId: String,
                          @RequestBody body: ChapterDto): ResponseEntity<Any> {
        return try {
            val chapter = chapterService.findById(chapterId)

            chapter.name = body.name
            chapter.order = body.order
            chapter.book = bookService.findById(body.bookId)

            chapterService.save(chapter)
            ResponseEntity(HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{chapterId}")
    fun deleteChapterById(@PathVariable("chapterId") chapterId: String): ResponseEntity<Any> {
        chapterService.deleteById(chapterId)
        return ResponseEntity(HttpStatus.OK)
    }
}