package io.ermdev.papershelf.rest.hateoas

import io.ermdev.papershelf.rest.controller.ChapterController
import org.springframework.hateoas.Link
import org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo

class ChapterHateoas {

    companion object {
        fun getSelfLink(id: String): Link {
            return linkTo(ChapterController::class.java)
                    .slash(id)
                    .withSelfRel()
        }
    }
}