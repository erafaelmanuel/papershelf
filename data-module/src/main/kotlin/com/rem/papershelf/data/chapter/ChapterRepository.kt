package com.rem.papershelf.data.chapter

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ChapterRepository : JpaRepository<Chapter, String>, JpaSpecificationExecutor<Chapter> {

    @Query("select p from Page as p join p.chapter as c where c.id = :chapterId")
    fun findPagesById(@Param("chapterId") chapterId: String, pageable: Pageable)
            : Page<com.rem.papershelf.data.page.Page>
}