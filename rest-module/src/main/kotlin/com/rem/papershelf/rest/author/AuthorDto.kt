package com.rem.papershelf.rest.author

import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.core.Relation

@Relation(value = "author", collectionRelation = "authors")
class AuthorDto(var id: String = "",
                var name: String = "") : ResourceSupport()