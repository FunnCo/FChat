package org.example.fchat

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class FChatApplication

fun main(args: Array<String>) {
    runApplication<FChatApplication>(*args)
}
