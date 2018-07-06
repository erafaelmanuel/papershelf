package io.ermdev.papershelf.data.book

import io.ermdev.papershelf.data.author.Author
import io.ermdev.papershelf.data.genre.Genre
import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestParam
import javax.persistence.criteria.*

class BookSpecification(@RequestParam("title") private val title: String = "",
                        @RequestParam("status") private val status: String = "",
                        @RequestParam("authorId") private val authorId: String = "",
                        @RequestParam("genreId") private val genreId: String = "") : Specification<Book> {

    override fun toPredicate(root: Root<Book>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        val predicates: MutableSet<Predicate> = HashSet()

        if (!StringUtils.isEmpty(title)) {
            predicates.add(builder.like(root.get<String>("title"), "%$title%"))
        }
        if (!StringUtils.isEmpty(status)) {
            predicates.add(builder.equal(root.get<String>("status"), status))
        }
        if (!StringUtils.isEmpty(authorId)) {
            val join = root.join<Book, Author>("authors", JoinType.LEFT)
            predicates.add(builder.equal(join.get<String>("id"), authorId))
        }
        if (!StringUtils.isEmpty(genreId)) {
            val join = root.join<Book, Genre>("genres", JoinType.LEFT)
            predicates.add(builder.equal(join.get<String>("id"), genreId))
        }
        return when {
            predicates.size > 0 -> builder.or(*predicates.toTypedArray())
            else -> null
        }
    }

}