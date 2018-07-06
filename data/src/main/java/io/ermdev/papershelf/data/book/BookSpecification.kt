package io.ermdev.papershelf.data.book

import io.ermdev.papershelf.data.author.Author
import io.ermdev.papershelf.data.genre.Genre
import org.springframework.data.jpa.domain.Specification
import org.springframework.web.bind.annotation.RequestParam
import javax.persistence.criteria.*

class BookSpecification(@RequestParam("id") var id: String = "",
                        @RequestParam("title") var title: String = "",
                        @RequestParam("status") var status: String = "",
                        @RequestParam("authorId") private val authorId: String = "",
                        @RequestParam("genreId") private val genreId: String = "") : Specification<Book> {

    override fun toPredicate(root: Root<Book>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        val idPath = root.get<String>("id")
        val titlePath = root.get<String>("title")
        val statusPath = root.get<String>("status")

        val authorsPath: Path<String>
        val genresPath: Path<String>

        val joinBookAuthor: Join<Book, Author> = root.join<Book, Author>("authors", JoinType.LEFT)
        val joinBookGenre: Join<Book, Genre>

        authorsPath = joinBookAuthor.get("id")
        joinBookGenre = joinBookAuthor.parent.join<Book, Genre>("genres", JoinType.LEFT)
        genresPath = joinBookGenre.get("id")

        return joinBookGenre.on(
                builder.or(
                        builder.equal(idPath, id),
                        builder.equal(titlePath, title),
                        builder.equal(statusPath, status),
                        builder.equal(authorsPath, authorId),
                        builder.equal(genresPath, genreId)
                )).on
    }

}