package io.ermdev.papershelf.data.repository

import io.ermdev.papershelf.data.entity.Book
import org.springframework.data.jpa.repository.JpaRepository

interface BookRepository : JpaRepository<Book, String> {

}