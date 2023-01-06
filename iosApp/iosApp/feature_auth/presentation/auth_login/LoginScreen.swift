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
    
    private var validationUseCase: ValidationUseCase
    private var authRepository: AuthRepository
    
    @ObservedObject var viewModel: IOSLoginViewModel
    
    init(authRepository: AuthRepository, validationUseCase: ValidationUseCase) {
        self.authRepository = authRepository
        self.validationUseCase = validationUseCase
        self.viewModel = IOSLoginViewModel(
            authRepository: authRepository,
            validationUseCase: validationUseCase
        )
    }
    
    var body: some View {
        
        let phoneNumberError = viewModel.state.signInPhoneNumberError
        let passwordError = viewModel.state.signInPasswordError
        
        Form {
            Section {
                TextField("Phone number", text: Binding(
                    get: { viewModel.state.signInPhoneNumber },
                    set: { value in
                        viewModel.onEvent(
                            event: LoginEvent.SignInPhoneNumberChanged(value: value)
                        )
                    }
                ))
                .textContentType(.telephoneNumber)
                .keyboardType(.phonePad)
                
                PasswordTextField(password: Binding(
                    get: { viewModel.state.signInPassword },
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
        .onDisappear {
            viewModel.dispose()
        }
        .overlay {
            if viewModel.state.isLoading  {
                Color(.systemBackground).ignoresSafeArea()
                ProgressView()
            }
        }
        .overlay(alignment: .bottom) {
            if !viewModel.state.isLoading {
                HStack {
                    Text("Don't have an account yet?")
                    NavigationLink(
                        destination: RegisterScreen(
                            authRepository: authRepository,
                            validationUseCase: validationUseCase
                        )
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
