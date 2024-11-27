//
//  Home.swift
//  SignInUsingGoogle
//
//  Created by Swee Kwang Chua on 12/5/22.
//

import SwiftUI
import GoogleSignIn
import SDWebImageSwiftUI
import _MapKit_SwiftUI

public struct Home: View {
    @State private var userName: String = ""
    @State private var userProfileImageURL: URL? = nil
    @State private var isUserSignedIn: Bool = false

    
    public var body: some View {
       
        NavigationView {
            
            VStack {
                if let imageURL = userProfileImageURL {
                    WebImage(url: imageURL)
                        .resizable()
                        .aspectRatio(contentMode: .fill)
                        .frame(width: 100, height: 100)
                        .clipShape(Circle())
                        .overlay(Circle().stroke(Color.gray, lineWidth: 2))
                        .padding()
                } else {
                    Image(systemName: "person.circle.fill")
                        .resizable()
                        .frame(width: 100, height: 100)
                        .foregroundColor(.gray)
                        .padding()
                }

                Text(userName.isEmpty ? "User" : userName)
                    .font(.title)
                    .padding()

                Button(action: {
                                   signOut()
                               }) {
                                   Text("Sign Out")
                                       .foregroundColor(.white)
                                       .padding()
                                       .background(Color.red)
                                       .cornerRadius(10)
                               }
                               .padding()

                               NavigationLink(destination: ContentViewtwo()
                                   .navigationBarHidden(true) // Прячем навигационную панель на экране ContentViewtwo
                               ) {
                                   Text("Go to Map")
                               }
                           }
                           .onAppear {
                               fetchUserInfo()
                           }
                           .navigationBarHidden(true) // Прячем навигационную панель на экране Home
                       }
                   }


    func fetchUserInfo() {
        // Пример получения информации о пользователе
        let userInfo = FirebAuth.share.getCurrentUserInfo()

        if let uid = userInfo.uid, !uid.isEmpty {
            if let name = userInfo.name {
                userName = name
            }
            
            if let profileImageURL = userInfo.profileImageURL {
                userProfileImageURL = profileImageURL
            }
            
            isUserSignedIn = true
        } else {
            isUserSignedIn = false
        }
    }
   

    func signOut() {
        FirebAuth.share.signOut { error in
            if let error = error {
                print("Failed to sign out: \(error.localizedDescription)")
            } else {
                print("Successfully signed out")
                isUserSignedIn = false
            }
        }
    }
}
