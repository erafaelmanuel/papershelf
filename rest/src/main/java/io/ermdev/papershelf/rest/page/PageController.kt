package io.ermdev.papershelf.rest.page

import io.ermdev.papershelf.data.entity.Page
import io.ermdev.papershelf.data.service.PageService
import io.ermdev.papershelf.exception.PaperShelfException
import io.ermdev.papershelf.rest.Message
import io.ermdev.papershelf.rest.ResourceFinder.Companion.getLocalFile
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.ByteArrayOutputStream
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/pages")
class PageController(@Autowired val pageService: PageService,
                     @Autowired val request: HttpServletRequest) {

    @Value("\${papershelf.path}")
    lateinit var path: String

    @GetMapping(produces = ["application/json"])
    fun getPages(): ResponseEntity<Any> {
        val resources = ArrayList<PageDto>()
        pageService.findAll().forEach({ page ->
            val dto = PageDto(id = page.id, order = page.order, image = page.image)

            dto.add(linkTo(methodOn(this::class.java).getPageById(page.id)).withSelfRel())
            resources.add(dto)
        })
        return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
    }

    @GetMapping(value = ["/{pageId}"], produces = ["image/jpeg", "application/json"])
    fun getPageById(@PathVariable("pageId") pageId: String): ResponseEntity<Any> {
        if (request.getHeader("Accept") == "application/json") {
            return try {
                val page = pageService.findById(pageId)
                val dto = PageDto(id = page.id, order = page.order, image = page.image)

                dto.add(linkTo(methodOn(this::class.java).getPageById(page.id)).withSelfRel())
                ResponseEntity(Resource(dto), HttpStatus.OK)
            } catch (e: PaperShelfException) {
                val message = Message(status = 404, error = "Not Found", message = e.message)
                ResponseEntity(message, HttpStatus.NOT_FOUND)
            }
        } else {
            return try {
                val page = pageService.findById(pageId)
                val input = getLocalFile(path + page.image)
                val output = ByteArrayOutputStream()

                var read = 0
                val bytes = ByteArray(size = 10240)

                while (input.read(bytes, 0, bytes.size).let({ read = it; read != -1 })) {
                    output.write(bytes, 0, read)
                }
                output.flush()
                output.close()

                ResponseEntity(output.toByteArray(), HttpStatus.OK)
            } catch (e: PaperShelfException) {
                val headers = HttpHeaders()
                val message = Message(status = 400, error = "Bad Request", message = e.message)

                headers.add("Content-Type", "application/json")
                ResponseEntity(message, headers, HttpStatus.INTERNAL_SERVER_ERROR)
            }
        }
    }

    @PostMapping(consumes = ["application/json"])
    fun addPage(@RequestBody body: PageDto): ResponseEntity<Any> {
        return try {
            val page = Page(order = body.order, image = body.image)

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