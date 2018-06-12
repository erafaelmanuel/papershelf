package io.ermdev.papershelf.data.repository

import io.ermdev.papershelf.data.entity.Chapter
import org.springframework.data.jpa.repository.JpaRepository

interface ChapterRepository : JpaRepository<Chapter, String> {

}