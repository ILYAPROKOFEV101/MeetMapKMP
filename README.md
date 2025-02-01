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

| Карта с метками | Детали метки | Список друзей |
|-----------------|--------------|----------------|
| <img src="https://github.com/user-attachments/assets/dfe25d18-dc11-4b92-9302-8e5a738cc5c3" width="300"> | <img src="https://github.com/user-attachments/assets/9dcb4214-f2df-4ac2-a201-6a6b61a1ce4d" width="300"> | <img src="https://github.com/user-attachments/assets/da9272dc-ea7f-4cd4-80ad-2136392bc567" width="300"> |

| Чат с друзьями | Управление сообщениями |
|---------------|------------------------|
| <img src="https://github.com/user-attachments/assets/c975b9c0-99e8-45ad-b5a0-62156486e3bb" width="300"> | <img src="https://github.com/user-attachments/assets/db55c2cb-9472-4e3f-bc2d-36abc60d1871" width="300"> |

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





