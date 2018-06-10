package io.ermdev.papershelf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PaperShelfApplication

    fun main(args: Array<String>) {
        runApplication<PaperShelfApplication>(*args)
    }
