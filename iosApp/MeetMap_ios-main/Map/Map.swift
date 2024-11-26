//
//  Map.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 25.07.2024.
//
// English or Spanish 


// Представление карты
import SwiftUI
import MapKit
import CoreLocation
import Foundation
import GoogleSignIn
import SDWebImageSwiftUI
import _MapKit_SwiftUI


struct ContentViewtwo: View {
    @StateObject private var viewModel = ContentViewModel()
    @State private var showAddress: Bool = false
    @State private var userAnnotations: [MKPointAnnotation] = []
    @State private var userAnnotation = MKPointAnnotation()
    @StateObject private var markerStore = MarkerStore()
    @State private var mapType: MKMapType = .standard
    @State private var userName: String = ""
    @State private var userProfileImageURL: URL? = nil
    @State private var isUserSignedIn: Bool = false
    @State private var userUID: String = ""
    @State private var savedResponse: String = ""
    @State private var participantMarks: [Marker] = []
    @State private var errorMessage: String?
    @State private var isBottomSheetVisible: Bool = false
    @State private var showAlert: Bool = false
    @State private var alertMessage: String = ""
    @State private var alertUsers: [User] = []

    var body: some View {
        ZStack(alignment: .bottom) {
            CustomMapView(
                // Удалите `coordinateRegion`
                mapType: $mapType,
                markerStore: markerStore,
                uid: $userUID,
                key: $savedResponse,
                userLocation: $viewModel.userCoordinate
            )
            .onAppear {
                if let response = UserDefaults.standard.string(forKey: "responseKey") {
                    savedResponse = response
                }
                
                let userInfo = FirebAuth.share.getCurrentUserInfo()
                if let uid = userInfo.uid, !uid.isEmpty {
                    userUID = uid
                    if let name = userInfo.name {
                        userName = name
                    }
                    if let profileImageURL = userInfo.profileImageURL {
                        userProfileImageURL = profileImageURL
                    }
                }
                
                checkUser(uid: userUID) { key in
                    if let key = key {
                        savedResponse = key
                        print("Retrieved Key: \(key)")
                    } else {
                        print("Failed to retrieve key.")
                    }
                }
                
                fetchParticipantMarks(uid: userUID, key: savedResponse) { result in
                    DispatchQueue.main.async {
                        switch result {
                        case .success(let marks):
                            self.participantMarks = marks
                        case .failure(let error):
                            self.errorMessage = error.localizedDescription
                        }
                    }
                }
                
                // Убедитесь, что регион не меняется при загрузке карты
                // Например, закомментируйте следующий код или удалите его, если он есть:
                // viewModel.region = MKCoordinateRegion(
                //     center: someLocation,
                //     span: MKCoordinateSpan(latitudeDelta: 0.01, longitudeDelta: 0.01)
                // )
            }
            .edgesIgnoringSafeArea(.all)

            ZStack {
                VStack {
                    TextField("Enter address", text: $viewModel.address, onCommit: {
                        withAnimation {
                            showAddress = true
                        }
                        DispatchQueue.main.asyncAfter(deadline: .now() + 10) {
                            withAnimation {
                                showAddress = false
                            }
                        }
                    })
                    .textFieldStyle(RoundedBorderTextFieldStyle())
                    .padding()

                    if showAddress {
                        Text(viewModel.address)
                            .padding()
                            .background(Color.yellow)
                            .transition(.opacity)
                    }

                    Spacer()

                    HStack {
                        Spacer()

                        VStack(alignment: .leading) {
                            Spacer()
                            Button(action: {
                                viewModel.updateAddress()
                            }) {
                                Image(systemName: "location.circle.fill")
                                    .resizable()
                                    .frame(width: 44, height: 44)
                                    .background(Color.white.opacity(0.8))
                                    .clipShape(Circle())
                                    .padding()
                            }
                            Button(action: {
                                mapType = (mapType == .standard) ? .satellite : .standard
                            }) {
                                Image(systemName: "globe")
                                    .resizable()
                                    .frame(width: 44, height: 44)
                                    .background(Color.white.opacity(0.8))
                                    .clipShape(Circle())
                                    .padding()
                            }
                        }
                        .padding(.bottom, 150)  // Отступ снизу для всего BottomSheetView
                    }
                    .frame(maxWidth: .infinity, alignment: .trailing)  // Выравнивание по правому краю
                }

                BottomSheetView(isOpen: $isBottomSheetVisible, minHeight: 50, maxHeight: 800) {
                    VStack {
                        Text("Additional Information")
                            .font(.headline)

                        if let errorMessage = errorMessage {
                            Text("Error: \(errorMessage)")
                                .foregroundColor(.red)
                        } else {
                            LazyVStack {
                                ForEach(participantMarks, id: \.id) { mark in
                                    VStack(alignment: .leading, spacing: 10) {
                                        Text(mark.name)
                                            .font(.title2)
                                            .fontWeight(.bold)

                                        Text("Street: \(mark.street)")
                                        Text("Username: \(mark.username)")
                                        Text("Participants: \(mark.participants)")
                                        Text("Date: \(mark.startDate) - \(mark.endDate)")
                                        Text("Time: \(mark.startTime) - \(mark.endTime)")
                                        Text("Description: \(mark.whatHappens)")
                                    }
                                    .padding()
                                    .background(Color.gray.opacity(0.1))
                                    .cornerRadius(8)
                                }
                            }
                        }
                    }
                    .padding()
                }
            }
            .onReceive(NotificationCenter.default.publisher(for: .didDetectShake)) { _ in
                Task {
                    await handleShakeEvent()
                }
            }
            .sheet(isPresented: $showAlert)
            {
                        ShakeAlertView(message: alertMessage, users: alertUsers, isPresented: $showAlert)
            }
        }
    }

    private func handleShakeEvent() async {
        print("Shake event handled in ContentView")
        
        // Вызов асинхронного метода fetchData
        let userLocation = viewModel.userCoordinate
        let urlString = "wss://meetmap.up.railway.app/shake/\(savedResponse)/\(userLocation.latitude)/\(userLocation.longitude)"
        print("URL for WebSocket: \(urlString)")

        if let users = await fetchData(from: urlString) {
            DispatchQueue.main.async {
                alertMessage = "Shake Detected"
                alertUsers = users
                showAlert = true
            }
            print("Received users: \(users)")
        } else {
            print("No data received or an error occurred")
        }
    }


}


extension Notification.Name {
    static let didDetectShake = Notification.Name("didDetectShake")
}
