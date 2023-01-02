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
    @MainActor class IOSLoginViewModel: ObservableObject {
        
        private var authRepository: AuthRepository
        private let viewModel: LoginViewModel
        
        @Published var state: LoginState = LoginState(
            isLoading: false,
            loginResult: nil,
            signInPhoneNumber: "",
            signInPhoneNumberError: nil,
            signInPassword: "",
            signInPasswordError: nil
        )
        
        private var handle: DisposableHandle?
        
        init(authRepository: AuthRepository) {
            self.authRepository = authRepository
            self.viewModel = LoginViewModel(
                repository: authRepository,
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
