package com.rem.papershelf.rest.page

import com.rem.papershelf.data.chapter.ChapterService
import com.rem.papershelf.data.page.Page
import com.rem.papershelf.data.page.PageService
import com.rem.papershelf.data.page.PageSpecification
import com.rem.papershelf.exception.PaperShelfException
import com.rem.papershelf.exception.ResourceException
import com.rem.papershelf.rest.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.hateoas.Resource
import org.springframework.hateoas.Resources
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo
import org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/pages")
class PageController(@Autowired val pageService: PageService,
                     @Autowired val chapterService: ChapterService) {

    @GetMapping(produces = ["application/json", "application/hal+json"])
    fun getPages(specification: PageSpecification,
                 @PageableDefault(sort = ["order"], size = 20) pageable: Pageable): ResponseEntity<Any> {
        val resources = ArrayList<PageDto>()
        pageService.findAll(specification, pageable).forEach({ page ->
            val dto = PageDto(id = page.id, order = page.order, imageUrl = page.imageUrl, chapterId = page.chapter.id)

            dto.add(linkTo(methodOn(this::class.java).getPageById(page.id)).withSelfRel())
            resources.add(dto)
        })
        return ResponseEntity(Resources(resources, linkTo(this::class.java).withSelfRel()), HttpStatus.OK)
    }

    @GetMapping(value = ["/{pageId}"], produces = ["application/json", "application/hal+json"])
    fun getPageById(@PathVariable("pageId") pageId: String): ResponseEntity<Any> {
        return try {
            val page = pageService.findById(pageId)
            val dto = PageDto(id = page.id, order = page.order, imageUrl = page.imageUrl, chapterId = page.chapter.id)

            dto.add(linkTo(methodOn(this::class.java).getPageById(page.id)).withSelfRel())
            ResponseEntity(Resource(dto), HttpStatus.OK)
        } catch (e: PaperShelfException) {
            val message = Message(status = 404, error = "Not Found", message = e.message)
            ResponseEntity(message, HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping(consumes = ["application/json"])
    fun addPage(@RequestBody body: PageDto): ResponseEntity<Any> {
        return try {
            if (StringUtils.isEmpty(body.chapterId)) {
                throw ResourceException("chapterId cannot be empty")
            }
            val page = Page(order = body.order, imageUrl = body.imageUrl, chapter = chapterService.findById(body.chapterId))

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
            page.imageUrl = body.imageUrl
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