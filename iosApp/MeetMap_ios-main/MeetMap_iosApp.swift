//
//  MeetMap_iosApp.swift
//  MeetMap_ios
//
//  Created by Ilya Prokofev on 25.07.2024.
//

import Foundation
import SwiftUI
import FirebaseCore
import GoogleSignIn

@main
struct MeetMap_iosApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    @AppStorage("signIn") var isSignIn = false

    var body: some Scene {
        WindowGroup {
            if !isSignIn {
                LoginScreen()
            } else {
                  // Home()
                ContentViewtwo()
            }
        }
    }
}
