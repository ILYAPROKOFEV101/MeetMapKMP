//
//  ContentView.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 25.07.2024.
//


import SwiftUI
import MapKit

// Основное представление
struct ContentViewt: View {
    @State private var region = MKCoordinateRegion(
        center: CLLocationCoordinate2D(latitude: 37.7749, longitude: -122.4194),
        span: MKCoordinateSpan(latitudeDelta: 0.05, longitudeDelta: 0.05)
    )
    
    var body: some View {
        MapView(region: $region, showsUserLocation: true)
            .edgesIgnoringSafeArea(.all)
            .onAppear {
                // Обновите координаты региона на текущие координаты пользователя
                if let location = CLLocationManager().location {
                    region.center = location.coordinate
                }
            }
    }
}

// Превью для Canvas
struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
