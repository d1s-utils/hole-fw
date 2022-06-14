package dev.d1s.holefw

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class HoleFwApplication

fun main(args: Array<String>) {
    runApplication<HoleFwApplication>(*args)
}