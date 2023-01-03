//
//  IOSOtpViewModel.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import Foundation
import shared
import Firebase

@MainActor class IOSOtpViewModel: ObservableObject {
    
    private let validationUseCase = ValidationUseCase()
    
    private let authRepository: AuthRepository
    private let viewModel: OtpViewModel
    
    @Published var otp: String = ""
    
    @Published var showAlert: Bool = false
    @Published var errorMessage: String = ""
    
    @Published var verificationCode: String = ""
    
    @Published var isLoading: Bool = false
    
    @Published var navigationTag: String?
    
    @Published var state: OtpState = OtpState(
        isLoading: false, signUpResult: nil
    )
    
    init(authRepository: AuthRepository) {
        self.authRepository = authRepository
        self.viewModel = OtpViewModel(
            repository: authRepository, coroutineScope: nil
        )
    }
    
    private var handle: DisposableHandle?
    
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
    
    func verifyOtpAndSignUp(
        firstName: String,
        lastName: String,
        phoneNumber: String,
        password: String
    ) async {
        do {
            isLoading = true
            
            print("credential")
            let credential = PhoneAuthProvider
                .provider()
                .credential(withVerificationID: verificationCode, verificationCode: otp)
            
            print("try await start")
            try await Auth.auth().signIn(with: credential)
            print("try await end")
            
            print("view model on event")
            viewModel.onEvent(
                event: OtpEvent.SignUp(
                    firstName: firstName,
                    lastName: lastName,
                    phoneNumber: phoneNumber,
                    password: password
                )
            )
            
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
    
    func onEvent(event: OtpEvent) {
        self.viewModel.onEvent(event: event)
    }
    
    func startObserving() {
        handle = viewModel.state.subscribe { [weak self] state in
            if let state { self?.state = state }
        }
    }
    
    func handleError(error: String) {
        DispatchQueue.main.async {
            self.isLoading = false
            self.errorMessage = error
            self.showAlert.toggle()
        }
    }
    
    func dispose() {
        handle?.dispose()
    }
}
