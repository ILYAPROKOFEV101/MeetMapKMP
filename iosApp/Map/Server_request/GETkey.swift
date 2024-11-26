//
//  GETkey.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 03.08.2024.
//

import Foundation

import Foundation

// Обновленная функция checkUser с замыканием
func checkUser(uid: String, completion: @escaping (String?) -> Void) {
    guard let url = URL(string: "https://meetmap.up.railway.app/checkUser/\(uid)") else {
        print("Invalid URL")
        completion(nil)
        return
    }
    print("Данные пл ссылки котроый пришли \(url)")
    
    var request = URLRequest(url: url)
    request.httpMethod = "GET"
    
    let session = URLSession.shared
    
    let task = session.dataTask(with: request) { data, response, error in
        if let error = error {
            print("Error: \(error)")
            completion(nil)
            return
        }
        
        guard let data = data else {
            print("No data")
            completion(nil)
            return
        }
        
        // Преобразование данных в строку
        if let responseString = String(data: data, encoding: .utf8) {
            print("Received Key: \(responseString)")
            
            // Сохранение ключа в UserDefaults
            UserDefaults.standard.set(responseString, forKey: "responseKey")
            completion(responseString)
        } else {
            print("Failed to convert data to string")
            completion(nil)
        }
    }
    
    task.resume()
}


func retrieveKey() -> String? {
    let savedKey = UserDefaults.standard.string(forKey: "responseKey")
    if let key = savedKey {
        print("Retrieved Key from UserDefaults: \(key)")
    } else {
        print("No key found in UserDefaults")
    }
    return savedKey
}
