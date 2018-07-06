package io.ermdev.papershelf.data.page

import io.ermdev.papershelf.data.page.Page
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PageRepository : JpaRepository<Page, String> {

    @Query("SELECT p FROM Page as p ORDER BY p.order ASC")
    override fun findAll(): List<Page>
}