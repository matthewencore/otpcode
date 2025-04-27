
# OTP Project Documentation

## Описание проекта
OTP — это Spring Boot приложение для:
- Генерации и валидации одноразовых кодов (OTP)
- Интеграции с Telegram ботом для отправки уведомлений
- Отправки кодов по Email и через SMPP шлюзы
- Управления пользователями и их ролями
- Аутентификации через JWT токены

Проект построен с применением Spring Security, JPA, REST API и асинхронной обработки сообщений.

## Технологии проекта
- Java 17+
- Spring Boot 3.x
- Spring Security
- Spring Data JPA (Hibernate)
- PostgreSQL
- JWT (JSON Web Token)
- Telegram Bots API
- Email (SMTP)
- SMPP Protocol
- Maven

## Структура проекта

| Путь | Назначение |
|:-----|:-----------|
| controller/ | REST API контроллеры |
| service/ | Бизнес-логика |
| repository/ | Работа с базой данных |
| models/ | Сущности, DTO, исключения |
| config/ | Конфигурации безопасности и интеграций |
| utils/ | Утилиты: генерация OTP, планировщики |

---

# Основные компоненты

## Безопасность (JWT)
- Генерация Access/Refresh токенов при логине
- JWT-аутентификация через фильтр `JwtAuthFilter`
- Роли пользователей (`ROLE_ADMIN`, `ROLE_USER`)

## Отправка сообщений
- Email: через SMTP (`EmailService`)
- Telegram: через бота (`OtpTelegramBot`)
- SMPP: для SMS-сообщений (`SMPPService`)

## Генерация OTP кодов
- Создание одноразовых паролей
- Автоматическая чистка просроченных кодов через планировщик (`OtpSchedule`)

---

# Контроллеры

| Контроллер | Основные функции |
|:-----------|:-----------------|
| AuthController | Страницы `/login` и `/register` |
| AuthApiController | REST API для логина `/api/auth/login` и регистрации `/api/auth/register` |
| MainController | Главная страница `/` |
| AdminController | Управление пользователями и настройками OTP через `/api/admin` |
| OTPCodeController | Валидация OTP кодов `/api/otp/validate` |
| TelegramRestController | Работа с Telegram: отправка сообщений, регистрация chat_id `/api/telegram/...` |
| EmailController | Отправка кода на Email `/api/email/send-otp` |
| SMPPController | Отправка кода через SMPP `/api/smpp/send-otp` |
| FileController | Генерация временного файла `/api/file/create` |
| ExceptionHandler | Глобальная обработка исключений в API |

---

# Модели

| Модель | Описание |
|:-------|:---------|
| User | Сущность пользователя (логин, пароль, роль) |
| RoleUser | Роли пользователей |
| OTPCode | Сущность одноразового пароля (код, статус, время жизни) |
| OtpConfig | Конфигурация генерации OTP кодов |
| TelegramChats | Модель для хранения chat_id пользователей Telegram |
| StatusOtp | Enum: статус OTP кода (ACTIVE, USED, EXPIRED) |

## DTO

| DTO | Описание |
|:----|:---------|
| LoginRequest | DTO для логина (username, password) |
| LoginResponse | DTO для ответа после логина (accessToken, refreshToken) |
| UserDTO | Упрощенное представление пользователя |
| ConfigOTPCode | Конфигурация OTP генерации в API |
| EmailDTO | Параметры email отправки (адрес, тема, текст) |
| SMPP_DTO | Параметры SMPP отправки (номер телефона, текст) |
| TelegramChatId | DTO для привязки chat_id |

---

# Сервисы

| Сервис | Основная задача |
|:-------|:----------------|
| AuthUserService | Регистрация и авторизация пользователей |
| UserService | Управление пользователями |
| RoleService | Управление ролями пользователей |
| OTPCodeService | Генерация, валидация и очистка OTP кодов |
| ConfigOTPCodeService | Настройка параметров генерации кодов |
| TelegramService | Отправка сообщений в Telegram |
| EmailService | Отправка сообщений на Email |
| SMPPService | Отправка SMS сообщений |
| FileService | Генерация/обработка файлов |

---

# Утилиты

| Класс | Назначение |
|:------|:-----------|
| OtpGenerator | Генерация случайных OTP кодов |
| OtpSchedule | Планировщик для удаления просроченных OTP кодов |

---

# Необходимые настройки в application.yaml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/otp_db
    username: your_db_user
    password: your_db_password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.PostgreSQLDialect

jwt:
  secret: your-super-secret-key
  expiration: 3600000 # время жизни токена в миллисекундах

spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your_email@gmail.com
    password: your_email_password
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true

smpp:
  host: your_smpp_gateway_host
  port: 2775
  system-id: your_system_id
  password: your_smpp_password
  system-type: your_system_type
```
Телеграм бота оставил настроенным

---

# Полные маршруты проекта

| HTTP Метод | Путь | Назначение |
|:-----------|:-----|:-----------|
| POST | `/register` | Регистрация нового пользователя через форму |
| POST | `/api/auth/login` | Аутентификация пользователя (JWT) |
| POST | `/api/auth/register` | Регистрация нового пользователя через API |
| GET | `/` | Главная страница |
| GET | `/api/telegram/get/status-link` | Проверка статуса привязки Telegram chat_id |
| POST | `/api/telegram/send-telegram` | Отправка сообщения в Telegram |
| POST | `/api/telegram/fill-chat-id` | Регистрация chat_id пользователя |
| POST | `/api/otp/validate` | Проверка OTP кода |
| POST | `/api/admin/otp-config` | Создание/настройка конфигурации OTP |
| POST | `/api/admin/get-all-users` | Получение списка пользователей |
| DELETE | `/api/admin/delete/{id}` | Удаление пользователя по ID |
| POST | `/api/email/send-otp` | Отправка OTP кода на Email |
| POST | `/api/smpp/send-otp` | Отправка OTP кода через SMPP |
| GET | `/api/file/create` | Генерация тестового файла |

---

# Инструкция по запуску проекта

1. Установить PostgreSQL, создать базу данных `otp_db`
2. Заполнить файл `application.yaml` по примеру
3. Собрать проект командой:
   ```bash
   mvn clean install
   ```
4. Запустить проект:
   ```bash
   mvn spring-boot:run
   ```
5. API доступно на `http://localhost:8080`

---
