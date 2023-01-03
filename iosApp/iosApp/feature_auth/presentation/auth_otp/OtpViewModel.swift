//
//  OtpViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import SwiftUI
import Firebase

@MainActor class OtpViewModel: ObservableObject {
    
    private let validationUseCase = ValidationUseCase()
    
    @Published var otp: String = ""
    
    @Published var showAlert: Bool = false
    @Published var errorMessage: String = ""
    
    @Published var verificationCode: String = ""
    
    @Published var isLoading: Bool = false
    
    @Published var navigationTag: String?
    
    func sendOtp(phoneNumber: String) async {
        if isLoading { return }
        do {
            isLoading = true
            
            let result = try await
            PhoneAuthProvider.provider().verifyPhoneNumber(phoneNumber, uiDelegate: nil)
            
            DispatchQueue.main.async {
                self.isLoading = false
                self.verificationCode = result
                self.navigationTag = "Verification"
            }
        } catch {
            print(error.localizedDescription)
            handleError(error: error.localizedDescription)
        }
    }
    
    func verifyOtp() async {
        do {
            isLoading = true
            
            let credential = PhoneAuthProvider
                .provider()
                .credential(withVerificationID: verificationCode, verificationCode: otp)
            
            try await Auth.auth().signIn(with: credential)
            
            DispatchQueue.main.async { [self] in
                isLoading = false
                print("Success")
            }
        } catch {
            print(error.localizedDescription)
            handleError(error: error.localizedDescription)
        }
    }
    
    func validateOtp() -> ValidationResult {
        let otpResult = validationUseCase.validateOTP(otp: otp)
        return ValidationResult(successful: otpResult.successful, errorMessage: otpResult.errorMessage)
    }
    
    func handleError(error: String) {
        DispatchQueue.main.async {
            self.isLoading = false
            self.errorMessage = error
            self.showAlert.toggle()
        }
    }
}
