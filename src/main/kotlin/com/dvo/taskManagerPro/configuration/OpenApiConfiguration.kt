package com.dvo.taskManagerPro.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfiguration {

    @Bean
    fun openAPIDescription(): OpenAPI {
        val localhostServer = Server()
        localhostServer.url = "http://localhost:8080"
        localhostServer.description = "Local environment"
        val contact = Contact()
        contact.name = "Dzgoev Vadim"
        contact.email = "vadim-bmk@yandex.ru"
        val license = License().name("GNU AGPLv3").url("https://www.gnu.org/licenses/agpl-3.0.en.html")
        val info = Info()
            .title("Task Manager Pro")
            .version("1.0")
            .contact(contact)
            .description("API for task manager service")
            .termsOfService("http://example.term.url")
            .license(license)
        return OpenAPI().info(info).servers(listOf(localhostServer))

    }
}