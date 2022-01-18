package com.eathub.eathub

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

const val BASE_PACKAGE = "com.eathub.eathub"

@ConfigurationPropertiesScan
@SpringBootApplication
class EathubApplication

fun main(args: Array<String>) {
    runApplication<EathubApplication>(*args)
}
