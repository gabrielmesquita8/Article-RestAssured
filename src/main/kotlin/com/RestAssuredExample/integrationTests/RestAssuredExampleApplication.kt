package com.RestAssuredExample.integrationTests

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories


@EnableAutoConfiguration
@ComponentScan(basePackages = ["com.RestAssuredExample"])
@EnableJpaRepositories(basePackages = ["com.RestAssuredExample.repository"])
@EntityScan(basePackages = ["com.RestAssuredExample.model"])
class RestAssuredExampleApplication

fun main(args: Array<String>) {
	runApplication<RestAssuredExampleApplication>(*args)
}
