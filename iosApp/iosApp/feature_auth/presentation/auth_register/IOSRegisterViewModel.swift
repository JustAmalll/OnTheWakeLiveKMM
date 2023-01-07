//
//  RegisterViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 2/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation
import shared

@MainActor final class IOSRegisterViewModel: ObservableObject {
    
    private let validationUseCase: ValidationUseCase
    private let viewModel: RegisterViewModel
    
    @Published var state: RegisterState = RegisterState(
        isLoading: false,
        signUpFirstName: "",
        signUpFirstNameError: nil,
        signUpLastName: "",
        signUpLastNameError: nil,
        signUpPhoneNumber: "",
        signUpPhoneNumberError: nil,
        signUpPassword: "",
        signUpPasswordError: nil
    )
    
    private var handle: DisposableHandle?
    
    init(validationUseCase: ValidationUseCase) {
        self.validationUseCase = validationUseCase
        self.viewModel = RegisterViewModel(
            validationUseCase: validationUseCase
        )
    }
    
    func isValidationSuccess() -> Bool {
        return viewModel.isValidationSuccess()
    }
    
    func onEvent(event: RegisterEvent) {
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
