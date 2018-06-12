package io.ermdev.papershelf.rest.dto

import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.core.Relation

@Relation(value = "book", collectionRelation = "books")
class BookDto(var id: String = "",
              var title: String = "",
              var status: String = "",
              var summary: String = "") : ResourceSupport()