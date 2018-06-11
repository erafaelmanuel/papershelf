package io.ermdev.papershelf.rest.dto

import org.springframework.hateoas.core.Relation

@Relation(collectionRelation = "authors")
class AuthorDto(var id: String = "",
                var name: String = "")