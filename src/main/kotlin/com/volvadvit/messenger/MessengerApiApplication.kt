package com.volvadvit.messenger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MessengerApiApplication

fun main(args: Array<String>) {
	runApplication<MessengerApiApplication>(*args)
}