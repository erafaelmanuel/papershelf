package ermdev.papershelf

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PapershelfApplication

fun main(args: Array<String>) {
    runApplication<PapershelfApplication>(*args)
}
