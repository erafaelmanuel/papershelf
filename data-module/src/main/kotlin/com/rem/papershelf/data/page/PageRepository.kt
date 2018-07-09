package com.rem.papershelf.data.page

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface PageRepository : JpaRepository<Page, String>, JpaSpecificationExecutor<Page>