//
//  LoginScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct LoginScreen: View {
    
    @State private var phoneNumber = ""
    
    var body: some View {
        Form {
            Section {
                TextField("Phone number", text: $phoneNumber)
                    .textContentType(.telephoneNumber)
                    .keyboardType(.phonePad)
                PasswordTextField()
            } header: {
                Text("")
            }
            Button() {
                
            } label: {
                Text("Submit")
            }
        }
        .overlay(alignment: .bottom) {
            HStack {
                Text("Don't have an account yet?")
                NavigationLink(destination: RegisterScreen()) {
                    Text("Sign Up!")
                }
            }
            .padding(.bottom)
        }
        .navigationTitle("Login")
    }
}

struct LoginScreen_Previews: PreviewProvider {
    static var previews: some View {
        LoginScreen()
    }
}
