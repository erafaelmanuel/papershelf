package io.ermdev.papershelf.data.page

import io.ermdev.papershelf.data.page.Page
import io.ermdev.papershelf.data.page.PageRepository
import io.ermdev.papershelf.exception.EntityException
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PageService(val pageRepository: PageRepository) {

    fun findAll(): List<Page> = pageRepository.findAll()

    fun findAll(pageable: Pageable): org.springframework.data.domain.Page<Page> {
        return pageRepository.findAll(pageable)
    }

    fun findById(id: String): Page {
        return pageRepository.findById(id).orElseThrow({
            EntityException("No page with id '$id' exists!")
        })
    }

    fun save(page: Page) = pageRepository.save(page)

    fun deleteById(id: String) = pageRepository.deleteById(id)
}