package io.ermdev.papershelf.data.author

import io.ermdev.papershelf.data.author.Author
import org.springframework.data.jpa.repository.JpaRepository

interface AuthorRepository: JpaRepository<Author, String> {

}