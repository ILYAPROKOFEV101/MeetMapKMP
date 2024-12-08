//
//  Server.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 02.12.2024.
//

import UIKit
import common // Импортируем общий модуль KMP

class ViewController: UIViewController {

    private var webSocketClient: WebSocketClient?

    override func viewDidLoad() {
        super.viewDidLoad()

        // Инициализация клиента и подключение
        webSocketClient = WebSocketClient(url: "wss://example.com/websocket")
        connectWebSocket()
    }

    private func connectWebSocket() {
        // Используем корутину из общего кода для подключения
        webSocketClient?.connect(completionHandler: { error in
            if let error = error {
                print("Error connecting: \(error.localizedDescription)")
            } else {
                print("Connected to WebSocket")
            }
        })
        
        // Прослушивание входящих сообщений
        webSocketClient?.incomingMessages.collect { message in
            print("Received message: \(message)")
        }
    }

    @IBAction func sendMessage(_ sender: UIButton) {
        // Отправка сообщения через WebSocket
        webSocketClient?.sendMessage(message: "Hello from iOS!", completionHandler: { error in
            if let error = error {
                print("Error sending message: \(error.localizedDescription)")
            } else {
                print("Message sent successfully")
            }
        })
    }

    deinit {
        // Закрытие WebSocket при завершении работы
        webSocketClient?.disconnect(completionHandler: nil)
    }
}
