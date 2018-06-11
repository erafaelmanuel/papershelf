package io.ermdev.papershelf.rest.hateoas

import io.ermdev.papershelf.rest.controller.BookController
import org.springframework.hateoas.Link
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

class BookHateoas {

    companion object {
        fun getSelfLink(id: String): Link {
            return linkTo(BookController::class.java)
                    .slash(id)
                    .withSelfRel()
        }

        fun getAuthorLink(bookId: String): Link {
            return linkTo(BookController::class.java)
                    .slash(bookId)
                    .slash("author")
                    .withRel("author")
        }
    }
}