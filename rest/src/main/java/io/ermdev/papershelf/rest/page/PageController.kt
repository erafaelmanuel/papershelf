package io.ermdev.papershelf.rest.page

import io.ermdev.papershelf.data.entity.Page
import io.ermdev.papershelf.data.service.ChapterService
import io.ermdev.papershelf.data.service.PageService
import io.ermdev.papershelf.exception.PaperShelfException
import io.ermdev.papershelf.exception.ResourceException
import io.ermdev.papershelf.rest.Message
import io.ermdev.papershelf.rest.ResourceFinder.Companion.getLocalFile
import org.apache.commons.io.IOUtils
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Pageable
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/pages")
class PageController(@Autowired val pageService: PageService,
                     @Autowired val chapterService: ChapterService,
                     @Autowired val request: HttpServletRequest) {

    @Value("\${papershelf.path}")
    lateinit var path: String

    @GetMapping(produces = ["application/json"])
    fun getPages(pageable: Pageable): ResponseEntity<Any> {
        val resources = ArrayList<PageDto>()
        pageService.findAll(pageable).forEach({ page ->
            val dto = PageDto(id = page.id, order = page.order, image = page.image, chapterId = page.chapter.id)

            dto.add(linkTo(methodOn(this::class.java).getPageById(page.id)).withSelfRel())
            resources.add(dto)
        })
        return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
    }

    @GetMapping(value = ["/{pageId}"], produces = ["image/jpeg", "application/json"])
    fun getPageById(@PathVariable("pageId") pageId: String): ResponseEntity<Any> {
        val headers = HttpHeaders()
        return try {
            val page = pageService.findById(pageId)
            if (request.getHeader("Accept") == "application/json") {
                val dto = PageDto(id = page.id, order = page.order, image = page.image, chapterId = page.chapter.id)

                dto.add(linkTo(methodOn(this::class.java).getPageById(page.id)).withSelfRel())
                ResponseEntity(Resource(dto), HttpStatus.OK)
            } else {
                return try {
                    val input = getLocalFile(path + page.image)

                    ResponseEntity(IOUtils.toByteArray(input), HttpStatus.OK)
                } catch (e: PaperShelfException) {
                    val message = Message(status = 500, error = "Internal Server Error", message = e.message)

                    headers.add("Content-Type", "application/json")
                    ResponseEntity(message, headers, HttpStatus.INTERNAL_SERVER_ERROR)
                }
            }
        } catch (e: PaperShelfException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)

            headers.add("Content-Type", "application/json")
            ResponseEntity(message, headers, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = ["application/json"])
    fun addPage(@RequestBody body: PageDto): ResponseEntity<Any> {
        return try {
            if (StringUtils.isEmpty(body.chapterId)) {
                throw ResourceException("chapterId cannot be empty")
            }
            val page = Page(order = body.order, image = body.image, chapter = chapterService.findById(body.chapterId))

            pageService.save(page)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: PaperShelfException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping(value = ["/{pageId}"], consumes = ["application/json"])
    fun updatePageById(@PathVariable("pageId") pageId: String,
                       @RequestBody body: PageDto): ResponseEntity<Any> {
        return try {
            val page = pageService.findById(pageId)

            page.order = body.order
            page.image = body.image
            page.chapter = chapterService.findById(body.chapterId)

            pageService.save(page)
            ResponseEntity(HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @DeleteMapping("/{pageId}")
    fun deletePageById(@PathVariable("pageId") pageId: String): ResponseEntity<Any> {
        pageService.deleteById(pageId)
        return ResponseEntity(HttpStatus.OK)
    }

}