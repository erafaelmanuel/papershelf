package com.rem.papershelf.data.chapter

import com.rem.papershelf.exception.EntityException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*

@Service
class ChapterService(@Autowired val chapterRepository: ChapterRepository) {

    fun findAll(specification: Specification<Chapter>, pageable: Pageable): Page<Chapter> {
        return chapterRepository.findAll(specification, pageable)
    }

    @Throws(exceptionClasses = [EntityException::class])
    fun findById(id: String): Chapter {
        return chapterRepository.findById(id).orElseThrow({
            EntityException("No chapter with id '$id' exists!")
        })
    }

    fun findPagesById(chapterId: String, pageable: Pageable): Page<com.rem.papershelf.data.page.Page> {
        return chapterRepository.findPagesById(chapterId, pageable)
    }

    @Throws(exceptionClasses = [EntityException::class])
    fun save(chapter: Chapter) {
        if (StringUtils.isEmpty(chapter.name)) {
            throw EntityException("name cannot be empty")
        }
        if (!chapter.name.matches(Regex("^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$"))) {
            throw EntityException("name cannot contain special characters")
        }
        if (StringUtils.isEmpty(chapter.id)) {
            chapter.id = UUID.randomUUID().toString()
        }
        chapterRepository.save(chapter)
    }

    fun deleteById(id: String) {
        chapterRepository.deleteById(id)
    }
}