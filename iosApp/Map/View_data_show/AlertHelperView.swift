//
//  Alert_Dealog.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 28.07.2024.
//

import MapKit
import SwiftUI


import SwiftUI
import MapKit

import SwiftUI
import MapKit

struct AlertHelperView: View {
    @ObservedObject var markerStore: MarkerStore
    let coordinate: CLLocationCoordinate2D
    let uid: String
    let key: String
    let adress: String
    
    var completion: (MKPointAnnotation, Double) -> Void
    
    @State private var markerName = ""
    @State private var description = ""
    @State private var startDate = Date()
    @State private var endDate = Date()
    @State private var radius: Double = 100
    @State private var isPublic = false
    @State private var access = "public" // Переменная для доступа, инициализированная по умолчанию
    
    // Форматтеры для дат и времени
    private var dateFormatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter
    }
    
    
    
    private var timeFormatter: DateFormatter {
        let formatter = DateFormatter()
        formatter.dateFormat = "HH:mm"
        return formatter
    }
    
    // Функция для отправки данных на сервер
    private func sendMarkerToServer(_ marker: Marker) {
        guard let url = URL(string: "https://meetmap.up.railway.app/mark/\(uid)/\(key)") else { return }
        print("server data url ссыдка  \(url) ")
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let encoder = JSONEncoder()
        do {
            let jsonData = try encoder.encode(marker)
            request.httpBody = jsonData
            
            let task = URLSession.shared.dataTask(with: request) { data, response, error in
                if let error = error {
                    print("Error sending marker: \(error.localizedDescription)")
                    return
                }
                
                guard let response = response as? HTTPURLResponse, response.statusCode == 200 else {
                    print("Server error")
                    return
                }
                
                print("Marker successfully sent")
            }
            task.resume()
        } catch {
            print("Error encoding marker: \(error.localizedDescription)")
        }
    }
    
    var body: some View {
        VStack {
            VStack {
                Text("Enter details for the marker").font(.headline.bold())
                Spacer()
                TextField("Marker name", text: $markerName)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                
                Spacer().frame(height: 30)
                
                TextField("Description", text: $description)
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    
                VStack {
                    DatePicker("Start date and time", selection: $startDate)
                        .datePickerStyle(CompactDatePickerStyle())
                        .frame(height: 70)
                    Spacer().frame(height: 20)
                    
                    DatePicker("End date and time", selection: $endDate)
                        .datePickerStyle(CompactDatePickerStyle())
                        .frame(height: 70)
                }
                               
                Spacer().frame(height: 30)
                
                Slider(value: $radius, in: 2...100, step: 1) {
                    
                }
                Text("People: \(Int(radius)) ")
               
                Spacer().frame(height: 30)
                
                // Toggle для выбора состояния метки
                Toggle(isOn: $isPublic) {
                    Text("Make marker " + access)
                }
                .onChange(of: isPublic) { newValue in
                    access = newValue ? "private" : "public"
                }
                
                Spacer().frame(height: 30)
                
                HStack {
                    Button("Cancel") {
                        // Действие при отмене
                    }
                    .font(.headline)
                    
                    Spacer()
                    
                    Button("Add") {
                        // Создаем объект Marker с введенными данными
                        let marker = Marker(
                            key: key,
                            username: "ilya", // Можно заменить на текущее значение пользователя
                            imguser: "imag",
                            photomark: "photomark",
                            street: adress,
                            id: UUID().uuidString, // Генерируем уникальный ID
                            lat: coordinate.latitude,
                            lon: coordinate.longitude,
                            name: markerName,
                            whatHappens: description,
                            startDate: dateFormatter.string(from: startDate),
                            endDate: dateFormatter.string(from: endDate),
                            startTime: timeFormatter.string(from: startDate),
                            endTime: timeFormatter.string(from: endDate),
                            participants: Int(radius),
                            access: false
                        )
                        
                        // Отправляем данные на сервер
                        sendMarkerToServer(marker)
                        
                        // Вызываем completion handler
                        let annotation = MKPointAnnotation()
                        annotation.coordinate = coordinate
                        annotation.title = markerName
                        completion(annotation, radius)
                    }
                    .font(.headline)
                }
                .padding()
            }
            .padding()
        }
    }
}
