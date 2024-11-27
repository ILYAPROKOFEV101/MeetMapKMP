//
//  Get_Friends_just.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 20.08.2024.
//

import Foundation

import Foundation

// Модель данных для парсинга JSON
struct User: Codable {
    let userName: String
    let img: String
    let key: String

    enum CodingKeys: String, CodingKey {
        case userName = "user_name"
        case img
        case key
    }
}

// Переменная для хранения времени последнего запуска WebSocket
var lastWebSocketRunTime: Date?

// Функция для подключения к WebSocket и получения данных
func fetchData(from urlString: String) async -> [User]? {
    // Проверка времени последнего запуска WebSocket
    if let lastRunTime = lastWebSocketRunTime {
        let timeSinceLastRun = Date().timeIntervalSince(lastRunTime)
        if timeSinceLastRun < 10 {
            print("WebSocket уже запущен. Подождите \(10 - Int(timeSinceLastRun)) секунд.")
            return nil
        }
    }

    // Обновляем время последнего запуска WebSocket
    lastWebSocketRunTime = Date()

    guard let url = URL(string: urlString) else {
        print("Invalid URL")
        return nil
    }
    
    print("WebSocket URL: \(url)")
    let session = URLSession(configuration: .default)
    let webSocketTask = session.webSocketTask(with: url)
    
    // Массив для хранения полученных данных
    var users = [User]()
    
    print("Initial users array: \(users)")
    
    // Запускаем задачу WebSocket
    webSocketTask.resume()
    
    // Функция для получения сообщений
    func receiveMessage() async throws {
        while true {
            let result = try await webSocketTask.receive()
            switch result {
            case .string(let text):
                print("Received text message: \(text)")
                if let data = text.data(using: .utf8) {
                    do {
                        let user = try JSONDecoder().decode(User.self, from: data)
                        users.append(user)
                        print("User added: \(user)")
                    } catch {
                        print("Error parsing JSON: \(error)")
                    }
                }
            case .data(let data):
                print("Received binary data: \(data)")
            @unknown default:
                fatalError("Unknown WebSocket message type")
            }
        }
    }
    
    do {
        try await receiveMessage()
    } catch {
        print("Error receiving messages: \(error)")
    }
    
    // Завершаем соединение WebSocket после 10 секунд
    try? await Task.sleep(nanoseconds: 10_000_000_000)
    webSocketTask.cancel(with: .goingAway, reason: nil)
    
    // Возвращаем полученные данные
    return users
}
