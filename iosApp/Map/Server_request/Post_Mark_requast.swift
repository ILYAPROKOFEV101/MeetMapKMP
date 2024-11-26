//
//  Post_Mark_requast.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 06.08.2024.
//

import Foundation

func postMarkerData(urlString: String, marker: Marker, completion: @escaping (Result<Bool, Error>) -> Void) {
    
    guard let url = URL(string: urlString) else {
        print("Invalid URL")
        completion(.failure(NSError(domain: "", code: -1, userInfo: [NSLocalizedDescriptionKey: "Invalid URL"])))
        return
    }
    
    var request = URLRequest(url: url)
    request.httpMethod = "POST"
    request.setValue("application/json", forHTTPHeaderField: "Content-Type")
    
    do {
        let jsonData = try JSONEncoder().encode(marker)
        request.httpBody = jsonData
    } catch let error {
        print("JSON serialization error: \(error.localizedDescription)")
        completion(.failure(error))
        return
    }
    
    let task = URLSession.shared.dataTask(with: request) { data, response, error in
        if let error = error {
            print("Error: \(error.localizedDescription)")
            completion(.failure(error))
            return
        }
        
        guard let httpResponse = response as? HTTPURLResponse, httpResponse.statusCode == 200 else {
            print("Invalid response or status code")
            completion(.failure(NSError(domain: "", code: -1, userInfo: [NSLocalizedDescriptionKey: "Invalid response or status code"])))
            return
        }
        
        completion(.success(true))
    }
    
    task.resume()
}
