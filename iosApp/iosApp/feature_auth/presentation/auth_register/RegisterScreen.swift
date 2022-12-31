//
//  RegisterScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct RegisterScreen: View {
    
    @State private var firstName = ""
    @State private var lastName = ""
    @State private var phoneNumber = ""
    
    var body: some View {
        Form {
            Section {
                TextField("First name", text: $firstName)
                    .textContentType(.name)
                    .keyboardType(.namePhonePad)
                TextField("Last name", text: $lastName)
                    .textContentType(.familyName)
                    .keyboardType(.namePhonePad)
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
        .navigationTitle("Register")
    }
}

struct RegisterScreen_Previews: PreviewProvider {
    static var previews: some View {
        RegisterScreen()
    }
}
