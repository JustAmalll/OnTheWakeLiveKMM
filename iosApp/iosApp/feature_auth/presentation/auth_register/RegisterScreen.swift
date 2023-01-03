//
//  RegisterScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct RegisterScreen: View {
    
    private var authRepository: AuthRepository
    
    @ObservedObject var otpViewModel: IOSOtpViewModel
    @StateObject var registerViewModel = IOSRegisterViewModel()
    
    @State var validationError: String? = nil
    
    init(authRepository: AuthRepository) {
        self.authRepository = authRepository
        self.otpViewModel = IOSOtpViewModel(authRepository: authRepository)
    }
    
    var body: some View {
        
        Form {
            Section {
                TextField( "First name", text: $registerViewModel.firstName)
                    .textContentType(.name)
                    .keyboardType(.namePhonePad)
                
                TextField( "Last name", text: $registerViewModel.lastName)
                    .textContentType(.familyName)
                    .keyboardType(.namePhonePad)
                
                TextField("Phone number", text: $registerViewModel.phoneNumber)
                    .textContentType(.telephoneNumber)
                    .keyboardType(.numberPad)
                
                PasswordTextField(
                    password: $registerViewModel.password
                )
            } header: {
                Text("")
            } footer: {
                Text(validationError ?? "")
                    .foregroundColor(.red)
            }
            
            Button() {
                let validationResult = registerViewModel.validateRegisterForm()
                validationError = validationResult.errorMessage
                
                if validationResult.successful {
                    Task {
                        await otpViewModel.sendOtp(
                            phoneNumber: registerViewModel.phoneNumber
                        )
                    }
                }
            } label: {
                Text("Submit")
            }
        }
        .navigationTitle("Register")
        .background {
            NavigationLink(tag: "Verification", selection: $otpViewModel.navigationTag) {
                OtpScreen(
                    firstName: registerViewModel.firstName,
                    lastName: registerViewModel.lastName,
                    phoneNumber: registerViewModel.phoneNumber,
                    password: registerViewModel.password
                )
                .environmentObject(otpViewModel)
            } label: {}
        }
    }
}
