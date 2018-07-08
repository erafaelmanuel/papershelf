package com.rem.papershelf.data.page

import com.rem.papershelf.exception.EntityException
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service

@Service
class PageService(val pageRepository: PageRepository) {

    fun findAll(specification: Specification<Page>, pageable: Pageable): org.springframework.data.domain.Page<Page> {
        return pageRepository.findAll(specification, pageable)
    }

    fun findById(id: String): Page {
        return pageRepository.findById(id).orElseThrow({
            EntityException("No page with id '$id' exists!")
        })
    }

    fun save(page: Page) {
        pageRepository.save(page)
    }

    fun deleteById(id: String) {
        pageRepository.deleteById(id)
    }
}