# MeetMap 🌍  
**Социальная сеть с интеграцией карт для планирования встреч**

[![Kotlin Multiplatform](https://img.shields.io/badge/Kotlin-Multiplatform-%237F52FF?logo=kotlin)](https://kotlinlang.org/docs/multiplatform.html)
[![Firebase](https://img.shields.io/badge/Firebase-Cloud_Service-%23FFCA28?logo=firebase)](https://firebase.google.com/)

Социальная сеть, где пользователи отмечают места встреч на карте, общаются в чатах, обмениваются медиа и управляют друзьями.

---

## ✨ Возможности
- **Карта с метками**  
  Отмечайте места встреч, просматривайте детали, присоединяйтесь к событиям и прокладывайте маршруты.
- **Чаты**  
  Личные сообщения, отправка фото/видео, файлов и удаление сообщений.
- **Друзья**  
  Поиск пользователей, добавление в друзья, просмотр последних сообщений.
- **Уведомления**  
  Реaltime-оповещения о событиях через Firebase Cloud Messaging.

---

## 📸 Скриншоты

| Карта с метками | Детали метки | Чат с друзьями |
|-----------------|--------------|----------------|
| <img src="https://github.com/user-attachments/assets/c16d7a9b-fef7-4838-998b-1705b64e0153" width="300"> | <img src="https://github.com/user-attachments/assets/068defd3-df8e-43b2-91c2-a352310f3d42" width="300"> | <img src="https://github.com/user-attachments/assets/fa8444cf-5d15-4b0e-bc5e-de7bc5da701f" width="300"> |

| Список друзей | Управление сообщениями |
|---------------|------------------------|
| <img src="https://github.com/user-attachments/assets/bd2a3e36-a2ee-4bbd-b6f5-f9fdd6c00f81" width="300"> | <img src="https://github.com/user-attachments/assets/e72dfc9a-a654-45b8-8381-12a945238030" width="300"> |

---

## 🛠 Технологии и библиотеки

### Клиент (Kotlin Multiplatform)
- **Карты**: Google Maps SDK + [кастомная библиотека MapMarker](https://github.com/ILYAPROKOFEV101/mapmarker) для анимированных меток
- **API**: Ktor Client, Retrofit, OkHttp
- **База данных**: SQLDelight (кеширование данных)
- **Архитектура**: MVVM с общим кодом для iOS/Android

### Сервер
- **Фреймворк**: Ktor (REST API + WebSockets)
- **Аутентификация**: Firebase Admin
- **Базы данных**: PostgreSQL (основные данные), Redis (кеш и сессии)
- **Хостинг**: Railway

### Инфраструктура
- **Уведомления**: Firebase Cloud Messaging
- **Мониторинг**: Sentry, Firebase Crashlytics

---

## 🏗 Архитектура
**MVVM** + **Kotlin Multiplatform**  
- Общий код для бизнес-логики (70% кодовой базы)
- Платформозависимые модули для UI (SwiftUI на iOS, Jetpack Compose на Android)
- Реaltime-синхронизация через WebSockets

---





