package io.ermdev.papershelf.rest.hateoas

import io.ermdev.papershelf.rest.controller.PageController
import org.springframework.hateoas.Link
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

class PageHateoas {

    companion object {
        fun getSelfLink(id: String): Link {
            return linkTo(PageController::class.java)
                    .slash(id)
                    .withSelfRel()
        }
    }
}