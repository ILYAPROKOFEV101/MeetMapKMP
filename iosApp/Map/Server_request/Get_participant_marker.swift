//
//  Get_participant_marker.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 14.08.2024.
//

import Foundation



import Foundation



func fetchParticipantMarks(uid: String, key: String, completion: @escaping (Result<[Marker], Error>) -> Void) {
    let urlString = "https://meetmap.up.railway.app/get/participantmark/\(uid)/\(key)"
    
    guard let url = URL(string: urlString) else {
        print("Invalid URL")
        completion(.failure(NSError(domain: "Invalid URL", code: 400, userInfo: nil)))
        return
    }

    let task = URLSession.shared.dataTask(with: url) { data, response, error in
        if let error = error {
            completion(.failure(error))
            return
        }

        if let httpResponse = response as? HTTPURLResponse {
            guard (200...299).contains(httpResponse.statusCode) else {
                print("HTTP Status Code: \(httpResponse.statusCode)")
                completion(.failure(NSError(domain: "Server error", code: httpResponse.statusCode, userInfo: nil)))
                return
            }
        }

        guard let data = data else {
            print("No data received")
            completion(.failure(NSError(domain: "No data", code: 204, userInfo: nil)))
            return
        }

        do {
            let decoder = JSONDecoder()
            // Если данные дат/времени в формате ISO 8601, то можно использовать:
            // decoder.dateDecodingStrategy = .iso8601
            let participantMarks = try decoder.decode([Marker].self, from: data)
            completion(.success(participantMarks))
        } catch {
            print("Decoding error: \(error)")
            completion(.failure(error))
        }
    }
    
    task.resume()
}
