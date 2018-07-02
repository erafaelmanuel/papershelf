package io.ermdev.papershelf.rest.page

import io.ermdev.papershelf.data.entity.Page
import io.ermdev.papershelf.data.service.PageService
import io.ermdev.papershelf.exception.EntityException
import io.ermdev.papershelf.rest.Message
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pages")
class PageController(val pageService: PageService) {

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

    @GetMapping(value = ["/{pageId}"], produces = ["application/json"])
    fun getPageById(@PathVariable("pageId") pageId: String): ResponseEntity<Any> {
        return try {
            val page = pageService.findById(pageId)
            val dto = PageDto(id = page.id, order = page.order, image = page.image)

            dto.add(linkTo(methodOn(this::class.java).getPageById(page.id)).withSelfRel())
            ResponseEntity(Resource(dto), HttpStatus.OK)
        } catch (e: EntityException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = ["application/json"])
    fun addPage(@RequestBody body: Page): ResponseEntity<Any> {
        return try {
            pageService.save(body)
            ResponseEntity(HttpStatus.CREATED)
        } catch (e: EntityException) {
            val message = Message(status = 400, error = "Bad Request", message = e.message)
            ResponseEntity(message, HttpStatus.BAD_REQUEST)
        }
    }

    @PutMapping(value = ["/{pageId}"], consumes = ["application/json"])
    fun updatePageById(@PathVariable("pageId") pageId: String,
                       @RequestBody body: Page): ResponseEntity<Any> {
        return try {
            val page = pageService.findById(pageId)

            page.order = body.order
            page.image = body.image
            pageService.save(page)
            ResponseEntity(HttpStatus.OK)
        } catch (e: EntityException) {
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