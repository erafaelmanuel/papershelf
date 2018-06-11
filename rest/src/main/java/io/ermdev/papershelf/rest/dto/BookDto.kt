package io.ermdev.papershelf.rest.dto

import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.core.Relation

@Relation(collectionRelation = "books")
class BookDto(var id: String = "",
              var title: String = "") : ResourceSupport()