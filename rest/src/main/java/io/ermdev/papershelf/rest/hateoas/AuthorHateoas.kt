package io.ermdev.papershelf.rest.hateoas

import io.ermdev.papershelf.rest.controller.AuthorController
import org.springframework.hateoas.Link
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

class AuthorHateoas {

    companion object {
        fun getSelfLink(id: String): Link {
            return linkTo(AuthorController::class.java)
                    .slash(id)
                    .withSelfRel()
        }
    }
}