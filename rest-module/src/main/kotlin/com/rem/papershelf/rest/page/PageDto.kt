package com.rem.papershelf.rest.page

import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.core.Relation

@Relation(value = "page", collectionRelation = "pages")
class PageDto(var id: String = "",
              var order: Short = 0,
              var imageUrl: String = "",
              var chapterId: String = ""): ResourceSupport()