package io.ermdev.papershelf.data.book

import io.ermdev.papershelf.data.author.Author
import io.ermdev.papershelf.data.chapter.Chapter
import io.ermdev.papershelf.data.genre.Genre
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BookRepository : JpaRepository<Book, String>, JpaSpecificationExecutor<Book> {

    @Query("select a from Author as a join a.books as b where b.id = :bookId")
    fun findAuthorsById(@Param("bookId") book: String, pageable: Pageable): Page<Author>

    @Query("select c from Chapter as c join c.book as b where b.id = :bookId")
    fun findChaptersById(@Param("bookId") bookId: String, pageable: Pageable): Page<Chapter>

    @Query("select g from Genre as g join g.books as b where b.id = :bookId")
    fun findGenresById(@Param("bookId") bookId: String, pageable: Pageable): Page<Genre>
}