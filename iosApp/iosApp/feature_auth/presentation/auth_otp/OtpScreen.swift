//
//  OtpScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct OtpScreen: View {
    
    @EnvironmentObject var otpViewModel: IOSOtpViewModel
    
    @State var validationError: String? = nil
    
    let firstName: String
    let lastName: String
    let phoneNumber: String
    let password: String
    
    var body: some View {
        Form {
            Section {
                TextField("OTP", text: $otpViewModel.otp)
                    .textContentType(.oneTimeCode)
                    .keyboardType(.phonePad)
                
                Button {
                    let validationResult = otpViewModel.validateOtp()
                    validationError = validationResult.errorMessage
                    
                    if validationResult.successful {
                        Task {
                            await otpViewModel.verifyOtpAndSignUp(
                                firstName: firstName,
                                lastName: lastName,
                                phoneNumber: phoneNumber,
                                password: password
                            )
                        }
                    }
                } label: {
                    Text("Verify")
                }
            } header: {
                Text("")
            }
        }
        .overlay {
            if otpViewModel.isLoading {
                Color(.systemBackground).ignoresSafeArea()
                ProgressView()
            }
        }
        .navigationTitle("Verification")
    }
}
