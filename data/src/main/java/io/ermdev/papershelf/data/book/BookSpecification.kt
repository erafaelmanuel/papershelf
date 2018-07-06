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

        predicates.add(builder.like(root.get<String>("title"), "%$title%"))
        predicates.add(builder.equal(root.get<String>("status"), status))

        return if (StringUtils.isEmpty(authorId) && StringUtils.isEmpty(genreId)) {
            if (StringUtils.isEmpty(title) && StringUtils.isEmpty(status)) {
                return null
            }
            builder.or(*predicates.toTypedArray())
        } else {
            val join1 = root.join<Book, Author>("authors", JoinType.LEFT)
            predicates.add(builder.equal(join1.get<String>("id"), authorId))

            val join2 = join1.parent.join<Book, Genre>("genres", JoinType.LEFT)
            predicates.add(builder.equal(join2.get<String>("id"), genreId))
            builder.or(*predicates.toTypedArray())
        }
    }

}