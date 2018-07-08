package com.rem.papershelf.data.book

import com.rem.papershelf.data.author.Author
import com.rem.papershelf.data.genre.Genre
import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestParam
import javax.persistence.criteria.*

class BookSpecification(@RequestParam("title") private val title: String? = null,
                        @RequestParam("status") private val status: String? = null,
                        @RequestParam("authorId") private val authorId: String? = null,
                        @RequestParam("genreId") private val genreId: String? = null) : Specification<Book> {

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