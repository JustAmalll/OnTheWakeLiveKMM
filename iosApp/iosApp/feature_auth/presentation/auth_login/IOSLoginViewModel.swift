//
//  IOSLoginViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 2/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

extension LoginScreen {
    @MainActor final class IOSLoginViewModel: ObservableObject {
        
        private let authRepository: AuthRepository
        private let validationUseCase: ValidationUseCase
        
        private let viewModel: LoginViewModel
        
        @Published var loginError: AuthResult?
        @Published var hasLoginError: Bool = false
        
        @Published var state: LoginState = LoginState(
            isLoading: false,
            loginResult: nil,
            signInPhoneNumber: "",
            signInPhoneNumberError: nil,
            signInPassword: "",
            signInPasswordError: nil
        )
    
        private var handle: DisposableHandle?
        
        init(authRepository: AuthRepository, validationUseCase: ValidationUseCase) {
            self.authRepository = authRepository
            self.validationUseCase = validationUseCase
            self.viewModel = LoginViewModel(
                repository: authRepository,
                validationUseCase: validationUseCase,
                coroutineScope: nil
            )
        }
        
        func onEvent(event: LoginEvent) {
            self.viewModel.onEvent(event: event)
        }
        
        func startObserving() {
            handle = viewModel.state.subscribe { [weak self] state in
                if let state { self?.state = state }
            }
        }
        
        func dispose() {
            handle?.dispose()
        }
    }
}
