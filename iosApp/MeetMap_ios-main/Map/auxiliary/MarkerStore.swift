//
//  MarkerStore.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 29.07.2024.
//

import Foundation
import MapKit


import Foundation

class MarkerStore: ObservableObject {
    @Published var markers = [Marker]()
    
    init() {
        loadMarkersFromUserDefaults()
    }
}
