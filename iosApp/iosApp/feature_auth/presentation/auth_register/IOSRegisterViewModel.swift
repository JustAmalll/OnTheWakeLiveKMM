//
//  RegisterViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 2/1/23.
//  Copyright © 2023 orgName. All rights reserved.
//

import Foundation

extension RegisterScreen {
    @MainActor final class IOSRegisterViewModel: ObservableObject {
        
        @Published var firstName: String = ""
        @Published var lastName: String = ""
        @Published var phoneNumber: String = ""
        @Published var password: String = ""
        
        private let validationUseCase = ValidationUseCase()
                
        func validateRegisterForm() -> ValidationResult {
            let firstNameResult = validationUseCase.validateFirstName(firstName: firstName)
            let lastNameResult = validationUseCase.validateLastName(lastName: lastName)
            let phoneNumberResult = validationUseCase.validatePhoneNumber(phoneNumber: phoneNumber)
            let passwordResult = validationUseCase.validatePassword(password: password)
            
            let validationResults = [firstNameResult, lastNameResult, phoneNumberResult, passwordResult]
            
            let validationError = validationResults.filter { $0.errorMessage != nil }.first?.errorMessage
            let isValidationSuccess = validationResults.allSatisfy { $0.successful }
            
            return ValidationResult(successful: isValidationSuccess, errorMessage: validationError)
        }
    }
}
