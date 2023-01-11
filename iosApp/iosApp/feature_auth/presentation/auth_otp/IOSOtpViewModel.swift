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

@MainActor final class IOSOtpViewModel: ObservableObject {
    
    private let authRepository: AuthRepository
    private let validationUseCase: ValidationUseCase
    
    private let viewModel: OtpViewModel
    
    @Published var showAlert: Bool = false
    @Published var errorMessage: String = ""
    
    @Published var verificationCode: String = ""
    
    @Published var isLoading: Bool = false
    
    @Published var navigationTag: String?
    
    @Published var isOtpVerified: Bool = false
    
    @Published var state: OtpState = OtpState(
        isLoading: false,
        signUpResult: nil,
        otp: "",
        otpError: nil
    )
    
    init(authRepository: AuthRepository, validationUseCase: ValidationUseCase) {
        self.authRepository = authRepository
        self.validationUseCase = validationUseCase
        self.viewModel = OtpViewModel(
            repository: authRepository,
            validationUseCase: validationUseCase,
            coroutineScope: nil
        )
    }
    
    private var handle: DisposableHandle?
    
    func sendOtp(phoneNumber: String) async {
        if isLoading { return }
        do {
            isLoading = true
            
            let isUserAlreadyExists = try await
            authRepository.isUserAlreadyExists(phoneNumber: phoneNumber)
            
            if isUserAlreadyExists == false {
                let result = try await
                PhoneAuthProvider.provider().verifyPhoneNumber(phoneNumber, uiDelegate: nil)
                
                DispatchQueue.main.async {
                    self.isLoading = false
                    self.verificationCode = result
                    self.navigationTag = "Verification"
                }
            } else {
                handleError(error: "Oops! User with this phone number already exists")
            }
        } catch {
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
            
            let credential = PhoneAuthProvider
                .provider()
                .credential(
                    withVerificationID: verificationCode,
                    verificationCode: state.otp
                )

            try await Auth.auth().signIn(with: credential)
  
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
                isOtpVerified = true
            }
        } catch {
            print(error.localizedDescription)
            isOtpVerified = false
            handleError(error: error.localizedDescription)
        }
    }
    
    func isOtpValidationSuccess() -> Bool {
        return viewModel.isOtpValidationSuccess()
    }
    
    func onEvent(event: OtpEvent) {
        viewModel.onEvent(event: event)
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
