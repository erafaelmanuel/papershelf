package com.rem.papershelf.data.genre

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface GenreRepository : JpaRepository<Genre, String>, JpaSpecificationExecutor<Genre>