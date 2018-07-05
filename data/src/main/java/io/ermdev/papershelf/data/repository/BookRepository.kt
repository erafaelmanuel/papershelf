package io.ermdev.papershelf.data.repository

import io.ermdev.papershelf.data.entity.Author
import io.ermdev.papershelf.data.entity.Book
import io.ermdev.papershelf.data.entity.Chapter
import io.ermdev.papershelf.data.entity.Genre
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface BookRepository : JpaRepository<Book, String> {

    @Query("select T1 from Book as T1 join T1.authors as T2 where T2.id=:authorId")
    fun findByAuthorId(@Param("authorId") authorId: String, pageable: Pageable): Page<Book>

    @Query("select T1 from Book as T1 join T1.genres as T2 where T2.id=:genreId")
    fun findByGenreId(@Param("genreId") genreId: String, pageable: Pageable): Page<Book>

    @Query("select T1 from Book as T1 join T1.authors as T2 join T1.genres as T3 where T2.id=:authorId " +
            "and T3.id=:genreId")
    fun findByAuthorIdAndGenreId(@Param("authorId") authorId: String, @Param("genreId") genreId: String,
                                 pageable: Pageable): Page<Book>

    @Query("SELECT a FROM Author AS a JOIN a.books AS b WHERE b.id=:bookId")
    fun findAuthorsById(@Param("bookId") book: String, pageable: Pageable?): Page<Author>

    @Query("SELECT c FROM Chapter AS c JOIN c.book AS b WHERE b.id=:bookId")
    fun findChaptersById(@Param("bookId") bookId: String, pageable: Pageable?): Page<Chapter>

    @Query("SELECT g FROM Genre AS g JOIN g.books AS b WHERE b.id=:bookId")
    fun findGenresById(@Param("bookId") bookId: String, pageable: Pageable?): Page<Genre>
}