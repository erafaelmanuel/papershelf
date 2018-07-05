package io.ermdev.papershelf.data.repository

import io.ermdev.papershelf.data.entity.Chapter
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ChapterRepository : JpaRepository<Chapter, String> {

    @Query("SELECT p FROM Page AS p JOIN p.chapter AS c WHERE c.id=:chapterId")
    fun findPagesById(@Param("chapterId") chapterId: String,
                      pageable: Pageable): Page<io.ermdev.papershelf.data.entity.Page>
}