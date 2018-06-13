package io.ermdev.papershelf.rest.dto

import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.core.Relation

@Relation(value = "page", collectionRelation = "pages")
class PageDto(var id: String = "",
              var number: Short = 0,
              var image: String = ""): ResourceSupport()