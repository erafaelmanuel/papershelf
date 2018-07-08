package com.rem.papershelf.data.chapter

import com.rem.papershelf.data.book.Book
import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestParam
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class ChapterSpecification(@RequestParam("name") private val name: String? = null,
                           @RequestParam("level") private val level: Double? = null,
                           @RequestParam("bookId") private val bookId: String? = null) : Specification<Chapter> {

    override fun toPredicate(root: Root<Chapter>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        val predicates: MutableSet<Predicate> = HashSet()

        if (!StringUtils.isEmpty(name)) {
            predicates.add(builder.like(root.get<String>("name"), "%$name%"))
        }
        if (level != null && level > -1) {
            predicates.add(builder.equal(root.get<Double>("level"), level))
        }
        if (!StringUtils.isEmpty(bookId)) {
            val join = root.join<Chapter, Book>("book")
            predicates.add(builder.equal(join.get<String>("id"), bookId))
        }
        return when {
            predicates.size > 0 -> builder.or(*predicates.toTypedArray())
            else -> null
        }
    }
}