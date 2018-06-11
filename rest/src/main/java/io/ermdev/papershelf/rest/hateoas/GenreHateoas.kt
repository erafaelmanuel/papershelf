package io.ermdev.papershelf.rest.hateoas

import io.ermdev.papershelf.rest.controller.GenreController
import org.springframework.hateoas.Link
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

class GenreHateoas {

    companion object {
        fun getSelfLink(id: String): Link {
            return linkTo(GenreController::class.java)
                    .slash(id)
                    .withSelfRel()
        }
    }
}