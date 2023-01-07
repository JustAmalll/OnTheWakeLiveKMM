//
//  LoginScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct LoginScreen: View {
    
    @EnvironmentObject var viewModel: IOSLoginViewModel
    
    var body: some View {
        
        let state = viewModel.state
        
        let phoneNumberError = state.signInPhoneNumberError
        let passwordError = state.signInPasswordError
        
        NavigationView {
            Form {
                Section {
                    TextField("Phone number", text: Binding(
                        get: { state.signInPhoneNumber },
                        set: { value in
                            viewModel.onEvent(
                                event: LoginEvent.SignInPhoneNumberChanged(value: value)
                            )
                        }
                    ))
                    .textContentType(.telephoneNumber)
                    .keyboardType(.phonePad)
                    
                    PasswordTextField(password: Binding(
                        get: { state.signInPassword },
                        set: { value in
                            viewModel.onEvent(
                                event: LoginEvent.SignInPasswordChanged(value: value)
                            )
                        }
                    ))
                } header: {
                    Text("")
                } footer: {
                    if let phoneNumberError = phoneNumberError {
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
                    viewModel.onEvent(event: LoginEvent.SignIn())
                } label: {
                    Text("Submit")
                }
            }
            .onAppear {
                viewModel.startObserving()
            }
            .alert(isPresented: $viewModel.hasLoginError, error: viewModel.state.loginResult) {
                Button(
                    action: {
                        viewModel.onEvent(
                            event: LoginEvent.OnLoginResultSeen()
                        )
                    }
                ) {
                    Text("OK")
                }
            }
            .onDisappear {
                viewModel.dispose()
            }
            .overlay {
                if state.isLoading  {
                    Color(.systemBackground).ignoresSafeArea()
                    ProgressView()
                }
            }
            .overlay(alignment: .bottom) {
                if !state.isLoading {
                    HStack {
                        Text("Don't have an account yet?")
                        NavigationLink(
                            destination: RegisterScreen()
                        ) {
                            Text("Sign Up!")
                        }
                    }
                    .padding(.bottom)
                }
            }
            .navigationTitle("login")
        }
    }
}
