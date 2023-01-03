//
//  OtpScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI

struct OtpScreen: View {
    
    @EnvironmentObject var otpModel: OtpViewModel
    
    @State var validationError: String? = nil
    
    let firstName: String
    let lastName: String
    let phoneNumber: String
    let password: String
    
    var body: some View {
        Form {
            Section {
                TextField("OTP", text: $otpModel.otp)
                    .textContentType(.oneTimeCode)
                    .keyboardType(.phonePad)
                
                Button {
                    let validationResult = otpModel.validateOtp()
                    validationError = validationResult.errorMessage
                    
                    if validationResult.successful {
                        Task { await otpModel.verifyOtp() }
                    }
                } label: {
                    Text("Verify")
                }
            } header: {
                Text("")
            }
        }
        .navigationTitle("Verification")
    }
}
