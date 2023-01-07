//
//  OtpScreen.swift
//  iosApp
//
//  Created by Amal Nuritdinkhodzhaev on 31/12/22.
//  Copyright © 2022 orgName. All rights reserved.
//

import SwiftUI
import shared

struct OtpScreen: View {
    
    @EnvironmentObject var otpViewModel: IOSOtpViewModel

    let firstName: String
    let lastName: String
    let phoneNumber: String
    let password: String
        
    var body: some View {        
        Form {
            Section {
                TextField("OTP", text: Binding(
                    get: { otpViewModel.state.otp },
                    set: { value in
                        otpViewModel.onEvent(
                            event: OtpEvent.OtpChanged(value: value)
                        )
                    }
                ))
                .textContentType(.oneTimeCode)
                .keyboardType(.phonePad)
                
                Button {
                    if otpViewModel.isOtpValidationSuccess() {
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
            } footer: {
                if let otpError = otpViewModel.state.otpError {
                    Text(LocalizedStringKey(otpError))
                        .foregroundColor(.red)
                } else {
                    Text("")
                }
            }
        }
        .navigationTitle("Verification")
        .alert(otpViewModel.errorMessage, isPresented: $otpViewModel.showAlert) {}
        .overlay {
            if otpViewModel.isLoading {
                Color(.systemBackground).ignoresSafeArea()
                ProgressView()
            }
        }
//        .background {
//            NavigationLink(tag: "Queue", selection: $otpViewModel.navigationTag) {
//                ContentView()
//            } label: {
//                EmptyView()
//            }
//        }
        .onAppear {
            otpViewModel.startObserving()
        }
        .onDisappear {
            otpViewModel.dispose()
        }
    }
}
