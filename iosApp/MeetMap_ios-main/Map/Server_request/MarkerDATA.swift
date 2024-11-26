//
//  MarkerDATA.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 03.08.2024.
//
import Foundation
import CoreLocation

struct Marker: Codable, Hashable {
    let key: String
    let username: String
    let imguser: String
    let photomark: String
    let street: String
    let id: String
    let lat: Double
    let lon: Double
    let name: String
    let whatHappens: String
    let startDate: String
    let endDate: String
    let startTime: String
    let endTime: String
    let participants: Int
    let access: Bool
}

extension Marker {
    var locationCoordinate2D: CLLocationCoordinate2D {
        return CLLocationCoordinate2D(latitude: lat, longitude: lon)
    }
}

// Массив для хранения меток и множество для хранения идентификаторов
var markers = [Marker]()
var uniqueIDs = Set<String>()

func fetchMarkers(urlString: String, completion: @escaping ([Marker]) -> Void) {
    guard let url = URL(string: urlString) else {
        print("Invalid URL")
        return
    }
    print("Пришедшие данные \(url)")
    let task = URLSession.shared.dataTask(with: url) { data, response, error in
        if let error = error {
            print("Error: \(error.localizedDescription)")
            return
        }

        guard let data = data else {
            print("No data")
            return
        }

        // Отладка: Выводим полученные данные в консоль
//        if let responseString = String(data: data, encoding: .utf8) {
//            print("Response Data: \(responseString)")
//        }

        do {
            // Декодирование данных в [Marker]
            let fetchedMarkers = try JSONDecoder().decode([Marker].self, from: data)
            completion(fetchedMarkers)
        } catch {
            print("JSON decoding error: \(error.localizedDescription)")
        }
    }

    task.resume()
}



func storeUniqueMarkers(_ fetchedMarkers: [Marker]) {
    for marker in fetchedMarkers {
        if uniqueIDs.contains(marker.id) {
            // Если id метки уже существует в множестве, пропускаем добавление
            //  print("Метка с id \(marker.id) уже существует")
        } else {
            // Добавляем id метки в множество и саму метку в массив
            uniqueIDs.insert(marker.id)
            markers.append(marker)
        }
    }
    saveMarkersToUserDefaults()
}

func saveMarkersToUserDefaults() {
    let encoder = JSONEncoder()
    if let encodedMarkers = try? encoder.encode(markers) {
        UserDefaults.standard.set(encodedMarkers, forKey: "storedMarkers")
    }
}

func loadMarkersFromUserDefaults() {
    let decoder = JSONDecoder()
    if let savedMarkersData = UserDefaults.standard.data(forKey: "storedMarkers") {
        if let savedMarkers = try? decoder.decode([Marker].self, from: savedMarkersData) {
            markers = savedMarkers
            uniqueIDs = Set(savedMarkers.map { $0.id })
        }
    }
}

// Функция для получения данных маркера по id
func getMarkerData(by id: String) -> Marker? {
    return markers.first { $0.id == id }
}
