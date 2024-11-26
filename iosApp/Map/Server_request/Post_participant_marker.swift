//
//  Post_participant_marker.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 14.08.2024.
//

import Foundation

func sendPostRequest(uid: String, key: String, id: String) {
    // Формируем URL
    guard let url = URL(string: "https://meetmap.up.railway.app/became/participant/\(uid)/\(key)/\(id)") else {
        print("Invalid URL")
        return
    }
    
    print("Ссылка на ссылку по  даннымм \(url)")
    
    // Создаем запрос
    var request = URLRequest(url: url)
    request.httpMethod = "POST"
    
    // Устанавливаем заголовки, если это необходимо
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    
    // Если необходимо передать тело запроса, установите его здесь
    // let body: [String: Any] = ["key": "value"]
    // request.httpBody = try? JSONSerialization.data(withJSONObject: body, options: [])
    
    // Отправляем запрос
    let task = URLSession.shared.dataTask(with: request) { data, response, error in
        // Обрабатываем ответ
        if let error = error {
            print("Error: \(error.localizedDescription)")
            return
        }
        
        guard let httpResponse = response as? HTTPURLResponse,
              (200...299).contains(httpResponse.statusCode) else {
            print("Server error")
            return
        }
        
        if let data = data, let responseString = String(data: data, encoding: .utf8) {
            print("Response: \(responseString)")
        }
    }
    
    task.resume()
}
