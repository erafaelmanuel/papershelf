package io.ermdev.papershelf.data.service

import io.ermdev.papershelf.data.entity.Page
import io.ermdev.papershelf.data.repository.PageRepository
import io.ermdev.papershelf.exception.EntityException
import org.springframework.stereotype.Service

@Service
class PageService(val pageRepository: PageRepository) {

    fun findAll(): List<Page> = pageRepository.findAll()

    fun findById(id: String): Page {
        return pageRepository.findById(id).orElseThrow({
            EntityException("No page with id '$id' exists!")
        })
    }

    fun save(page: Page) = pageRepository.save(page)

    fun deleteById(id: String) = pageRepository.deleteById(id)
}