//
//  MarkerData.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 29.07.2024.
//

import Foundation
import MapKit
import CoreLocation

struct Coordinate: Codable {
    let latitude: Double
    let longitude: Double
    
    init(_ coordinate: CLLocationCoordinate2D) {
        self.latitude = coordinate.latitude
        self.longitude = coordinate.longitude
    }
    
    var locationCoordinate2D: CLLocationCoordinate2D {
        return CLLocationCoordinate2D(latitude: latitude, longitude: longitude)
    }
}

struct MarkerData: Codable, Identifiable {
    let key: String
    let username: String
    let imguser: String
    let photomark: String
    let street: String
    let id: String
    let coordinate: Coordinate // Используем Coordinate для координат
    let name: String
    let whatHappens: String
    let startDate: String
    let endDate: String
    let startTime: String
    let endTime: String
    let participants: Int
    let access: Bool
    
    // Инициализатор
    init(key: String, username: String, imguser: String, photomark: String, id: String, coordinate: CLLocationCoordinate2D, name: String, whatHappens: String, startDate: String, endDate: String, startTime: String, endTime: String, participants: Int, access: Bool,  street: String) {
        self.key = key
        self.username = username
        self.imguser = imguser
        self.photomark = photomark
        self.street = street
        self.id = id
        self.coordinate = Coordinate(coordinate) // Используем Coordinate
        self.name = name
        self.whatHappens = whatHappens
        self.startDate = startDate
        self.endDate = endDate
        self.startTime = startTime
        self.endTime = endTime
        self.participants = participants
        self.access = access
    }
    
    // Получение CLLocationCoordinate2D из MarkerData
    var locationCoordinate2D: CLLocationCoordinate2D {
        return coordinate.locationCoordinate2D
    }
}
