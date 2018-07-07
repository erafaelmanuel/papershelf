package io.ermdev.papershelf.data.author

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface AuthorRepository : JpaRepository<Author, String>, JpaSpecificationExecutor<Author> {

}