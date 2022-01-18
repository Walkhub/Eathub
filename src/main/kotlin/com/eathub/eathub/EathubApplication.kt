package com.eathub.eathub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EathubApplication

fun main(args: Array<String>) {
    runApplication<EathubApplication>(*args)
}
