//
//  findlocation.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 11.08.2024.
//

import Foundation
import CoreLocation


func resolveLocationName(with location: CLLocation, completion: @escaping (String) -> Void) {
    let geocoder = CLGeocoder()
    geocoder.reverseGeocodeLocation(location, preferredLocale: .current) { placemarks, error in
        guard let place = placemarks?.first, error == nil else {
            completion("")
            return
        }

        var name = ""
        if let locality = place.subThoroughfare {
            name += locality
        }
        if let street = place.thoroughfare {
            name += " \(street)"
        }
        if let city = place.locality {
            name += " \(city)"
        }
        if let adminRegion = place.administrativeArea {
            name += ", \(adminRegion)"
        }
        if let zipCode = place.postalCode {
            name += " \(zipCode)"
        }

        completion(name)
    }
}
