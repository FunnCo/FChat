package org.example.fchat

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@OpenAPIDefinition(
    info = Info(
        title = "FChat",
        description = "Это сервис, с помощью которого можно реализовать простой чат. Если вы найдете какие-то баги или опечатки, то можете написать в тг @FunnCoTS, и, если баг мешает вам выполнять задание, сообщите об этом Ирине Александровне или ее ассистенту. Не факт, что баг будет исправлен быстро," +
                " но о нем станет хотя бы известно. \n\n Что касается авторизации: перед токеном обязательно нужно вставлять слово Bearer. Т.е. там, где будет требоваться токен авторизации, необходимо отправлять" +
                "что-то в формате 'Bearer <токен>'\n\n P.S. (отсутвие проверки на корректность почты - это не баг. Невозможность удалить своего пользователя и свои сообщения - это тоже не баг. Так и задуманно)",
        version = "1.0",
    )
)
@SpringBootApplication
class FChatApplication

fun main(args: Array<String>) {
    runApplication<FChatApplication>(*args)
}
