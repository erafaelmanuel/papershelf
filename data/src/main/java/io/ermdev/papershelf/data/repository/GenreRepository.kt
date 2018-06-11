package io.ermdev.papershelf.data.repository

import io.ermdev.papershelf.data.entity.Genre
import org.springframework.data.jpa.repository.JpaRepository

interface GenreRepository: JpaRepository<Genre, String> {

}