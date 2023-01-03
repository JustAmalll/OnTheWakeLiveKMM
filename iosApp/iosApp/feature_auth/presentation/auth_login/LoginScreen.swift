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
    
    private var authRepository: AuthRepository
    
    @ObservedObject var viewModel: IOSLoginViewModel
    
    @State var validationError: String? = nil
    
    init(authRepository: AuthRepository) {
        self.authRepository = authRepository
        self.viewModel = IOSLoginViewModel(
            authRepository: authRepository
        )
    }
    
    
    var body: some View {
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
                Text(validationError ?? "")
                    .foregroundColor(.red)
            }
            
            Button {
                let validationResult = viewModel.validateLoginForm()
                validationError = validationResult.errorMessage
                
                if validationResult.successful {
                    viewModel.onEvent(event: LoginEvent.SignIn())
                }
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
                        destination: RegisterScreen(authRepository: authRepository)
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
