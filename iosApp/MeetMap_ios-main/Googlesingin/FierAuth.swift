//
//  FierAuth.swift
//  SignInUsingGoogle
//
//  Created by Ilya Prokofev on 23.07.2024.
//

import Foundation
import FirebaseAuth
import GoogleSignIn
import Firebase

struct FirebAuth {
    
    static let share = FirebAuth()
    
    private init() {}
    
    func signinWithGoogle(presenting: UIViewController, completion: @escaping (Error?) -> Void) {
            guard let clientID = FirebaseApp.app()?.options.clientID else { return }
        _ = GIDConfiguration(clientID: clientID)
        
        guard (FirebaseApp.app()?.options.clientID) != nil else { return }

  

        // Start the sign in flow!
        GIDSignIn.sharedInstance.signIn(withPresenting: presenting) { signResult, error in
          if let error = error {
              completion(error)
            return
          }

            guard let user = signResult?.user,
                      let idToken = user.idToken
            else {
                return
            }

            
               let accessToken = user.accessToken
                      
               let credential = GoogleAuthProvider.credential(withIDToken: idToken.tokenString, accessToken: accessToken.tokenString)

              // Use the credential to authenticate with Firebase

          



            Auth.auth().signIn(with: credential) { result, error in
                guard error == nil else {
                    completion(error)
                    return
                }
                print("SIGN IN")
                UserDefaults.standard.set(true, forKey: "signIn") // When this change to true, it will go to the home screen
            }
        }
    }
    
    // New method for signing out
        func signOut(completion: @escaping (Error?) -> Void) {
            let firebaseAuth = Auth.auth()
            
            do {
                // Sign out from Firebase
                try firebaseAuth.signOut()
                
                // Sign out from Google
                GIDSignIn.sharedInstance.signOut()
                
                // Perform any additional cleanup or state updates here
                UserDefaults.standard.set(false, forKey: "signIn")
                completion(nil)
            } catch let signOutError as NSError {
                print("Error signing out: %@", signOutError)
                completion(signOutError)
            }
        }
    
    
    // Метод для получения информации о текущем пользователе
        func getCurrentUserInfo() -> (uid: String?, name: String?, profileImageURL: URL?) {
            if let user = Auth.auth().currentUser {
                let uid = user.uid
                let name = user.displayName
                let profileImageURL = user.photoURL
                return (uid, name, profileImageURL)
            } else {
                return (nil, nil, nil)
            }
        }
}
