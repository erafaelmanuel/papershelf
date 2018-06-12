package io.ermdev.papershelf.data.service

import io.ermdev.papershelf.data.entity.Author
import io.ermdev.papershelf.data.repository.AuthorRepository
import io.ermdev.papershelf.exception.EntityException
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import java.util.*

@Service
class AuthorService(val authorRepository: AuthorRepository) {

    fun findAll(): List<Author> = authorRepository.findAll()

    fun findById(id: String): Author {
        return authorRepository.findById(id).orElseThrow({
            EntityException("No author with id '$id' exists!")
        })
    }

    fun save(author: Author) {
        if (StringUtils.isEmpty(author.name)) {
            throw EntityException("name cannot be empty")
        }
        if (!author.name.matches(Regex("^[a-zA-Z0-9]+( [a-zA-Z0-9]+)*$"))) {
            throw EntityException("name cannot contain special characters")
        }
        if (StringUtils.isEmpty(author.id)) {
            author.id = UUID.randomUUID().toString()
        }
        authorRepository.save(author)
    }

    fun deleteById(id: String) = authorRepository.deleteById(id)
}