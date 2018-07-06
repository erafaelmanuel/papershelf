package io.ermdev.papershelf.data.genre

import org.springframework.data.jpa.repository.JpaRepository

interface GenreRepository : JpaRepository<Genre, String> {

}