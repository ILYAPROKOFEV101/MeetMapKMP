//
//  CustomButton.swift
//  SignInUsingGoogle
//
//  Created by Swee Kwang Chua on 4/9/22.
//

import SwiftUI

struct CustomButton: View {
    var body: some View {
        if #available(iOS 15.0, *) {
            Button(action: {}) {
                HStack {
                    Spacer()
                    Text("Login")
                        .foregroundColor(.white)
                    Spacer()
                }
            }
            .padding()
            .background(.black)
            .cornerRadius(12)
            .padding()
        } else {
            // Fallback on earlier versions
        }
    }
}

struct CustomButton_Previews: PreviewProvider {
    static var previews: some View {
        CustomButton()
    }
}
