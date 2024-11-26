//
//  dataMarkView.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 30.07.2024.
//

import SwiftUI



import SwiftUI

// SwiftUI View for displaying marker data
struct DataMarkView: View {
    let id: String
    let uid: String
    let key: String
    @ObservedObject var markerStore: MarkerStore
    @State private var markerData: Marker? = nil
    
    var body: some View {
        VStack(spacing: 16) {
            if let url = URL(string: "https://firebasestorage.googleapis.com/v0/b/code-with-friends-73cde.appspot.com/o/image%2Fpng%2FgM8AWXsJ0r%2F7136e392-a847-4e81-98ac-f5112bbfb825?alt=media&token=86d456f9-b0ff-403d-a43b-37e66b1a72f8") {
                if #available(iOS 15.0, *) {
                    AsyncImage(url: url) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 60, height: 60)
                            .padding(.top)
                    } placeholder: {
                        ProgressView()
                            .frame(width: 60, height: 60)
                            .padding(.top)
                    }
                } else {
                    // Fallback on earlier versions
                }
            }
            
            // Name
            Text("\(markerData?.name ?? "No Name")")
                .font(.headline)
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color.white)
                .cornerRadius(10)
            
                    // About marker
                        Text(markerData?.whatHappens ?? "No Information")
                            .padding()
                            .background(Color.white)
                            .cornerRadius(10)
                            .frame(maxWidth: .infinity, alignment: .leading) // Расширяет текст на всю доступную ширину
                            .fixedSize(horizontal: false, vertical: true) // Позволяет тексту занимать несколько строк
            
            // Street
            Text("\(markerData?.street ?? "No Street")")
                .frame(maxWidth: .infinity)
                .padding()
                .background(Color.white)
                .cornerRadius(10)
            
            // Start Date and Time
            if let startDate = markerData?.startDate, let startTime = markerData?.startTime {
                Text("\(formattedDate(from: startDate, time: startTime))")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.white)
                    .cornerRadius(10)
            }
            
            // End Date and Time
            if let endDate = markerData?.endDate, let endTime = markerData?.endTime {
                Text("\(formattedDate(from: endDate, time: endTime))")
                    .frame(maxWidth: .infinity)
                    .padding()
                    .background(Color.white)
                    .cornerRadius(10)
            }
            HStack(spacing: 20) { // Устанавливаем отступ между кнопками
                        Button(action: {
                            // Действие первой кнопки
                            
                        }) {
                            Text("Not now")
                                .padding()
                                .background(Color.blue)
                                .foregroundColor(.white)
                                .cornerRadius(8)
                        }

                        Button(action: {
                            sendPostRequest(uid: uid, key: key, id: id)
                            
                        }) {
                            Text("Ready")
                                .padding()
                                .background(Color.green)
                                .foregroundColor(.white)
                                .cornerRadius(8)
                        }
                    }
                    .padding() // Отступ от границ HStack
        }
        .padding()
        .onAppear {
            self.markerData = getMarkerData(by: id)
        }
    }
    
    private func formattedDate(from dateString: String, time: String) -> String {
        guard let date = formattedDate(from: dateString) else { return "Invalid Date" }
        let dateFormatter = DateFormatter()
        dateFormatter.dateStyle = .medium
        dateFormatter.timeStyle = .short
        let dateString = dateFormatter.string(from: date)
        return "\(dateString) Time \(time)"
    }
    
    private func formattedDate(from dateString: String) -> Date? {
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd"
        return formatter.date(from: dateString)
    }
}


#Preview {
    DataMarkView(id: "6GkAx0f6cJcWCmihMTpTe41IsqFMIV",uid:"", key:"", markerStore: MarkerStore())
}








import MapKit

class CustomAnnotation: NSObject, MKAnnotation {
    var coordinate: CLLocationCoordinate2D
    var title: String?
    var subtitle: String?
    var id: String

    init(id: String) {
        self.id = id
        self.coordinate = CLLocationCoordinate2D(latitude: 0, longitude: 0) // Установите начальные значения
        super.init()
    }
}

