//
//  ShakeAlertView.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 20.08.2024.
//

import SwiftUI

import SwiftUI


struct ShakeAlertView: View {
    let message: String
    let users: [User]
    @Binding var isPresented: Bool

    var body: some View {
        VStack {
            Text(message)
                .font(.headline)
                .padding()

            List(users, id: \.key) { user in
                HStack {
                    if let url = URL(string: user.img), let imageData = try? Data(contentsOf: url), let uiImage = UIImage(data: imageData) {
                        Image(uiImage: uiImage)
                            .resizable()
                            .scaledToFit()
                            .frame(width: 50, height: 50)
                            .clipShape(Circle())
                            .transition(.fade(duration: 0.5)) // Плавное появление изображения
                    } else {
                        // Заглушка, если URL недействителен
                        Image(systemName: "person.crop.circle.fill")
                            .resizable()
                            .scaledToFit()
                            .frame(width: 50, height: 50)
                    }

                    Text(user.userName)
                        .font(.body)
                        .padding(.leading, 8)
                }
            }

            Button(action: {
                isPresented = false
            }) {
                Text("OK")
                    .padding()
                    .background(Color.blue)
                    .foregroundColor(.white)
                    .clipShape(Capsule())
            }
        }
        .padding()
        .background(Color.white)
        .cornerRadius(10)
        .shadow(radius: 10)
    }
}

    

