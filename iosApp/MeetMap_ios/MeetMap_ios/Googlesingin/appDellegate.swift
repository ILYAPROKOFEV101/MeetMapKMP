//
//  appDellegate.swift
//  SignInUsingGoogle
//
//  Created by Ilya Prokofev on 23.07.2024.
//

import Foundation
import SwiftUI
import FirebaseCore
import GoogleSignIn


open class AppDelegate: UIResponder, UIApplicationDelegate {

    public var window: UIWindow?

    public func application(_ application: UIApplication,
                     didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        FirebaseApp.configure()
        return true
    }

  public  func application(_ app: UIApplication, open url: URL, options: [UIApplication.OpenURLOptionsKey : Any] = [:]) -> Bool {
        return GIDSignIn.sharedInstance.handle(url)
    }
}
