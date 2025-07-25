# Task Manager Pro

**Task Manager Pro** — это RESTful веб-приложение для управления задачами и пользователями в проектах. Поддерживает аутентификацию, роли пользователей (ADMIN, MANAGER, EMPLOYEE), разграничение доступа через Spring Security и проверку прав с помощью AOP.

## 📌 Возможности

- Регистрация и аутентификация пользователей
- Роли: `ROLE_ADMIN`, `ROLE_MANAGER`, `ROLE_EMPLOYEE`
- Управление пользователями (создание, обновление, просмотр)
- CRUD-операции над задачами:
    - Назначение исполнителей
    - Метки, приоритеты, статусы, дедлайны
- Управление проектами
- Управление метками и комментариями
- Ограничение доступа на основе ролей и прав с помощью AOP
- Swagger (OpenAPI) для документирования API

## 🚀 Технологии

- Kotlin
- Spring Boot
- Spring Security
- Spring AOP
- Spring Data JPA
- PostgreSQL
- MapStruct
- Swagger / OpenAPI
- JUnit 5 + Mockito + MockMvc

## 🔐 Безопасность

- Аутентификация с помощью Spring Security
- Пользовательские роли и проверка прав
- AOP-аннотации:
    - `@CheckAccessToUser`
    - `@CheckAccessToTask`
    - `@CheckAccessToComment`
- Получение пользователя через `SecurityContext`

##  📄 Swagger
  API-документация доступна по адресу:
 
http://localhost:8080/swagger-ui/index.html