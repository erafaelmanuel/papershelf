package io.ermdev.papershelf.rest.chapter

import org.springframework.hateoas.ResourceSupport
import org.springframework.hateoas.core.Relation
import java.sql.Timestamp

@Relation(value = "chapter", collectionRelation = "chapters")
class ChapterDto(var id: String = "",
                 var name: String = "",
                 var index: Short = 0,
                 var uploadDate: Timestamp = Timestamp(System.currentTimeMillis()),
                 var bookId: String = "") : ResourceSupport()