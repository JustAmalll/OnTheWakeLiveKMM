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
    
    @EnvironmentObject var otpViewModel: IOSOtpViewModel
    @EnvironmentObject var registerViewModel: IOSRegisterViewModel
    
    var body: some View {
        
        let state = registerViewModel.state
        
        let firstNameError = state.signUpFirstNameError
        let lastNameError = state.signUpLastNameError
        let phoneNumberError = state.signUpPhoneNumberError
        let passwordError = state.signUpPasswordError
        
        Form {
            Section {
                TextField("First name", text: Binding(
                    get: { state.signUpFirstName },
                    set: { value in
                        registerViewModel.onEvent(
                            event: RegisterEvent.SignUpFirstNameChanged(value: value)
                        )
                    }
                ))
                .textContentType(.name)
                .keyboardType(.namePhonePad)

                TextField( "Last name", text: Binding(
                    get: { state.signUpLastName },
                    set: { value in
                        registerViewModel.onEvent(
                            event: RegisterEvent.SignUpLastNameChanged(value: value)
                        )
                    }
                ))
                .textContentType(.familyName)
                .keyboardType(.namePhonePad)

                TextField("Phone number", text: Binding(
                    get: { state.signUpPhoneNumber },
                    set: { value in
                        registerViewModel.onEvent(
                            event: RegisterEvent.SignUpPhoneNumberChanged(value: value)
                        )
                    }
                ))
                .textContentType(.telephoneNumber)
                .keyboardType(.numberPad)

                PasswordTextField(
                    password: Binding(
                        get: { state.signUpPassword },
                        set: { value in
                            registerViewModel.onEvent(
                                event: RegisterEvent.SignUpPasswordChanged(value: value)
                            )
                        }
                    )
                )
            } header: {
                Text("")
            } footer: {
                if let firstNameError = firstNameError {
                    Text(LocalizedStringKey(firstNameError))
                        .foregroundColor(.red)
                } else if let lastNameError = lastNameError {
                    Text(LocalizedStringKey(lastNameError))
                        .foregroundColor(.red)
                } else if let phoneNumberError = phoneNumberError {
                    Text(LocalizedStringKey(phoneNumberError))
                        .foregroundColor(.red)
                } else if let passwordError = passwordError {
                    Text(LocalizedStringKey(passwordError))
                        .foregroundColor(.red)
                } else {
                    Text("")
                }
            }
            
            Button {
                if registerViewModel.isValidationSuccess() {
                    Task {
                        await otpViewModel.sendOtp(
                            phoneNumber: state.signUpPhoneNumber
                        )
                    }
                }
            } label: {
                Text("Submit")
            }
        }
        .navigationTitle("Register")
        .alert(otpViewModel.errorMessage, isPresented: $otpViewModel.showAlert) {}
        .onAppear {
            registerViewModel.startObserving()
        }
        .onDisappear {
            registerViewModel.dispose()
        }
        .overlay {
            if otpViewModel.isLoading {
                Color(.systemBackground).ignoresSafeArea()
                ProgressView()
            }
        }
        .background {
            NavigationLink(tag: "Verification", selection: $otpViewModel.navigationTag) {
                OtpScreen(
                    firstName: state.signUpFirstName,
                    lastName: state.signUpLastName,
                    phoneNumber: state.signUpPhoneNumber,
                    password: state.signUpPassword
                )
            } label: {
                EmptyView()
            }
        }
    }
}
