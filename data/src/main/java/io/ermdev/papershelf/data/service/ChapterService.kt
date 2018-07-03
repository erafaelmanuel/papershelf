package io.ermdev.papershelf.data.service

import io.ermdev.papershelf.data.entity.Chapter
import io.ermdev.papershelf.data.repository.ChapterRepository
import io.ermdev.papershelf.exception.EntityException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*

@Service
class ChapterService(@Autowired val chapterRepository: ChapterRepository) {

    fun findAll(): List<Chapter> = chapterRepository.findAll()

    fun findAll(pageable: Pageable): Page<Chapter> {
        return chapterRepository.findAll(pageable)
    }

    fun findById(id: String): Chapter {
        return chapterRepository.findById(id).orElseThrow({
            EntityException("No chapter with id '$id' exists!")
        })
    }

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

    fun deleteById(id: String) = chapterRepository.deleteById(id)
}