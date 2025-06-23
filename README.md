# Variant Annotator Service

**Variant Annotator** — это REST-сервис на Java + Spring Boot, предназначенный для извлечения аннотированных генетических вариантов из архива ClinVar (`.tsv.gz` + `.tbi`) с использованием индексированного доступа через Tabix.

## Возможности

- Поиск аннотаций по координатам (`rac`, `lap`, `rap`, `refkey`)
- Быстрый доступ к большим архивам ClinVar через индекс Tabix
- Поддержка JSON-ответов и структурированных ошибок
- Режим отладки: просмотр всех аннотаций
- Простое локальное тестирование и REST-интерфейс

## Структура проекта
- `AnnotationController` — REST API для получения аннотаций
- `AnnotationService` — логика поиска по Tabix
- `AnnotationRecord` — модель строки файла
- `ErrorResponse` — формат ошибок
- `application.properties` — путь к архиву с аннотациями
## Установка и запуск
1. Установите Java 21 и Maven
2. Скачайте `clinvar_test.tsv.gz` и `clinvar_test.tsv.gz.tbi`
3. Укажите путь к архиву в `src/main/resources/application.properties`:
```properties
annotation.file.path=C:/Users/user/Desktop/clinvar/clinvar_test.tsv.gz
```
http://localhost:8080/info?rac=NC_000001.11&lap=925951&rap=925953&refkey=A - Локальный хост

## Сборка и запуск с помощью Docker
Для удобного запуска сервис можно собрать и запустить через Docker:
1. Сборка проекта
```
mvn clean package
```
2. Запуск контейнера (Docker Compose)
```
docker-compose up --build
```
3. Доступ к сервису
http://localhost:8080/info?rac=NC_000001.11&lap=925951&rap=925953&refkey=A
Структура:
Dockerfile — инструкция по сборке контейнера
docker-compose.yml — запуск с пробросом порта и монтированием архива

файл clinvar_test.tsv.gz находится в корне проекта