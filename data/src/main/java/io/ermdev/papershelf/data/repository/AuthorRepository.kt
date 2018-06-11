package io.ermdev.papershelf.data.repository

import io.ermdev.papershelf.data.entity.Author
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorRepository: JpaRepository<Author, String> {
}