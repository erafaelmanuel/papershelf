package com.rem.papershelf.data.page

import com.rem.papershelf.data.book.Book
import org.springframework.data.jpa.domain.Specification
import org.springframework.util.StringUtils
import org.springframework.web.bind.annotation.RequestParam
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root

class PageSpecification(@RequestParam("order") private val order: Short? = null,
                        @RequestParam("chapterId") private val chapterId: String? = null) : Specification<Page> {

    override fun toPredicate(root: Root<Page>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        val predicates: MutableSet<Predicate> = HashSet()

        if (order != null && order > -1) {
            predicates.add(builder.equal(root.get<Short>("order"), "$order"))
        }
        if (!StringUtils.isEmpty(chapterId)) {
            val join = root.join<Page, Book>("chapter")
            predicates.add(builder.equal(join.get<String>("id"), chapterId))
        }
        return when {
            predicates.size > 0 -> builder.or(*predicates.toTypedArray())
            else -> null
        }
    }
}