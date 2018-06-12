package io.ermdev.papershelf.data.repository

import io.ermdev.papershelf.data.entity.Page
import org.springframework.data.jpa.repository.JpaRepository

interface PageRepository: JpaRepository<Page, String> {

}