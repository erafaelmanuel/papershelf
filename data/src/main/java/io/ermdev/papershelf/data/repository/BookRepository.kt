package io.ermdev.papershelf.data.repository

import io.ermdev.papershelf.data.entity.Book
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BookRepository : JpaRepository<Book, String> {

    @Query("select T1 from Book as T1 join T1.author as T2 where T2.id=:authorId")
    fun findByAuthorId(@Param("authorId") authorId: String): List<Book>
}