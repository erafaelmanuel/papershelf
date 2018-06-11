package io.ermdev.papershelf.rest.dto

import org.springframework.hateoas.core.Relation

@Relation(collectionRelation = "genres")
class GenreDto(var id: String = "",
               var name: String = "",
               var description: String = "")